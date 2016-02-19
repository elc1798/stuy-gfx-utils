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

    // An image is of type pixel**
    printf("Creating a new picture\n");
    pixel **pic = new_picture(XRES, YRES);

    // Fill entire image a white background
    printf("Filling image with white background \n");
    fill_rect(pic, new_pixel(255, 255, 255), new_point(0, 0), new_point(XRES, YRES));

    // Draw blue horizontal lines to test
    printf("Drawing blue horizontal lines\n");
    int i; for (i = 0; i < XRES; i += 5) {
        draw_line(pic, new_pixel(0, 0, 255), new_point(0, i), new_point(i, i));
    }

    // Draw red vertical lines to test
    printf("Drawing red vertical lines to test\n");
    for (i = 0; i < YRES; i += 5) {
        draw_line(pic, new_pixel(255, 0, 0), new_point(i, 0), new_point(i, i));
    }

    fill_rect(pic, new_pixel(0, 0, 0), new_point((XRES / 2) - 100, (YRES / 2) - 100),
            new_point((XRES / 2) + 100, (YRES / 2) + 100));

    // Oh boi~~~
    for (i = (YRES / 2) - 100; i <= (YRES / 2) + 100; i += 10) {
        draw_line(pic, new_pixel(0, 255, 0), new_point((XRES / 2), (YRES / 2)),
                new_point((XRES / 2) + 100, i));
        draw_line(pic, new_pixel(255, 255, 0), new_point((XRES / 2), (YRES / 2)),
                new_point(-(XRES / 2) + 100, -i));
    }

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

