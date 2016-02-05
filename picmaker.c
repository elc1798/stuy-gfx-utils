#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"

#define FILENAME    "pic1.ppm"
#define XRES        512
#define YRES        512
#define MAX_C_VAL   255

void fill_rect(pixel **pic, int r, int g, int b, int x, int y, int dx, int dy) {
    int i,j;
    for (i = x; i < XRES && i < x + dx; i++) {
        for (j = y; j < YRES && j < y + dy; j++) {
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

    // Let's just create a whole bunch of random rectangles of different sizes
    // and colors

    int devrand = open("/dev/urandom", O_RDONLY);
    if (errno) {
        printf("%s\n", strerror(errno));
        return errno;
    }

    int shade_of_grey;
    read(devrand, &shade_of_grey, sizeof(int));
    shade_of_grey = shade_of_grey % 256;
    fill_rect(pic, shade_of_grey, shade_of_grey, shade_of_grey, 0, 0, XRES, YRES);

    int num_rects = 0;
    read(devrand, &num_rects, sizeof(int));
    num_rects = 10 + (num_rects % 6);

    printf("Generating %d random rectangles\n", num_rects);

    int params[7];
    while (num_rects--) {
        printf("%d rectangles left...\n", num_rects + 1);
        for (i = 0; i < 7; i++) {
            read(devrand, &params[i], sizeof(int));
            params[i] = params[i] & 0x7FF; // Lighten the load
        }
        fill_rect(pic, params[0] % 256, params[1] % 256, params[2] % 256,
                params[3] % XRES, params[4] % YRES, 60 + (params[5] % 241), 60 + (params[6] % 241));
    }

    filewrite(FILENAME, pic2string(pic, XRES, YRES, MAX_C_VAL), get_size_of_buff(XRES, YRES, MAX_C_VAL));
    // Free the memory
    for (i = 0; i < XRES; i++) {
        free(pic[i]);
        pic[i] = NULL;
    }
    free(pic);
    pic = NULL;

    close(devrand);
    if (errno) {
        printf("%s\n", strerror(errno));
        return errno;
    }
    return 0;
}

