package org.stuygfx.graphics;

import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;

public class Image {

    public int XRES;
    public int YRES;
    public Pixel[][] canvas;

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
        try {
            if (reflectOverX) {
                this.canvas[(this.YRES - 1) - (p.y + originY)][p.x + originX] = color;
            } else {
                this.canvas[p.y + originY][p.x + originX] = color;
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void plot(int x, int y, Pixel color) {
        try {
            if (reflectOverX) {
                this.canvas[(this.YRES - 1) - (y + originY)][x + originX] = color;
            } else {
                this.canvas[y + originY][x + originX] = color;
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
