package fez.main.Exceptions;

import fez.main.Objects.Position;

public class IllegalCharacterException extends Exception {
    public IllegalCharacterException(Position position, String message) {
        super(position, "Illegal Character", message);
    }
}
