#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>

#include "netpbm.h"
#include "matrix.h"

#define PI 3.14159265358979

matrix *new_matrix(int rows, int cols) {
    matrix *mat = malloc(sizeof(matrix));
    mat->rows = rows;
    mat->cols = cols;
    mat->contents = malloc(sizeof(double *) * rows);
    int i; for (i = 0; i < rows; i++) {
        mat->contents[i] = malloc(sizeof(double) * cols);
    }
    for (i = 0; i < mat->rows; i++) {
        int j; for (j = 0; j < mat->cols; j++) {
            mat->contents[i][j] = 0;
        }
    }
    return mat;
}

void free_matrix(matrix *m) {
    int i; for (i = 0; i < m->rows; i++) {
        free(m->contents[i]);
    }
    free(m->contents);
    free(m);
}

void print_matrix(matrix *m) {
    int i; for (i = 0; i < m->rows; i++) {
        printf("[ ");
        int j; for (j = 0; j < m->cols; j++) {
            printf("%02.2f ", m->contents[i][j]);
        }
        printf("]\n");
    }
}

double dot_product(matrix *m1, matrix *m2) {
    assert(m1->rows == 1 && m2->rows == 1);
    assert(m1->cols == m2->cols);
    double sum = 0.0;
    int i; for (i = 0; i < m1->cols; i++) {
        sum += m1->contents[0][i] * m2->contents[0][i];
    }
    return sum;
}

matrix *cross_product(matrix *m1, matrix *m2) {
    assert(m1->cols == m2->rows);
    matrix *cross_prod = new_matrix(m1->rows, m2->cols);
    int i; for (i = 0; i < m1->rows; i++) {
        int j; for (j = 0; j < m2->cols; j++) {
            // Construct temporary mats to dot product with
            matrix *t1 = new_matrix(1, m1->cols);
            int k; for (k = 0; k < m1->cols; k++) {
                t1->contents[0][k] = m1->contents[i][k];
            }

            matrix *t2 = new_matrix(1, m2->rows);
            for (k = 0; k < m2->rows; k++) {
                t2->contents[0][k] = m2->contents[k][j];
            }

            cross_prod->contents[i][j] = dot_product(t1, t2);
            free_matrix(t1);
            free_matrix(t2);
        }
    }
    return cross_prod;
}

void scalar_multiply(matrix *m, int a) {
    int i; for (i = 0; i < m->rows; i++) {
        int j; for (j = 0; j < m->cols; j++) {
            m->contents[i][j] *= a;
        }
    }
}

matrix *identity_matrix(int n) {
    matrix *id_mat = new_matrix(n, n);
    int i; for (i = 0; i < id_mat->rows; i++) {
        int j; for (j = 0; j < id_mat->cols; j++) {
            id_mat->contents[i][j] = (i == j) ? 1 : 0;
        }
    }
    return id_mat;
}

matrix *get_translation_matrix(int dx, int dy, int dz) {
    matrix *trans_mat = identity_matrix(4);
    trans_mat->contents[0][3] = dx;
    trans_mat->contents[1][3] = dy;
    trans_mat->contents[2][3] = dz;
    return trans_mat;
}

matrix *get_scale_matrix(double x_fac, double y_fac, double z_fac) {
    matrix *trans_mat = identity_matrix(4);
    trans_mat->contents[0][0] = x_fac;
    trans_mat->contents[1][1] = y_fac;
    trans_mat->contents[2][2] = z_fac;
    return trans_mat;
}

double rad2deg(double rad) {
    return rad / PI * 180.0;
}

double deg2rad(double deg) {
    return deg / 180.0 * PI;
}

matrix *get_rot_x_matrix(double theta) {
    theta = deg2rad(theta);
    matrix *rot_x_mat = identity_matrix(4);
    rot_x_mat->contents[1][1] = cos(theta);
    rot_x_mat->contents[1][2] = -1 * sin(theta);
    rot_x_mat->contents[2][1] = sin(theta);
    rot_x_mat->contents[2][2] = cos(theta);
    return rot_x_mat;
}

matrix *get_rot_y_matrix(double theta) {
    theta = deg2rad(theta);
    matrix *rot_y_mat = identity_matrix(4);
    rot_y_mat->contents[0][0] = cos(theta);
    rot_y_mat->contents[0][2] = -1 * sin(theta);
    rot_y_mat->contents[2][0] = sin(theta);
    rot_y_mat->contents[2][2] = cos(theta);
    return rot_y_mat;
}

matrix *get_rot_z_matrix(double theta) {
    theta = deg2rad(theta);
    matrix *rot_z_mat = identity_matrix(4);
    rot_z_mat->contents[0][0] = cos(theta);
    rot_z_mat->contents[0][1] = -1 * sin(theta);
    rot_z_mat->contents[1][0] = sin(theta);
    rot_z_mat->contents[1][1] = cos(theta);
    return rot_z_mat;
}

void apply_trans(matrix *master, point_matrix *pt_mat) {
    while (pt_mat) {
        matrix *pt = new_matrix(4, 1);
        pt->contents[0][0] = pt_mat->pt.x;
        pt->contents[1][0] = pt_mat->pt.y;
        pt->contents[2][0] = pt_mat->pt.z;
        pt->contents[3][0] = 1;

        matrix *new_pt = cross_product(master, pt);
        pt_mat->pt.x = (int) new_pt->contents[0][0];
        pt_mat->pt.y = (int) new_pt->contents[1][0];
        pt_mat->pt.z = (int) new_pt->contents[2][0];

        free_matrix(pt);
        free_matrix(new_pt);

        pt_mat = pt_mat->next;
    }
}

