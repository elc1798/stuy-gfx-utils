#include <stdio.h>
#include <stdlib.h>

#include "matrix.h"

int main() {
    matrix *a = new_matrix(3, 4);
    int i; for (i = 0; i < a->rows; i++) {
        int j; for (j = 0; j < a->cols; j++) {
            a->contents[i][j] = i * a->cols + j;
        }
    }
    print_matrix(a);
    printf("\n\n");

    matrix *b = new_matrix(4, 5);
    for (i = 0; i < b->rows; i++) {
        int j; for (j = 0; j < b->cols; j++) {
            b->contents[i][j] = i * b->cols + j;
        }
    }
    print_matrix(b);
    printf("\n\n");

    matrix *c = cross_product(a, b);
    print_matrix(c);
    printf("\n\n");

    scalar_multiply(a, 3);
    print_matrix(a);
    printf("\n\n");

    matrix *d = identity_matrix(7);
    print_matrix(d);
    printf("\n\n");

    free_matrix(a);
    free_matrix(b);
    free_matrix(c);
    free_matrix(d);

    return 0;
}

