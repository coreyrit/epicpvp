package cja.game.pvp.goblin

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.MoveCard

public class ThrowingDaggerCard : MoveCard {

    constructor(img : String) : super("Throwing Dagger", 1, 1, 1) {
        this.text = "Assign blocks: Your enemy cannot assign moves with a printed cost of 4 or higher to block this attack.";
    }

    override fun validBlock(card : MoveCard) : Boolean {
        return card.cost < 4;
    }

    override fun create() : EpicPvpCard {
        return ThrowingDaggerCard("");
    }
}
