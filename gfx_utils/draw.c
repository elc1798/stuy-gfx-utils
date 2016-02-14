#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
#include "gfx_utils/draw.h"

// Simple rectangular fill algorithm
void fill_rect(pixel **pic, int r, int g, int b, int x, int y, int dx, int dy) {
    int i,j;
    for (i = x; i < x + dx; i++) {
        for (j = y; j < y + dy; j++) {
            pic[i][j] = new_pixel(r, g, b);
        }
    }
}

// Implementation of Bresenham's line algorithm
void draw_line(pixel **pic, int r, int g, int b, int x0, int y0, int x1, int y1) {
    // Determine the octant without using divide
    int octant = -1;
    if (x1 - x0 > 0 && y1 - y0 > 0) {
        octant = (y1 - y0 > x1 - x0) ? 2 : 1;
    } else if (x1 - x0 < 0 && y1 - y0 > 0) {
        octant = (y1 - y0 > x0 - x1) ? 3 : 4;
    } else if (x1 - x0 < 0 && y1 - y0 < 0) {
        octant = (y0 - y1 > x0 - x1) ? 6 : 5;
    } else if (x0 - x1 > 0 && y1 - y0 < 0) {
        octant = (y0 - y1 > x1 - x0) ? 7 : 8;
    }
}

