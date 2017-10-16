package cja.game

import kotlin.js.Date

open class GameDeck : GameObject {
    var rand : Random = Random(Date().getTime());
    var cards : MutableList<GameCard> = ArrayList<GameCard>();
    var drawFirst : Boolean = true;

    constructor(r : Random) {
        rand = r;
    }

    fun shuffle() {
        var temp = ArrayList<GameCard>();
        while(cards.size > 0) {
            var i = rand.nextInt(cards.size);
            temp.add(cards.removeAt(i));
        }
        cards = temp;
    }

    fun pop() : GameCard {
        return cards.removeAt(0);
    }

    override fun draw(g : GameGraphics) {
        g.drawRectangle1(x, y, width, height);
        if(cards.size > 0) {
            if(drawFirst) {
                cards[0].setPosition(x, y, cards[0].width, cards[0].height);
                cards[0].draw(g);
            } else {
                cards[cards.size-1].setPosition(x, y, cards[cards.size-1].width, cards[cards.size-1].height);
                cards[cards.size-1].draw(g);
            }

            //g.drawString1("(" + cards.size + ")", x+width-30, y+height-25, 12);
        }
    }
}