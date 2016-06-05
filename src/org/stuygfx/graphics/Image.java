package org.stuygfx.graphics;

import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;

public class Image {

    public int XRES;
    public int YRES;
    public Pixel[][] canvas;
    public double[][] zBuffer;

    private int originX;
    private int originY;
    private boolean reflectOverX;

    public Image() {
        this.XRES = 512;
        this.YRES = 512;
        this.originX = 0;
        this.originY = 0;
        this.reflectOverX = false;
        this.canvas = new Pixel[YRES][XRES];
        this.zBuffer = new double[YRES][XRES];

        // Fill the zBuffer with the minimum double
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[i].length; j++) {
                zBuffer[i][j] = Double.NEGATIVE_INFINITY;
            }
        }

        initializeCanvas();
    }

    public Image(int xres, int yres) {
        this.XRES = xres;
        this.YRES = yres;
        this.originX = 0;
        this.originY = 0;
        this.reflectOverX = false;
        this.canvas = new Pixel[YRES][XRES];
        initializeCanvas();
    }

    public void initializeCanvas() {
        for (int i = 0; i < this.canvas.length; i++) {
            for (int j = 0; j < this.canvas[0].length; j++) {
                this.canvas[i][j] = new Pixel();
            }
        }
    }

    public void resetCanvas() {
        for (int i = 0; i < this.canvas.length; i++) {
            for (int j = 0; j < this.canvas[0].length; j++) {
                this.canvas[i][j].set(0, 0, 0);
            }
        }
    }

    public void setOrigin(Point origin) {
        this.originX = origin.x;
        this.originY = origin.y;
    }

    public void setOriginX(int x) {
        this.originX = x;
    }

    public void setOriginY(int y) {
        this.originY = y;
    }

    public void shouldRelectOverX(boolean b) {
        this.reflectOverX = b;
    }

    public void plot(Point p, Pixel color) {
        plot(p.x, p.y, p.z, color);
    }

    public void plot(int x, int y, Pixel color) {
        plot(x, y, 0, color);
    }

    public void plot(int x, int y, int z, Pixel color) {
        try {
            int convertedY = y + originY;
            int convertedX = x + originX;
            if (reflectOverX) {
                convertedY = (this.YRES - 1) - (y + originY);
            }
            if (z > zBuffer[convertedY][convertedX]) {
                this.canvas[convertedY][convertedX] = color;
                this.zBuffer[convertedY][convertedX] = z;
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
