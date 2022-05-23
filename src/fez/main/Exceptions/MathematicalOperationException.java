package fez.main.Exceptions;

import fez.main.Objects.Context;
import fez.main.Objects.Position;

public class MathematicalOperationException extends RuntimeException {
    public MathematicalOperationException(Context context, Position position, String message) {
        super(context, position, "Invalid Mathematical Operation", message);
    }
}