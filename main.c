
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
#include "gfx_utils/matrix.h"
#include "gfx_utils/draw.h"

#define FILENAME    "rendered.ppm"

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
    matrix *master_transformation_matrix = NULL;

    fill_rect(pic, new_pixel(0, 0, 0), new_point(0, 0), new_point(XRES, YRES));

pt_mat = add_circle(pt_mat, new_point(250, 250), 200);
pt_mat = add_edge(pt_mat, new_edge(new_point_3d(0, 0, 0), new_point_3d(100, 100, 0)));
pt_mat = add_hermite(pt_mat, 150, 150, 150, 50, 350, 150, 350, 300);
pt_mat = add_bezier(pt_mat, 200, 250, 150, 50, 300, 250, 300, 250);

            render_point_matrix(pic, new_pixel(0, 255, 0), pt_mat);
            char *s = pic2string(pic);
            filewrite(FILENAME, s, get_size_of_buff());
            free(s);
            s = NULL;
        


    free_picture(pic);
    pic = NULL;

    free_point_matrix(pt_mat);
    pt_mat = NULL;

    free_matrix(master_transformation_matrix);
    master_transformation_matrix = NULL;

    return 0;
}
