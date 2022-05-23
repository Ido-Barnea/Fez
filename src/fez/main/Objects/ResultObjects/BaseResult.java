package fez.main.Objects.ResultObjects;

import fez.main.Exceptions.Exception;

public class BaseResult {
    
    private final Exception exception;

    public BaseResult(Exception exception) {
        this.exception = exception;
    }

    public Exception exception() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }

}
