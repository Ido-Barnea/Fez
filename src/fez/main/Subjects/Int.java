package fez.main.Subjects;

import fez.main.Exceptions.MathematicalOperationException;
import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;
import static fez.main.Constants.TRUE;
import static fez.main.Constants.FALSE;

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
    public Int equals(Subject other) {
        if (other instanceof Number) return floatValue() == (other).floatValue() ? TRUE : FALSE;
        return FALSE;
    }

    @Override
    public InterpreterResult greaterThan(Subject other, Position position) {
        return new InterpreterResult((intValue() > other.floatValue()) ? TRUE : FALSE);
    }

    @Override
    public InterpreterResult lessThan(Subject other, Position position) {
        return new InterpreterResult((intValue() < other.floatValue()) ? TRUE : FALSE);
    }

    @Override
    public InterpreterResult greaterOrEqualTo(Subject other, Position position) {
        return new InterpreterResult((intValue() >= other.floatValue()) ? TRUE : FALSE);
    }

    @Override
    public InterpreterResult lessOrEqualTo(Subject other, Position position) {
        return new InterpreterResult((intValue() <= other.floatValue()) ? TRUE : FALSE);
    }

    // Boolean operations
    public boolean isTrue() {
        return value == 1;
    }

    @Override
    public InterpreterResult and(Subject other, Position position) {
        return new InterpreterResult((isTrue() && other.intValue() == 1) ? TRUE : FALSE);
    }

    @Override
    public InterpreterResult or(Subject other, Position position) {
        return new InterpreterResult((isTrue() || other.intValue() == 1) ? TRUE : FALSE);
    }

    @Override
    public InterpreterResult not(Position position) {
        return new InterpreterResult(isTrue() ? FALSE : TRUE);
    }

}
