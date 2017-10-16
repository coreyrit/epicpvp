package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.TurnAction
import cja.game.cp.YearMat

class TravelThroughTime : GameAction {

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
        if(number > 0 && pawnNumber > 0 && number == pawnNumber && !mat.isBlocked(pawnNumber)) {
            myMat.removePawn(gs.pawns[gs.playerTurn]);
            mat.setPawnSquare(pawnNumber, gs.pawns[gs.playerTurn]);
            gs.turnAction = TurnAction.Move;
            return true;
        }
        return false;
    }
}
