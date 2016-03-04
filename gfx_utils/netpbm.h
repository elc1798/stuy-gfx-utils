#ifndef _GFX_NETPBM
#define _GFX_NETPBM

#include <stdbool.h>

#define PPM_HEADER "P3"

typedef int pixel;

typedef struct {
    int x;
    int y;
} point;

typedef struct {
    point start;
    point end;
} edge;

typedef struct pt_mat {
    struct pt_mat *prev;
    struct pt_mat *next;
    point pt;
    bool connect;
} point_matrix;

int get_red(pixel p);
int get_green(pixel p);
int get_blue(pixel p);

pixel new_pixel(int r, int g, int b);
point new_point(int x, int y);
edge new_edge(point start, point end);

void free_point_matrix(point_matrix *pt_mat);
point_matrix *add_point(point_matrix *pt_mat, point p);
point_matrix *add_edge(point_matrix *pt_mat, edge e);

pixel **new_picture(int xres, int yres);
void free_picture(pixel **pic);

#endif
