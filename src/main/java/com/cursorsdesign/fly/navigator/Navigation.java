package com.cursorsdesign.fly.navigator;

import java.net.URISyntaxException;
import java.util.*;

import com.cursorsdesign.fly.demo.CurrentScent;
import com.cursorsdesign.fly.scene.BlendsFrame;
import com.cursorsdesign.fly.scene.Frame;
import com.cursorsdesign.fly.scene.HUD;
import lombok.*;
import org.jetbrains.skija.*;

public class Navigation {
    public static TreeMap<String, Frame> scenes;
    public static Frame prevScene;
    public static Frame currentScene = new BlendsFrame();
    public static Frame nextScene;
    public static HUD hud = new HUD();
    public static boolean vsync = true;
    public static boolean stats = true;

    static {
        scenes = new TreeMap<>();
        scenes.put("Blends", null);
        setScene(new CurrentScent());
    }


    public static void push(Frame prevScene, Frame currentScene){
        Navigation.prevScene = prevScene;
        Navigation.currentScene = currentScene;
    }


    @SneakyThrows
    public static Frame newScene(Frame frame) {
        return frame;
    }

    public static Frame nextScene() {
       return nextScene();
    }

    public static Frame prevScene() {
        return prevScene;
    }

    public static Frame setScene(Frame scene)  {
        currentScene = scene;
        return currentScene;
    }

    public static Frame currentScene() {
        return currentScene;
    }

    public static void draw(Canvas canvas, int width, int height, float scale, int mouseX, int mouseY) {
        canvas.clear(0xFFFFFFFF);
        int layer = canvas.save();
        var scene = currentScene();
        if (scene.scale())
            canvas.scale(scale, scale);
        try {
            scene.main(canvas, width, height, scale, mouseX, mouseY);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        canvas.restoreToCount(layer);

        hud.tick();
        if (stats) {
            layer = canvas.save();
            canvas.scale(scale, scale);
            hud.draw(canvas, scene, width, height);
            canvas.restoreToCount(layer);
        } else
            hud.log();
    }
}