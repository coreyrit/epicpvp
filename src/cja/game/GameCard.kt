package cja.game

open class GameCard : GameObject {
    var frontImage : String = "";
    var backImage : String = "";

    var isRotated : Boolean = false;
    var isFront : Boolean = true;

    constructor(front : String?, back : String?) {
        if(front != null) this.frontImage = front;
        if(back != null) this.backImage = back;
    }

    fun rotate() {
        isRotated = !isRotated;
    }

    override fun draw(g : GameGraphics) {
        if(isFront) {
            g.drawImage2(frontImage, x, y, width, height, isRotated);
        } else {
            g.drawImage2(backImage, x, y, width, height, isRotated);
        }
        if(!g.lastImageSuccess()) {
            g.drawRectangle1(x, y, width, height);
        }
    }
}
