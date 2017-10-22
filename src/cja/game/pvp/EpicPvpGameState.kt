package cja.game.pvp

import cja.game.*
import cja.game.pvp.druid.*
import cja.game.pvp.dwarf.DwarfRace
import cja.game.pvp.goblin.*
import cja.game.pvp.human.*
import cja.game.pvp.paladin.*

public class EpicPvpGameState : GameState {
    var allCards : MutableMap<Int, EpicPvpCard> = HashMap();

    val startButton = GameButton("Start");

    var player1area : PlayerArea = PlayerArea("blue", HumanRace(true), PaladinClass(true));
    var player1race : RaceCard = player1area.playerRace;
    var player1class : ClassCard = player1area.playerClass;
    var player1deck : GameDeck = player1area.playerDeck;
    var player1discard : GameDeck = player1area.playerDiscard;
    var player1hand : PlayerHand = player1area.playerHand;
    var player1life : LifeDeck = player1area.playerLife;
    var player1aggression : Aggression = player1area.playerAggression;
    var player1permanents : MutableList<EpicPvpCard> = player1area.playerPermanents;
    var player1cardProcesses : MutableList<CardProcess> = ArrayList();

    var player2area : PlayerArea = PlayerArea("red", DwarfRace(false), DruidClass(false));
    var player2race : RaceCard = player2area.playerRace;
    var player2class : ClassCard = player2area.playerClass;
    var player2deck : GameDeck = player2area.playerDeck;
    var player2discard : GameDeck = player2area.playerDiscard;
    var player2hand : PlayerHand = player2area.playerHand;
    var player2life : LifeDeck = player2area.playerLife;
    var player2aggression : Aggression = player2area.playerAggression;
    var player2permanents : MutableList<EpicPvpCard> = player2area.playerPermanents;
    var player2cardProcesses : MutableList<CardProcess> = ArrayList();

    var moveArea : MoveArea = MoveArea();

    var player1turn : Boolean = true;
    var turnAction : EpicPvpTurn = EpicPvpTurn.GameStart;

    var aggressionDraw : Int = 2;
    var aggressionRemaining : Int = 0;
    var selectedMoveCard : MoveCard? = null;
    var damageTaken : Int = 0;
    var movesPlayed : Int = 0;
    var specialsPlayed : Int = 0;
    var aggressionDrawn : Int = 0;
    var discardedCards : MutableList<EpicPvpCard> = ArrayList();
    var specials : MutableList<SpecialCard> = ArrayList();

    constructor() {
        this.width = 1200f;
        this.height = 800f;
    }

