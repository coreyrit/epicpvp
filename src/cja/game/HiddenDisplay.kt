package cja.game

class HiddenDisplay : GameDisplay {
    override fun getGraphics(game : Game) : GameGraphics {
        return HiddenGraphics();
    }
}