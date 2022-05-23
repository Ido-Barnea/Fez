package fez.main.Subjects;

import fez.main.Exceptions.MathematicalOperationException;
import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class Float extends Number {
    
    private final float value;

    public Float(float value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.valueOf(value);
    }

    @Override
    public InterpreterResult add(Subject other, Position position) {
        return new InterpreterResult(new Float(value + other.floatValue()));
    }

    @Override
    public InterpreterResult subtract(Subject other, Position position) {
        return new InterpreterResult(new Float(value - other.floatValue()));
    }

    @Override
    public InterpreterResult multiply(Subject other, Position position) {
        return new InterpreterResult(new Float(value * other.floatValue()));
    }

    @Override
    public InterpreterResult divide(Subject other, Position position) {
        if (other.intValue() == 0) return new InterpreterResult(new MathematicalOperationException(context, position, "Division by 0 is undefined"));
        return new InterpreterResult(new Float(value / other.floatValue()));
    }

    @Override
    public InterpreterResult power(Subject other, Position position) {
        return new InterpreterResult(new Float((float) Math.pow(value, other.floatValue())));
    }

    @Override
    public InterpreterResult modulo(Subject other, Position position) {
        if (other.intValue() == 0) return new InterpreterResult(new MathematicalOperationException(context, position, "Division by 0 is undefined"));
        return new InterpreterResult(new Float(value % other.floatValue()));
    }

    @Override
    public Boolean equals(Subject other) {
        if (other instanceof Number) return new Boolean(floatValue() == (other).floatValue());
        return new Boolean(false);
    }

    @Override
    public InterpreterResult greaterThan(Subject other, Position position) {
        return new InterpreterResult(new Boolean(floatValue() > other.floatValue()));
    }

    @Override
    public InterpreterResult lessThan(Subject other, Position position) {
        return new InterpreterResult(new Boolean(floatValue() < other.floatValue()));
    }

    @Override
    public InterpreterResult greaterOrEqualTo(Subject other, Position position) {
        return new InterpreterResult(new Boolean(floatValue() >= other.floatValue()));
    }

    @Override
    public InterpreterResult lessOrEqualTo(Subject other, Position position) {
        return new InterpreterResult(new Boolean(floatValue() <= other.floatValue()));
    }
}