    fun clone() : EpicPvpGameState {
        var state = EpicPvpGameState();

        state.player1area = player1area.clone(state);
        state.player1race = state.player1area.playerRace;
        state.player1class = state.player1area.playerClass;
        state.player1deck = state.player1area.playerDeck;
        state.player1discard = state.player1area.playerDiscard;
        state.player1hand = state.player1area.playerHand;
        state.player1life = state.player1area.playerLife;
        state.player1aggression = state.player1area.playerAggression;
        state.player1permanents = state.player1area.playerPermanents;

        state.player2area = player2area.clone(state);
        state.player2race = state.player2area.playerRace;
        state.player2class = state.player2area.playerClass;
        state.player2deck = state.player2area.playerDeck;
        state.player2discard = state.player2area.playerDiscard;
        state.player2hand = state.player2area.playerHand;
        state.player2life = state.player2area.playerLife;
        state.player2aggression = state.player2area.playerAggression;
        state.player2permanents = state.player2area.playerPermanents;


        state.moveArea = moveArea.clone(state);

        state.player1turn = player1turn;
        state.turnAction = turnAction;

        for(card in player1cardProcesses) {
            var pvp = card as EpicPvpCard;
            try {
                state.player1cardProcesses.add(state.allCards.get(pvp.id) as CardProcess)
            } catch(ex : Exception) {
                println("invalid id: " + pvp.id + ", " + pvp.name);
            }
        }
        for(card in player2cardProcesses) {
            var pvp = card as EpicPvpCard;
            try {
                state.player2cardProcesses.add(state.allCards.get(pvp.id) as CardProcess)
            } catch(ex : Exception) {
                println("invalid id: " + pvp.id + ", " + pvp.name);
            }
        }

        state.aggressionDraw = aggressionDraw;
        state.aggressionRemaining = aggressionRemaining;

        if(selectedMoveCard != null) {
            state.selectedMoveCard = state.allCards.get(selectedMoveCard!!.id) as MoveCard;
        }

        state.damageTaken = damageTaken;
        state.movesPlayed = movesPlayed;
        state.specialsPlayed = specialsPlayed;
        state.aggressionDrawn = aggressionDrawn;

        for(card in discardedCards) {
            state.discardedCards.add(state.allCards.get(card.id) as MoveCard);
        }
        for(card in specials) {
            state.specials.add(state.allCards.get(card.id) as SpecialCard);
        }

        state.gameObjects.add(state.player1area.playerDeck);
        state.gameObjects.add(state.player1area.playerDiscard);
        state.gameObjects.add(state.player1area.playerHand);
        state.gameObjects.add(state.player1area.playerAggression);
        state.gameObjects.add(state.player1area.playerLife);
        state.gameObjects.add(state.player1area.playerClass);
        state.gameObjects.add(state.player1area.playerRace);

        state.gameObjects.add(state.player2area.playerDeck);
        state.gameObjects.add(state.player2area.playerDiscard);
        state.gameObjects.add(state.player2area.playerHand);
        state.gameObjects.add(state.player2area.playerAggression);
        state.gameObjects.add(state.player2area.playerLife);
        state.gameObjects.add(state.player2area.playerClass);
        state.gameObjects.add(state.player2area.playerRace);

        state.gameObjects.add(state.moveArea);

        for(obj in gameObjects) {
            if(obj is EpicPvpButton) {
                var buttonsCard = state.allCards.get(obj.card.id) as ButtonsCard;
                var button = buttonsCard.getButton(obj.buttonLabel);
                button.setPosition(obj.x, obj.y, obj.width, obj.height);
                button.highlight = obj.highlight;
                state.gameObjects.add(0, button);
            }
            else if(obj is PlayerHand && obj != player1area.playerHand && obj != player2area.playerHand) {
                var cloneHand = PlayerHand(obj.playerColor);
                for(card in obj.hand) {
                    var pvp = card as EpicPvpCard;
                    cloneHand.hand.add(state.allCards.get(pvp.id)!!);
                }
                state.gameObjects.add(0, cloneHand);
            }
        }

        return state;
    }

    fun setup() {
        EpicPvpSetup().setup(this);
        setHighlights();

        startButton.setPosition(100f, 100f, 100f, 50f);
        gameObjects.add(0, startButton);
    }

    fun getPlayerRace(player : Boolean) : RaceCard {
        return when(player) { true -> player1race; false -> player2race };
    }

    fun getPlayerClass(player : Boolean) : ClassCard {
        return when(player) { true -> player1class; false -> player2class };
    }

    fun getPlayerDeck(player : Boolean) : GameDeck {
        return when(player) { true -> player1deck; false -> player2deck };
    }

    fun getPlayerAggression(player : Boolean) : Aggression {
        return when(player) { true -> player1aggression; false -> player2aggression };
    }

    fun getPlayerHand(player : Boolean) : PlayerHand {
        return when(player) { true -> player1hand; false -> player2hand };
    }

    fun getPlayerDiscard(player : Boolean) : GameDeck {
        return when(player) { true -> player1discard; false -> player2discard };
    }

    fun getPlayerLife(player : Boolean) : GameDeck {
        return when(player) { true -> player1life; false -> player2life };
    }

