package org.stuygfx.parser.tables;

public class OPFrames extends OPCode {

    private int num;

    public OPFrames(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public String toString() {
        return "Frames: " + num;
    }

}
