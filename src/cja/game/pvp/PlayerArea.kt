package cja.game.pvp

import cja.game.GameDeck
import cja.game.PlayerHand
import cja.game.Random
import java.util.*

public class PlayerArea {
    var r : Random = Random(Date().getTime());

    var playerRace : RaceCard;
    var playerClass : ClassCard;
    var playerDeck : GameDeck = GameDeck(r);
    var playerDiscard : GameDeck = GameDeck(r);
    var playerHand : PlayerHand;
    var playerLife : LifeDeck = LifeDeck(r);
    var playerAggression : Aggression;
    var playerPermanents : MutableList<EpicPvpCard> = ArrayList();

    constructor(color : String, raceCard : RaceCard, classCard : ClassCard) {
        playerRace = raceCard;
        playerClass = classCard;
        playerHand = PlayerHand(color);
        playerAggression = Aggression(color);
    }

    fun clone(state : EpicPvpGameState) : PlayerArea {
        var playerArea = PlayerArea(playerHand.playerColor, playerRace.create() as RaceCard, playerClass.create() as ClassCard);
        playerArea.playerRace.id = playerRace.id;
        playerArea.playerClass.id = playerClass.id;
        state.allCards.put(playerArea.playerRace.id, playerArea.playerRace);
        state.allCards.put(playerArea.playerClass.id, playerArea.playerClass);

        playerArea.playerDeck.highlight = playerDeck.highlight;
        playerArea.playerDeck.drawFirst = playerDeck.drawFirst;
        playerArea.playerDeck.setPosition(playerDeck.x, playerDeck.y, playerDeck.width, playerDeck.height);
        for(card in playerDeck.cards) {
            var pvp = (card as EpicPvpCard).create();
            card.clonePropertiesTo(pvp);
            playerArea.playerDeck.cards.add(pvp);

            state.allCards.put(pvp.id, pvp);
        }
        playerArea.playerDiscard.highlight = playerDiscard.highlight;
        playerArea.playerDiscard.drawFirst = playerDiscard.drawFirst;
        playerArea.playerDiscard.setPosition(playerDiscard.x, playerDiscard.y, playerDiscard.width, playerDiscard.height);
        for(card in playerDiscard.cards) {
            var pvp = (card as EpicPvpCard).create();
            card.clonePropertiesTo(pvp);
            playerArea.playerDiscard.cards.add(pvp)

            state.allCards.put(pvp.id, pvp);
        }
        playerArea.playerHand.highlight = playerHand.highlight;
        playerArea.playerHand.setPosition(playerHand.x, playerHand.y, playerHand.width, playerHand.height);
        for(card in playerHand.hand) {
            var pvp = (card as EpicPvpCard).create();
            card.clonePropertiesTo(pvp);
            playerArea.playerHand.hand.add(pvp)

            state.allCards.put(pvp.id, pvp);
        }
        playerArea.playerLife.highlight = playerLife.highlight;
        playerArea.playerLife.drawFirst = playerLife.drawFirst;
        playerArea.playerLife.setPosition(playerLife.x, playerLife.y, playerLife.width, playerLife.height);
        for(card in playerLife.cards) {
            var pvp = (card as EpicPvpCard).create();
            card.clonePropertiesTo(pvp);
            playerArea.playerLife.cards.add(pvp)

            state.allCards.put(pvp.id, pvp);
        }
        playerArea.playerAggression.highlight = playerAggression.highlight;
        playerArea.playerAggression.setPosition(playerAggression.x, playerAggression.y, playerAggression.width, playerAggression.height);
        for(card in playerAggression.aggression) {
            var pvp = card.create();
            card.clonePropertiesTo(pvp);
            playerArea.playerAggression.aggression.add(pvp)

            state.allCards.put(pvp.id, pvp);
        }
        for(card in playerPermanents) {
            var pvp = card.create();
            card.clonePropertiesTo(pvp);
            playerArea.playerPermanents.add(pvp)

            state.allCards.put(pvp.id, pvp);
        }

        return playerArea;
    }
}
