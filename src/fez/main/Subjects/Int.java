package fez.main.Subjects;

import fez.main.Exceptions.MathematicalOperationException;
import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class Int extends Number {

    private final int value;

    public Int(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }
    
    @Override
    public java.lang.String toString() {
        return java.lang.String.valueOf(value);
    }

    @Override
    public InterpreterResult add(Subject other, Position position) {
        if (other instanceof Float) return new InterpreterResult(new Float(value + other.floatValue()));
        return new InterpreterResult(new Int(value + other.intValue()));
    }

    @Override
    public InterpreterResult subtract(Subject other, Position position) {
        if (other instanceof Float) return new InterpreterResult(new Float(value - other.floatValue()));
        return new InterpreterResult(new Int(value - other.intValue()));
    }

    @Override
    public InterpreterResult multiply(Subject other, Position position) {
        if (other instanceof Float) return new InterpreterResult(new Float(value * other.floatValue()));
        return new InterpreterResult(new Int(value * other.intValue()));
    }

    @Override
    public InterpreterResult divide(Subject other, Position position) {
        if (other.intValue() == 0) return new InterpreterResult(new MathematicalOperationException(context, position, "Division by 0 is undefined"));
        if (other instanceof Float) return new InterpreterResult(new Float(value / other.floatValue()));
        return new InterpreterResult(new Int(value / other.intValue()));
    }

    @Override
    public InterpreterResult power(Subject other, Position position) {
        if (other instanceof Float) return new InterpreterResult(new Float((float) Math.pow(value, other.floatValue())));
        return new InterpreterResult(new Int((int) Math.pow(value, other.intValue())));
    }

    @Override
    public InterpreterResult modulo(Subject other, Position position) {
        if (other.intValue() == 0) return new InterpreterResult(new MathematicalOperationException(context, position, "Division by 0 is undefined"));
        return new InterpreterResult(new Int(value % other.intValue()));
    }

    @Override
    public Boolean equals(Subject other) {
        if (other instanceof Number) return new Boolean(floatValue() == (other).floatValue());
        return new Boolean(false);
    }

    @Override
    public InterpreterResult greaterThan(Subject other, Position position) {
        return new InterpreterResult(new Boolean(intValue() > other.floatValue()));
    }

    @Override
    public InterpreterResult lessThan(Subject other, Position position) {
        return new InterpreterResult(new Boolean(intValue() < other.floatValue()));
    }

    @Override
    public InterpreterResult greaterOrEqualTo(Subject other, Position position) {
        return new InterpreterResult(new Boolean(intValue() >= other.floatValue()));
    }

    @Override
    public InterpreterResult lessOrEqualTo(Subject other, Position position) {
        return new InterpreterResult(new Boolean(intValue() <= other.floatValue()));
    }

}
