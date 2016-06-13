# stuy-gfx-utils

I got really lazy so I ported the entire C project to Java. So now, here it is!
I did a lot of hackery with Java Reflects to get the Interpreter to work nicely
and to make it easy to add functions.

There 2 main ways to build the project:

```
$ ant
$ make recompile
```

To run the project, simply do:

```
$ make run
```

If you wish to use another `.pgi` file (Procedurally Generated Image), do:

```
$ java -jar StuyGFX.jar file.pgi
```

## Features implemented

- Flat shading
- Importing Waveform OBJ files
- ZBuffer
- Backface culling
- Animation

