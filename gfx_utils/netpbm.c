/*
 * Small C library that packages RGB values into ints using simple bit shifts
 */

#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "netpbm.h"
#include "matrix.h"

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

// Helpful parameterized functions for a generic circle

double circle_x(double t, int cx, int r) {
    return r * cos(2 * M_PI * t) + cx;
}

double circle_y(double t, int cy, int r) {
    return r * sin(2 * M_PI * t) + cy;
}

point_matrix *add_circle(point_matrix *pt_mat, point center, int radius) {
    double x0, y0, x, y, t;
    double step = 0.01;
    double CEILING = 1.001;

    x0 = circle_x(0, center.x, radius);
    y0 = circle_y(0, center.y, radius);

    for (t = step; t < CEILING; t += step) {
        x = circle_x(t, center.x, radius);
        y = circle_y(t, center.y, radius);
        pt_mat = add_edge(pt_mat, new_edge(new_point(x0, y0), new_point(x, y)));

        x0 = x;
        y0 = y;
    }

    return pt_mat;
}

double hermite_polynomial(double t, double a, double b, double c, double d) {
    return a * pow(t, 3) + b * pow(t, 2) + c * t + d;
}

point_matrix *add_hermite(point_matrix *pt_mat, double x0, double y0,
                            double dx0, double dy0, double x1, double y1,
                            double dx1, double dy1) {
    matrix *x_in = new_matrix(1, 4);
    matrix *y_in = new_matrix(1, 4);

    x_in->contents[0][0] = x0;
    x_in->contents[0][1] = dx0;
    x_in->contents[0][2] = x1;
    x_in->contents[0][3] = dx1;

    y_in->contents[0][0] = y0;
    y_in->contents[0][1] = dy0;
    y_in->contents[0][2] = y1;
    y_in->contents[0][3] = dy1;

    matrix *inverse = new_matrix(4, 4);
    inverse->contents[0][0] = 2;
    inverse->contents[0][1] = -2;
    inverse->contents[0][2] = 1;
    inverse->contents[0][3] = 1;

    inverse->contents[1][0] = -3;
    inverse->contents[1][1] = 3;
    inverse->contents[1][2] = -2;
    inverse->contents[1][3] = -1;

    inverse->contents[2][0] = 0;
    inverse->contents[2][1] = 0;
    inverse->contents[2][2] = 1;
    inverse->contents[2][3] = 0;

    inverse->contents[3][0] = 1;
    inverse->contents[3][1] = 0;
    inverse->contents[3][2] = 0;
    inverse->contents[3][3] = 0;

    matrix *res_x = cross_product(inverse, x_in);
    matrix *res_y = cross_product(inverse, y_in);

    double x_i, y_i, x_f, y_f, t;
    double step = 0.01;
    double CEILING = 1.001;

    x_i = hermite_polynomial(0, res_x->contents[0][0], res_x->contents[0][1], res_x->contents[0][2], res_x->contents[0][3]);
    y_i = hermite_polynomial(0, res_y->contents[0][0], res_y->contents[0][1], res_y->contents[0][2], res_y->contents[0][3]);

    for (t = step; t < CEILING; t += step) {
        x_f = hermite_polynomial(t, res_x->contents[0][0], res_x->contents[0][1], res_x->contents[0][2], res_x->contents[0][3]);
        y_f = hermite_polynomial(t, res_y->contents[0][0], res_y->contents[0][1], res_y->contents[0][2], res_y->contents[0][3]);
        pt_mat = add_edge(pt_mat, new_edge(new_point(x_i, y_i), new_point(x_f, y_f)));
        x_i = x_f;
        y_i = y_f;
    }

    return pt_mat;

}

double bezier_polynomial(double t, double a, double b, double c, double d) {
    return a * pow(1 - t, 3) + 3 * b * pow(1 - t, 2) * t + 3 * c * (1 - t) * pow(t, 2) + d * pow(t, 3);
}

point_matrix *add_bezier(point_matrix *pt_mat,
                         float x0, float y0,
                         float x1, float y1,
                         float x2, float y2,
                         float x3, float y3) {
    double x_i, y_i, x_f, y_f, t;
    double step = 0.01;
    double CEILING = 1.001;

    x_i = bezier_polynomial(0, x0, x1, x2, x3);
    y_i = bezier_polynomial(0, y0, y1, y2, y3);

    for (t = step; t < CEILING; t += step) {
        x_f = bezier_polynomial(t, x0, x1, x2, x3);
        y_f = bezier_polynomial(t, y0, y1, y2, y3);
        pt_mat = add_edge(pt_mat, new_edge(new_point(x_i, y_i), new_point(x_f, y_f)));
        x_i = x_f;
        y_i = y_f;
    }
    return pt_mat;
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

