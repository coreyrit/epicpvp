package cja.game

import org.w3c.dom.*
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.js.Math

class HTMLCanvasGraphics : GameGraphics {
    val canvas = document.getElementById("canvas") as HTMLCanvasElement;
    val context = canvas.getContext("2d") as CanvasRenderingContext2D

    var lastImageDrawn : Boolean = false;

    constructor(clickHandler : ((Event) -> dynamic)?) {
        canvas.onclick = clickHandler;
    }

    override fun lastImageSuccess() : Boolean {
        return lastImageDrawn;
    }

    override fun drawImage1(img: String?, x: Float, y: Float, w: Float, h: Float) {
        drawImage2(img, x, y, w, h, false);
    }

    override fun drawImage2(img: String?, x: Float, y: Float, w: Float, h: Float, flipped: Boolean) {
        lastImageDrawn = false;
        if(img != null) {
            var i1 = img.lastIndexOf("/")
            if(i1 > 0) {
                var imgId = img.substring(i1 + 1);
                var i2 = imgId.indexOf(".");
                imgId = imgId.substring(0, i2);
//                println(imgId);
                try {
                    var image = document.getElementById(imgId) as HTMLImageElement;
                    context.save();
                    context.beginPath();
                    if(flipped) {
                        context.translate(x+w/2.0, y+h/2.0);
                        context.rotate(180* Math.PI/180);
                        context.drawImage(image, -(w/2.0), -(h/2.0), w.toDouble(), h.toDouble());
                    } else {
                        context.drawImage(image, x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble());
                    }
                    context.restore();
                    lastImageDrawn = true;
                } catch(ex : Exception) {
                }
            }
        }
    }

    override fun fillRectangle1(x: Float, y: Float, w: Float, h: Float, color: String) {
        fillRectangle2(x, y, w, h, color, 1f);
    }

    override fun fillRectangle2(x: Float, y: Float, w: Float, h: Float, color: String, a: Float) {
        context.save();
        context.beginPath();
        context.fillStyle = color;
        context.fillRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble());
        context.restore();
    }

    override fun drawRectangle1(x: Float, y: Float, w: Float, h: Float) {
        drawRectangle3(x, y, w, h, "Black", 1);
    }

    override fun drawRectangle2(x: Float, y: Float, w: Float, h: Float, color: String) {
        drawRectangle3(x, y, w, h, color, 1);
    }

    override fun drawRectangle3(x: Float, y: Float, w: Float, h: Float, color: String, thick: Int) {
        context.save();
        context.beginPath();
        context.lineWidth = thick.toDouble();
        context.strokeStyle = color;
        context.moveTo(x.toDouble(), y.toDouble());
        context.lineTo((x+w).toDouble(), y.toDouble());
        context.lineTo((x+w).toDouble(), (y+h).toDouble());
        context.lineTo(x.toDouble(), (y+h).toDouble());
        context.lineTo(x.toDouble(), y.toDouble());
        context.stroke();
        context.restore();
    }

    override fun drawString1(str: String, x: Float, y: Float, size: Int) {
        context.save();
        context.beginPath();
        context.font = size.toString() + "px Arial";
        context.fillText(str, x.toDouble(), (y+size).toDouble());
        context.restore();
    }

    override fun drawString2(str: String, x: Float, y: Float, w: Float, h: Float, size: Int) {
        context.save();
        context.beginPath();
        context.font = size.toString() + "px Arial";

        var yy = y;
        var words = str.split(" ");
        var line = "";
        for(n in 0..words.size-1) {
            var testLine = line + words.get(n) + " ";
            var metrics = context.measureText(testLine.trim());
            if(metrics.width > w && n > 0) {
                context.fillText(line, x.toDouble(), yy.toDouble());
                line = words.get(n) + " ";
                yy += size;
            } else {
                line = testLine;
            }
        }
        context.fillText(line, x.toDouble(), yy.toDouble());
        context.restore();
    }

//    @native
    override fun drawLine1(x1: Float, y1: Float, x2: Float, y2: Float) {
        drawLine3(x1, y1, x2, y2, "Black", 1);
    }

//    @native
    override fun drawLine2(x1: Float, y1: Float, x2: Float, y2: Float, thick: Int) {
        drawLine3(x1, y1, x2, y2, "Black", thick);
    }

//    @native
    override fun drawLine3(x1: Float, y1: Float, x2: Float, y2: Float, color: String, thick: Int) {
        context.save();
        context.beginPath();
        context.lineWidth = thick.toDouble();
        context.strokeStyle = color;
        context.moveTo(x1.toDouble(), y1.toDouble());
        context.lineTo(x2.toDouble(), y2.toDouble());
        context.stroke();
        context.restore();
    }
}
