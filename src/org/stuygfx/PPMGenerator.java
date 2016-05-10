package org.stuygfx;

import static org.stuygfx.CONSTANTS.ANSI_GREEN;
import static org.stuygfx.CONSTANTS.ANSI_RED;
import static org.stuygfx.CONSTANTS.ANSI_RESET;
import static org.stuygfx.CONSTANTS.ANSI_YELLOW;
import static org.stuygfx.CONSTANTS.MAX_COLOR_VALUE;
import static org.stuygfx.CONSTANTS.PPM_HEADER;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.stuygfx.graphics.Image;
import org.stuygfx.graphics.Pixel;

public class PPMGenerator {

    public static void createPPM(String filename, Image img) throws IOException {
        PrintWriter fout = new PrintWriter(new File(filename));
        fout.println(PPM_HEADER);
        fout.printf("%d %d %d\n", img.XRES, img.YRES, MAX_COLOR_VALUE);
        for (int y = 0; y < img.YRES; y++) {
            for (int x = 0; x < img.XRES; x++) {
                Pixel tmp = img.canvas[y][x];
                fout.printf("%d %d %d ", tmp.r, tmp.g, tmp.b);
            }
        }
        fout.close();
        fout = null;
    }

    public static void setUpOutputDirectory() throws IOException {
        setUpOutputDirectory("generated");
    }

    public static void setUpOutputDirectory(String basename) throws IOException {
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        System.out.printf("%sSETTING UP OUTPUT DIRECTORY IN [%s]%s\n", ANSI_GREEN, currentPath, ANSI_RESET);
        String osName = System.getProperty("os.name");
        String osVer = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        String dirSep = System.getProperty("file.separator");

        System.out.printf("%sUsing file separator '%s' for OS '%s %s %s'%s\n", ANSI_GREEN, dirSep, osName, osVer, osArch, ANSI_RESET);
        String outputDir = currentPath + dirSep + basename;
        File dirMaker = new File(outputDir);
        if (dirMaker.exists()) {
            if (dirMaker.isDirectory()) {
                System.out.printf("%sOutput directory '%s/' already exists!%s\n", ANSI_YELLOW, basename, ANSI_RESET);
                System.out.printf("%sAttempting to purge output directory... %s\n", ANSI_YELLOW, ANSI_RESET);
                File[] filesFound = dirMaker.listFiles();
                for (File found : filesFound) {
                    System.out.printf("  %sDiscovered [%s]%s", ANSI_YELLOW, found.getName(), ANSI_RESET);
                    if (found.isDirectory()) {
                        System.out.printf(" %s... CANNOT REMOVE - File is directory%s\n", ANSI_RED, ANSI_RESET);
                    } else {
                        if (!found.delete()) {
                            System.out.printf(" %s... CANNOT REMOVE - Deletion failed%s\n", ANSI_RED, ANSI_RESET);
                        } else {
                            System.out.printf(" %s... Removed!%s\n", ANSI_GREEN, ANSI_RESET);
                        }
                    }
                }
            } else {
                System.out.printf("%sOutput directory '%s' has conflicting file!%s\n", ANSI_RED, basename, ANSI_RESET);
                System.exit(-1);
            }
        } else {
            System.out.printf("%sCreating output directory...%s\n", ANSI_GREEN, ANSI_RESET);
            if (!dirMaker.mkdir()) {
                System.out.printf("CANNOT CREATE DIRECTORY!%s\n", ANSI_RED, ANSI_RESET);
                System.exit(-1);
            } else {
                System.out.printf("%sOutput directory '%s' created!%s\n", ANSI_GREEN, basename, ANSI_RESET);
            }
        }
    }
}
