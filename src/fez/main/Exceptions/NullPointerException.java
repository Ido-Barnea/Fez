package fez.main.Exceptions;

import fez.main.Objects.Context;
import fez.main.Objects.Position;

public class NullPointerException extends RuntimeException {

    public NullPointerException(Context context, Position position, String message) {
        super(context, position, "Null Pointer Exception", message);
    }
    
}
