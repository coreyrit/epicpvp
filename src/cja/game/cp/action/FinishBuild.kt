package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState

class FinishBuild : GameAction {

    var gs: CPGameState;

    constructor(gs: CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
        gs.nextPlayerTurn();
        return true;
    }
}
