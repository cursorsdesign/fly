package com.cursorsdesign.fly.window;

import com.cursorsdesign.fly.demo.PevScene;
import com.cursorsdesign.fly.global.UI;
import com.cursorsdesign.fly.navigator.Navigation;
import com.cursorsdesign.fly.scene.ColorFiltersFrame;
import com.cursorsdesign.fly.scene.FigmaFrame;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.Library;
import org.jetbrains.skija.impl.Stats;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static long window;
    public int width;
    public int height;
    public   String title = "Demo";
    private float dpi = 1f;
    private int xpos = 0;
    private int ypos = 0;
    private boolean vsync = true;
    private boolean stats = true;
    private int[] refreshRates;
    private String os = System.getProperty("os.name").toLowerCase();
    private DirectContext context;
    private BackendRenderTarget renderTarget;
    private Surface surface;
    private Canvas canvas;


    protected void lauch(){
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int width = (int) (vidmode.width() * 0.75);
        int height = (int) (vidmode.height() * 0.75);
        IRect bounds = IRect.makeXYWH(
                Math.max(0, (vidmode.width() - width) / 2),
                Math.max(0, (vidmode.height() - height) / 2),
                width,
                height);
       // var window = new com.cursorsdesign.fly.layout.Window();
        refreshRates = getRefreshRates();

        createWindow(bounds);
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private int[] getRefreshRates() {

        var monitors = glfwGetMonitors();
        int[] res = new int[monitors.capacity()];
        for (int i=0; i < monitors.capacity(); ++i) {
            res[i] = glfwGetVideoMode(monitors.get(i)).refreshRate();
        }
        return res;
    }



    private void updateDimensions() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(window, width, height);

        float[] xscale = new float[1];
        float[] yscale = new float[1];
        glfwGetWindowContentScale(window, xscale, yscale);
        assert xscale[0] == yscale[0] : "Horizontal dpi=" + xscale[0] + ", vertical dpi=" + yscale[0];

        this.width = (int) (width[0] / xscale[0]);
        this.height = (int) (height[0] / yscale[0]);
        this.dpi = xscale[0];
        System.out.println("FramebufferSize " + width[0] + "x" + height[0] + ", scale " + this.dpi + ", window " + this.width + "x" + this.height);
    }

    private void createWindow(IRect bounds) {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(bounds.getWidth(), bounds.getHeight(), title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetWindowPos(window, bounds.getLeft(), bounds.getTop());
        updateDimensions();
        xpos = width / 2;
        ypos = height / 2;

        glfwMakeContextCurrent(window);
        glfwSwapInterval(vsync ? 1 : 0); // Enable v-sync
        glfwShowWindow(window);
    }


    private void initSkia() {
        Stats.enabled = true;
        if (surface != null)
            surface.close();
        if (renderTarget != null)
            renderTarget.close();

        renderTarget = BackendRenderTarget.makeGL(
                (int) (width * dpi),
                (int) (height * dpi),
                /*samples*/0,
                /*stencil*/8,
                /*fbId*/0,
                FramebufferFormat.GR_GL_RGBA8);

        surface = Surface.makeFromBackendRenderTarget(
                context,
                renderTarget,
                SurfaceOrigin.BOTTOM_LEFT,
                SurfaceColorFormat.RGBA_8888,
                ColorSpace.getDisplayP3(),  // TODO load monitor profile
                new SurfaceProps(PixelGeometry.RGB_H));
        //utiliser UI.canvas pour eviterr de toujour les declarer
        UI.canvas = surface.getCanvas();
        this.canvas = UI.canvas;
    }

    private void draw() {
        Navigation.draw(canvas, width, height, dpi, xpos, ypos);
        context.flush();
        glfwSwapBuffers(window);
    }

    private void loop() {
        GL.createCapabilities();
        if ("false".equals(System.getProperty("skija.staticLoad")))
            Library.load();
        context = DirectContext.makeGL();

        org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
            updateDimensions();
            initSkia();
            draw();
        });

        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if(os.contains("mac") || os.contains("darwin")) {
                this.xpos = (int) xpos;
                this.ypos = (int) ypos;
            } else {
                this.xpos = (int) (xpos / dpi);
                this.ypos = (int) (ypos / dpi);
            }
        });

        //ici
/*
        glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
            Scenes.setScene(Scenes.currentScene).onScroll((float) xoffset * dpi, (float) yoffset * dpi);
        });
*/
        /*
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                switch (key) {
                    case GLFW_KEY_LEFT:
                        Navigation.setScene(new PevScene("Bonjour")); System.out.println("Figama");
                        break;
                    case GLFW_KEY_RIGHT:
                        Navigation.nextScene();
                        break;
                    case GLFW_KEY_UP:
                        Navigation.currentScene().changeVariant(-1);
                        break;
                    case GLFW_KEY_DOWN:
                        Navigation.currentScene().changeVariant(1);
                        break;
                    case GLFW_KEY_V:
                        Navigation.vsync = !Navigation.vsync;
                        glfwSwapInterval(Navigation.vsync ? 1 : 0);
                        break;
                    case GLFW_KEY_S:
                        Navigation.stats = !Navigation.stats;
                        Stats.enabled = Navigation.stats;
                        break;
                    case GLFW_KEY_G:
                        System.out.println("Before GC " + Stats.allocated);
                        System.gc();
                        break;
                }
            }
        });
*/
        initSkia();

        while (!glfwWindowShouldClose(window)) {
            draw();
            glfwPollEvents();
        }
    }

}
