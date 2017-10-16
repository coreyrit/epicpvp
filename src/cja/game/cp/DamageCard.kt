package cja.game.cp

import cja.game.GameGraphics

class DamageCard : MatCard {
    var damageColumn : Int = 0;

    constructor(column : Int, front : String, back : String) : super(front, back) {
        this.damageColumn = column;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(isFront) {
            g.drawString1("#" + damageColumn, x+5, y+5, 12);
        } else {
            g.drawString1("?", x+5, y+5, 12);
        }
    }
}