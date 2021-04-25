package com.cursorsdesign.fly.scene;

import java.net.*;

import org.jetbrains.skija.*;
import org.jetbrains.skija.svg.*;

public class SVGScalingFrame extends Frame {
    public final Paint border = new Paint().setColor(0xFF3333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFFCC3333);

    @Override
    public void main(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(40, 60);

        for (var img: new String[] { "images/svg/bug_none.svg",
                                     "images/svg/bug_viewport.svg",
                                     "images/svg/bug_viewbox.svg",
                                     "images/svg/bug_viewport_viewbox.svg" })
        {
            canvas.drawString(img, 0, -20, inter13, fill);

            try (var data = Data.makeFromFileName(file(img));
                 var svg = new SVGDOM(data);)
            {
                var containerSize = svg.getContainerSize();
                if (!containerSize.isEmpty())
                    drawIcon(canvas, containerSize, svg);
                drawIcon(canvas, new Point(8, 8), svg);
                drawIcon(canvas, new Point(16, 16), svg);
                drawIcon(canvas, new Point(32, 32), svg);
                drawIcon(canvas, new Point(64, 64), svg);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            canvas.translate(40, 0);
        }
    }

    public void drawIcon(Canvas canvas, Point size, SVGDOM svg) {
        svg.setContainerSize(size);
        svg.render(canvas);
        canvas.drawRect(Rect.makeWH(size.getX(), size.getY()), border);
        
        canvas.drawString(String.format("%.0f×%.0f", size.getX(), size.getY()), 0, size.getY() + 20, inter13, fill);
        canvas.translate(size.getX() + 30, 0);
    }
}
