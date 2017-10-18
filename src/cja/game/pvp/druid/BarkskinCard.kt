package cja.game.pvp.druid

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard
import cja.game.pvp.SpecialCard

public class BarkskinCard : SpecialCard {

    constructor() : super("Barkskin") {
        this.text = "After you take damage from an attack of 7 or more: Gain two aggression";
    }

    // interrupt would be better but technically could see ahead of time that you'd want this
    override fun afterEndPhase(state : EpicPvpGameState) {
        for(card in state.discardedCards) {
            if(card is MoveCard && card.blockedBy == null && card.attack >= 7) {
                for(i in 1..2) {
                    state.getPlayerAggression(this.owner).aggression.add(state.drawCard(this.owner));
                }
            }
        }
    }

    override fun create() : EpicPvpCard {
        return BarkskinCard();
    }
}
