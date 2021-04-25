package com.cursorsdesign.fly.layout;

import com.cursorsdesign.fly.component.Component;
import org.jetbrains.skija.*;

import java.util.List;

public class Container extends Layout{

    public Container(){
        super();
    }
    @Override
    public void addComponent(Component component) {
        super.addComponent(component);
    }

    @Override
    public void addLayout(Layout layout) {
        super.addLayout(layout);
    }

    @Override
    public void addLayouts(List<Layout> layoutsList) {
        super.addLayouts(layoutsList);
    }

    @Override
    public void addComponents(List<Component> componentList) {
        super.addComponents(componentList);
    }

    @Override
    public void draw() {
        super.draw();
        var paint = new Paint().setColor(0xFF1D7AA2).setMode(PaintMode.STROKE).setStrokeWidth(1f);
        var canvas = this.getCanvas();
        canvas.save();
        canvas.drawRRect(RRect.makeXYWH(0, 0, this.width, this.height, 10), paint);
       //utiliser les translate pour modifier la potition , dx sur l'axe x et dy su l'axe y
        canvas.translate(60, 0);
        canvas.drawRRect(RRect.makeXYWH(0, 0, this.width, this.height, 10), paint);
        canvas.translate(75, 0);
        canvas.restore();
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setWidth(int width){
        this.width = width;
    }





}
