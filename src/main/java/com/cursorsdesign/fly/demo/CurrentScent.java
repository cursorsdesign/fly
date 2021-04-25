package com.cursorsdesign.fly.demo;

import com.cursorsdesign.fly.scene.*;
import org.jetbrains.skija.Canvas;

import java.net.URISyntaxException;


public class CurrentScent extends Frame  {


    @Override
    public void main(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) throws URISyntaxException {
      /*  var container = new Container();
        container.setHeight(50);
        container.setWidth(60);
        container.draw();*/
        new TextStyleFrame().main(canvas, width, height, dpi,xpos,ypos);
    }
}
