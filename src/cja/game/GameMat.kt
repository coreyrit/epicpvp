package cja.game

open class GameMat : GameObject {
    var frontImage : String = "";
    var backImage : String = "";

    var isFront : Boolean = true;

    constructor(front : String?, back : String?) {
        if(front != null) frontImage = front;
        if(back != null) backImage = back;
    }

    override fun draw(g : GameGraphics) {
        if(isFront) {
            g.drawImage1(frontImage, x, y, width, height);
        } else {
            g.drawImage1(backImage, x, y, width, height);
        }
    }
}
