#ifndef _NETPBM
#define _NETPBM

#define PPM_HEADER "P3"

typedef int pixel;
typedef struct point_t {
    int x;
    int y;
} point;

int get_red(pixel p);
int get_green(pixel p);
int get_blue(pixel p);

pixel new_pixel(int r, int g, int b);
point new_point(int x, int y);
pixel **new_picture(int xres, int yres);
void free_picture(pixel **pic);

#endif
