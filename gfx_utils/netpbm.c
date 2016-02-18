/*
 * Small C library that packages RGB values into ints using simple bit shifts
 */

#include <stdlib.h>
#include "netpbm.h"

#define RED_SHIFT 16
#define GREEN_SHIFT 8
#define BLUE_SHIFT 0
#define BIT_MASK 0xFF

int XRES = 512;
int YRES = 512;
int MAX_C_VAL = 255;

int get_red(pixel p) {
    return (p >> RED_SHIFT) & BIT_MASK;
}

int get_green(pixel p) {
    return (p >> GREEN_SHIFT) & BIT_MASK;
}

int get_blue(pixel p) {
    return (p >> BLUE_SHIFT) & BIT_MASK;
}

pixel new_pixel(int r, int g, int b) {
    int retval = 0;
    return retval | ((r & BIT_MASK) << RED_SHIFT) | ((g & BIT_MASK) << GREEN_SHIFT) | ((b & BIT_MASK) << BLUE_SHIFT);
}

point new_point(int x, int y) {
    point retval;
    retval.x = x;
    retval.y = y;
    return retval;
}

pixel **new_picture(int xres, int yres) {
    pixel **pic;
    pic = malloc(yres * sizeof(pixel*));
    int i; for (i = 0; i < xres; i++) {
        pic[i] = malloc(xres * sizeof(pixel));
    }
    XRES = xres;
    YRES = yres;
    return pic;
}

void free_picture(pixel **pic) {
    int i; for (i = 0; i < YRES; i++) {
        free(pic[i]);
        pic[i] = NULL;
    }
    free(pic);
}

