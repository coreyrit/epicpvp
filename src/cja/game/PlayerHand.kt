package cja.game

import java.util.*

open class PlayerHand : GameObject {
    var playerColor : String = "";
    var hand : MutableList<GameCard> = ArrayList<GameCard>();

    constructor(color : String) {
        this.playerColor = color;
    }

    fun getCardAt(x : Float, y : Float) : GameCard? {
        for(card in hand) {
            if(x >= card.x && x <= card.x+card.width && y >= card.y && y <= card.y+card.height) {
                return card;
            }
        }
        return null;
    }

    override fun draw(g : GameGraphics) {
        g.fillRectangle1(x, y, width, height, "white");
        g.drawRectangle3(x, y, width, height, playerColor, 3);
        var xx = x+5;
        var yy = y+5;
        for(card in hand) {
            card.setPosition(xx, yy, card.width, card.height);
            card.draw(g);
            xx += card.width+5;

            if(xx >= x + width) {
                yy += card.height+5;
                xx = x+5;
            }
        }
    }
}