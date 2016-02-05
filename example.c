#include <stdlib.h>
#include <stdio.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"

#define FILENAME    "pic1.ppm"
#define XRES        512
#define YRES        512
#define MAX_C_VAL   255

void fill_rect(pixel pic[XRES][YRES], int r, int g, int b, int x, int y, int dx, int dy) {
    int i,j;
    for (i = x; i < dx; i++) {
        for (j = y; j < dy; j++) {
            pic[i][j] = new_pixel(r, g, b);
        }
    }
}

int main() {
    pixel pic[XRES][YRES];
    // White base
    fill_rect(pic, 255, 255, 255, 0, 0, XRES, YRES);
    filewrite(FILENAME, pic2string(pic, XRES, YRES), get_size_of_buff(pic, XRES, YRES));
    return 0;
}

