package org.stuygfx;

import static org.stuygfx.CONSTANTS.MAX_COLOR_VALUE;
import static org.stuygfx.CONSTANTS.PPM_HEADER;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;

public class PPMGenerator {

    public static void createPPM(String filename, Image img) throws IOException {
        PrintWriter fout = new PrintWriter(new File(filename));
        fout.println(PPM_HEADER);
        fout.printf("%d %d %d\n", img.XRES, img.YRES, MAX_COLOR_VALUE);
        for (int y = 0; y < img.YRES; y++) {
            for (int x = 0; x < img.XRES; x++) {
                Pixel tmp = img.canvas[y][x];
                fout.printf("%d %d %d ", tmp.r, tmp.g, tmp.b);
            }
        }
        fout.close();
        fout = null;
    }

}
