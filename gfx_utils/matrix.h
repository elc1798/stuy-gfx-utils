#ifndef _GFX_MATRIX
#define _GFX_MATRIX

#include "netpbm.h"

typedef point edge[2];
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

#endif

