package cja.game

class GameMessage : GameObject {
    var message : String = "";
    var fillBackground : Boolean = false;

    constructor(msg : String, fill : Boolean) {
        this.message = msg;
        this.fillBackground = fill;
    }

    override fun draw(g : GameGraphics) {
        if(fillBackground) {
            g.fillRectangle1(x, y, width, height, "White");
        }
        g.drawRectangle1(x, y, width, height);
        g.drawString1(message, x, y, 14);
    }
}
