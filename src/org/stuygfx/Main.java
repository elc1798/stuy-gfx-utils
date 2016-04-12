package org.stuygfx;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Interpreter interpreter;
    private static Scanner sc;
    private static String currCommand;
    private static Object[] currArgs;

    public static void main(String[] args) throws IOException {
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
        }

        System.gc();
    }

}
