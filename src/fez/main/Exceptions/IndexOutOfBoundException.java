package fez.main.Exceptions;

import fez.main.Objects.Context;
import fez.main.Objects.Position;

public class IndexOutOfBoundException extends RuntimeException {
    public IndexOutOfBoundException(Context context, Position position, String message) {
        super(context, position, "Index out of bounds", message);
    }
}
