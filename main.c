#include <stdlib.h>
#include <stdio.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
#include "gfx_utils/draw.h"

#define FILENAME    "pic1.ppm"
#define XRES        512
#define YRES        512
#define MAX_C_VAL   255

int main() {
    // An image is of type pixel**
    pixel **pic = new_picture(XRES, YRES);

    // Fill entire image a white background
    fill_rect(pic, new_pixel(255, 255, 255), 0, 0, XRES, YRES);

    // Draw blue horizontal lines to test
    int i; for (i = 0; i < XRES; i += 5) {
        draw_line(pic, new_pixel(0, 0, 255), 0, i, i, i);
    }

    // Draw red vertical lines to test
    for (i = 0; i < YRES; i += 5) {
        draw_line(pic, new_pixel(255, 0, 0), i, 0, i, i);
    }

    // Draw some diagonal parallel to y = x
    for (i = 0; i < XRES; i += 10) {
        draw_line(pic, new_pixel(0, 255, 0), 0, i, XRES - i, 0);
    }

    // Draw some diagonal parallel to y = -x
    for (i = 0; i < XRES; i += 10) {
        draw_line(pic, new_pixel(0, 0, 0), 0, i, i, 0);
    }

    // Write it out to an image
    char *s = pic2string(pic, XRES, YRES, MAX_C_VAL);
    filewrite(FILENAME, s, get_size_of_buff(XRES, YRES, MAX_C_VAL));

    // Free the memory
    free(s);
    s = NULL;

    free_picture(pic, YRES);
    pic = NULL;
    return 0;
}

