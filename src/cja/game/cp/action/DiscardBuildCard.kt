package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.TurnAction

class DiscardBuildCard : GameAction {
    var gs: CPGameState;
    var ptx : Float;
    var pty : Float;

    constructor(gs: CPGameState, ptx : Float, pty : Float) {
        this.gs = gs;
        this.ptx = ptx;
        this.pty = pty;
    }

    override fun execute(): Boolean {
        var card = gs.players[gs.playerTurn].getCardAt(ptx, pty);
        if(card != null) {
            gs.players[gs.playerTurn].hand.remove(card);
            gs.discardDeck.cards.add(card);

            // damage now comes from drawn build cards!

//            if(gs.players[gs.playerTurn].scientistName.equals("Chemist")) {
//                gs.turnAction = TurnAction.TimeTravel;
//            } else {
//                gs.turnAction = TurnAction.DrawDamage;
//            }

            if(gs.turnAction == TurnAction.DiscardBuild) {
                gs.turnAction = TurnAction.TimeTravel;
            } else {
                gs.nextPlayerTurn();
            }

            return true;
        }
        return false;
    }
}

