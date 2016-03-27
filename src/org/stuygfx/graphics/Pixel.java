package org.stuygfx.graphics;

public class Pixel {

	public short a;
	public short r;
	public short g;
	public short b;

	public Pixel() {
		this.a = 0;
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}

	public Pixel(short r, short g, short b) {
		this.a = 0;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Pixel(short a, short r, short g, short b) {
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

}
