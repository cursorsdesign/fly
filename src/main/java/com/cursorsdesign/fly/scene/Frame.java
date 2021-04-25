package com.cursorsdesign.fly.scene;

import org.jetbrains.skija.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

public abstract class Frame {
    public static Typeface inter = null;

    static {
        try {
            inter = Typeface.makeFromFile(file("fonts/InterHinted-Regular.ttf"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static final Font     inter13 = new Font(inter, 13).setSubpixel(true);
    public static final Paint    blackFill = new Paint().setColor(0xFF000000);

    public String[] _variants = new String[] { "Default" };
    public int _variantIdx = 0;
    
    public boolean scale() {
        return true;
    }

    public String variantTitle() {
        return _variants[_variantIdx];
    }

    public void changeVariant(int delta) {
        _variantIdx = (_variantIdx + _variants.length + delta) % _variants.length;
    }

    public abstract void main(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) throws URISyntaxException;

    public void onScroll(float dx, float dy) {}

    public static String file(String path) throws URISyntaxException {
        path = Frame.getFileFromResource(path).getPath();
        return path;
    }

    public static void drawStringCentered(Canvas canvas, String text, float x, float y, Font font, Paint paint) {
        Rect bounds = font.measureText(text, paint);
        float lineHeight = font.getMetrics().getCapHeight();
        canvas.drawString(text,
            x - bounds.getRight() / 2,
            y + lineHeight / 2,
            font, paint);
    }

    public static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = Frame.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }

    public static void drawStringLeft(Canvas canvas, String text, Rect outer, Font font, Paint paint) {
        Rect inner = font.measureText(text, paint);
        FontMetrics metrics = font.getMetrics();
        float innerHeight = metrics.getDescent() - metrics.getAscent();

        canvas.drawString(text,
            outer.getLeft(), 
            outer.getTop() + (outer.getHeight() - innerHeight) / 2f - metrics.getAscent(),
            font, paint);
    }
    public static String formatFloat(float f) {
        return String.format(Locale.ENGLISH, "%.02f", f).replaceAll("\\.?0+$", "");
    }
    public static String formatFloatArray(float[] fs) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < fs.length; ++i) {
            sb.append(formatFloat(fs[i]));
            if (i < fs.length - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}