    fun getPlayerPermanents(player : Boolean) : MutableList<EpicPvpCard> {
        return when(player) { true -> player1permanents; false -> player2permanents };
    }

    fun getCardProcesses(player : Boolean) : MutableList<CardProcess> {
        return when(player) { true -> player1cardProcesses; false -> player2cardProcesses };
    }

    fun clearHighlights() {
        for(obj in gameObjects) {
            obj.highlight = false;
        }
        for(card in player1hand.hand) {
            card.highlight = false;
            (card as EpicPvpCard).embelish = false;
        }
        for(card in player2hand.hand) {
            card.highlight = false;
            (card as EpicPvpCard).embelish = false;
        }
        for(card in player1aggression.aggression) {
            card.highlight = false;
        }
        for(card in player2aggression.aggression) {
            card.highlight = false;
        }
        for(card in moveArea.moves.keys) {
            card.highlight = false;
        }
        for(card in player1discard.cards) {
            card.highlight = false;
        }
        for(card in player2discard.cards) {
            card.highlight = false;
        }
    }

    fun setHighlights() {
        clearHighlights();

        if(turnAction == EpicPvpTurn.GameStart) {
            startButton.highlight = true;
            return;
        }

        // current player's turn first
//        var turn1 = (player1turn && turnAction != EpicPvpTurn.DrawAggression) || (!player1turn && turnAction == EpicPvpTurn.DrawAggression);
        if(getCardProcesses(player1turn).size > 0) {
            getCardProcesses(player1turn).get(0).setHighlights(this);
            return;
        }
        // then enemy player
//        if(getCardProcesses(!turn1).size() > 0) {
//            getCardProcesses(!turn1).get(0).setHighlights(this);
//            return;
//        }

        var gameDeck = getPlayerDeck(player1turn);
        var aggression = getPlayerAggression(player1turn);
        var hand = getPlayerHand(player1turn);

        if(movesPlayed == 0) {
            aggressionRemaining = aggression.aggression.size;
        }

        if(turnAction == EpicPvpTurn.DrawAggression) {
            gameDeck.highlight = true;
        }

        if(turnAction == EpicPvpTurn.DrawCards) {
            aggression.highlight = true;
            for(card in aggression.aggression) {
                card.highlight = true;
            }
        }

        if(turnAction == EpicPvpTurn.PlayMove) {
            hand.highlight = true;
            for(card in hand.hand) {
                var pvp = card as EpicPvpCard;
                if(pvp.cost <= aggressionRemaining) {
                    card.highlight = true;
                }
            }
        }

        if(turnAction == EpicPvpTurn.AssignMove) {
            selectedMoveCard!!.embelish = true;
            moveArea.highlight = true;
            for(card in moveArea.moves.keys) {
                if(card.owner != player1turn &&
                        moveArea.moves.get(card) == null &&
                        //selectedMoveCard!!.canBlock(card) && // Allow any assign because of modifiers
                        card.validBlock(selectedMoveCard!!)) {
                    card.highlight = true;
                }
            }
        }
    }

    fun placeButtons(message : GameMessage, button1 : GameButton, button2 : GameButton, width : Float) {
        placeMessage(message, width)
        button1.setPosition(moveArea.x + moveArea.width/2 - width - 5f, moveArea.y + moveArea.height/2 - 20f, width, 40f)
        gameObjects.add(0, button1)
        button2.setPosition(moveArea.x + moveArea.width/2 + 5f, moveArea.y + moveArea.height/2 - 20f, width, 40f)
        gameObjects.add(0, button2)
    }

    fun placeButtons(message : GameMessage, button2 : GameButton, width : Float) {
        placeMessage(message, width)
        button2.setPosition(moveArea.x + moveArea.width/2 + 5f, moveArea.y + moveArea.height/2 - 20f, width, 40f);
        gameObjects.add(0, button2);
    }

