/*
 * Small C library that packages RGB values into ints using simple bit shifts
 */

#include <stdlib.h>
#include "netpbm.h"

#define RED_SHIFT 16
#define GREEN_SHIFT 8
#define BLUE_SHIFT 0
#define BIT_MASK 0xFF

int XRES = 512;
int YRES = 512;
int MAX_C_VAL = 255;

int get_red(pixel p) {
    return (p >> RED_SHIFT) & BIT_MASK;
}

int get_green(pixel p) {
    return (p >> GREEN_SHIFT) & BIT_MASK;
}

int get_blue(pixel p) {
    return (p >> BLUE_SHIFT) & BIT_MASK;
}

pixel new_pixel(int r, int g, int b) {
    int retval = 0;
    return retval | ((r & BIT_MASK) << RED_SHIFT) | ((g & BIT_MASK) << GREEN_SHIFT) | ((b & BIT_MASK) << BLUE_SHIFT);
}

point new_point(int x, int y) {
    point retval;
    retval.x = x;
    retval.y = y;
    retval.z = 0;
    return retval;
}

point new_point_3d(int x, int y, int z) {
    point retval;
    retval.x = x;
    retval.y = y;
    retval.z = z;
    return retval;
}

edge new_edge(point start, point end) {
    edge retval;
    retval.start = start;
    retval.end = end;
    return retval;
}

void free_point_matrix(point_matrix *pt_mat) {
    if (pt_mat && pt_mat->next) {
        free_point_matrix(pt_mat->next);
        free(pt_mat);
    }
}

point_matrix *new_pt_mat_point(point p) {
    point_matrix *retval = malloc(sizeof(point_matrix));
    retval->prev = NULL;
    retval->next = NULL;
    retval->pt = p;
    retval->connect = false;
    return retval;
}

point_matrix *add_point(point_matrix *pt_mat, point p) {
    if (!pt_mat) {
        return new_pt_mat_point(p);
    } else {
        point_matrix *new_pt = new_pt_mat_point(p);
        new_pt->next = pt_mat;
        pt_mat->prev = new_pt;
        return new_pt;
    }
}

point_matrix *add_edge(point_matrix *pt_mat, edge e) {
    point_matrix *endpt = add_point(pt_mat, e.end);
    point_matrix *startpt = new_pt_mat_point(e.start);
    startpt->connect = true;
    startpt->next = endpt;
    endpt->prev = startpt;
    return startpt;
}

pixel **new_picture(int xres, int yres) {
    pixel **pic;
    pic = malloc(yres * sizeof(pixel*));
    int i; for (i = 0; i < xres; i++) {
        pic[i] = malloc(xres * sizeof(pixel));
    }
    XRES = xres;
    YRES = yres;
    return pic;
}

void free_picture(pixel **pic) {
    int i; for (i = 0; i < YRES; i++) {
        free(pic[i]);
        pic[i] = NULL;
    }
    free(pic);
}

