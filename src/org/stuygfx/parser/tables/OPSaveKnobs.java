package org.stuygfx.parser.tables;

public class OPSaveKnobs extends OPCode {

    private String name;

    public OPSaveKnobs(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Saveknobs: " + name;
    }

}
