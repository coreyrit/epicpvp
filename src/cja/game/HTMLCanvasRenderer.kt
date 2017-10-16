package cja.game

import org.w3c.dom.events.Event

class HTMLCanvasRenderer : GameRenderer {

    var clickHandler : ((Event) -> dynamic)?
    constructor(handler : ((Event) -> dynamic)?) : super() {
        clickHandler = handler;
    }

    override fun createDisplay(w : Float, h : Float) : GameDisplay {
        return HTMLCanvasDisplay(clickHandler);
    }
}
