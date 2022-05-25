package fez.main.Subjects;

import fez.main.Exceptions.RuntimeException;
import fez.main.Objects.Context;
import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class Subject {

    protected Context context;
    protected Position position;

    public Subject() {
        context = null;
        position = null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context context() {
        return context;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }

    public int intValue() {
        return 0;
    }

    public float floatValue() {
        return 0;
    }

    public Int equals(Subject other) {
        return new Int(0);
    }

    public InterpreterResult add(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult subtract(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult multiply(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult divide(Subject other, Position position) {
        return illegalOperation(position);
    }
    
    public InterpreterResult power(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult modulo(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult and(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult or(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult not(Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult greaterThan(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult lessThan(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult greaterOrEqualTo(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult lessOrEqualTo(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult retrieve(Subject other, Position position) {
        return illegalOperation(position);
    }

    public InterpreterResult illegalOperation(Position position) {
        return new InterpreterResult(new RuntimeException(context, position, "Illegal Operation"));
    }

}
