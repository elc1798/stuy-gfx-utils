package org.stuygfx.wavefront;

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

    private Scanner fin;

    public Reader(String filename) {
        fin = new Scanner(filename);
    }

    public void reset() {
        fin.reset();
    }

    public PolygonMatrix getFaces() {
        // A line in the form:
        // v <float> <float> <float>
        // specifies a vertex
        reset();
        String currLine = "";
        ArrayList<Point> vertices = new ArrayList<Point>();
        
        PolygonMatrix pm = new PolygonMatrix();
        while (fin.hasNextLine() && !currLine.startsWith("s ")) {
            currLine = fin.nextLine();
            if (currLine.startsWith("v ")) {
                String[] line = currLine.split(" ");
                vertices.add(new Point(
                        Double.parseDouble(line[1]) * 100,
                        Double.parseDouble(line[2]) * 100,
                        Double.parseDouble(line[3]) * 100
                ));
            }
        }
        // A line 's off' separates vertices from faces
        while(fin.hasNextLine()) {
            currLine = fin.nextLine();
            if (currLine.startsWith("f ")) {
                String[] line = currLine.split(" ");
                pm.addTriangle(
                    vertices.get(Integer.parseInt(line[1]) + 1),
                    vertices.get(Integer.parseInt(line[2]) + 1),
                    vertices.get(Integer.parseInt(line[3]) + 1)
                );
            }
        }
        return pm;
    }
}
