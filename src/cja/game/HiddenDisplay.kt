package cja.game

class HiddenDisplay : GameDisplay {
    override fun getGraphics() : GameGraphics {
        return HiddenGraphics();
    }
}