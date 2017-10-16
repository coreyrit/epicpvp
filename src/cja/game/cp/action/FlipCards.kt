package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.BuildRailCard
import cja.game.cp.CPGameState
import cja.game.cp.RailCard

class FlipCards : GameAction {
    var gs : CPGameState;

    constructor(gs : CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
        gs.cardsFlipped = !gs.cardsFlipped;
        for(deck in gs.railDecks) {
            for(c in deck.cards) {
                var card = c as RailCard;
                card.routes.clear();
                card.rotate();
                gs.addRoutes(card, card.railLetter, gs.cardsFlipped);
            }
        }
        for(card in gs.buildDeck.cards) {
            if(card is BuildRailCard) {
                card.routes.clear();
                card.rotate();
                gs.addRoutes(card, card.buildLetter, gs.cardsFlipped);
            }
        }
        for(card in gs.discardDeck.cards) {
            if(card is BuildRailCard) {
                card.routes.clear();
                card.rotate();
                gs.addRoutes(card, card.buildLetter, gs.cardsFlipped);
            }
        }
        for(hand in gs.players) {
            for(card in hand.hand) {
                if(card is BuildRailCard) {
                    card.routes.clear();
                    card.rotate();
                    gs.addRoutes(card, card.buildLetter, gs.cardsFlipped);
                }
            }
        }
        return true;
    }
}
