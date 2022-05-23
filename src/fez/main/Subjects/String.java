package fez.main.Subjects;

import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class String extends Subject {
    
    private final java.lang.String string;

    public String(java.lang.String string) {
        this.string = string;
    }

    @Override
    public InterpreterResult add(Subject other, Position position) {
        if (other instanceof String) return new InterpreterResult(new String(string + ((String) other).value()));
        return new InterpreterResult(new String(string + other.toString()));
    }

    public java.lang.String value() {
        return string;
    }

    @Override
    public Boolean equals(Subject other) {
        if (other instanceof String) return new Boolean(string.equals(((String) other).value()));
        return new Boolean(false);
    }
    
    @Override
    public java.lang.String toString() {
        return string;
    }

}
