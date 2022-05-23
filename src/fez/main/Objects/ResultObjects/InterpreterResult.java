package fez.main.Objects.ResultObjects;

import fez.main.Exceptions.Exception;
import fez.main.Subjects.Subject;

public class InterpreterResult extends BaseResult {

    private final Subject result;

    public InterpreterResult(Subject result) {
        super(null);
        this.result = result;
    }

    public InterpreterResult(Exception exception) {
        super(exception);
        this.result = null;
    }

    public InterpreterResult() {
        super(null);
        this.result = null;
    }

    public Subject result() {
        return result;
    }

}
