package cja.game.cp

import cja.game.GameGraphics

class WildCard : BuildCard {
    constructor(front : String, back : String) : super(front, back) {
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!g.lastImageSuccess()) {
            if (isFront) {
                g.drawString1("Wild", x + 5, y + 5, 12);
            } else {
                g.drawString1("?", x + 5, y + 5, 12);
            }
        }
    }
}