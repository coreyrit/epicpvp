package cja.game

class HiddenRenderer : GameRenderer() {
    override fun createDisplay(w : Float, h : Float) : GameDisplay {
        return HiddenDisplay();
    }
}