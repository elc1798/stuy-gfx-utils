package org.stuygfx.graphics;

public class Draw {

    private static Point convert_to_octant_1(Point p, int octant) {
        Point retval = new Point(p.x, p.y);
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
        Point retval = new Point(p.x, p.y);
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
        int rise = end.y - start.y;
        int run = end.x - start.x;
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
        Point p1_converted = convert_to_octant_1(new Point(p1.x, p1.y), octant);
        Point p2_converted = convert_to_octant_1(new Point(p2.x, p2.y), octant);
        // Always plot from left to right (lower x to higher x) so we can use
        // the
        // same loop guard for all cases
        if (p2_converted.x < p1_converted.x) {
            Point tmp = p1_converted;
            p1_converted = p2_converted;
            p2_converted = tmp;
        }
        int x = p1_converted.x;
        int y = p1_converted.y;
        int A = p2_converted.y - p1_converted.y;
        int B = -(p2_converted.x - p1_converted.x);
        int d = 2 * A + B;
        while (x <= p2_converted.x) {
            // Convert the point to its original octant, and plot with p1 as the
            // origin
            pic.plot(convert_from_octant_1(new Point(x, y), octant), color);
            if (d > 0) {
                y += 1;
                d += 2 * B;
            }
            x += 1;
            d += 2 * A;
        }
    }

    public static void edgeMatrix(Image pic, Pixel color, EdgeMatrix em) {
        for (Edge e : em.edges) {
            line(pic, color, e.start, e.end);
        }
    }

    public static void polygonMatrix(Image pic, Pixel color, PolygonMatrix pm) {
        for (Triangle t : pm.polygons) {
            line(pic, color, t.p1, t.p2);
            line(pic, color, t.p1, t.p3);
            line(pic, color, t.p2, t.p3);
        }
    }
}
