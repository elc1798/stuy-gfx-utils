package org.stuygfx.parser.tables;

public class OPSave extends OPCode {

    private String name;

    public OPSave(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Save: " + name;
    }
}
