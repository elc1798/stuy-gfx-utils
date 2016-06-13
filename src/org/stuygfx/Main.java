package org.stuygfx;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.stuygfx.graphics.PolygonMatrix;
import org.stuygfx.parser.MDLParser;
import org.stuygfx.parser.ParseException;
import org.stuygfx.parser.tables.OPCode;
import org.stuygfx.parser.tables.SymbolTable;
import org.stuygfx.wavefront.Reader;

public class Main {

    private static Interpreter interpreter;
    private static Scanner sc;
    private static String currCommand;
    private static Object[] currArgs;

    public static void interpreter(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("StuyGFX exitting...");
                System.gc();
            }
        });

        interpreter = new Interpreter();

        if (args.length == 0) {
            sc = new Scanner(System.in);
        } else {
            sc = new Scanner(new File(args[0]));
        }

        while (sc.hasNextLine()) {
            currCommand = sc.nextLine().trim();
            System.out.println(">>> " + currCommand);
            if (!interpreter.contains(currCommand)) {
                System.err.println("Unrecognized command");
                continue;
            }
            if (interpreter.hasNoParams(currCommand)) {
                currArgs = CONSTANTS.NO_ARGS;
            } else {
                currArgs = interpreter.getParams(currCommand, sc.nextLine().trim());
            }
            if (currArgs == null) {
                continue;
            }
            System.out.printf(">> Calling %s with %d parameters\n", currCommand, currArgs.length);
            interpreter.call(currCommand, currArgs);
            System.out.println("\n\n");
        }

        System.gc();
    }

    public static void main(String[] args) throws IOException, ParseException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("StuyGFX exitting...");
                System.gc();
            }
        });

        ArrayList<OPCode> opcodes;
        SymbolTable symTab;
        MDLParser parser;
        PolygonMatrix fromWaveFront = null;

        String filename;
        if (args.length == 3) {
            filename = args[0];
            Reader wavefrontFile = new Reader(args[1]);
            fromWaveFront = wavefrontFile.getFaces(Integer.parseInt(args[2]));
        } else if (args.length == 2) {
            filename = args[0];
            Reader wavefrontFile = new Reader(args[1]);
            fromWaveFront = wavefrontFile.getFaces(100);
        } else if (args.length == 1) {
            filename = args[0];
        } else {
            filename = "test.mdl";
        }

        try {
            parser = new MDLParser(new FileReader(filename));
        } catch (IOException e) {
            parser = new MDLParser(System.in);
        }

        parser.start();
        opcodes = parser.getOps();
        symTab = parser.getSymTab();

        MDLReader mdlr;
        if (fromWaveFront == null) {
            mdlr = new MDLReader(opcodes, symTab);
        } else {
            mdlr = new MDLReader(opcodes, symTab, fromWaveFront);
        }
        mdlr.process();
    }
}
