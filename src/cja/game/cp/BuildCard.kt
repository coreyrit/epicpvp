package cja.game.cp

import cja.game.GameCard
import cja.game.GameGraphics

open class BuildCard : GameCard {
    constructor(front : String, back : String) : super(front, back) {
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!isFront) {
            g.drawString1("?", x+5, y+5, 12);
        }
    }
}