    fun placeMessage(message : GameMessage, width : Float) {
        message.setPosition(moveArea.x + moveArea.width/2 - width - 5f, moveArea.y + moveArea.height/2 - 70f, width*2 + 10f, 40f)
        gameObjects.add(0, message)
    }

    fun dealDamage(player : Boolean) {
        var life = getPlayerLife(player);
        if(life.cards.size > 0) {
            var lifeCard = life.pop() as EpicPvpCard;
            discardCard(player, lifeCard);
        }

        damageTaken++;

        if(life.cards.size == 0) {
            // GAME OVER
            clearHighlights();

            val msg : String;
            if(player) {
                msg = "Player 2";
            } else {
                msg = "Player 1";
            }

            var endGame = GameMessage(msg + " wins!", true);
            placeMessage(endGame, 100f)
            setTurnAction(EpicPvpTurn.GameOver);
        }
    }

    fun drawCard(player : Boolean) : EpicPvpCard {
        var deck = getPlayerDeck(player);
        if(deck.cards.size == 0) {
            dealDamage(player);
            var discard = getPlayerDiscard(player);
            while(discard.cards.size > 0) {
                var card = discard.pop();
                card.isFront = false;
                deck.cards.add(card);
            }
            deck.shuffle();
        }
        return deck.pop() as EpicPvpCard;
    }

    fun discardCard(player : Boolean, card : EpicPvpCard) {
        card.isFront = true;
        getPlayerDiscard(player).cards.add(0, card);
    }

    fun setTurnAction(action : EpicPvpTurn) {
        turnAction = action;
    }

    fun discardMove(card : MoveCard, blocker : EpicPvpCard?) {
        card.blockedBy = blocker;
        moveArea.moves.remove(card);
        discardCard(card.owner, card);
        discardedCards.add(card);

        if(blocker == null) {
            // deal damage
            for(i in 1..card.damage) {
                dealDamage(!card.owner);
            }
        }
    }

    fun countMoves(player : Boolean) : Int {
        var count = 0;
        for(card in moveArea.moves.keys) {
            if (card.owner == player || (moveArea.moves.get(card) != null && moveArea.moves.get(card)!!.owner == player)) {
                count++;
            }
        }
        return count;
    }

    fun checkForNextPlayerTurn(player : Boolean) {
        if(turnAction == EpicPvpTurn.EndPhase && getCardProcesses(player).size == 0) {
            discardedCards.clear();
            aggressionDraw = 2;
            specials.clear();
            damageTaken = 0;
            player1turn = !player;
            setTurnAction(EpicPvpTurn.DrawAggression);

            // rotate cards
            for(card in moveArea.moves.keys) {
                card.isRotated = player1turn;
            }
        }
    }

