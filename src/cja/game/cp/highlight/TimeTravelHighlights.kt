package cja.game.cp.highlight

import cja.game.cp.CPGameState
import cja.game.cp.YearMat

class TimeTravelHighlights : Highlights {
    var gs: CPGameState;
    var myMat : YearMat;

    constructor(gs: CPGameState, myMat : YearMat) {
        this.gs = gs;
        this.myMat = myMat;
    }

    override fun highlight() {
        gs.pawns[gs.playerTurn].highlight = true;
        var number = myMat.getPawnNumber(gs.pawns[gs.playerTurn]);
        for(mat in gs.yearMats) {
            if(!mat.isBlocked(number)) {
                mat.highlightSquare(number);
            }
        }
    }
}

