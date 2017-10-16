package cja.game.pvp

public interface CardProcess {
    fun startProcess(state : EpicPvpGameState);

    fun handleClick(state : EpicPvpGameState, ptx : Float, pty : Float) : Boolean;

    fun setHighlights(state : EpicPvpGameState);
}
