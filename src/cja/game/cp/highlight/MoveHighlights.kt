package cja.game.cp.highlight

import cja.game.cp.CPGameState
import cja.game.cp.YearMat

class MoveHighlights : Highlights {
    var gs: CPGameState;
    var myMat : YearMat;
    var pawnNumber : Int;

    constructor(gs : CPGameState, myMat : YearMat, pawnNumber : Int) {
        this.gs = gs;
        this.myMat = myMat;
        this.pawnNumber = pawnNumber;
    }

    override fun highlight() {
        gs.pawns[gs.playerTurn].highlight = true;
        var moves = gs.allowedMovement[pawnNumber];
        for(num in moves) {
            if(!myMat.isBlocked(num)) {
                myMat.highlightSquare(num);
            }
        }
        myMat.highlightSquare(pawnNumber);
        if(gs.players[gs.playerTurn].scientistName.equals("Physicist")) {
            for(num in 1..15) {
                if(!myMat.isBlocked(num)) {
                    myMat.highlightSquare(num);
                }
            }
        }
    }
}
