package cja.game

abstract class GameRenderer {
    fun render(game : Game) : GameDisplay {
        var bmp = createDisplay(game.gameState.width, game.gameState.height);
        var g = bmp.getGraphics(game);
        g.fillRectangle1(0f, 0f, game.gameState.width, game.gameState.height, "White");
        for(obj in game.gameState.gameObjects.reversed()) { // draw in reverse order
            obj.draw(g);
            if(obj.highlight) {
                //println("highlighting " + obj);
                g.drawRectangle3(obj.x-2, obj.y-2, obj.width+4, obj.height+4, "Black", 5);
            }
        }
        return bmp;
    }

    abstract fun createDisplay(w : Float, h : Float) : GameDisplay;
}