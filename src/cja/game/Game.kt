package cja.game

//import cja.game.cp.CPGameState
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.EpicPvpTurn
import org.w3c.dom.events.MouseEvent
import kotlin.js.Math
import kotlin.browser.window

class Game {
    //var gameState : GameState = CPGameState("./img", arrayOf("Geologist", "Physicist"));

    val gameState: GameState = EpicPvpGameState("./img");
    var gameRenderer : GameRenderer = HTMLCanvasRenderer(
            {ev -> click(
                    Math.round((ev as MouseEvent).pageX),
                    Math.round((ev as MouseEvent).pageY)
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

        score += state.movesPlayed * 20;
        score += state.player1life.cards.size * 100;
        score += Math.max(state.player1aggression.aggression.size, 10) * 10;
        score += state.player1hand.hand.size * 5;
        score += state.countMoves(true) * 20;

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
        playSequence(route);
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
