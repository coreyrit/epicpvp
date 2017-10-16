package cja.game

class HTMLCanvasDisplay : GameDisplay {
    override fun getGraphics(game : Game) : GameGraphics {
        return HTMLCanvasGraphics(game);
    }
}
