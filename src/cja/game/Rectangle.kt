package cja.game

data class Rectangle(val x : Float, val y : Float, val width : Float, val height : Float) {
    fun contains(ptx : Float, pty : Float) : Boolean {
        return ptx >= x && ptx <= x+width && pty >= y && pty <= y+height;
    }
}