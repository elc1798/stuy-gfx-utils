/*
 * Small C library that packages RGB values into ints using simple bit shifts
 */

#include <stdlib.h>
#include "netpbm.h"

#define RED_SHIFT 16
#define GREEN_SHIFT 8
#define BLUE_SHIFT 0
#define BIT_MASK 0xFF

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

