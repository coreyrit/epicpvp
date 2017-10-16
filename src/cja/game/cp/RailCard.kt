package cja.game.cp

import cja.game.GameGraphics
import cja.game.Point

class RailCard : MatCard {
    var rotated : Boolean = false;
    var railLetter : String = "";
    var unavailable : String = "";

    var routes : MutableMap<RailLocation, RailLocation> = HashMap<RailLocation, RailLocation>();

    constructor(letter : String, front : String, unav : String) : super(front, front) {
        this.railLetter = letter;
        this.unavailable = unav;
    }

    fun addRoute(a : RailLocation, b : RailLocation) {
        routes.put(a, b);
        routes.put(b, a);
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!isFront) {
            g.fillRectangle2(x, y, width, height, "rgba(0, 0, 0, 0.33)", 0.5f);
            g.drawImage1(unavailable, x, y, width, 10f);
        }
        if(!g.lastImageSuccess()) {
            for (src in routes.keys) {
                var a = getLocationPoint(src);
                var b = getLocationPoint(routes[src]);
                if (isFront) {
                    g.drawLine3(x + a.x, y + a.y, x + b.x, y + b.y, "Black", 3);
                } else {
                    g.drawLine3(x + a.x, y + a.y, x + b.x, y + b.y, "Red", 3);
                }
            }
            if (isFront) {
                g.drawString1(railLetter, x + 5, y + 5, 12);
            } else {
                g.drawRectangle2(x, y, width, height, "Red");
                g.drawString1(railLetter, x + 5, y + 5, 12);
            }
        }
    }

    fun getLocationPoint(loc : RailLocation?) : Point {
        if(rotated) {
            return when(loc) {
                RailLocation.TopRight -> Point(width / 3, height);
                RailLocation.TopLeft -> Point(width*2/3, height);
                RailLocation.CenterRight -> Point(0f, height/2-7);
                RailLocation.CenterLeft -> Point(width, height/2-7);
                RailLocation.BottomRight -> Point(width/3, 0f);
                RailLocation.BottomLeft -> Point(width*2/3, 0f);
                else -> Point(-10f, -10f);
            }
        } else {
            return when(loc) {
                RailLocation.BottomLeft -> Point(width / 3, height);
                RailLocation.BottomRight -> Point(width*2/3, height);
                RailLocation.CenterLeft -> Point(0f, height/2-7);
                RailLocation.CenterRight -> Point(width, height/2-7);
                RailLocation.TopLeft -> Point(width/3, 0f);
                RailLocation.TopRight -> Point(width*2/3, 0f);
                else -> Point(-10f, -10f);
            }
        }
    }
}
