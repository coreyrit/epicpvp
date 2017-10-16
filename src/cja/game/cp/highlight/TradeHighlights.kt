package cja.game.cp.highlight

import cja.game.cp.CPGameState
import cja.game.cp.YearMat

class TradeHighlights : Highlights {
    var gs: CPGameState;
    var myMat : YearMat;

    constructor(gs: CPGameState, myMat : YearMat) {
        this.gs = gs;
        this.myMat = myMat;
    }

    override fun highlight() {
        var tradeValid = true;
        for(i in 0..gs.players.size-1) {
            var hisMat = gs.getPawnMat(gs.pawns[i]);
            if(hisMat == myMat) {
                gs.players[i].highlight = true;
            }
            var bonus = 0;
            if(gs.players[i].scientistName.equals("Geologist")) {
                bonus = 1;
            }
            if(gs.players[i].hand.size > gs.maxCards+bonus) {
                tradeValid = false;
            }
        }
        if(gs.players[gs.playerTurn].scientistName.equals("Astronomer")) {
            for(player in gs.players) {
                player.highlight = true;
            }
        }
        gs.tradeButton.highlight = tradeValid;
    }
}
