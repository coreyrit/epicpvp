package cja.game.pvp.human

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class QuickStabCard : MoveCard {

    constructor() : super("Quick Stab", 1, 1, 2) {
        this.text = "End phase: If this attack was blocked, and your enemy took damage from another attack, your enemy takes 1 damage.";
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        if (blockedBy != null) {
            if(state.damageTaken > 0) {
                state.dealDamage(!this.owner);
            }
        }
    }

    override fun create() : EpicPvpCard {
        return QuickStabCard();
    }
}
