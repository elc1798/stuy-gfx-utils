package org.stuygfx.parser.tables;

public class OPMesh extends OPShape {

    private String filename;

    public OPMesh(String filename, String cs, String constants) {
        this.filename = filename;
        this.cs = cs;
        this.constants = constants;
    }

    public String toString() {
        return "Mesh: " + filename + "  cs - " + cs + " Contsants - " + constants;
    }

    public String getFilename() {
        return filename;
    }

}
