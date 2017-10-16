package cja.game.cp

import cja.game.GameGraphics

class MoveCard : BuildCard {
    var moveYear : Int = 0;

    constructor(year : Int, front : String, back : String) : super(front, back) {
        this.moveYear = year;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!g.lastImageSuccess()) {
            if (isFront) {
                g.drawString1(moveYear.toString(), x + 5, y + 5, 12);
            } else {
                g.drawString1("?", x + 5, y + 5, 12);
            }
        }
    }
}