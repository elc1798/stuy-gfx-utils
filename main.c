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
    point_matrix *pt_mat = NULL;

    fill_rect(pic, new_pixel(0, 0, 0), new_point(0, 0), new_point(XRES, YRES));

    int i; for (i = 0; i < YRES; i += 5) {
        pt_mat = add_edge(pt_mat, new_edge(new_point(0, i), new_point(i, i)));
        pt_mat = add_point(pt_mat, new_point(i + 10, i));
    }

    render_point_matrix(pic, new_pixel(255, 0, 0), pt_mat);

    // Write it out to an image
    char *s = pic2string(pic);
    filewrite(FILENAME, s, get_size_of_buff());

    // Free the memory
    free(s);
    s = NULL;

    free_picture(pic);
    pic = NULL;

    free_point_matrix(pt_mat);
    pt_mat = NULL;

    return 0;
}

