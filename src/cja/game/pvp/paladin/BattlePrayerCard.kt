package cja.game.pvp.paladin

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.SpecialCard

public class BattlePrayerCard : SpecialCard {

    constructor() : super("Battle Prayer") {
        this.text = "After you block: If one of your blocks is at least three higher than the attack its blocking, gain 1 life and draw a card.";
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        for(card in state.moveArea.moves.keys) {
            if(card.owner == this.owner && card.delta >= 3) {
                state.getPlayerLife(this.owner).cards.add(state.drawCard(this.owner));
                var draw = state.drawCard(this.owner);
                draw.isFront = true;
                state.getPlayerHand(this.owner).hand.add(draw);
            }
        }
    }

    override fun create() : EpicPvpCard {
        return BattlePrayerCard();
    }
}
