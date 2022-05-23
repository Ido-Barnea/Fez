package fez.main.Subjects;

import fez.main.Exceptions.MathematicalOperationException;
import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class Boolean extends Int {
    private final Int value;

    private static final Int TRUE = new Int(1);
    private static final Int FALSE = new Int(0);

    public Boolean(Int value) {
        super(value == TRUE ? 1 : 0);
        this.value = value == TRUE ? TRUE : FALSE;
    }

    public Boolean(int value) {
        super(value == 1 ? 1 : 0);
        this.value = (value == 1 ? TRUE : FALSE);
    }

    public Boolean(boolean value) {
        super(value ? 1 : 0);
        this.value = (value ? TRUE : FALSE);
    }

    public boolean isTrue() {
        return value.intValue() == 1;
    }

    @Override
    public Boolean equals(Subject other) {
        if (other instanceof Boolean) return new Boolean(isTrue() == ((Boolean) other).isTrue());
        return new Boolean(false);
    }

    @Override
    public InterpreterResult and(Subject other, Position position) {
        return new InterpreterResult(new Boolean(isTrue() && other.intValue() == 1));
    }

    @Override
    public InterpreterResult or(Subject other, Position position) {
        return new InterpreterResult(new Boolean(isTrue() || other.intValue() == 1));
    }

    @Override
    public InterpreterResult not(Position position) {
        return new InterpreterResult(isTrue() ? new Boolean(FALSE) : new Boolean(TRUE));
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

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public InterpreterResult add(Subject other, Position position) {
        return new InterpreterResult(new Int(value.intValue() + other.intValue()));
    }

    @Override
    public InterpreterResult subtract(Subject other, Position position) {
        return new InterpreterResult(new Int(value.intValue() - other.intValue()));
    }

    @Override
    public InterpreterResult multiply(Subject other, Position position) {
        return new InterpreterResult(new Int(value.intValue() * other.intValue()));
    }

    @Override
    public InterpreterResult divide(Subject other, Position position) {
        if (other.intValue() == 0) return new InterpreterResult(new MathematicalOperationException(context, position, "Division by 0 is undefined"));
        return new InterpreterResult(new Int(value.intValue() / other.intValue()));
    }

    @Override
    public InterpreterResult power(Subject other, Position position) {
        return new InterpreterResult(new Int((int) Math.pow(value.intValue(), other.intValue())));
    }

    @Override
    public InterpreterResult modulo(Subject other, Position position) {
        if (other.intValue() == 0) return new InterpreterResult(new MathematicalOperationException(context, position, "Division by 0 is undefined"));
        return new InterpreterResult(new Int(value.intValue() % other.intValue()));
    }

    @Override
    public java.lang.String toString() {
        return isTrue() ? "true" : "false";
    }
}
