package org.stuygfx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.stuygfx.graphics.EdgeMatrix;
import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Point;
import org.stuygfx.math.MasterTransformationMatrix;
import org.stuygfx.math.Matrix;

public class Interpreter {

    private final ConcurrentHashMap<String, Command> fxnMapper;
    public Image canvas;
    public EdgeMatrix em;
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

    public void initializeDefinitions() {
        try {

            addDef("line", new Command(em, EdgeMatrix.class.getMethod("addEdge", new Class[] {
                    Point.class, Point.class
            })));

            addDef("circle", new Command(em, EdgeMatrix.class.getMethod("addCircle", new Class[] {
                    Point.class, Double.class
            })));

            addDef("hermite", new Command(em, EdgeMatrix.class.getMethod("addHermiteCurve", new Class[] {
                    Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class,
                    Double.class
            })));

            addDef("bezier", new Command(em, EdgeMatrix.class.getMethod("addBezierCurve", new Class[] {
                    Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class,
                    Double.class
            })));
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
            } else {
                System.err.println("Unrecognized parameter type! Fix dictionary!");
                System.exit(-1);
            }
        }

        return params.toArray();
    }

    public Object[] getParams(String cmd, String args) {
        return getParams(cmd, args.split(" "));
    }
}
