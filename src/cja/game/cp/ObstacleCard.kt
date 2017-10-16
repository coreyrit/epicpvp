package cja.game.cp

import cja.game.GameGraphics

class ObstacleCard : MatCard {
    constructor(front : String?, back : String?) : super(front, back) {
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!g.lastImageSuccess()) {
            g.drawString1("XXX", x + 5, y + 5, 12);
        }
    }
}
