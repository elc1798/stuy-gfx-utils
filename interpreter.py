import sys, os

FILE_HEAD = """
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
#include "gfx_utils/matrix.h"
#include "gfx_utils/draw.h"

#define FILENAME    "rendered.ppm"

extern int XRES;
extern int YRES;
extern int MAX_C_VAL;

int main() {
    // XRES and YRES exist already. This allows easy configuration
    // The following are the default values of these constants
    XRES = 512;
    YRES = 512;
    MAX_C_VAL = 255;

    pixel **pic = new_picture(XRES, YRES);
    point_matrix *pt_mat = NULL;
    matrix *master_transformation_matrix = NULL;

    fill_rect(pic, new_pixel(0, 0, 0), new_point(0, 0), new_point(XRES, YRES));
"""

FILE_FOOT = """
    free_picture(pic);
    pic = NULL;

    free_point_matrix(pt_mat);
    pt_mat = NULL;

    free_matrix(master_transformation_matrix);
    master_transformation_matrix = NULL;

    return 0;
}
"""

FILE_BODY = ""

KNOWN_COMMANDS = {
    "line" : {
        "argc" : 6,
        "function" : "pt_mat = add_edge(pt_mat, new_edge(new_point_3d(%d, %d, %d), new_point_3d(%d, %d, %d)));"
    },
    "circle" : {
        "argc" : 3,
        "function" : "pt_mat = add_circle(pt_mat, new_point(%d, %d), %d);"
    },
    "hermite" : {
        "argc" : 8,
        "function" : "pt_mat = add_hermite(pt_mat, %d, %d, %d, %d, %d, %d, %d, %d);"
    },
    "bezier" : {
        "argc" : 8,
        "function" : "pt_mat = add_bezier(pt_mat, %d, %d, %d, %d, %d, %d, %d, %d);"
    },
    "display" : {
        "argc" : 0,
        "function" : """
            render_point_matrix(pic, new_pixel(0, 255, 0), pt_mat);
            char *s = pic2string(pic);
            filewrite(FILENAME, s, get_size_of_buff());
            free(s);
            s = NULL;
        """,
        "shell" : "make run"
    }
}

TO_EXEC = []

def interpret(commands):
    global FILE_BODY, TO_EXEC
    counter = 0
    while counter < len(commands):
        line = commands[counter]
        if line not in KNOWN_COMMANDS:
            print "Unknown command: %s" % (line);

        argc = KNOWN_COMMANDS[line]["argc"]
        if argc > 0:
            counter += 1

            args = commands[counter].strip().split(" ")

            if len(args) != argc:
                print("Line %d:" % (counter))
                print("Argument Mismatch: Expected %d, got %d" % (argc, len(args)))
                sys.exit(1)

        if argc == 0:
            FILE_BODY += KNOWN_COMMANDS[line]["function"]
        else:
            intified = [ int(x) for x in args ]
            FILE_BODY += KNOWN_COMMANDS[line]["function"] % tuple(intified)
        FILE_BODY += "\n"

        if "shell" in KNOWN_COMMANDS[line]:
            TO_EXEC.append(KNOWN_COMMANDS[line]["shell"]);

        counter += 1

    OUTFILE = open("main.c", 'w')
    OUTFILE.write("\n".join((FILE_HEAD, FILE_BODY, FILE_FOOT)))
    OUTFILE.close()

def main():
    if len(sys.argv) > 1:
        f = open(sys.argv[1], 'r')
        cmds = f.read().strip().split("\n")
        interpret(cmds)
    for sh in TO_EXEC:
        os.system(sh)

if __name__ == "__main__":
    main()

