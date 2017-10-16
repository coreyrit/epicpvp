package cja.game

class HTMLCanvasRenderer : GameRenderer() {
    override fun createDisplay(w : Float, h : Float) : GameDisplay {
        return HTMLCanvasDisplay();
    }
}
