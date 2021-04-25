package com.cursorsdesign.fly.demo;

import com.cursorsdesign.fly.scene.ColorFiltersFrame;
import com.cursorsdesign.fly.scene.Frame;
import org.jetbrains.skija.Canvas;

public class PevScene  extends Frame{
    String name ;

    public PevScene(String name){
        this.name = name;
    }

    @Override
    public void main(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        System.out.println(this.name);
        new ColorFiltersFrame().main(canvas, width, height, dpi, xpos, ypos);
    }
}
