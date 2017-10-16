package cja.game.cp

import cja.game.GameGraphics

class ScrapsCard : MatCard {
    constructor(front : String, back : String) : super(front, back) {
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!g.lastImageSuccess()) {
            if (isFront) {
                g.drawString1("Scrap", x + 5, y + 5, 12);
            } else {
                g.drawString1("Used", x + 5, y + 5, 12);
            }
        }
    }
}
