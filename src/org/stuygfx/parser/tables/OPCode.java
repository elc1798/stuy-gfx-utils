package org.stuygfx.parser.tables;

public class OPCode {

    public OPCode() {
        // TODO Auto-generated constructor stub
    }

    protected String triple(double[] s) {
        if (s == null)
            return "";
        else
            return "" + s[0] + "," + s[1] + "," + s[2];
    }

    public String toString() {
        return "GENERIC OPCODE";
    }
}
