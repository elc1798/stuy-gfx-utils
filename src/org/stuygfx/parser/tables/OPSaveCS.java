package org.stuygfx.parser.tables;

public class OPSaveCS extends OPCode {

    private String name;

    public OPSaveCS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "SaveCS: " + name;
    }

}
