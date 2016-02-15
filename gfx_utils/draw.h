#ifndef _GFX_DRAW
#define _GFX_DRAW

#include "netpbm.h"

void plot(pixel **pic, pixel p, int x, int y);
void fill_rect(pixel **pic, pixel p, int x, int y, int dx, int dy);
void draw_line(pixel **pic, pixel p, int x0, int y0, int x1, int y1);

#endif

