# stuy-gfx-utils

A helper library in C for the Computer Graphics course at Stuyvesant High School, NYC

### How to use this library

Clone the repo or download it, and copy the `gfx_utils` folder into your project folder.

Depending on the relative path to the `gfx_utils` folder, the path of the `include` will change, but should look like
the following:

```
#include "gfx_utils/fout.h"
#include "gfx_utils/netpbm.h"
```

It is also handy to have some `defined` constants:

```
#define FILENAME    "pic1.ppm"
#define XRES        512
#define YRES        512
#define MAX_C_VAL   255
```

To begin working with a picture, we need to initialize it:

```
pixel **pic;
pic = malloc(XRES * sizeof(pixel*));
int i; for (i = 0; i < XRES; i++) {
    pic[i] = malloc(YRES * sizeof(pixel));
}
```

Because we are using pointers, it is good practice to `free` this memory after we are done using it:

```
for (i = 0; i < XRES; i++) {
    free(pic[i]);
    pic[i] = NULL;
}
free(pic);
pic = NULL;
```

To write the picture out to a file, we only need a single line:

```
filewrite(FILENAME, pic2string(pic, XRES, YRES, MAX_C_VAL), get_size_of_buff(XRES, YRES, MAX_C_VAL));
```

Replace the parameters as necessary.

