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

import static org.stuygfx.CONSTANTS.ANSI_RED;
import static org.stuygfx.CONSTANTS.ANSI_RESET;
import static org.stuygfx.CONSTANTS.ANSI_YELLOW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.stuygfx.graphics.Draw;
import org.stuygfx.graphics.EdgeMatrix;
import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Point;
import org.stuygfx.graphics.PolygonMatrix;
import org.stuygfx.graphics.TransformationStack;
import org.stuygfx.graphics.Lighting.AmbientSource;
import org.stuygfx.graphics.Lighting.PointSource;
import org.stuygfx.math.Matrix;
import org.stuygfx.math.Transformations;
import org.stuygfx.parser.ParseException;
import org.stuygfx.parser.tables.OPAmbient;
import org.stuygfx.parser.tables.OPBasename;
import org.stuygfx.parser.tables.OPBox;
import org.stuygfx.parser.tables.OPCode;
import org.stuygfx.parser.tables.OPConstants;
import org.stuygfx.parser.tables.OPDisplay;
import org.stuygfx.parser.tables.OPFrames;
import org.stuygfx.parser.tables.OPLight;
import org.stuygfx.parser.tables.OPLine;
import org.stuygfx.parser.tables.OPMove;
import org.stuygfx.parser.tables.OPPop;
import org.stuygfx.parser.tables.OPPush;
import org.stuygfx.parser.tables.OPRotate;
import org.stuygfx.parser.tables.OPSave;
import org.stuygfx.parser.tables.OPScale;
import org.stuygfx.parser.tables.OPSphere;
import org.stuygfx.parser.tables.OPTorus;
import org.stuygfx.parser.tables.OPTrans;
import org.stuygfx.parser.tables.OPVary;
import org.stuygfx.parser.tables.SymbolTable;

public class MDLReader {

    private ArrayList<OPCode> opcodes;
    private SymbolTable symbols;
    private Set<String> symKeys;
    private TransformationStack origins;
    private Image canvas;
    private EdgeMatrix em;
    private PolygonMatrix pm;

    private String basename;
    private String formatString;

    // For animation
    private boolean isAnimated;
    private int numFrames;
    private Hashtable<String, Double[]> knobs;

    // For lighting
    private AmbientSource ambient;
    private Hashtable<String, PointSource> lights;
    private Hashtable<String, Matrix> lightConstants;

