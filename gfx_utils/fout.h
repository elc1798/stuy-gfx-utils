/*
 * Small C library to easily write to a file
 */
#include "netpbm.h"

#ifndef _FWRITE
#define _FWRITE
int get_size_of_buff(pixel pic[512][512], int XRES, int YRES);
char *pic2string(pixel pic[512][512], int XRES, int YRES);
int filewrite(char *filename, char *buffer, int bytes);
#endif
