package cja.game

//import cja.game.cp.CPGameState
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.EpicPvpTurn
import org.w3c.dom.events.MouseEvent
import kotlin.js.Math
import kotlin.browser.window

class Game {
    //var gameState : GameState = CPGameState("./img", arrayOf("Geologist", "Physicist"));

    val gameState: GameState = EpicPvpGameState();
    var gameRenderer : GameRenderer = HTMLCanvasRenderer(
            {ev -> click(
                    Math.round((ev as MouseEvent).pageX),
                    Math.round(ev.pageY)
            )}
    );

    var aiRenderer : GameRenderer = HiddenRenderer();

    fun run() {
        (gameState as EpicPvpGameState).setup();
        gameState.setHighlights();
        gameRenderer.render(gameState);
        click(-1, -1);
    }

    fun printSequence(score : Int, route : List<Point>) {
        print("Sequence: ");
        for(pt in route) {
            print("(" + pt.x + "," + pt.y + ") ");
        }
        println(": " + score);
    }

    fun playSequence(route : List<Point>) {
        val routeSize = route.size
        var index = 0
        fun play(pt : Point){
            console.log("Thinking for a second before doing (" + pt.x + "," + pt.y + ")")
            window.setTimeout({
                gameState.handleClick(pt.x, pt.y)
                aiRenderer.render(gameState)
                gameRenderer.render(gameState)
                index++
                if (index < routeSize) { play(route[index]) }
            }, 1500)
        }
        play(route[index])
    }

    fun recurseAction(state : EpicPvpGameState, pt : Point, depth : Int, route : List<Point>, choices : MutableList<Pair<Int, List<Point>>>) {
        aiRenderer.render(state);

        if(!state.player1turn || state.turnAction == EpicPvpTurn.GameOver) {
            //println(route + ": " + getStateScore(state));
            choices.add(Pair(getStateScore(state), route));
            return;
        }
//        if(choices.size > 100) {
//            println("I fond enough choices");
//            return;
//        }
        if(depth > 20) {
            println("Something went wrong!");
            return;
        }
        state.handleClick(pt.x, pt.y);
        aiRenderer.render(state);
        for(nextPt in state.getValidActions()) {
            var newRoute = ArrayList(route);
            newRoute.add(pt);
            recurseAction(state.clone(), nextPt, depth+1, newRoute, choices);
        }
    }

    fun getStateScore(state : EpicPvpGameState) : Int {
        var score = 0;

        score += state.movesPlayed * 20; // it is good to play cards
        score += state.player1life.cards.size * 100; // its best to keep life

        // having aggression is good, but not too much
        if(state.player1aggression.aggression.size < 10) {
            score += state.player1aggression.aggression.size * 10;
        } else {
            score += -50;
        }

        // score big points for hurting your opponent
        score += (10 - state.player2life.cards.size) * 100;

        // the lower the opponent's aggression, the better
        score += (20-state.player2aggression.aggression.size) * 20;

        // you want SOME hands in your card, but you always want them leaving your hand
        if(state.player1hand.hand.size < 2) {
            score += -50;
        } else if(state.player1hand.hand.size > 8) {
            score += -100;
        } else {
            score += (8-state.player1hand.hand.size) * 20;
        }

        // the lower the opponent's and the better
        score += (20-state.player2hand.hand.size) * 20;

        score += state.countMoves(true) * 20; // is this double counting the playing of moves?
        score += state.specialsPlayed * 10; // specials can be good

        return score;
    }

    fun runAi() {
        println("Thinking...");
        var choices = ArrayList<Pair<Int, List<Point>>>();
        for (pt in (gameState as EpicPvpGameState).getValidActions()) {
            recurseAction(gameState.clone(), pt, 0, ArrayList<Point>(), choices);
        }
        var max = -10000;
        var route = emptyList<Point>();
        for(choice in choices) {
            if(choice.first > max) {
                max = choice.first;
                route = choice.second;
            }
        }
        printSequence(max, route);
        if(max > -10000) {
            playSequence(route);
        }
//        println("----------------------------------------------------------------");
    }

    //    @native("")
    fun click(x : Int, y : Int) {
        gameState.handleClick(x + 0.0f, y + 0.0f);
        gameRenderer.render(gameState);
        var state = gameState as EpicPvpGameState;

        if(state.player1turn) {
            runAi();
        }
    }
}

fun main(args: Array<String>) {
    Game().run();
}
