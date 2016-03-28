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
        Pixel blue = new Pixel(0, 0, 255);
        Pixel red = new Pixel(255, 0, 0);
        Pixel yellow = new Pixel(255, 255, 0);

        EdgeMatrix em = new EdgeMatrix();
        em.addEdge(new Point(-50, 50), new Point(50, 50));
        em.addEdge(new Point(50, 50), new Point(50, -50));
        em.addEdge(new Point(50, -50), new Point(-50, -50));
        em.addEdge(new Point(-50, -50), new Point(-50, 50));

        for (int i = 0; i < 3; i++) {
            Matrix rot = Transformations.getRotZMatrix(30.0);
            Matrix trans = Transformations.getTranslationMatrix(img.XRES / 2, img.YRES / 2, 0);
            Matrix master = MatrixMath.crossProduct(trans, rot);

            Transformations.applyTransformation(master, em);
            Draw.edgeMatrix(img, green, em);

            // Reset the translation
            Matrix reset = Transformations.getTranslationMatrix(-img.XRES / 2, -img.YRES / 2, 0);
            Transformations.applyTransformation(reset, em);
        }

        PPMGenerator.createPPM("test.ppm", img);
        System.gc();
    }

}
