package org.stuygfx.graphics;

public class Triangle {

    private Point p1;
    private Point p2;
    private Point p3;

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public boolean isFacingOutwards() {
        /*
         * A triangle is facing outwards if:
         *
         *     p2
         *     /\
         *    /  \
         * p3/____\ p1
         *
         * p1.x > p2.x
         * p2.x > p3.x
         *
         * However, we must assert this test with all different rotations of
         * the triangle
         */

        boolean p1_p2_p3 = p1.x >= p2.x && p2.x >= p3.x;
        boolean p2_p3_p1 = p2.x >= p3.x && p3.x >= p1.x;
        boolean p3_p1_p2 = p3.x >= p1.x && p1.x >= p2.x;
        return p1_p2_p3 || p2_p3_p1 || p3_p1_p2;
    }

}
