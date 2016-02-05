#ifndef _NETPBM
#define _NETPBM

#define PPM_HEADER "P3"

typedef int pixel;

int get_red(pixel p);
int get_green(pixel p);
int get_blue(pixel p);

void set_red(pixel *p, int r);
void set_green(pixel *p, int g);
void set_blue(pixel *p, int b);

pixel new_pixel(int r, int g, int b);

#endif
