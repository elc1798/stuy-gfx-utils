#ifndef _GFX_NETPBM
#define _GFX_NETPBM

#include <stdbool.h>

#define PPM_HEADER "P3"

/* PIXEL COLOR FUNCTIONS */
typedef int pixel;

int get_red(pixel p);
int get_green(pixel p);
int get_blue(pixel p);

// Creates a new pixel valude
pixel new_pixel(int r, int g, int b);

/* POINT AND EDGE FUNCTIONS */

typedef struct {
    int x;
    int y;
    int z;
} point;

typedef struct {
    point start;
    point end;
} edge;

typedef double (*param_func)(double);

typedef struct {
    param_func x;
    param_func y;
} parametric;

/*
 * A point matrix is a doubly linked list of points.
 * prev - The previous node
 * next - The next node
 * pt - The point location
 * connect - Whether or not it should connect to the next node.
 */
typedef struct pt_mat {
    struct pt_mat *prev;
    struct pt_mat *next;
    point pt;
    bool connect;
} point_matrix;

// Creates a new point given coordinates
point new_point(int x, int y);
point new_point_3d(int x, int y, int z);

// Creates a new edge given endpoints
edge new_edge(point start, point end);

// Free a point matrix given a head node for pt_mat
void free_point_matrix(point_matrix *pt_mat);

// Adds a new point to the point matrix. The new node is the new head of the
// linked list, and the new head is then returned at the completion of the
// function.
point_matrix *add_point(point_matrix *pt_mat, point p);

// Adds an edge (line) to the point matrix. The start point is the new head of
// the linked list, who's next node is the end point. The original list is after
// the end point. The new head is returned at the completion of the function
point_matrix *add_edge(point_matrix *pt_mat, edge e);

/* PICTURE OPERATIONS */
pixel **new_picture(int xres, int yres);
void free_picture(pixel **pic);

#endif
