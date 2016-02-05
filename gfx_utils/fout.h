/*
 * Small C library to easily write to a file
 */
#include "netpbm.h"

#ifndef _FWRITE
#define _FWRITE
int get_size_of_buff(int XRES, int YRES, int MAX_COLOR_VAL);
char *pic2string(pixel **pic, int XRES, int YRES, int MAX_COLOR_VAL);
int filewrite(char *filename, char *buffer, int bytes);
#endif