    override fun handleClick(ptx : Float, pty : Float) : Boolean {
        // player's turn first
//        var turn1 = (player1turn && turnAction != EpicPvpTurn.DrawAggression) || (!player1turn && turnAction == EpicPvpTurn.DrawAggression);
        if(getCardProcesses(player1turn).size > 0) {
            getCardProcesses(player1turn).get(0).handleClick(this, ptx, pty);
            setHighlights();
            return true;
        }
        // then enemy player
//        if(getCardProcesses(!turn1).size() > 0) {
//            getCardProcesses(!turn1).get(0).handleClick(this, ptx, pty);
//            setHighlights();
//            return true;
//        }

        var obj = getObjectAt(ptx, pty);

        var gameDeck = getPlayerDeck(player1turn);
        var aggression = getPlayerAggression(player1turn);
        var hand = getPlayerHand(player1turn);
        var discard = getPlayerDiscard(player1turn);

        if(obj != null) {
            if(turnAction == EpicPvpTurn.GameStart && obj == startButton) {
                gameObjects.remove(startButton);
                setTurnAction(EpicPvpTurn.DrawAggression);
            }
            else if(turnAction == EpicPvpTurn.DrawAggression && obj == gameDeck) {
//                println("draw aggression");
                for(card in moveArea.moves.keys) {
                    card.beforeAggressionPhase(this);
                }
                for(i in 1..aggressionDraw) {
                    aggression.aggression.add(drawCard(player1turn));
                }
                setTurnAction(EpicPvpTurn.DrawCards);
            }
            else if(turnAction == EpicPvpTurn.DrawCards && obj == aggression) {
//                println("draw cards");
                var card = aggression.getCardAt(ptx, pty);
                aggressionDrawn = 0;
                if(card != null && card.highlight) {
                    // draw every card up to this one
                    var count = aggression.aggression.size - aggression.aggression.indexOf(card);
                    aggressionDrawn = count;
                    while(count > 0) {
                        var draw = aggression.aggression.removeAt(0);
                        draw.isFront = !player1turn;
                        hand.hand.add(draw);
                        count--;
                    }
                }

                // notify of draw phase
                for(move in moveArea.moves.keys) {
                    move.afterDrawPhase(this);
                }

                // before move phase
                getPlayerRace(player1turn).beforeMovePhase(this);
                getPlayerClass(player1turn).beforeMovePhase(this);
                for(permanent in getPlayerPermanents(player1turn)) {
                    permanent.beforeMovePhase(this);
                }

                movesPlayed = 0;
                specialsPlayed = 0;
                setTurnAction(EpicPvpTurn.PlayMove);
            }
            else if(turnAction == EpicPvpTurn.PlayMove && obj == hand) {
                var card = hand.getCardAt(ptx, pty);
                if(card != null && !card.highlight) {
//                    println("Clicked a card but it isn't highlighted.");
                }
                else if(card != null && card.highlight) {
                    if(card is MoveCard) {
                        card.isFront = true;
//                        println("play move (" + ptx + "," + pty + ")");
                        setTurnAction(EpicPvpTurn.AssignMove);
                        selectedMoveCard = card;
                        card.highlight = false;
                    }
                    else if(card is SpecialCard) {
//                        println("play special (" + ptx + "," + pty + ")");
                        card.owner = player1turn;
                        specials.add(card);
                        hand.hand.remove(card);
                        discard.cards.add(card);
                        card.play(this);

                        specialsPlayed++;
                    }
                    else {
//                        println("What kind of card is THIS?? (" + ptx + "," + pty + ")");
                    }
                } else {
//                    println("stop playing moves (" + ptx + "," + pty + ")");
                    discardedCards.clear();

                    // finished playing moves
                    getPlayerRace(player1turn).afterMovePhase(this);
                    getPlayerClass(player1turn).afterMovePhase(this);
                    for(move in moveArea.moves.keys) {
                        if(move.owner == player1turn) {
                            move.afterMovePhase(this);
                        } else if(moveArea.moves.get(move) != null && moveArea.moves.get(move)!!.owner == player1turn) {
                            moveArea.moves.get(move)!!.afterMovePhase(this);
                        }
                    }
                    for(special in specials) {
                        special.afterMovePhase(this);
                    }
                    for(permanent in getPlayerPermanents(player1turn)) {
                        permanent.afterMovePhase(this);
                    }

                    setTurnAction(EpicPvpTurn.ResolveMoves);
                    handleClick(-1f, -1f); // trigger an action
                }
            }
            else if(turnAction == EpicPvpTurn.AssignMove && obj == moveArea) {
//                println("assign move");
                hand.hand.remove(selectedMoveCard as GameCard);
                selectedMoveCard!!.embelish = false;

                var card = moveArea.getCardAt(ptx, pty);
                var attack = selectedMoveCard!!;
                var defend : MoveCard? = null;
                if(card != null && card.highlight) {
                    attack = card;
                    defend = selectedMoveCard;
                }

                selectedMoveCard!!.isRotated = player1turn;
                selectedMoveCard!!.owner = player1turn;
                moveArea.assignMove(attack, defend);
                aggressionRemaining -= selectedMoveCard!!.cost;
                selectedMoveCard = null;
                movesPlayed++;
                setTurnAction(EpicPvpTurn.PlayMove);
            }
        }
        else if(turnAction == EpicPvpTurn.ResolveMoves) {
//            println("resolve moves");

            // move invalid defends off of attacks
            for(card in moveArea.moves.keys) {
                if(moveArea.moves.get(card) != null && !moveArea.moves.get(card)!!.canBlock(card)) {
                    var defend = moveArea.moves.get(card);
                    moveArea.moves.put(card, null);
                    moveArea.assignMove(defend!!, null);
                }
            }

            // now resolve damage
            for(card in moveArea.moves.keys) {
                if(moveArea.moves.get(card) == null) {
                    if(card.owner == player1turn) {
                        card.isRotated = !card.isRotated;
                    } else {
                        discardMove(card, null);
                    }
                } else {
                    // card was blocked
                    var defend = moveArea.moves.get(card);
                    discardMove(card, defend);
                    defend!!.delta = defend.defend - card.attack;
                    moveArea.assignMove(defend, null);
                }
            }

            if(turnAction != EpicPvpTurn.GameOver) {
                // activate any end phase actions against discaded cards
                for (card in discardedCards) {
                    card.afterEndPhase(this);
                }
                for (special in specials) {
                    special.afterEndPhase(this);
                }
                for (card in getPlayerPermanents(player1turn)) {
                    card.afterEndPhase(this);
                }
                getPlayerRace(player1turn).afterEndPhase(this);
                getPlayerClass(player1turn).afterEndPhase(this);

                setTurnAction(EpicPvpTurn.EndPhase);
            }
        }

        if(turnAction != EpicPvpTurn.GameOver) {
            checkForNextPlayerTurn(player1turn);
            setHighlights();
        }
        return true;
    }

