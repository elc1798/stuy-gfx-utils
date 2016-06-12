package org.stuygfx.graphics;

import static org.stuygfx.CONSTANTS.DEFAULT_VIEW_VEC;

import java.util.ArrayList;

import org.stuygfx.graphics.Lighting.AmbientSource;
import org.stuygfx.graphics.Lighting.Flat;
import org.stuygfx.graphics.Lighting.PointSource;
import org.stuygfx.math.Matrix;

public class Draw {

    private static Point convert_to_octant_1(Point p, int octant) {
        Point retval = new Point(p.x, p.y, p.z);
        switch (octant) {
            case 1:
                retval.x = p.x;
                retval.y = p.y;
                break;
            case 2:
                retval.x = p.y;
                retval.y = p.x;
                break;
            case 3:
                retval.x = p.y;
                retval.y = -p.x;
                break;
            case 4:
                retval.x = -p.x;
                retval.y = p.y;
                break;
            case 5:
                retval.x = -p.x;
                retval.y = -p.y;
                break;
            case 6:
                retval.x = -p.y;
                retval.y = -p.x;
                break;
            case 7:
                retval.x = -p.y;
                retval.y = p.x;
                break;
            case 8:
                retval.x = p.x;
                retval.y = -p.y;
                break;
            default:
                System.err.printf("Unknown octant provided : %d\n", octant);
                System.exit(-1);
        }
        return retval;
    }

    private static Point convert_from_octant_1(Point p, int octant) {
        Point retval = new Point(p.x, p.y, p.z);
        switch (octant) {
            case 1:
                retval.x = p.x;
                retval.y = p.y;
                break;
            case 2:
                retval.x = p.y;
                retval.y = p.x;
                break;
            case 3:
                retval.x = -p.y;
                retval.y = p.x;
                break;
            case 4:
                retval.x = -p.x;
                retval.y = p.y;
                break;
            case 5:
                retval.x = -p.x;
                retval.y = -p.y;
                break;
            case 6:
                retval.x = -p.y;
                retval.y = -p.x;
                break;
            case 7:
                retval.x = p.y;
                retval.y = -p.x;
                break;
            case 8:
                retval.x = p.x;
                retval.y = -p.y;
                break;
            default:
                System.err.printf("Unknown octant provided : %d\n", octant);
                System.exit(-1);
        }
        return retval;
    }

    private static int get_octant(Point start, Point end) {
        int rise = (int) (end.y - start.y);
        int run = (int) (end.x - start.x);
        boolean mirror = false;
        int octant = 0;
        if (run < 0) {
            rise *= -1;
            run *= -1;
            mirror = true;
        }
        if (rise >= 0) {
            octant = (rise > run) ? 2 : 1;
        } else {
            octant = (-rise > run) ? 3 : 4;
        }
        if (mirror) {
            octant = ((octant - 1 + 4) % 8) + 1;
        }
        return octant;
    }

    // Implementation of Bresenham's line algorithm
    public static void line(Image pic, Pixel color, Point p1, Point p2) {
        int octant = get_octant(p1, p2);

        // We can "shift" p1 to (0, 0), and plot with an offset
        Point p1_converted = convert_to_octant_1(new Point(p1.x, p1.y, p1.z), octant);
        Point p2_converted = convert_to_octant_1(new Point(p2.x, p2.y, p2.z), octant);
        // Always plot from left to right (lower x to higher x) so we can use
        // the
        // same loop guard for all cases
        if (p2_converted.x < p1_converted.x) {
            Point tmp = p1_converted;
            p1_converted = p2_converted;
            p2_converted = tmp;
        }
        int x = (int) p1_converted.x;
        int y = (int) p1_converted.y;
        double z = (double) p1_converted.z;

        int A = (int) (p2_converted.y - p1_converted.y);
        int B = (int) (-(p2_converted.x - p1_converted.x));
        int d = 2 * A + B;

        double dz = ((double) (p2_converted.z - p1_converted.z)) / ((double) (p2_converted.y - p1_converted.y));

        while (x <= p2_converted.x) {
            // Convert the point to its original octant, and plot with p1 as the
            // origin
            pic.plot(convert_from_octant_1(new Point(x, y, (int) z), octant), color);
            if (d > 0) {
                y += 1;
                d += 2 * B;
            }
            x += 1;
            d += 2 * A;

            z += dz;
        }
    }

    public static void edgeMatrix(Image pic, Pixel color, EdgeMatrix em) {
        for (Edge e : em.edges) {
            line(pic, color, e.start, e.end);
        }
    }

    public static void polygonMatrix(Image pic, Pixel color, PolygonMatrix pm) {
        for (Triangle t : pm.polygons) {
            if (t.isVisible()) {
                line(pic, color, t.p1, t.p2);
                line(pic, color, t.p1, t.p3);
                line(pic, color, t.p2, t.p3);
            }
        }
    }

    public static void polygonMatrix(Image pic, PolygonMatrix pm, AmbientSource ambience, Matrix constants, ArrayList<PointSource> lights) {
        for (Triangle t : pm.polygons) {
            if (t.isVisible()) {
                Pixel color = Flat.applyShading(t, ambience, lights, constants.data[0], constants.data[1], constants.data[2], DEFAULT_VIEW_VEC);
                line(pic, color, t.p1, t.p2);
                line(pic, color, t.p1, t.p3);
                line(pic, color, t.p2, t.p3);
                doScanlineConversion(pic, t.p1, t.p2, t.p3, color);
            }
        }
    }

    private static void doScanlineConversion(Image pic, Point p0, Point p1, Point p2, Pixel color) {
        Point tmp;
        // Sort p0, p1, and p2 such that p0.y <= p1.y <= p2.y
        // This is literally a max-3 step bubble sort written out
        if (p0.compareTo(p1, 'y') > 0) {
            tmp = p0;
            p0 = p1;
            p1 = tmp;
        }
        if (p0.compareTo(p2, 'y') > 0) {
            tmp = p0;
            p0 = p2;
            p2 = tmp;
        }
        if (p1.compareTo(p2, 'y') > 0) {
            tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        double x0 = p0.x;
        double x1 = p0.x;
        double dx0 = ((double) p2.x - (double) p0.x) / (p2.y - p0.y);
        double dx1 = ((double) p1.x - (double) p0.x) / (p1.y - p0.y);

        int y = (int) p0.y;

        double z0 = p0.z;
        double z1 = p0.z;
        double dz0 = (p2.z - p0.z) / ((int) p2.y - (int) p0.y);
        double dz1 = (p1.z - p0.z) / ((int) p1.y - (int) p0.y);

        int midY = (int) p1.y;
        while (y < midY) {
            x0 += dx0;
            x1 += dx1;
            y++;
            z0 += dz0;
            z1 += dz1;
            line(pic, color, new Point((int) x0, (int) y, (int) z0), new Point((int) x1, (int) y, (int) z1));
        }

        x1 = p1.x; // Ensure that x1 is set to the x-coor of the middle point
        z1 = p1.z; // Ensure that z1 is set to the z-coor of the middle point
        dx1 = ((double) p2.x - (double) p1.x) / (p2.y - p1.y);

        int topY = (int) p2.y;
        while (y < topY) {
            x0 += dx0;
            x1 += dx1;
            y++;
            line(pic, color, new Point((int) x0, (int) y, (int) z0), new Point((int) x1, (int) y, (int) z1));
        }
    }
}
