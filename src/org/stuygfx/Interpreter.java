package org.stuygfx;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.stuygfx.graphics.Draw;
import org.stuygfx.graphics.EdgeMatrix;
import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;
import org.stuygfx.math.MasterTransformationMatrix;
import org.stuygfx.math.Transformations;

public class Interpreter {

    private final ConcurrentHashMap<String, Command> fxnMapper;
    private Image canvas;
    private EdgeMatrix em;
    private MasterTransformationMatrix masterTrans;

    private class Command {

        private Method func;
        private Object caller;

        public Command(Object caller, Method func) {
            this.caller = caller;
            this.func = func;
        }

        public Object run(Object[] params) {
            try {
                return func.invoke(caller, params);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @SuppressWarnings("rawtypes")
        public Class[] getParamTypes() {
            return this.func.getParameterTypes();
        }
    }

    public Interpreter() {
        fxnMapper = new ConcurrentHashMap<String, Command>();
        canvas = new Image();
        canvas.shouldRelectOverX(true);
        em = new EdgeMatrix();
        masterTrans = new MasterTransformationMatrix();

        initializeDefinitions();
    }

    private void addDef(String key, Command cmd) {
        fxnMapper.put(key, cmd);
    }

    @SuppressWarnings("rawtypes")
    private void addEdgeMatrixOp(String key, String fxnName, Class[] paramTypes) {
        try {
            addDef(key, new Command(em, EdgeMatrix.class.getMethod(fxnName, paramTypes)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void addTransformOp(String key, String fxnName, Class[] paramTypes) {
        try {
            addDef(key, new Command(masterTrans, MasterTransformationMatrix.class.getMethod(fxnName, paramTypes)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void draw() {
        Draw.edgeMatrix(canvas, new Pixel(0, 255, 0), em);
    }

    public void apply() {
        Transformations.applyTransformation(masterTrans, em);
    }

    public void save(String filename) {
        draw();
        try {
            PPMGenerator.createPPM(filename, canvas);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void display() {
        save(CONSTANTS.TMP_FILE_NAME);
        try {
            Runtime.getRuntime().exec("display " + CONSTANTS.TMP_FILE_NAME).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        System.gc();
        System.exit(0);
    }

    public void initializeDefinitions() {
        addEdgeMatrixOp("line", "addEdge", new Class[] {
            Point.class, Point.class
        });

        addEdgeMatrixOp("circle", "addCircle", new Class[] {
            Point.class, Double.class
        });

        addEdgeMatrixOp("hermite", "addHermiteCurve", new Class[] {
            Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class,
            Double.class
        });

        addEdgeMatrixOp("bezier", "addBezierCurve", new Class[] {
            Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class,
            Double.class
        });

        addEdgeMatrixOp("clear", "empty", new Class[] {});

        addEdgeMatrixOp("box", "addBox", new Class[] {
            Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
        });

        addEdgeMatrixOp("sphere", "addSphere", new Class[] {
            Double.class, Double.class, Double.class, Double.class
        });

        addTransformOp("ident", "reset", new Class[] {});

        addTransformOp("scale", "addScale", new Class[] {
            Double.class, Double.class, Double.class
        });

        addTransformOp("translate", "addTranslate", new Class[] {
            Integer.class, Integer.class, Integer.class
        });

        addTransformOp("xrotate", "addRotX", new Class[] {
            Double.class
        });

        addTransformOp("yrotate", "addRotY", new Class[] {
            Double.class
        });

        addTransformOp("zrotate", "addRotZ", new Class[] {
            Double.class
        });

        try {
            addDef("apply", new Command(this, Interpreter.class.getMethod("apply", new Class[] {})));
            addDef("save", new Command(this, Interpreter.class.getMethod("save", new Class[] {
                String.class
            })));
            addDef("display", new Command(this, Interpreter.class.getMethod("display", new Class[] {})));
            addDef("quit", new Command(this, Interpreter.class.getMethod("cleanup", new Class[] {})));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public Object call(String key, Object[] params) {
        if (fxnMapper.containsKey(key)) {
            return fxnMapper.get(key).run(params);
        } else {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public Object[] getParams(String cmd, String[] args) {
        if (!fxnMapper.containsKey(cmd)) {
            return null;
        }

        int counter = 0;
        Class[] paramTypes = fxnMapper.get(cmd).getParamTypes();
        ArrayList<Object> params = new ArrayList<Object>();

        for (Class c : paramTypes) {
            if (c.equals(Point.class)) {
                params.add(new Point(Integer.parseInt(args[counter]), Integer.parseInt(args[counter + 1])));
                counter += 2;
            } else if (c.equals(Double.class)) {
                params.add(Double.parseDouble(args[counter]));
                counter++;
            } else if (c.equals(Integer.class)) {
                params.add(Integer.parseInt(args[counter]));
                counter++;
            } else {
                System.err.println("Unrecognized parameter type! Fix dictionary!");
                System.exit(-1);
            }
        }

        return params.toArray();
    }

    public Object[] getParams(String cmd, String args) {
        System.out.printf("Getting parameters for [%s]\n", args);
        return getParams(cmd, args.split(" "));
    }

    public boolean hasNoParams(String cmd) {
        return fxnMapper.get(cmd).getParamTypes().length == 0;
    }
}
