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

// Adds a circle to the point matrix. This will continously add edges to the
// point matrix to fill the circle
point_matrix *add_circle(point_matrix *pt_mat, point center, int radius);

// Adds a Hermite curve to the point matrix, by (like the circle) continuously
// adding adges to the point matrix. Takes initial point, rate of change at
// initial point, final point, and rate of change at final point
point_matrix *add_hermite(point_matrix *pt_mat, double x0, double y0,
                            double dx0, double dy0, double x1, double y1,
                            double dx1, double dy1);

// Adds a Bezier curve to the point matrix by continously adding edges to the
// point matrix
point_matrix *add_bezier(point_matrix *pt_mat,
                         float x0, float y0,
                         float x1, float y1,
                         float x2, float y2,
                         float x3, float y3);

/* PICTURE OPERATIONS */
pixel **new_picture(int xres, int yres);
void free_picture(pixel **pic);

#endif
