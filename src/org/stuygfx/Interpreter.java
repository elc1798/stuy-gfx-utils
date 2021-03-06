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
import org.stuygfx.graphics.PolygonMatrix;
import org.stuygfx.graphics.TransformationStack;
import org.stuygfx.math.Transformations;

public class Interpreter {

    private final ConcurrentHashMap<String, Command> fxnMapper;
    private ArrayList<String> transFxns;
    private ArrayList<String> drawFxns;
    private Image canvas;
    private EdgeMatrix em;
    private PolygonMatrix pm;
    private TransformationStack transStack;

    private class Command {

        private Method func;
        private Object caller;

        public Command(Object caller, Method func) {
            this.caller = caller;
            this.func = func;
        }

        public Object run(Object[] params) {
            try {
                System.out.println("Running [ " + func.toString() + " ] on " + caller.toString());
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
        transFxns = new ArrayList<String>();
        drawFxns = new ArrayList<String>();
        canvas = new Image();
        canvas.shouldRelectOverX(true);
        em = new EdgeMatrix();
        pm = new PolygonMatrix();
        transStack = new TransformationStack();

        em.empty();
        pm.empty();
        initializeDefinitions();
    }

    private void addDef(String key, Command cmd) {
        fxnMapper.put(key, cmd);
    }

    @SuppressWarnings("rawtypes")
    private void addEdgeMatrixOp(String key, String fxnName, Class[] paramTypes) {
        try {
            addDef(key, new Command(em, EdgeMatrix.class.getMethod(fxnName, paramTypes)));
            drawFxns.add(key);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void addPolygonMatrixOp(String key, String fxnName, Class[] paramTypes) {
        try {
            addDef(key, new Command(pm, PolygonMatrix.class.getMethod(fxnName, paramTypes)));
            drawFxns.add(key);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void addTransformOp(String key, String fxnName, Class[] paramTypes) {
        try {
            addDef(key, new Command(transStack, TransformationStack.class.getMethod(fxnName, paramTypes)));
            transFxns.add(key);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void addStackOp(String key, String fxnName, Class[] paramTypes) {
        try {
            addDef(key, new Command(transStack, TransformationStack.class.getMethod(fxnName, paramTypes)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void draw() {
        Draw.polygonMatrix(canvas, new Pixel(255, 20, 255), pm);
        Draw.edgeMatrix(canvas, new Pixel(255, 0, 0), em);
    }

    public boolean contains(String key) {
        return fxnMapper.containsKey(key);
    }

    public void apply() {
        System.out.println("APPLYING TRANSFORMATIONS USING " + transStack.peek().toString());
        Transformations.applyTransformation(transStack.peek(), em);
        Transformations.applyTransformation(transStack.peek(), pm);
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

        addEdgeMatrixOp("box_dots", "addBox", new Class[] {
            Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
        });

        addEdgeMatrixOp("sphere_dots", "addSphere", new Class[] {
            Double.class, Double.class, Double.class, Double.class
        });

        addEdgeMatrixOp("torus_dots", "addTorus", new Class[] {
            Double.class, Double.class, Double.class, Double.class, Double.class
        });

        addPolygonMatrixOp("sphere_mesh", "addSphere", new Class[] {
            Double.class, Double.class, Double.class, Double.class
        });

        addPolygonMatrixOp("box_mesh", "addRectPrism", new Class[] {
            Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
        });

        addPolygonMatrixOp("torus_mesh", "addTorus", new Class[] {
            Double.class, Double.class, Double.class, Double.class, Double.class
        });

        addPolygonMatrixOp("sphere", "addSphere", new Class[] {
            Double.class, Double.class, Double.class, Double.class
        });

        addPolygonMatrixOp("box", "addRectPrism", new Class[] {
            Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
        });

        addPolygonMatrixOp("torus", "addTorus", new Class[] {
            Double.class, Double.class, Double.class, Double.class, Double.class
        });

        addTransformOp("ident", "reset", new Class[] {});

        addTransformOp("scale", "addScale", new Class[] {
            Double.class, Double.class, Double.class
        });

        addTransformOp("translate", "addTranslate", new Class[] {
            Double.class, Double.class, Double.class
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

        addStackOp("push", "push", new Class[] {});

        addStackOp("pop", "pop", new Class[] {});

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
        em.empty();
        pm.empty();
        if (fxnMapper.containsKey(key)) {
            Object retval = fxnMapper.get(key).run(params);
            if (drawFxns.contains(key)) {
                apply();
                draw();
                em.empty();
                pm.empty();
            }
            return retval;
        } else {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public Object[] getParams(String cmd, String[] args) {
        if (!fxnMapper.containsKey(cmd)) {
            System.err.println("!! Command not recognized, cannot get parameters");
            return null;
        }

        int counter = 0;
        int required = 0;
        Class[] paramTypes = fxnMapper.get(cmd).getParamTypes();
        ArrayList<Object> params = new ArrayList<Object>();

        for (Class c : paramTypes) {
            required = (c.equals(Point.class)) ? 2 : 1; // Point needs 2,
                                                        // everything else needs
                                                        // 1
            if (counter + required - 1 >= args.length) {
                System.err.println("!! Incorrect number of arguments provided");
                return null;
            }
            if (c.equals(Point.class)) {
                params.add(new Point(Integer.parseInt(args[counter]), Integer.parseInt(args[counter + 1])));
                counter += 2;
            } else if (c.equals(Double.class)) {
                params.add(Double.parseDouble(args[counter]));
                counter++;
            } else if (c.equals(Integer.class)) {
                params.add(Integer.parseInt(args[counter]));
                counter++;
            } else if (c.equals(String.class)) {
                params.add(args[counter].toString());
                counter++;
            } else {
                System.err.println("!! Unrecognized parameter type! Fix dictionary!");
                System.exit(-1);
            }
        }

        return params.toArray();
    }

    public Object[] getParams(String cmd, String args) {
        System.out.printf(">> Getting parameters for [%s]\n", args);
        return getParams(cmd, args.split(" "));
    }

    public boolean hasNoParams(String cmd) {
        return fxnMapper.get(cmd).getParamTypes().length == 0;
    }
}
