#include <stdlib.h>
#include <assert.h>
#include "matrix.h"

matrix *new_matrix(int rows, int cols) {
    matrix *mat = malloc(sizeof(matrix));
    mat->rows = rows;
    mat->cols = cols;
    mat->contents = malloc(sizeof(double *) * rows);
    int i; for (i = 0; i < rows; i++) {
        mat->contents[i] = malloc(sizeof(double) * cols);
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

double dot_product(matrix *m1, matrix *m2) {
    assert(m1->rows == 1 && m2->rows == 1);
    assert(m1->cols == m2->cols);
    double sum = 0.0;
    int i; for (i = 0; i < m1->cols; i++) {
        sum += m1->contents[0][i] * m2->contents[0][i];
    }
    return sum;
}

matrix *cross_product(matrix *m1, matrix *m2);
void scalar_multiply(matrix *m, int a);

