#include "fout.h"
#include "netpbm.h"
#include "draw.h"

void plot(pixel **pic, pixel p, int x, int y) {
    pic[y][x] = p;
}

// Simple rectangular fill algorithm
void fill_rect(pixel **pic, pixel p, int x, int y, int dx, int dy) {
    int i, j;
    for (i = x; i < x + dx; i++) {
        for (j = y; j < y + dy; j++) {
            plot(pic, p, i, j);
        }
    }
}

// Implementation of Bresenham's line algorithm
void draw_line(pixel **pic, pixel p, int x0, int y0, int x1, int y1) {
    int x_c, y_c, inc;
    // Draw special cases first
    if (y0 == y1) { // Horizontal
        x_c = x0;
        inc = (x1 - x0 < 0) ? -1 : 1;
        while (x_c != x1) {
            plot(pic, p, x_c, y0);
            x_c += inc;
        }
        plot(pic, p, x1, y0);
        return;
    }
    if (x0 == x1) { // Vertical
        y_c = y0;
        inc = (y1 - y0 < 0) ? -1 : 1;
        while (y_c != y1) {
            plot(pic, p, x0, y_c);
            y_c += inc;
        }
        plot(pic, p, x0, y1);
        return;
    }
    if (y1 - y0 == x1 - x0) { // y = x
        x_c = x0;
        y_c = y0;
        inc = (y1 - y0 < 0) ? -1 : 1;
        while (x_c != x1) {
            plot(pic, p, x_c, y_c);
            x_c += inc;
            y_c += inc;
        }
    }
    if (y1 - y0 == -1 * (x1 - x0)) { // y = -x
        x_c = x0;
        y_c = y0;
        inc = (y1 - y0 < 0) ? -1 : 1;
        while (x_c != x1) {
            plot(pic, p, x_c, y_c);
            x_c -= inc;
            y_c += inc;
        }
    }
}

