package cja.game

open class GamePawn : GameObject {
    var image : String = "";

    constructor(pawn : String) {
        this.image = pawn;
    }

    override fun draw(g : GameGraphics) {
        g.drawImage1(image, x, y, width, height);
    }
}