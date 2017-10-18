package cja.game.pvp.druid

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class CallOfTheWildCard : MoveCard {

    constructor() : super("Call of the Wild", 1, 1, 1) {
        this.text = "After draw phase: This move gains a +1 attack counter for each card your enemy drew from their aggression pile.";
    }

    override fun afterDrawPhase(state : EpicPvpGameState) {
        this.attack += state.aggressionDrawn;
    }

    override fun create() : EpicPvpCard {
        return CallOfTheWildCard();
    }
}
