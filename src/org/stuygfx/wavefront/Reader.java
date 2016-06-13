package org.stuygfx.wavefront;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.stuygfx.graphics.Point;
import org.stuygfx.graphics.PolygonMatrix;

/**
 * A Wavefront .obj file reader designed to parse .obj files exported from Blender
 * into a PolygonMatrix object.
 *
 * The file must have been exported from Blender with the options:
 * <br>
 * - Triangulate Faces
 * <br>
 * - No vertex normals
 * <br>
 * - No surface normals
 * <br>
 * - Vertices and faces only
 * <br>
 * - No materials
 */
public class Reader {

    private String filename;

    public Reader(String filename) {
        this.filename = filename;
    }

    public PolygonMatrix getFaces(int scale) {
        // A line in the form:
        // v <float> <float> <float>
        // specifies a vertex
        Scanner fin = null;
        try {
            fin = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String currLine = "";
        ArrayList<Point> vertices = new ArrayList<Point>();

        PolygonMatrix pm = new PolygonMatrix();
        while (fin.hasNextLine()) {
            currLine = fin.nextLine();
            // System.out.println(currLine);
            if (currLine.startsWith("v ")) {
                String[] line = currLine.split(" ");
                vertices.add(new Point(
                        Double.parseDouble(line[1]) * scale,
                        Double.parseDouble(line[2]) * scale,
                        Double.parseDouble(line[3]) * scale
                ));
            } else if (currLine.startsWith("s ")) {
                break;
            }
        }
        System.out.println("READ " + vertices.size() + " VERTICES FROM FILE");
        // A line 's off' separates vertices from faces
        while (fin.hasNextLine()) {
            currLine = fin.nextLine();
            if (currLine.startsWith("f ")) {
                String[] line = currLine.split(" ");
                pm.addTriangle(
                    vertices.get(Integer.parseInt(line[1]) - 1),
                    vertices.get(Integer.parseInt(line[2]) - 1),
                    vertices.get(Integer.parseInt(line[3]) - 1)
                );
            }
        }
        fin.close();
        System.out.println("READ " + pm.polygons.size() + " FACES FROM FILE");
        return pm;
    }
}
