package org.stuygfx.graphics;

public class Pixel {

    public int r;
    public int g;
    public int b;

    public Pixel() {
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void set(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public double[] toArrayOfDoubles() {
        return new double[] { (double) this.r, (double) this.g, (double) this.b };
    }

    public int[] toArrayOfInts() {
        return new int[] { this.r, this.g, this.b };
    }

    @Override
    public String toString() {
        return "[" + this.r + " , " + this.g + " , " + this.b + "]";
    }
}
