package org.stuygfx.graphics;

import java.util.LinkedList;

import org.stuygfx.math.MasterTransformationMatrix;
import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;
import org.stuygfx.math.Transformations;

public class TransformationStack {

    public LinkedList<MasterTransformationMatrix> transStack;

    public TransformationStack() {
        transStack = new LinkedList<MasterTransformationMatrix>();
        transStack.addFirst(new MasterTransformationMatrix());
    }

    public MasterTransformationMatrix pop() {
        System.out.println("Removing from stack: " + peek().toString());
        return transStack.removeFirst();
    }

    public MasterTransformationMatrix peek() {
        return transStack.getFirst();
    }

    public void reset() {
        transStack.clear();
        transStack.addFirst(new MasterTransformationMatrix());
    }

    public void push() {
        // System.out.println("Pushing to stack... original top: " + peek().toString());
        // peek().print();
        MasterTransformationMatrix originalTop = peek();
        transStack.push(peek().clone());
        // System.out.println("Pushed duplicate to stack: New top: " + peek().toString());
        // peek().print();
        assert (originalTop == transStack.get(1));
    }

    public void addTransformation(Matrix trans) {
        peek().set(MatrixMath.crossProduct(trans, peek()));
        // System.out.println(peek().toString());
        // peek().print();
    }

    public void addRotX(Double theta) {
        addTransformation(Transformations.getRotXMatrix(theta));
    }

    public void addRotY(Double theta) {
        addTransformation(Transformations.getRotYMatrix(theta));
    }

    public void addRotZ(Double theta) {
        addTransformation(Transformations.getRotXMatrix(theta));
    }

    public void addTranslate(Double dx, Double dy, Double dz) {
        addTransformation(Transformations.getTranslationMatrix(dx, dy, dz));
    }

    public void addScale(Double xFac, Double yFac, Double zFac) {
        addTransformation(Transformations.getScaleMatrix(xFac, yFac, zFac));
    }

    public void print() {
        System.out.println("+===============  TRANS STACK  ==============+");
        for (MasterTransformationMatrix mat : transStack) {
            mat.print();
        }
        System.out.println("+===============  ***********  ==============+");
    }
}
