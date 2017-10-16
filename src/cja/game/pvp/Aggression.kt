package cja.game.pvp

import cja.game.GameCard
import cja.game.GameGraphics
import cja.game.GameObject

public class Aggression : GameObject {
    var playerColor : String = "";
    var aggression : MutableList<EpicPvpCard> = ArrayList();

    constructor(color : String) {
        this.playerColor = color;
    }

    fun getCardAt(x : Float, y : Float) : GameCard? {
        for(card in aggression.reversed()) { // move backwards to account for overlap
            if(x >= card.x && x <= card.x+card.width && y >= card.y && y <= card.y+card.height) {
                return card;
            }
        }
        return null;
    }

    override fun draw(g : GameGraphics) {
        g.drawRectangle3(x, y, width, height, playerColor, 3);

        var offset = 0f;
        if(aggression.size > 1) {
            offset = width / aggression.size;
            var delta = 150f - offset;
            offset -= delta / aggression.size;
        }
        if(offset > 80f) {
            offset = 80f;
        }

        var xx = x + 5;
        var yy = y + 5;
        for (card in aggression) {
            card.setPosition(xx, yy, card.width, card.height);
            g.fillRectangle1(xx, yy, card.width, card.height, "white");
            card.draw(g);
            xx += offset;
        }

        g.drawString1(aggression.size.toString(), x + width-15f, y + height-15f, 12);
    }
}
