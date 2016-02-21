#include <stdlib.h>
#include <stdio.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
#include "gfx_utils/draw.h"

#define FILENAME    "pic1.ppm"

extern int XRES;
extern int YRES;
extern int MAX_C_VAL;

int main() {
    // XRES and YRES exist already. This allows easy configuration
    // The following are the default values of these constants
    XRES = 512;
    YRES = 512;
    MAX_C_VAL = 255;

    pixel **pic = new_picture(XRES, YRES);

    fill_rect(pic, new_pixel(0, 0, 0), new_point(0, 0), new_point(XRES, YRES));

    int red_val = 0;
    int inc = 2;

    int i; for (i = 0; i <= YRES; i += 4) {
        draw_line(pic, new_pixel(red_val, 0, 255), new_point(0, YRES - i), new_point(i, i));
        if (red_val + inc < 0 || red_val + inc > 255) {
            inc *= -1;
        }
        red_val += inc;
    }

    for (i = XRES; i >= 0; i -= 4) {
        draw_line(pic, new_pixel(red_val, 0, 255), new_point(i, i), new_point(XRES - i, 0));
        if (red_val + inc < 0 || red_val + inc > 255) {
            inc *= -1;
        }
        red_val += inc;
    }

    draw_line(pic, new_pixel(255, 0, 255), new_point(0, 0), new_point(XRES, YRES));

    // Write it out to an image
    char *s = pic2string(pic);
    filewrite(FILENAME, s, get_size_of_buff());

    // Free the memory
    free(s);
    s = NULL;

    free_picture(pic);
    pic = NULL;
    return 0;
}

