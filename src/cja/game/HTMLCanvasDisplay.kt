package cja.game

class HTMLCanvasDisplay : GameDisplay {
    override fun getGraphics() : GameGraphics {
        return HTMLCanvasGraphics();
    }
}
