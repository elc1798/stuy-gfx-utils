/*
 * Small C library to easily write to a file
 */
#include "netpbm.h"

#ifndef _FWRITE
#define _FWRITE
int get_size_of_buff();
char *pic2string(pixel **pic);
int filewrite(char *filename, char *buffer, int bytes);
#endif
