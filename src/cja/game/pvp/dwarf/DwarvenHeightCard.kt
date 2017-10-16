package cja.game.pvp.dwarf

import cja.game.pvp.BasicStrikeCard
import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.SpecialCard

public class DwarvenHeightCard : SpecialCard {
    constructor(img : String) : super("Dwarven Height") {
        this.text = "Before playing moves: Put in play as a permanent.  After playing moves: All of your Basic Strikes gain a +1 defense counter.";
    }

    override fun play(state : EpicPvpGameState) {
        state.getPlayerPermanents(this.owner).add(this);
        state.getPlayerDiscard(this.owner).cards.remove(this);
        state.discardedCards.remove(this);
        state.specials.remove(this);
    }

    override fun afterMovePhase(state: EpicPvpGameState) {
        for(card in state.moveArea.moves.keySet()) {
            if(card.owner == this.owner && card is BasicStrikeCard) {
                card.defend++;
            } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner && state.moveArea.moves.get(card) is BasicStrikeCard) {
                state.moveArea.moves.get(card)!!.defend++;
            }
        }
    }

    override fun create(): EpicPvpCard {
        return DwarvenHeightCard("");
    }
}
