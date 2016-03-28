package org.stuygfx;

import java.io.IOException;

import org.stuygfx.graphics.Draw;
import org.stuygfx.graphics.EdgeMatrix;
import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;
import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;
import org.stuygfx.math.Transformations;

public class Main {

    public static void main(String[] args) throws IOException {
        Image img = new Image();
        img.shouldRelectOverX(true);

        Point center = new Point(img.XRES / 2, img.YRES / 2);
        Pixel green = new Pixel(0, 255, 0);
        Pixel red = new Pixel(255, 0, 0);

        Interpreter interpreter = new Interpreter();
        String testLine = "0 0 255 255";
        interpreter.call("line", interpreter.getParams("line", testLine));

        String testCirc = "255 255 50";
        interpreter.call("circle", interpreter.getParams("circle", testCirc));

        String testHermite = "150 150 150 50 350 150 350 300";
        interpreter.call("hermite", interpreter.getParams("hermite", testHermite));

        String testBezier = "0 0 256 0 256 512 512 512";
        interpreter.call("bezier", interpreter.getParams("bezier", testBezier));

        Draw.edgeMatrix(interpreter.canvas, green, interpreter.em);

        PPMGenerator.createPPM("test.ppm", interpreter.canvas);
        System.gc();
    }

}
