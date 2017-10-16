package cja.game

abstract class GameObject {
    var x : Float = 0f;
    var y : Float = 0f;
    var width : Float = 0f;
    var height : Float = 0f;
    var highlight : Boolean = false;

    fun setPosition(x : Float, y : Float, width : Float, height : Float) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    abstract fun draw(g : GameGraphics);
}
