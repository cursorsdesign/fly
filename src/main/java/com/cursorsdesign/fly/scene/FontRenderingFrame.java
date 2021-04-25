package com.cursorsdesign.fly.scene;

import java.net.URISyntaxException;

import com.cursorsdesign.fly.window.Window;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

public class FontRenderingFrame extends Frame {
    public Typeface _inter;
    public Typeface _interHinted;
    public Typeface _interV;
    public Paint    _paint;
    public float    _dpi = 0;

    public FontRenderingFrame() throws URISyntaxException {
        _inter       = Typeface.makeFromFile(file("fonts/Inter-Regular.otf"));
        _interV      = Typeface.makeFromFile(file("fonts/Inter-V.ttf"));
        _interHinted = inter;
        _paint       = new Paint().setColor(0xFF000000);
        _variants    = new String[] { "Identity", "Scaled" };
    }

    public float _drawLine(Canvas canvas, String text, Font font) {
        var blob = Shaper.make().shape(text, font);
        if (blob != null) {
            var bounds = blob.getBounds();
            canvas.drawTextBlob(blob, 0, 0, _paint);
            canvas.translate(0, bounds.getHeight());
            return bounds.getHeight();
        }
        return 0;
    }

    @Override
    public void main(Canvas canvas, int windowWidth, int windowHeight, float dpi, int xpos, int ypos) {
        glfwSetMouseButtonCallback(Window.window, (window, button, action, mods) -> {
            System.out.println("Moi ici  " + button + " " + (action == 0 ? "released" : "pressed"));
        });
        float scale = "Identity".equals(variantTitle()) ? dpi : 1f;
        canvas.translate(30 * scale, 30 * scale);
        drawModes(canvas, windowWidth, scale);
    }

    @Override
    public boolean scale() {
        return "Scaled".equals(variantTitle());
    }

    public void drawModes(Canvas canvas, int windowWidth, float scale) {
        String common = "Font - ";

        for (var typeface: Pair.arrayOf("", _inter,
                                        "Hinted ", _interHinted,
                                        "Variable ", _interV))
        {
            canvas.save();
            for (var edging: Pair.arrayOf("Grayscale ", FontEdging.ANTI_ALIAS, "LCD ", FontEdging.SUBPIXEL_ANTI_ALIAS)) {
                for (var subpixel: Pair.arrayOf("", false, "Subpixel ", true)) {
                    for (var linear: Pair.arrayOf("", false, "LinearMetrics ", true)) {
                        for (var hinting: Pair.arrayOf("Hinting=NONE ",   FontHinting.NONE,
                                                       "Hinting=SLIGHT ", FontHinting.SLIGHT,
                                                       "Hinting=NORMAL ", FontHinting.NORMAL,
                                                       "Hinting=FULL ",   FontHinting.FULL)) {
                            try (var font = new Font(typeface.getSecond(), 11 * scale)) {
                                font.setEdging(edging.getSecond());
                                font.setSubpixel(subpixel.getSecond());
                                font.setMetricsLinear(linear.getSecond());
                                if (hinting.getSecond() != null)
                                    font.setHinting(hinting.getSecond());
                                _drawLine(canvas, common + "Inter " + (11 * scale) + " " + edging.getFirst() + typeface.getFirst() + subpixel.getFirst() + linear.getFirst() + hinting.getFirst(), font);
                            }
                        }
                        canvas.translate(0, 20);
                    }
                }
            }
            canvas.restore();
            canvas.translate(((windowWidth - 80) / 3  + 20) * scale, 0);
        }
    }
}