package cja.game.pvp

import cja.game.pvp.druid.*
import cja.game.pvp.dwarf.*
import cja.game.pvp.goblin.*
import cja.game.pvp.human.*
import cja.game.pvp.paladin.*
import java.util.*

public class EpicPvpSetup {

    fun druidDeck() : List<EpicPvpCard> {
        var deck = ArrayList<EpicPvpCard>();
        deck.add(MurderOfCrowsCard(""));
        deck.add(BarkskinCard(""));
        for(i in 1..3) {
            deck.add(BasicStrikeCard(3, 3, 3));
        }
        for(i in 1..3) {
            deck.add(BasicStrikeCard(4, 4, 4));
        }
        for(i in 1..2) {
            deck.add(BasicStrikeCard(7, 7, 7));
        }
        for(i in 1..3) {
            deck.add(CallOfTheWildCard(""));
        }
        for(i in 1..3) {
            deck.add(TanglevinesCard(2, 2, 2));
        }
        for(i in 1..2) {
            deck.add(TanglevinesCard(6, 6, 6));
        }
        for(i in 1..2) {
            deck.add(WolfSpiritCard(""));
        }
        return deck;
    }

    fun goblinDeck() : List<EpicPvpCard> {
        var deck = ArrayList<EpicPvpCard>();
        deck.add(ShadowCloakCard(""));
        deck.add(SkitterAwayCard(""));
        for(i in 1..3) {
            deck.add(BasicStrikeCard(3, 3, 3));
        }
        for(i in 1..3) {
            deck.add(BasicStrikeCard(5, 5, 5));
        }
        for(i in 1..4) {
            deck.add(ThrowingDaggerCard(""));
        }
        for(i in 1..3) {
            deck.add(GoForTheKneesCard(""));
        }
        for(i in 1..3) {
            deck.add(GleefulSlashCard(4, 3, 4));
        }
        for(i in 1..2) {
            deck.add(GleefulSlashCard(6, 5, 6));
        }
        return deck;
    }

    fun humanDeck() : List<EpicPvpCard> {
        var deck = ArrayList<EpicPvpCard>();
        deck.add(PowerStrikeCard(""));
        deck.add(PainIsMyTeacherCard(""));
        for(i in 1..3) {
            deck.add(BasicStrikeCard(3, 3, 3));
        }
        for(i in 1..2) {
            deck.add(BasicStrikeCard(6, 6, 6));
        }
        for(i in 1..2) {
            deck.add(BasicStrikeCard(7, 7, 7));
        }
        deck.add(BasicStrikeCard(8, 8, 8));
        for(i in 1..3) {
            deck.add(QuickStabCard(""));
        }
        for(i in 1..2) {
            deck.add(ViciousElbowCard(2, 2, 2));
        }
        for(i in 1..3) {
            deck.add(ViciousElbowCard(4, 4, 4));
        }
        for(i in 1..2) {
            deck.add(MirrorStrikeCard(""));
        }
        return deck;
    }

    fun paladinDeck() : List<EpicPvpCard> {
        var deck = ArrayList<EpicPvpCard>();
        deck.add(BattlePrayerCard(""));
        deck.add(CallLightningCard(""));
        for(i in 1..3) {
            deck.add(BasicStrikeCard(2, 2, 2));
        }
        for(i in 1..2) {
            deck.add(BasicStrikeCard(4, 4, 4));
        }
        for(i in 1..2) {
            deck.add(BasicStrikeCard(6, 6, 6));
        }
        deck.add(BasicStrikeCard(7, 7, 7));
        deck.add(BasicStrikeCard(8, 8, 8));
        for(i in 1..3) {
            deck.add(BlessedBladeCard(""));
        }
        for(i in 1..3) {
            deck.add(HolyStrikeCard(3, 3, 3));
        }
        for(i in 1..2) {
            deck.add(HolyStrikeCard(5, 5, 5));
        }
        deck.add(CircleSlashCard(""));
        return deck;
    }

    fun dwarfDeck() : List<EpicPvpCard> {
        var deck = ArrayList<EpicPvpCard>();
        deck.add(BarrelChargeCard(""));
        deck.add(DwarvenHeightCard(""));
        for(i in 1..4) {
            deck.add(BasicStrikeCard(3, 3, 3));
        }
        for(i in 1..2) {
            deck.add(BasicStrikeCard(6, 6, 6));
        }
        deck.add(BasicStrikeCard(8, 8, 8));
        deck.add(BasicStrikeCard(9, 9, 9));
        for(i in 1..2) {
            deck.add(AncientGrudgeCard(""));
        }
        for(i in 1..3) {
            deck.add(HammerSmashCard(""));
        }
        for(i in 1..3) {
            deck.add(ToughCard(2, 2, 5))
        }
        for(i in 1..2) {
            deck.add(ToughCard(5, 5, 9))
        }
        return deck;
    }