    fun getValidActions() : List<Point> {
        var actions = ArrayList<Point>();
        for(obj in gameObjects) {
            if(obj is PlayerHand && !obj.playerColor.equals("black")) {
                if (obj.highlight) {
                    actions.add(Point(obj.x + obj.width - 2, obj.y + obj.height - 2));
                }
                for (card in obj.hand) {
                    if (card.highlight) {
                        actions.add(Point(card.x + 2, card.y + 2));
                    }
                }
            }
            else if(obj is PlayerHand && obj.equals("black")) {
                // only the cards in the hand should be clickable for non player hands
                for(card in obj.hand) {
                    actions.add(Point(card.x+2, card.y+2));
                }
            }
            else if(obj is Aggression) {
                if (obj.highlight) {
                    actions.add(Point(obj.x + obj.width - 2, obj.y + obj.height - 2));
                }
                for (card in obj.aggression) {
                    if (card.highlight) {
                        actions.add(Point(card.x + 2, card.y + 2)); // hopefully they aren't REALLY tight
                    }
                }
            }
            else if(obj is MoveArea) {
                if(obj.highlight) {
                    actions.add(Point(obj.x + obj.width - 2, obj.y + obj.height - 2));
                }
                for(card in obj.moves.keys) {
                    if(card.highlight) {
                        actions.add(Point(card.x + 2, card.y + 2));
                    }
                    if(obj.moves.get(card) != null && obj.moves.get(card)!!.highlight) {
                        actions.add(Point(obj.moves.get(card)!!.x + 2, obj.moves.get(card)!!.y + 2));
                    }
                }
            }
            else if(obj is GameButton) {
//                println("Found a button!")
                if(obj.highlight) {
                    actions.add(Point(obj.x + 2, obj.y + 2));
                }
            }
            else {
                if(obj.highlight) {
                    actions.add(Point(obj.x + 2, obj.y + 2));
                }
            }
        }

//        println("Valid actions:");
//        for(pt in actions) {
//            println("  " + pt.x + "," + pt.y);
//        }

        return actions;
    }
}
