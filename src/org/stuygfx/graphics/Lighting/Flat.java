package org.stuygfx.graphics.Lighting;

import java.util.ArrayList;

import org.stuygfx.graphics.Pixel;
import org.stuygfx.graphics.Triangle;
import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;

public class Flat {
    private static double[] ambient(AmbientSource illumination, double[] constants) {
        assert (constants.length == 3);
        double[] ambience = illumination.color.toArrayOfDoubles();
        for (int i = 0; i < 3; i++) {
            ambience[i] *= constants[i];
        }
        return ambience;
    }

    private static double[] diffuse(Triangle face, PointSource light, double[] constants) {
        assert (constants.length == 3);
        Matrix surfaceNormal = face.getSurfaceNormal();
        Matrix lightVector = MatrixMath.normalize(light.lightVector);
        double diffuseAmount = MatrixMath.dotProduct(lightVector, surfaceNormal);
        double[] diffuse = light.color.toArrayOfDoubles();
        for (int i = 0; i < 3; i++) {
            diffuse[i] = Math.max(diffuse[i] * constants[i] * diffuseAmount, 0.0);
        }
        return diffuse;
    }

    private static double[] specular(Triangle face, PointSource light, double[] constants, Matrix viewVector) {
        assert (constants.length == 3);
        Matrix surfaceNormal = MatrixMath.normalize(face.getSurfaceNormal());
        // Try to mimic a perfect reflector with n = infinity
        double n = Math.round(Math.random() * 2 + 1);
        Matrix lightVector = MatrixMath.normalize(light.lightVector);

        double dotprod = MatrixMath.dotProduct(lightVector, surfaceNormal);
        Matrix reflectVector = MatrixMath.subtract(MatrixMath.scalarMultiply(surfaceNormal, 2 * dotprod), lightVector);
        double specularAmount = Math.pow(MatrixMath.dotProduct(reflectVector, viewVector), n);

        double[] specular = light.color.toArrayOfDoubles();
        for (int i = 0; i < 3; i++) {
            specular[i] = Math.max(specular[i] * constants[i] * specularAmount, 0.0);
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
     * @param lightVectors
     *            - Vector for the point light source
     * @param viewVector
     *            - Vector for the camera
     * @return - A Pixel containing the color calculated by flat shading
     */
    public static Pixel applyShading(Triangle face, AmbientSource ambientLight, ArrayList<PointSource> lights,
            double[] K_a, double[] K_d, double[] K_s, Matrix viewVector) {
        double[] ambient = ambient(ambientLight, K_a);
        double[] pointsource = new double[] { 0.0, 0.0, 0.0 };
        for (PointSource light : lights) {
            double[] diffuse = diffuse(face, light, K_d);
            double[] specular = specular(face, light, K_s, viewVector);
            for (int i = 0; i < 3; i++) {
                pointsource[i] += diffuse[i] + specular[i];
            }
        }
        return new Pixel((int) Math.min(ambient[0] + pointsource[0], 255.0),
                (int) Math.min(ambient[1] + pointsource[1], 255.0), (int) Math.min(ambient[2] + pointsource[2], 255.0));
    }
}