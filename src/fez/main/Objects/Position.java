package fez.main.Objects;

public class Position {
    private int index;
    private int line;
    private int column;
    private final String fileName;
    private final String fileContent;

    public Position(int index, int line, int column, String fileName, String fileContent) {
        this.index = index;
        this.line = line;
        this.column = column;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public void advance(Character currentCharacter) {
        index++;
        column++;

        if (currentCharacter != null && currentCharacter == '\n') {
            line++;
            column = 0;
        }
    }

    public int index() {
        return index;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }

    public String fileName() {
        return fileName;
    }

    public String fileContent() {
        return fileContent;
    }

    public Position copy() {
        return new Position(index, line, column, fileName, fileContent);
    }

    @Override
    public String toString() {
        return String.format("%s [Index: %s, Line: %s, Column: %s]", fileName, index, line, column);
    }
}