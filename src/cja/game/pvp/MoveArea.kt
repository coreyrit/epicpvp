package cja.game.pvp

import cja.game.GameGraphics
import cja.game.GameObject

public class MoveArea : GameObject {
    var moves : MutableMap<MoveCard, MoveCard?> = LinkedHashMap();

    constructor(color : String) {

    }

    fun getCardAt(x : Float, y : Float) : MoveCard? {
        for(card in moves.keys) {
            var defend = moves.get(card);
            if(defend != null && x >= defend.x && x <= defend.x+card.width && y >= defend.y && y <= defend.y+defend.height) {
                return defend;
            }
            if(x >= card.x && x <= card.x+card.width && y >= card.y && y <= card.y+card.height) {
                return card;
            }
        }
        return null;
    }

    fun assignMove(attack : MoveCard, defend : MoveCard?) {
        moves.put(attack, defend);
    }

    override fun draw(g : GameGraphics) {
        g.drawRectangle3(x, y, width, height, "black", 3);
        var xx = x+5;
        var yy = y+5;
        for(card in moves.keys) {
            card.setPosition(xx, yy, card.width, card.height);
            card.draw(g);

            if(moves.get(card) != null) {
                g.fillRectangle1(xx+15, yy, card.width, card.height, "white");
                moves.get(card)!!.setPosition(xx + 15, yy, card.width, card.height);
                moves.get(card)!!.draw(g);
            }

            xx += card.width+30;

            if(xx >= x + width) {
                yy += card.height+5;
                xx = x+5;
            }
        }
    }

    fun clone(state : EpicPvpGameState) : MoveArea {
        var moveArea = MoveArea("black");
        moveArea.highlight = highlight;
        moveArea.x = x;
        moveArea.y = y;
        moveArea.width = width;
        moveArea.height = height;
        for(card in moves.keys) {
            var pvp = card.create();
            card.clonePropertiesTo(pvp);
            state.allCards.put(pvp.id, pvp);
            if(moves.get(card) != null) {
                var defend = moves.get(card)!!.create();
                moves.get(card)!!.clonePropertiesTo(defend);
                state.allCards.put(defend.id, defend);
                moveArea.assignMove(pvp as MoveCard, defend as MoveCard);
            } else {
                moveArea.assignMove(pvp as MoveCard, null);
            }
        }
        return moveArea;
    }
}
