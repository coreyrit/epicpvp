package cja.game.cp.action

import cja.game.GameAction
import cja.game.GameDeck
import cja.game.cp.*

class RepairRail : GameAction {

    var gs: CPGameState;

    constructor(gs: CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
        var myMat = gs.getPawnMat(gs.pawns[gs.playerTurn]);
        var pawnNumber = myMat!!.getPawnNumber(gs.pawns[gs.playerTurn]);
        myMat.repairSquare(pawnNumber);
        gs.nextPlayerTurn();
        return true;
    }
}
