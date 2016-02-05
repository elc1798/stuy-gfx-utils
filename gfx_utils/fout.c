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

int get_size_of_buff(int XRES, int YRES, int MAX_COLOR_VAL) {
    // Explicitly stating the required size of the buffer for clarity
    int size = 0;
    size += 3;                                  // 'P3' header + space
    size += charlen_of_int(XRES) + 1;           // x resolution + space
    size += charlen_of_int(YRES) + 1;           // y resolution + space
    size += charlen_of_int(MAX_COLOR_VAL) + 1;  // max color value + space
    size += 4 * 3 * XRES * YRES;                // length 3 numbers + space for every pixel
    return size;
}

char *pic2string(pixel **pic, int XRES, int YRES, int MAX_COLOR_VAL) {
    int size = get_size_of_buff(XRES, YRES, MAX_COLOR_VAL);
    char *buff = malloc(size * sizeof(char));
    char *tmp = malloc(64 * sizeof(char));

    strncat(buff, PPM_HEADER, 2);

    sprintf(tmp, "\n%d %d %d\n", XRES, YRES, MAX_COLOR_VAL);
    strncat(buff, tmp, 64);

    int curr = strlen(buff);
    int i, j;

    for (i = 0; i < XRES; i++) {
        for (j = 0; j < YRES; j++) {
            sprintf(tmp, "%d %d %d ", get_red(pic[i][j]), get_green(pic[i][j]), get_blue(pic[i][j]));
            strncpy(buff + curr, tmp, 4 * 3);
            curr += strlen(tmp);
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

