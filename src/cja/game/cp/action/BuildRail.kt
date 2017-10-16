package cja.game.cp.action

import cja.game.GameAction
import cja.game.GameDeck
import cja.game.cp.*

class BuildRail : GameAction {

    var gs: CPGameState;
    var railDeck : GameDeck;

    constructor(gs: CPGameState, railDeck : GameDeck) {
        this.gs = gs;
        this.railDeck = railDeck;
    }

    override fun execute(): Boolean {
        var myMat = gs.getPawnMat(gs.pawns[gs.playerTurn]);
        var pawnNumber = myMat!!.getPawnNumber(gs.pawns[gs.playerTurn]);
        var startPlacing = false;
        var letter = "";
        for(year in gs.yearMats) {
            if(year == myMat) {
                startPlacing = true;
            }
            if(startPlacing && year.isEmpty(pawnNumber)) {
                var card = railDeck.pop() as RailCard;
                year.setCardSquare(pawnNumber, card);
                letter = card.railLetter;
            }
        }

        var numRemoves = when(myMat) {
            gs.yearMats[0] -> 3;
            gs.yearMats[1] -> 2;
            gs.yearMats[2] -> 2;
            gs.yearMats[3] -> 1;
            else -> 0;
        }

        for(k in 0..numRemoves-1) {
            var removed = false;
            for(card in gs.players[gs.playerTurn].hand) {
                if(card is BuildRailCard && card.buildLetter.equals(letter)) {
                    gs.players[gs.playerTurn].hand.remove(card);
                    removed = true;
                    break;
                }
            }
            if(!removed) {
                for(card in gs.players[gs.playerTurn].hand) {
                    if(card is WildCard) {
                        gs.players[gs.playerTurn].hand.remove(card);
                        break;
                    }
                }
            }
        }

        for(card in railDeck.cards) {
            card.isFront = false;
        }
        gs.nextPlayerTurn();
        return true;
    }
}
