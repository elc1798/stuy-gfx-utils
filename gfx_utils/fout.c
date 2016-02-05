/*
 * Small C library to easily write to a file.
 *
 * Includes utility functions for converting a 2-D pixel array to a string for easy
 * writing, as well as a function to get an approximate number of bytes for a
 * 2-D pixel array.
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>

#include "fout.h"
#include "netpbm.h"

int charlen_of_int(int i) {
    int len = 0;
    while (i) {
        i = i / 10;
        len++;
    }
    return len;
}

int get_size_of_buff(pixel pic[512][512], int XRES, int YRES) {
    // Explicitly stating the required size of the buffer for clarity
    int size = 0;
    size += 3;                          // 'P3' header + space
    size += charlen_of_int(XRES) + 1;   // x resolution + space
    size += charlen_of_int(YRES) + 1;   // y resolution + space
    size += 4 * 3 * XRES * YRES;        // length 3 numbers + space for every pixel
    return size;
}

char *pic2string(pixel pic[512][512], int XRES, int YRES) {
    int size = get_size_of_buff(pic, XRES, YRES);
    char *buff = malloc(size * sizeof(char));
    char *tmp = malloc(64 * sizeof(char));

    strncat(buff, PPM_HEADER, 2);
    strncat(buff, " ", 1);

    sprintf(tmp, "%d %d\n", XRES, YRES);
    strncat(buff, tmp, 64);

    int i, j;
    for (i = 0; i < XRES; i++) {
        for (j = 0; j < YRES; j++) {
            sprintf(tmp, "%d %d %d ", get_red(pic[i][j]), get_green(pic[i][j]), get_blue(pic[i][j]));
            strncat(buff, tmp, 4 * 3);
        }
    }
    free(tmp);
    return buff;
}

int filewrite(char *filename, char *buffer, int bytes) {
    int FOUT = open(filename, O_RDONLY | O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (errno) {
        return errno;
    }
    write(FOUT, buffer, bytes);
    if (errno) {
        return errno;
    }
    return close(FOUT);
}

