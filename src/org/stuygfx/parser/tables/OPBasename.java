package org.stuygfx.parser.tables;

public class OPBasename extends OPCode {

    private String name;

    public OPBasename(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Basename: " + name;
    }

}
