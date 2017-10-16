package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.TurnAction
import cja.game.cp.YearMat

class MovePawn : GameAction {

    var gs: CPGameState;
    var mat : YearMat;
    var ptx : Float;
    var pty : Float;

    constructor(gs: CPGameState, mat : YearMat, ptx : Float, pty : Float) {
        this.gs = gs;
        this.mat = mat;
        this.ptx = ptx;
        this.pty = pty;
    }

    override fun execute(): Boolean {
        var number = mat.getSquareNumberAt(ptx, pty);
        var myMat = gs.getPawnMat(gs.pawns[gs.playerTurn]);
        var pawnNumber = myMat!!.getPawnNumber(gs.pawns[gs.playerTurn]);
        if(number > 0 && pawnNumber > 0 && myMat == mat &&
                (gs.allowedMovement[pawnNumber]!!.contains(number) || gs.players[gs.playerTurn].scientistName.equals("Physicist")) &&
                !myMat.isBlocked(number)) {
            myMat.removePawn(gs.pawns[gs.playerTurn]);
            myMat.setPawnSquare(number, gs.pawns[gs.playerTurn]);

            gs.movements++;
            if((myMat == gs.yearMats[0] || myMat == gs.yearMats[1]) && gs.movements >= 1) {
                gs.turnAction = TurnAction.Trade;
            } else if((myMat == gs.yearMats[2] || myMat == gs.yearMats[3]) && gs.movements >=2) {
                gs.turnAction = TurnAction.Trade;
            } else if(gs.players[gs.playerTurn].scientistName.equals("Physicist")) {
                gs.turnAction = TurnAction.Trade;
            }
            return true;
        } else if(number == pawnNumber && mat == myMat) {
            gs.turnAction = TurnAction.Trade;
            return true;
        }
        return false;
    }
}
