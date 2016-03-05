#ifndef _GFX_DRAW
#define _GFX_DRAW

#include "netpbm.h"

void plot(pixel **pic, pixel pix, point p);
void fill_rect(pixel **pic, pixel pix, point p1, point p2);
void draw_line(pixel **pic, pixel pix, point p1, point p2);
void render_point_matrix(pixel **pic, pixel pix, point_matrix *pt_mat);

#endif

