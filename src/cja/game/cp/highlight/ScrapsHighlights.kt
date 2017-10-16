package cja.game.cp.highlight

import cja.game.cp.CPGameState

class ScrapsHighlights : Highlights {
    var gs: CPGameState;

    constructor(gs: CPGameState) {
        this.gs = gs;
    }

    override fun highlight() {
        for(deck in gs.railDecks) {
            if(deck.cards.size > 0) {
                var card = deck.cards[0];
                if(!card.isFront) {
                    deck.highlight = true;
                }
            }
        }
    }
}

