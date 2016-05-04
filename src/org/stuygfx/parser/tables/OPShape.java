package org.stuygfx.parser.tables;

public class OPShape extends OPCode {

    protected String cs;
    protected String constants;

    @Override
    public String toString() {
        return "GENERIC OPSHAPE";
    }

    public String getCs() {
        return cs;
    }

    public String getConstants() {
        return constants;
    }

}
