package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.TurnAction

class FinishTrade : GameAction {

    var gs: CPGameState;

    constructor(gs: CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
        gs.turnAction = TurnAction.BuildRepair;
        return true;
    }
}
