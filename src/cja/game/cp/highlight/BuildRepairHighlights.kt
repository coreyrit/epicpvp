package cja.game.cp.highlight

import cja.game.cp.*

class BuildRepairHighlights : Highlights {
    var gs : CPGameState;
    var myMat : YearMat;
    var pawnNumber : Int;

    constructor(gs : CPGameState, myMat : YearMat, pawnNumber : Int) {
        this.gs = gs;
        this.myMat = myMat;
        this.pawnNumber = pawnNumber;
    }

    override fun highlight() {
        myMat.highlightSquare(pawnNumber);
        var empty = myMat.isEmpty(pawnNumber);
        var anyUnavailable = false;
        for(deck in gs.railDecks) {
            if(deck.cards.size() > 0) {
                var card = deck.cards[0] as RailCard;
                if(card.isFront) {
                    var count = 0;
                    for(build in gs.players[gs.playerTurn].hand) {
                        if(build is BuildRailCard && build.buildLetter.equals(card.railLetter)) {
                            count++;
                        }
                        if(build is WildCard) {
                            count++;
                        }
                    }

                    if(empty) {
                        if(myMat == gs.yearMats[0] && count >= 3) {
                            deck.highlight = true;
                        } else if((myMat == gs.yearMats[1] || myMat == gs.yearMats[2]) && count >= 2) {
                            deck.highlight = true;
                        } else if (myMat == gs.yearMats[3] && count >= 1) {
                            deck.highlight = true;
                        }
                    }
                } else {
                    anyUnavailable = true;
                }
            }
        }

        if(anyUnavailable && gs.players[gs.playerTurn].hand.size() > 0) {
            for(card in gs.scrapCards) {
                if(card.isFront) {
                    card.highlight = true;
                }
            }
        }

        gs.buildButton.highlight = true;

        if(myMat.isDamaged(pawnNumber)) {
            gs.repairButton.highlight = true;
        }
    }
}
