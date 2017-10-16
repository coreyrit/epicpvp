package cja.game

interface GameGraphics {
    fun drawImage1(img : String?, x : Float, y : Float, w : Float, h : Float);
    fun drawImage2(img : String?, x : Float, y : Float, w : Float, h : Float, flipped : Boolean);

    fun fillRectangle1(x : Float, y : Float, w : Float, h : Float, color : String);
    fun fillRectangle2(x : Float, y : Float, w : Float, h : Float, color : String, a : Float);

    fun drawRectangle1(x : Float, y : Float, w : Float, h : Float);
    fun drawRectangle2(x : Float, y : Float, w : Float, h : Float, color : String);
    fun drawRectangle3(x : Float, y : Float, w : Float, h : Float, color : String, thick : Int);

    fun drawString1(str : String, x : Float, y : Float, size : Int);
    fun drawString2(str : String, x : Float, y : Float, w : Float, h : Float, size : Int);

    fun drawLine1(x1 : Float, y1 : Float, x2 : Float, y2 : Float);
    fun drawLine2(x1 : Float, y1 : Float, x2 : Float, y2 : Float, thick : Int);
    fun drawLine3(x1 : Float, y1 : Float, x2 : Float, y2 : Float, color : String, thick : Int);

    fun lastImageSuccess() : Boolean;
}