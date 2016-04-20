package org.stuygfx.graphics;

import java.util.Stack;

import org.stuygfx.math.MasterTransformationMatrix;

public class TransformationStack {

    public Stack<MasterTransformationMatrix> transStack;

    public TransformationStack() {
        transStack = new Stack<MasterTransformationMatrix>();
        transStack.push(new MasterTransformationMatrix());
    }

    public MasterTransformationMatrix pop() {
        return transStack.pop();
    }

    public MasterTransformationMatrix peek() {
        return transStack.peek();
    }

    public void push() {
        transStack.push(transStack.peek().clone());
    }
}
