#include <stdlib.h>
#include <stdio.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"

#define FILENAME    "pic1.ppm"
#define XRES        512
#define YRES        512
#define MAX_C_VAL   255

void fill_rect(pixel **pic, int r, int g, int b, int x, int y, int dx, int dy) {
    int i,j;
    for (i = x; i < x + dx; i++) {
        for (j = y; j < y + dy; j++) {
            pic[i][j] = new_pixel(r, g, b);
        }
    }
}

int main() {
    // An image is of type pixel**
    pixel **pic;
    pic = malloc(XRES * sizeof(pixel*));
    int i; for (i = 0; i < XRES; i++) {
        pic[i] = malloc(YRES * sizeof(pixel));
    }
    // Fill entire image with red
    fill_rect(pic, 255, 0, 0, 0, 0, XRES, YRES);
    char *s = pic2string(pic, XRES, YRES, MAX_C_VAL);
    filewrite(FILENAME, s, get_size_of_buff(XRES, YRES, MAX_C_VAL));

    // Free the memory
    free(s);
    s = NULL;

    for (i = 0; i < XRES; i++) {
        free(pic[i]);
        pic[i] = NULL;
    }
    free(pic);
    pic = NULL;
    return 0;
}

