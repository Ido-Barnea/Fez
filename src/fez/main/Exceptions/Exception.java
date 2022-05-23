package fez.main.Exceptions;

import fez.main.Objects.Position;

public class Exception {
    
    protected String errorName;
    protected String errorMessage;
    protected Position position;

    public Exception(Position position, String errorName, String errorMessage) {
        this.position = position;
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    protected String stringWithArrowPointer(Position position) {
        String text = position.fileContent();
        String[] lines = text.split("\n");
        String line = lines[position.line()];
        String arrowBuilder = " ".repeat(Math.max(0, position.column())) + "/\\";
        line += "\n" + arrowBuilder;
        return line;
    }

    @Override
    public String toString() {
        String result =  String.format(
            "\n\n%s in %s[line %s]: %s", 
            errorName,
            position.fileName(),
            position.line(),
            errorMessage
        );

        result += String.format("\n\n %s\n", stringWithArrowPointer(position));

        return result;
    }
}
