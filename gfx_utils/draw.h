#ifndef _GFX_DRAW
#define _GFX_DRAW

#include "netpbm.h"

void fill_rect(pixel **pic, int r, int g, int b, int x, int y, int dx, int dy);
void draw_line(pixel **pic, int r, int g, int b, int x0, int y0, int x1, int y1);

#endif

