package fez.main.Exceptions;

import fez.main.Objects.Position;

public class InvalidSyntaxException extends Exception {
    public InvalidSyntaxException(Position position, String message) {
        super(position, "Invalid Syntax", message);
    }
}
