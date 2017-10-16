package cja.game.cp

import cja.game.GameGraphics
import cja.game.GamePawn

class ScientistPawn : GamePawn {
    var color : String = "";

    constructor(img : String, color : String) : super(img) {
        this.color = color;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g)
        if(!g.lastImageSuccess()) {
            g.fillRectangle1(x, y, width, height, color);
        }
    }
}
