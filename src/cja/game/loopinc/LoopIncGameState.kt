package cja.game.loopinc

import cja.game.*
import kotlin.js.Date

class LoopIncGameState : GameState {
    var r : Random = Random(Date().getTime());

    var componentBoard : GameMat = GameMat(null, null);
    var scoreBoard : GameMat = GameMat(null, null);

    // player stuff
    var playerTokens : MutableList<GamePawn> = ArrayList();
    var playerBoards : MutableList<PlayerBoard> = ArrayList();

    // chits
    var wheels : MutableList<GameChit> = ArrayList();
    var spikes : MutableList<GameChit> = ArrayList();
    var propellers : MutableList<GameChit> = ArrayList();
    var nets : MutableList<GameChit> = ArrayList();
    var cameras : MutableList<GameChit> = ArrayList();

    var tears : MutableList<GameChit> = ArrayList();

    // trip cards
    var bcTripCards : GameDeck = GameDeck(r);
    var adTripCards : GameDeck = GameDeck(r);

    // action cards
    var garageCards : GameDeck = GameDeck(r);
    var shopCards : GameDeck = GameDeck(r);
    var armoryCards : GameDeck = GameDeck(r);
    var trashCards : GameDeck = GameDeck(r);
    var advertiseCards : GameDeck = GameDeck(r);
    var exchangeCards : GameDeck = GameDeck(r);
    var moveCards : GameDeck = GameDeck(r);


    constructor() {
        for(i in 1..4) {
            playerTokens.add(GamePawn(""));
            playerBoards.add(PlayerBoard());
        }

        for(i in 1..16) {
            tears.add(GameChit(null, null));
        }

        for(i in 1..6) {
            bcTripCards.cards.add(TripCard(0, 0, emptyList(), emptyList(), null, null));
        }
        for(i in 1..10) {
            adTripCards.cards.add(TripCard(0, 0, emptyList(), emptyList(), null, null));
        }
    }

    fun setup() {
        var numPlayers = 2;

        bcTripCards.shuffle();
        adTripCards.shuffle();

        var x = 50f;
        var y = 50f;

        for(i in 1..2) {
            var bcCard = bcTripCards.pop();
            bcCard.setPosition(x, y, 100f, 60f);
            gameObjects.add(bcCard);
            x += bcCard.width + 10f;
        }
        for(i in 1..3) {
            var adCard = adTripCards.pop();
            adCard.setPosition(x, y, 100f, 60f);
            gameObjects.add(adCard);
            x += adCard.width + 10f;
        }

        x = 50f;
        y += 70f; // height of card + 10

        componentBoard.setPosition(x, y, 250f, 100f);
        gameObjects.add(componentBoard);
        x += componentBoard.width + 10f;
        scoreBoard.setPosition(x, y, 250f, 100f);
        gameObjects.add(scoreBoard);

//        x = 50f;
        y += 110f; // height of board + 10

        for(i in 1..numPlayers) {
            wheels.add(GameChit(null, null));
            spikes.add(GameChit(null, null));
            propellers.add(GameChit(null, null));
            nets.add(GameChit(null, null));
            cameras.add(GameChit(null, null));
        }
    }
}
