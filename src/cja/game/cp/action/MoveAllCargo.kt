package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.MoveResult
import cja.game.cp.RailLocation
import cja.game.cp.TurnAction

class MoveAllCargo : GameAction {
    var gs: CPGameState;

    constructor(gs: CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
        for(i in gs.players[gs.playerTurn].hand.size()-1..0) {
            var gc = gs.players[gs.playerTurn].hand.remove(i);
            gs.discardDeck.cards.add(gc);
        }
        var lose = false;
        for(mat in gs.yearMats) {
            var move = mat.moveCargo();
            if(move != MoveResult.Safe) {
                gs.moveResult = move;
                gs.moveYear = mat.matYear;
                gs.turnAction = TurnAction.GameOver;
                lose = true;
            }
        }
        var win = true;
        for(mat in gs.yearMats) {
            if(mat.cargoLocation != RailLocation.Finish) {
                win = false;
            }
        }
        if(win) {
            gs.turnAction = TurnAction.GameOver;
            gs.moveResult = MoveResult.Win;
        }
        if(!win && !lose) {
            gs.nextPlayerTurn();
        }
        return true;
    }
}