    public MDLReader(ArrayList<OPCode> o, SymbolTable s) {
        opcodes = o;
        symbols = s;
        symKeys = s.keySet();

        canvas = new Image();
        canvas.shouldRelectOverX(true);
        em = new EdgeMatrix();
        pm = new PolygonMatrix();
        origins = new TransformationStack();

        basename = "";
        isAnimated = false;
        numFrames = -1;
        knobs = new Hashtable<String, Double[]>();

        ambient = null;
        lights = new Hashtable<String, PointSource>();
        lightConstants = new Hashtable<String, Matrix>();
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

    /**
     * Prints knobs and knob values for each frame (for debugging purposes)
     */
    public void printKnobs() {
        for (String key : knobs.keySet()) {
            System.out.printf("%s: %s\n", key, Arrays.toString(knobs.get(key)));
        }
    }

    public void printWarning(String message) {
        System.out.printf("%s[WARNING] %s%s", ANSI_YELLOW, message, ANSI_RESET);
    }

    public void printError(String message) {
        System.out.printf("%s[ERROR] %s%s", ANSI_RED, message, ANSI_RESET);
    }

    public void throwError(String message) throws ParseException {
        printError(message);
        throw new ParseException();
    }

    public void checkForAnimation() throws ParseException {
        for (OPCode opc : opcodes) {
            if (opc instanceof OPFrames) {
                if (numFrames != -1) { // -1 is the value for unset frame value
                    printWarning("Number of animation frames set multiple times");
                }
                isAnimated = true;
                numFrames = ((OPFrames) opc).getNum();
            } else if (opc instanceof OPBasename) {
                if (basename.length() != 0) {
                    printWarning("Animation frame / image file basename set multiple times");
                }
                isAnimated = true;
                basename = ((OPBasename) opc).getName();
            } else if (opc instanceof OPVary) {
                isAnimated = true;
            }
        }
    }

    public void animationPass() throws ParseException, IOException {
        // Ensure isAnimated is set to false beforehand
        isAnimated = false;
        checkForAnimation();
        if (!isAnimated) {
            numFrames = 1;
            return;
        }

        // Error check

        if (numFrames == -1) {
            throwError("Number of frames for animation not set");
        }

        if (basename.length() == 0) {
            throwError("Animation frame / image file basename not set");
        }

        // Get knob values

        for (OPCode opc : opcodes) {
            if (opc instanceof OPVary) {
                Double[] knobValues = knobs.get(((OPVary) opc).getKnob());

                // Create the knob values if not yet created
                if (knobValues == null) {
                    knobValues = new Double[numFrames];
                }

                int start = ((OPVary) opc).getStartframe();
                int end = ((OPVary) opc).getEndframe();

                double startVal = ((OPVary) opc).getStartval();
                double endVal = ((OPVary) opc).getEndval();

                // Throw error if start frame is not within bounds
                if (start < 0 || end >= numFrames) {
                    throwError("Frame start or end [" + ((OPVary) opc).getKnob() + "] is out of bounds");
                }

                for (int frame = start; frame <= end; frame++) {
                    knobValues[frame] = startVal;
                    // Add rate of change:
                    startVal += (endVal - startVal) / ((double) end - (double) start + 1);
                }

                knobs.put(((OPVary) opc).getKnob(), knobValues);
            }
        }
        PPMGenerator.setUpOutputDirectory(basename);
        int numberLength = Integer.toString(numFrames).length();
        formatString = basename + "/" + basename + "-%0" + numberLength + "d.ppm";
    }

    public double getKnobAtFrame(OPCode opc, int frame) throws ParseException {
        try {
            if (isAnimated && ((OPTrans) opc).getKnob() != null) {
                Double[] knobValues = knobs.get(((OPTrans) opc).getKnob());
                if (knobValues == null) {
                    throwError("Knob " + ((OPTrans) opc).getKnob() + " does not exist");
                }
                Double knobValue = knobValues[frame];
                if (knobValue == null) {
                    throwError("Knob " + ((OPTrans) opc).getKnob() + " does not have a value for frame " + frame);
                }
                return knobValue;
            }
        } catch (Exception e) {
            // This is purely to catch the OPTrans cast in the 'if' statement.
        }
        return 1.0;
    }

    public void lightingPass() throws ParseException {
        for (OPCode opc : opcodes) {
            if (opc instanceof OPLight) {
                OPLight opl = (OPLight) opc;
                if (lights.containsKey(opl.getName())) {
                    throwError("Cannot define light <" + opl.getName() + "> multiple times");
                }
                lights.put(opl.getName(), new PointSource(opl.getRgb(), opl.getLocation()));
            } else if (opc instanceof OPConstants) {
                OPConstants opconst = (OPConstants) opc;
                if (lightConstants.containsKey(opconst.getName())) {
                    printWarning("Redefined constant <" + opconst.getName() + ">!");
                }
                double[][] constants = new double[3][3];
                constants[0] = opconst.getAmbient();
                constants[1] = opconst.getDiffuse();
                constants[2] = opconst.getSpecular();
                lightConstants.put(opconst.getName(), new Matrix(constants));
            } else if (opc instanceof OPAmbient) {
                OPAmbient opa = (OPAmbient) opc;
                if (ambient != null) {
                    printWarning("Redfined ambient light!");
                    double[] rgb = opa.getRgb();
                    ambient = new AmbientSource(new Pixel(
                            (int) rgb[0],
                            (int) rgb[1],
                            (int) rgb[2]
                    ));
                }
            }
        }
    }

    public void process() throws ParseException, IOException {
        process(false);
    }

    public void process(boolean debug) throws ParseException, IOException {
        animationPass();
        lightingPass();

        for (int frame = 0; frame < numFrames; frame++) {

            System.out.printf("Rendering frame %d\n", frame);

            double timeStart = 0;
            ArrayList<Matrix> constants = new ArrayList<Matrix>();

            for (OPCode opc : opcodes) {

                if (debug) {
                    System.out.println("============== BEGIN OPERATION =============");
                    System.out.println(opc.getClass());
                    timeStart = System.currentTimeMillis();
                }

                // Get the knobValue. This will make it 1.0 if knobValue is
                // inapplicable for the current operation
                double knobValue = getKnobAtFrame(opc, frame);

                if (opc instanceof OPPush) {
                    origins.push();
                } else if (opc instanceof OPPop) {
                    origins.pop();
                } else if (opc instanceof OPMove) {
                    double[] values = ((OPMove) opc).getValues();
                    double dx = values[0] * knobValue;
                    double dy = values[1] * knobValue;
                    double dz = values[2] * knobValue;

                    origins.addTranslate(dx, dy, dz);
                } else if (opc instanceof OPScale) {
                    double[] values = ((OPScale) opc).getValues();
                    double xFac = values[0] * knobValue;
                    double yFac = values[1] * knobValue;
                    double zFac = values[2] * knobValue;

                    origins.addScale(xFac, yFac, zFac);
                } else if (opc instanceof OPRotate) {
                    char axis = ((OPRotate) opc).getAxis();
                    double degrees = ((OPRotate) opc).getDeg() * knobValue;
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
                    if (opb.getConstants() == null) {
                        constants.add(null);
                    } else {
                        constants.add(lightConstants.get(opb.getConstants()));
                    }
                } else if (opc instanceof OPSphere) {
                    OPSphere ops = (OPSphere) opc;
                    double[] center = ops.getCenter();
                    double cx = center[0];
                    double cy = center[1];
                    double cz = center[2];

                    double radius = ops.getRadius();

                    pm.addSphere(cx, cy, cz, radius);
                    if (ops.getConstants() == null) {
                        constants.add(null);
                    } else {
                        constants.add(lightConstants.get(ops.getConstants()));
                    }
                } else if (opc instanceof OPTorus) {
                    OPTorus opt = (OPTorus) opc;
                    double[] center = opt.getCenter();
                    double cx = center[0];
                    double cy = center[1];
                    double cz = center[2];

                    double outerRadius = opt.getOuterRadius();
                    double crossSectionRadius = opt.getCrossSectionRadius();

                    pm.addTorus(cx, cy, cz, outerRadius, crossSectionRadius);
                    if (opt.getConstants() == null) {
                        constants.add(null);
                    } else {
                        constants.add(lightConstants.get(opt.getConstants()));
                    }
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
                    if (frame == numFrames - 1) { // Only run display upon the
                                                  // last frame
                        try {
                            String command;
                            if (isAnimated) {
                                String filename = String.format(formatString, frame);
                                save(filename);
                                command = "animate " + basename + "/*.ppm";
                            } else {
                                save(CONSTANTS.TMP_FILE_NAME);
                                command = "display " + CONSTANTS.TMP_FILE_NAME;
                            }
                            Runtime.getRuntime().exec(command).waitFor();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Apply
                if (debug) {
                    System.out.println("APPLYING TRANSFORMATIONS USING " + origins.peek().toString());
                }

                Transformations.applyTransformation(origins.peek(), em);
                Transformations.applyTransformation(origins.peek(), pm);
                // Draw
                if (ambient == null) {
                    Draw.polygonMatrix(canvas, new Pixel(255, 20, 255), pm);
                } else {
                    Draw.polygonMatrix(canvas, pm, ambient, constants, lights.values());
                }
                Draw.edgeMatrix(canvas, new Pixel(255, 0, 0), em);
                // Empty
                em.empty();
                pm.empty();

                if (debug) {
                    System.out.println("=============== END OPERATION ==============");
                    System.out.printf("Rendered frame in %f ms\n", System.currentTimeMillis() - timeStart);
                    System.out.println("\n\n");
                }
            }

            if (isAnimated) {
                String filename = String.format(formatString, frame);
                save(filename);

                canvas.resetCanvas();
                em.empty();
                pm.empty();
                origins.reset();
            }
        }
        System.out.println("FINISHED PROCESSING");
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
