package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.ScrapsCard
import cja.game.cp.TurnAction

class BuildScraps : GameAction {

    var gs: CPGameState;
    var scrapsCard : ScrapsCard;

    constructor(gs: CPGameState, scrapsCard : ScrapsCard) {
        this.gs = gs;
        this.scrapsCard = scrapsCard;
    }

    override fun execute(): Boolean {
        gs.turnAction = TurnAction.ScrapsBuild;
        scrapsCard.isFront = false;
        return true;
    }
}
