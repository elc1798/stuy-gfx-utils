package org.stuygfx;

import org.stuygfx.math.Matrix;

public class CONSTANTS {

    public static String PPM_HEADER = "P3";
    public static int MAX_COLOR_VALUE = 255;
    public static String TMP_FILE_NAME = ".tmp.ppm";
    public static Object[] NO_ARGS = new Object[] {};
    public static double CEILING = 1.0001;
    public static double STEP = 0.0025;
    public static Matrix DEFAULT_VIEW_VEC = new Matrix(new double[][] {
        {
            0, 0, -1
        }
    });

}
