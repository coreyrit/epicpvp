package cja.game

import org.w3c.dom.events.Event

class HTMLCanvasDisplay : GameDisplay {

    var clickHandler : ((Event) -> dynamic)?
    constructor(handler : ((Event) -> dynamic)?) {
        clickHandler = handler;
    }

    override fun getGraphics() : GameGraphics {
        return HTMLCanvasGraphics(clickHandler);
    }
}
