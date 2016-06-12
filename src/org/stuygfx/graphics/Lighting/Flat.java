package org.stuygfx.graphics.Lighting;

import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Triangle;
import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;

public class Flat {
    private static double[] ambient(double[] illuminations, double[] constants) {
        assert (illuminations.length == 3 && constants.length == 3);
        double[] ambience = new double[3];
        for (int i = 0; i < 3; i++) {
            ambience[i] = illuminations[i] * constants[i];
        }
        return ambience;
    }

    private static double[] diffuse(Triangle face, double[] illuminations, double[] constants, Matrix lightVector) {
        assert (illuminations.length == 3 && constants.length == 3);
        Matrix surfaceNormal = face.getSurfaceNormal();
        lightVector = MatrixMath.normalize(lightVector);
        double diffuseAmount = MatrixMath.dotProduct(lightVector, surfaceNormal);
        double[] diffuse = new double[3];
        for (int i = 0; i < 3; i++) {
            diffuse[i] = Math.max(illuminations[i] * constants[i] * diffuseAmount, 0.0);
        }
        return diffuse;
    }

    private static double[] specular(Triangle face, double[] illuminations, double[] constants, Matrix lightVector,
            Matrix viewVector) {
        assert (illuminations.length == 3 && constants.length == 3);
        Matrix surfaceNormal = MatrixMath.normalize(face.getSurfaceNormal());
        // Try to mimic a perfect reflector with n = infinity
        double n = Math.round(Math.random() * 2 + 1);
        lightVector = MatrixMath.normalize(lightVector);

        double dotprod = MatrixMath.dotProduct(lightVector, surfaceNormal);
        Matrix reflectVector = MatrixMath.subtract(MatrixMath.scalarMultiply(surfaceNormal, 2 * dotprod), lightVector);
        double specularAmount = Math.pow(MatrixMath.dotProduct(reflectVector, viewVector), n);

        double[] specular = new double[3];
        for (int i = 0; i < 3; i++) {
            specular[i] = Math.max(illuminations[i] * constants[i] * specularAmount, 0.0);
        }
        return specular;
    }

    /**
     * Perform a flat shading algorithm given required values
     * 
     * @param face
     *            - The triangular face to calculate the color for
     * @param I_a
     *            - Color values for ambient light
     * @param I_p
     *            - Color values for point light source
     * @param K_a
     *            - Constants for ambient light
     * @param K_d
     *            - Constants for diffuse light
     * @param K_s
     *            - Constants for specular light
     * @param lightVector
     *            - Vector for the point light source
     * @param viewVector
     *            - Vector for the camera
     * @return - A Pixel containing the color calculated by flat shading
     */
    public static Pixel applyShading(Triangle face, double[] I_a, double[] I_p, double[] K_a, double[] K_d, double[] K_s,
            Matrix lightVector, Matrix viewVector) {
        double[] ambient = ambient(I_a, K_a);
        double[] diffuse = diffuse(face, I_p, K_d, lightVector);
        double[] specular = specular(face, I_p, K_s, lightVector, viewVector);
        return new Pixel(
            (int) Math.min(ambient[0] + diffuse[0] + specular[0], 255.0),
            (int) Math.min(ambient[1] + diffuse[1] + specular[1], 255.0),
            (int) Math.min(ambient[2] + diffuse[2] + specular[2], 255.0)
        );
    }
}