package org.stuygfx;

import java.io.IOException;

import org.stuygfx.graphics.Draw;
import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;

public class Main {

    public static void main(String[] args) throws IOException {
        Image img = new Image();
        img.shouldRelectOverX(true);

        Point center = new Point(img.XRES / 2, img.YRES / 2);
        Pixel green = new Pixel(0, 255, 0);
        Pixel blue = new Pixel(0, 0, 255);
        Pixel red = new Pixel(255, 0, 0);
        Pixel yellow = new Pixel(255, 255, 0);

        for (int i = 0; i < img.YRES; i += 4) {
            Draw.line(img, green, center, new Point(img.XRES - 1, i));
        }

        for (int i = 0; i < img.XRES; i += 4) {
            Draw.line(img, yellow, center, new Point(i, img.YRES - 1));
        }

        for (int i = 0; i < img.YRES; i += 4) {
            Draw.line(img, blue, center, new Point(0, i));
        }

        for (int i = 0; i < img.XRES; i += 4) {
            Draw.line(img, red, center, new Point(i, 0));
        }

        PPMGenerator.createPPM("test.ppm", img);
        System.gc();
    }

}
