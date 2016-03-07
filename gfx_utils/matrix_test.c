#include <stdio.h>
#include <stdlib.h>

#include "matrix.h"

int main() {
    matrix *m1 = new_matrix(3, 4);
    int i; for (i = 0; i < m1->rows; i++) {
        int j; for (j = 0; j < m1->cols; j++) {
            m1->contents[i][j] = i * m1->cols + j;
        }
    }
    print_matrix(m1);
    printf("\n\n");

    matrix *m2 = new_matrix(4, 5);
    for (i = 0; i < m2->rows; i++) {
        int j; for (j = 0; j < m2->cols; j++) {
            m2->contents[i][j] = i * m2->cols + j;
        }
    }
    print_matrix(m2);
    printf("\n\n");

    matrix *m3 = cross_product(m1, m2);
    print_matrix(m3);
    printf("\n\n");

    scalar_multiply(m1, 3);
    print_matrix(m1);
    printf("\n\n");

    matrix *m4 = identity_matrix(7);
    print_matrix(m4);
    printf("\n\n");

    matrix *m5 = get_translation_matrix(3, 2, 1);
    print_matrix(m5);
    printf("\n\n");

    matrix *m6 = get_scale_matrix(4, 3, 2);
    print_matrix(m6);
    printf("\n\n");

    matrix *m7 = get_rot_x_matrix(90.0);
    print_matrix(m7);
    printf("\n\n");

    matrix *m8 = get_rot_x_matrix(0.0);
    print_matrix(m8);
    printf("\n\n");

    matrix *m9 = get_rot_y_matrix(45.0);
    print_matrix(m9);
    printf("\n\n");

    free_matrix(m1);
    free_matrix(m2);
    free_matrix(m3);
    free_matrix(m4);
    free_matrix(m5);
    free_matrix(m6);
    free_matrix(m7);
    free_matrix(m8);
    free_matrix(m9);

    return 0;
}

