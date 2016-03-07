#ifndef _GFX_MATRIX
#define _GFX_MATRIX

#include "netpbm.h"

typedef struct {
    int rows;
    int cols;
    double **contents;
} matrix;

// Functions for creation / deletion
matrix *new_matrix(int rows, int cols);
void free_matrix(matrix *m);
void print_matrix(matrix *m);

// Matrix operations
double dot_product(matrix *m1, matrix *m2);
matrix *cross_product(matrix *m1, matrix *m2);
void scalar_multiply(matrix *m, int a);

matrix *identity_matrix(int n);

matrix *get_translation_matrix(int dx, int dy, int dz);
matrix *get_scale_matrix(double x_fac, double y_fac, double z_fac);
matrix *get_rot_x_matrix(double theta);
matrix *get_rot_y_matrix(double theta);
matrix *get_rot_z_matrix(double theta);
void apply_trans(matrix *master, point_matrix *pt_mat);

#endif

