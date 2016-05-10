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

    /*
     * Terminal ANSI Color Constants
     */

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
}
