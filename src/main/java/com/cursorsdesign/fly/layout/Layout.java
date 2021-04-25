package com.cursorsdesign.fly.layout;

import com.cursorsdesign.fly.component.Component;
import com.cursorsdesign.fly.global.UI;
import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.Color;

import java.util.List;


public abstract class Layout {
    private Canvas canvas;
    protected int width, height;
    public Layout(){
        //Initialiser le canvas avec le canvas globla
        this.canvas = UI.canvas;
    }
    public void addComponent(Component component){
       component.main(this.canvas);
    }

    public void addComponents(List<Component> componentList){
        for (Component component : componentList) {
            component.main(this.canvas);
        }
    }
    public void addLayout(Layout layout){
        layout.draw();
    }

    public void addLayouts(List<Layout> layoutsList){
        for (Layout layout : layoutsList) {
            layout.draw();
        }
    }




    protected void draw(){

    }
    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
