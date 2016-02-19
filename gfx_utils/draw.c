#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

#include "fout.h"
#include "netpbm.h"
#include "draw.h"

extern int XRES;
extern int YRES;
extern int MAX_C_VAL;

void plot(pixel **pic, pixel pix, point p) {
    if (p.x < 0 || p.y < 0 || p.x >= XRES || p.y >= YRES) {
        return;
    }
    pic[YRES - p.y - 1][p.x] = pix;
}

// Simple rectangular fill algorithm
void fill_rect(pixel **pic, pixel pix, point p1, point p2) {
    int i, j;
    for (i = p1.x; i < p2.x; i++) {
        for (j = p1.y; j < p2.y; j++) {
            plot(pic, pix, new_point(i, j));
        }
    }
}

point convert_to_octant_1(point p, int octant) {
    point retval;
    switch (octant) {
        case 1:
            retval.x = p.x;
            retval.y = p.y;
            break;
        case 2:
            retval.x = p.y;
            retval.y = p.x;
            break;
        case 3:
            retval.x = p.y;
            retval.y = -p.x;
            break;
        case 4:
            retval.x = -p.x;
            retval.y = p.y;
            break;
        case 5:
            retval.x = -p.x;
            retval.y = -p.y;
            break;
        case 6:
            retval.x = -p.y;
            retval.y = -p.x;
            break;
        case 7:
            retval.x = -p.y;
            retval.y = p.x;
            break;
        case 8:
            retval.x = p.x;
            retval.y = -p.y;
            break;
        default:
            printf("Unknown octant provided : %d\n", octant);
            exit(-1);
    }
    return retval;
}

point convert_from_octant_1(point p, int octant) {
    point retval;
    switch (octant) {
        case 1:
            retval.x = p.x;
            retval.y = p.y;
            break;
        case 2:
            retval.x = p.y;
            retval.y = p.x;
            break;
        case 3:
            retval.x = -p.y;
            retval.y = p.x;
            break;
        case 4:
            retval.x = -p.x;
            retval.y = p.y;
            break;
        case 5:
            retval.x = -p.x;
            retval.y = -p.y;
            break;
        case 6:
            retval.x = -p.y;
            retval.y = -p.x;
            break;
        case 7:
            retval.x = p.y;
            retval.y = -p.x;
            break;
        case 8:
            retval.x = p.x;
            retval.y = -p.y;
            break;
        default:
            printf("Unknown octant provided : %d\n", octant);
            exit(-1);
    }
    return retval;
}

int get_octant(point initial, point final) {
    int rise = final.y - initial.y;
    int run = final.x - initial.x;
    bool mirror = false;
    int octant = 0;
    if (run < 0) {
        rise *= -1;
        run *= -1;
        mirror = true;
    }
    if (rise >= 0) {
        octant = (rise > run) ? 2 : 1;
    } else {
        octant = (-rise > run) ? 3 : 4;
    }
    if (mirror) {
        octant = ((octant - 1 + 4) % 8) + 1;
    }
    return octant;
}

// Implementation of Bresenham's line algorithm
void draw_line(pixel **pic, pixel pix, point p1, point p2) {
    int octant = get_octant(p1, p2);

    point p1_converted = convert_to_octant_1(p1, octant);
    point p2_converted = convert_to_octant_1(p2, octant);
    int x = p1_converted.x;
    int y = p1_converted.y;
    int A = p2_converted.y - p1_converted.y;
    int B = -(p2_converted.x - p1_converted.x);
    int d = 2 * A + B;
    while (x <= p2_converted.x) {
        plot(pic, pix, convert_from_octant_1(new_point(x, y), octant));
        if (d > 0) {
            y += 1;
            d += 2 * B;
        }
        x += 1;
        d += 2 * A;
    }
}

