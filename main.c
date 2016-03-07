#include <stdlib.h>
#include <stdio.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
#include "gfx_utils/matrix.h"
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
    matrix *master_transformation_matrix;

    fill_rect(pic, new_pixel(0, 0, 0), new_point(0, 0), new_point(XRES, YRES));

    pt_mat = add_edge(pt_mat, new_edge(new_point(-50, 50), new_point(50, 50)));
    pt_mat = add_edge(pt_mat, new_edge(new_point(50, 50), new_point(50, -50)));
    pt_mat = add_edge(pt_mat, new_edge(new_point(50, -50), new_point(-50, -50)));
    pt_mat = add_edge(pt_mat, new_edge(new_point(-50, -50), new_point(-50, 50)));

    matrix *translate = get_translation_matrix(XRES / 2, YRES / 2 , 0);

    int size; for (size = 1; size < 4; size++) {

        matrix *scale = get_scale_matrix((double) size, (double) size, 1.0);
        apply_trans(scale, pt_mat);
        free_matrix(scale);

        int i; for (i = 0; i < 3; i++) {

            matrix *rotation = get_rot_z_matrix(30.0);

            master_transformation_matrix = cross_product(translate, rotation);

            apply_trans(master_transformation_matrix, pt_mat);
            render_point_matrix(pic, new_pixel(0, 218, 195 + 15 * i), pt_mat);

            // Reset for next rotation
            matrix *reset = get_translation_matrix(-XRES / 2, -YRES / 2, 0);
            apply_trans(reset, pt_mat);
            free_matrix(reset);

            free(rotation);
            free(master_transformation_matrix);
        }

        // Reset the scale for the next iteration
        matrix *reset = get_scale_matrix((double) 1 / size, (double) 1 / size, 1.0);
        apply_trans(reset, pt_mat);
        free_matrix(reset);
    }

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

    free_matrix(translate);
    translate = NULL;

    return 0;
}