    fun setup(state : EpicPvpGameState) {
        // create decks for both players
        //state.player1deck.cards.addAll(goblinDeck());
        state.player1deck.cards.addAll(dwarfDeck());
        state.player1deck.cards.addAll(druidDeck());
        state.player1deck.shuffle();

        state.player2deck.cards.addAll(humanDeck());
        state.player2deck.cards.addAll(paladinDeck());
        state.player2deck.shuffle();

        // init card sizes and ids
        var id = 1;
        for(card in state.player1deck.cards) {
            card.setPosition(0f, 0f, 100f, 135f);
            card.isFront = false;
            (card as EpicPvpCard).id = id++;
            state.allCards.put(card.id, card);
        }
        for(card in state.player2deck.cards) {
            card.setPosition(0f, 0f, 100f, 135f);
            card.isFront = false;
            (card as EpicPvpCard).id = id++;
            state.allCards.put(card.id, card);
        }
        state.player1race.id = id++;
        state.player1class.id = id++;
        state.player2race.id = id++;
        state.player2class.id = id++;
        state.allCards.put(state.player1race.id, state.player1race);
        state.allCards.put(state.player1class.id, state.player1class);
        state.allCards.put(state.player2race.id, state.player2race);
        state.allCards.put(state.player2class.id, state.player2class);


        var player1y = 10f;
        var player2y = 650f;

        // race and class cards
        state.player1race.setPosition(870f, player1y, 100f, 135f);
        state.player1class.setPosition(970f, player1y, 100f, 135f);
        state.gameObjects.add(state.player1race);
        state.gameObjects.add(state.player1class);
        state.player2race.setPosition(870f, player2y, 100f, 135f);
        state.player2class.setPosition(970f, player2y, 100f, 135f);
        state.gameObjects.add(state.player2race);
        state.gameObjects.add(state.player2class);

        // place the decks
        state.player1deck.setPosition(10f, player1y, 100f, 135f);
        state.gameObjects.add(state.player1deck);
        state.player2deck.setPosition(10f, player2y - 155f, 100f, 135f);
        state.gameObjects.add(state.player2deck);

        // draw life deck
        for(i in 1..state.player1race.life) {
            state.player1life.cards.add(state.player1deck.pop());
        }
        state.player1life.setPosition(1080f, player1y, 100f, 135f);
        state.gameObjects.add(state.player1life);

        for(i in 1..state.player2race.life) {
            state.player2life.cards.add(state.player2deck.pop());
        }
        state.player2life.setPosition(1080f, player2y, 100f, 135f);
        state.gameObjects.add(state.player2life);

        // discard
        state.player1discard.setPosition(10f, player1y + 150f, 100f, 135f);
        state.gameObjects.add(state.player1discard);
        state.player2discard.setPosition(10f, player2y, 100f, 135f);
        state.gameObjects.add(state.player2discard);

        // draw player hands
        for(i in 1..5) {
            var card1 = state.player1deck.pop();
            card1.isFront = true;
            state.player1hand.hand.add(card1);
            var card2 = state.player2deck.pop();
            card2.isFront = true;
            state.player2hand.hand.add(card2);
        }
        state.player1hand.setPosition(120f, player1y-5f, 740f, 290f);
        state.gameObjects.add(state.player1hand);
        state.player2hand.setPosition(120f, player2y-155f, 740f, 290f);
        state.gameObjects.add(state.player2hand);

        // aggression
        state.player1aggression.setPosition(870f, player1y + 140f, 310f, 145f);
        state.gameObjects.add(state.player1aggression);
        state.player2aggression.setPosition(870f, player2y - 155f, 310f, 145f);
        state.gameObjects.add(state.player2aggression);

        // move area
        state.moveArea.setPosition(10f, 325f, 1170f, 145f);
        state.gameObjects.add(state.moveArea);

        if(state.player1race.initiative + state.player1class.initiative > state.player2race.initiative + state.player2class.initiative) {
            state.player1turn = true;
        } else {
            state.player1turn = false;
        }
    }
}
