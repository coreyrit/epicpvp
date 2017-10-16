package cja.game.pvp

import cja.game.GameDeck
import cja.game.GameGraphics
import cja.game.Random

public class LifeDeck : GameDeck {

    constructor(r : Random) : super(r) {
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        g.drawString1(cards.size.toString(), x + width / 2 - 3f, y + height/2 - 8, 12);
    }
}
