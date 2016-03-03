#include <stdlib.h>
#include <stdio.h>
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

