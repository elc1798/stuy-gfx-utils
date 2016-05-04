// @formatter:off
/*========== MDLReader.java ==========
  MDLReader objects minimally contain an ArrayList<opCode> containing
  the opCodes generated when an mdl file is run through the java created
  lexer/parser, as well as the associated SymTab (Symbol Table).
  The provided methods are a constructor, and methods to print out the
  entries in the symbol table and command ArrayList.
  This is the only file you need to modify in order
  to get a working mdl project (for now).
  Your job is to go through each entry in opCodes and perform
  the required action from the list below:
  push: push a new origin matrix onto the origin stack
  pop: remove the top matrix on the origin stack
  move/scale/rotate: create a transformation matrix
                     based on the provided values, then
                     multiply the current top of the
                     origins stack by it.
  box/sphere/torus: create a solid object based on the
                    provided values. Store that in a
                    temporary matrix, multiply it by the
                    current top of the origins stack, then
                    call draw_polygons.
  line: create a line based on the provided values. Store
        that in a temporary matrix, multiply it by the
        current top of the origins stack, then call draw_lines.
  save: save the current screen with the provided filename
=================================*/
// @formatter:on

package org.stuygfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.stuygfx.graphics.Draw;
import org.stuygfx.graphics.EdgeMatrix;
import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;
import org.stuygfx.graphics.PolygonMatrix;
import org.stuygfx.graphics.TransformationStack;
import org.stuygfx.math.Transformations;
import org.stuygfx.parser.ParseException;
import org.stuygfx.parser.tables.OPBox;
import org.stuygfx.parser.tables.OPCode;
import org.stuygfx.parser.tables.OPDisplay;
import org.stuygfx.parser.tables.OPLine;
import org.stuygfx.parser.tables.OPMove;
import org.stuygfx.parser.tables.OPPop;
import org.stuygfx.parser.tables.OPPush;
import org.stuygfx.parser.tables.OPRotate;
import org.stuygfx.parser.tables.OPSave;
import org.stuygfx.parser.tables.OPScale;
import org.stuygfx.parser.tables.OPSphere;
import org.stuygfx.parser.tables.OPTorus;
import org.stuygfx.parser.tables.SymbolTable;

public class MDLReader {

    private ArrayList<OPCode> opcodes;
    private SymbolTable symbols;
    private Set<String> symKeys;
    private TransformationStack origins;
    private Image canvas;
    private EdgeMatrix em;
    private PolygonMatrix pm;

    String basename = "";
    String formatString;

    public MDLReader(ArrayList<OPCode> o, SymbolTable s) {
        opcodes = o;
        symbols = s;
        symKeys = s.keySet();

        canvas = new Image();
        canvas.shouldRelectOverX(true);
        em = new EdgeMatrix();
        pm = new PolygonMatrix();
        origins = new TransformationStack();
    }

    /**
     * Prints all commands that have been parsed by MDL
     */
    public void printCommands() {
        for (OPCode op : opcodes) {
            System.out.println(op);
        }
    }

    /**
     * Prints all systems that have been parsed by MDL
     */
    public void printSymbols() {
        System.out.println("Symbol Table:");
        for (String key : symKeys) {
            Object value = symbols.get(key);
            System.out.println(key + " = " + value);
        }
    }

    /**
     * Prints the matrix values in the stack
     */
    public void printStack() {
        origins.print();
    }

    public void process() throws ParseException {
        Iterator<OPCode> i = opcodes.iterator();
        OPCode opc;
        while (i.hasNext()) {
            opc = (OPCode) i.next();
            if (opc instanceof OPPush) {
                origins.push();
            } else if (opc instanceof OPPop) {
                origins.pop();
            } else if (opc instanceof OPMove) {
                double[] values = ((OPMove) opc).getValues();
                double dx = values[0];
                double dy = values[1];
                double dz = values[2];
                origins.addTranslate(dx, dy, dz);
            } else if (opc instanceof OPScale) {
                double[] values = ((OPScale) opc).getValues();
                double xFac = values[0];
                double yFac = values[1];
                double zFac = values[2];
                origins.addScale(xFac, yFac, zFac);
            } else if (opc instanceof OPRotate) {
                char axis = ((OPRotate) opc).getAxis();
                double degrees = ((OPRotate) opc).getDeg();
                switch (axis) {
                    case 'x':
                        origins.addRotX(degrees);
                        break;
                    case 'y':
                        origins.addRotY(degrees);
                        break;
                    case 'z':
                        origins.addRotZ(degrees);
                        break;
                    default:
                        System.err.println("INVALID AXIS!!!");
                        System.exit(1);
                }
            } else if (opc instanceof OPBox) {
                OPBox opb = (OPBox) opc;
                double[] loc = opb.getRootCoor();
                double x = loc[0];
                double y = loc[1];
                double z = loc[2];

                double[] dim = opb.getDimensions();
                double dx = dim[0];
                double dy = dim[1];
                double dz = dim[2];

                pm.addRectPrism((int) x, (int) y, (int) z, (int) dx, (int) dy, (int) dz);
            } else if (opc instanceof OPSphere) {
                OPSphere ops = (OPSphere) opc;
                double[] center = ops.getCenter();
                double cx = center[0];
                double cy = center[1];
                double cz = center[2];

                double radius = ops.getRadius();

                pm.addSphere(cx, cy, cz, radius);
            } else if (opc instanceof OPTorus) {
                OPTorus opt = (OPTorus) opc;
                double[] center = opt.getCenter();
                double cx = center[0];
                double cy = center[1];
                double cz = center[2];

                double outerRadius = opt.getOuterRadius();
                double crossSectionRadius = opt.getCrossSectionRadius();

                pm.addTorus(cx, cy, cz, outerRadius, crossSectionRadius);
            } else if (opc instanceof OPLine) {
                double[] start = ((OPLine) opc).getP1();
                double x0 = start[0];
                double y0 = start[1];
                double z0 = start[2];

                double[] end = ((OPLine) opc).getP2();
                double x1 = end[0];
                double y1 = end[1];
                double z1 = end[2];

                em.addEdge(new Point((int) x0, (int) y0, (int) z0), new Point((int) x1, (int) y1, (int) z1));
            } else if (opc instanceof OPSave) {
                String filename = ((OPSave) opc).getName();
                save(filename);
            } else if (opc instanceof OPDisplay) {
                save(CONSTANTS.TMP_FILE_NAME);
                try {
                    Runtime.getRuntime().exec("display " + CONSTANTS.TMP_FILE_NAME).waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Apply
            System.out.println("APPLYING TRANSFORMATIONS USING " + origins.peek().toString());
            Transformations.applyTransformation(origins.peek(), em);
            Transformations.applyTransformation(origins.peek(), pm);
            // Draw
            Draw.polygonMatrix(canvas, new Pixel(255, 20, 255), pm);
            Draw.edgeMatrix(canvas, new Pixel(255, 0, 0), em);
            // Empty
            em.empty();
            pm.empty();
        }
    }

    private void save(String filename) {
        try {
            PPMGenerator.createPPM(filename, canvas);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
