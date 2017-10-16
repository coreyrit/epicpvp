package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.*
import java.util.*

class DrawBuildCard : GameAction {

    var gs : CPGameState;

    constructor(gs : CPGameState) {
        this.gs = gs;
    }

    override fun execute() : Boolean {
        var card = gs.buildDeck.pop();
        card.setPosition(card.x, card.y, 90f, 125f);
        card.isFront = true;

        if(gs.buildDeck.cards.size() == 0) {
            while(gs.discardDeck.cards.size() > 0) {
                var gc = gs.discardDeck.pop();
                gc.isFront = false;
                gs.buildDeck.cards.add(gc);
            }
            gs.buildDeck.shuffle();
        }

        if(card is MoveCard) {
            var my = when(card.moveYear) {
                1930 -> MoveYear(gs.yearMats[0].moveCargo(), 0);
                1957 -> MoveYear(gs.yearMats[1].moveCargo(), 1);
                1984 -> MoveYear(gs.yearMats[2].moveCargo(), 2);
                2011 -> MoveYear(gs.yearMats[3].moveCargo(), 3);
                else -> MoveYear(MoveResult.Safe, -1);
            }

            if(gs.yearMats[my.year].cargoLocation == RailLocation.Finish) {
                gs.discardDeck.cards.add(card);
            } else {

                gs.moveCards.add(card); // keep track of drawn move cards
                gs.discardDeck.cards.add(card);

                if(gs.moveCards.size() >= 3) { // shuffle movement back in after 3 draws
                    gs.turnAction = TurnAction.ShuffleInMovement;
                } else {
                    gs.turnAction = TurnAction.TimeTravel;
                }
            }

            if(my.move != MoveResult.Safe) {
                gs.moveResult = my.move;
                gs.moveYear = card.moveYear;
                gs.turnAction = TurnAction.GameOver;
            }

            var win = true;
            for(mat in gs.yearMats) {
                if(mat.cargoLocation != RailLocation.Finish) {
                    win = false;
                }
            }
            if(win) {
                gs.turnAction = TurnAction.GameOver;
                gs.moveResult = MoveResult.Win;
            }
        } else {
            gs.players[gs.playerTurn].hand.add(card);
        }

        if(gs.turnAction != TurnAction.GameOver && gs.turnAction != TurnAction.ShuffleInMovement) {
            var bonus = 0;
            if(gs.players[gs.playerTurn].scientistName.equals("Geologist")) {
                bonus = 1;
            }
            if(gs.players[gs.playerTurn].hand.size() > gs.maxCards + bonus) {
                gs.turnAction = TurnAction.DiscardBuild;
            } else {
                // Damage now comes from drawn build cards!

//                if(gs.players[gs.playerTurn].scientistName.equals("Chemist")) {
                    gs.turnAction = TurnAction.TimeTravel;
//                } else {
//                    gs.turnAction = TurnAction.DrawDamage;
//                }
            }
        }

        // check for damage
        if(!gs.players[gs.playerTurn].scientistName.equals("Chemist") && card is BuildRailCard) {
//            var damageCard = DamageCard(card.damageColumn, "", "");
//            var damageCard = gs.damageGems[card.damageColumn-1];
            var damage = when (card.damageColumn) {
                1, 2, 3 -> gs.yearMats[0].checkForDamage(card.damageColumn);
                4, 5, 6 -> gs.yearMats[1].checkForDamage(card.damageColumn - 3);
                7, 8, 9 -> gs.yearMats[2].checkForDamage(card.damageColumn - 6);
                10, 11, 12 -> gs.yearMats[3].checkForDamage(card.damageColumn - 9);
                else -> false;
            }
            if(damage) {
                gs.lastDrawBuildCard = card;
                gs.turnAction = TurnAction.DrawDamage;
            }
        }

        return true;
    }
}
