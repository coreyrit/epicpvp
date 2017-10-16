package cja.game

abstract class GameRenderer {
    fun render(gameState: GameState) : GameDisplay {
        var bmp = createDisplay(gameState.width, gameState.height);
        var g = bmp.getGraphics();
        g.fillRectangle1(0f, 0f, gameState.width, gameState.height, "White");
        for(obj in gameState.gameObjects.reversed()) { // draw in reverse order
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