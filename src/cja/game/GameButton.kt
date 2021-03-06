package cja.game

open class GameButton : GameObject {
    var buttonLabel : String;
    var fill: Boolean = true;

    constructor(label : String) {
        this.buttonLabel = label;
    }

    override fun draw(g : GameGraphics) {
        if(fill) {
            g.fillRectangle1(x, y, width, height, "white");
        }
        g.drawRectangle1(x, y, width, height);
        g.drawString1(buttonLabel, x+2, y+2, 14);
    }
}
