package fez.main.Nodes;

import fez.main.Objects.Position;
import fez.main.Subjects.String;

public class StringNode extends Node {

    private final String string;

    public StringNode(java.lang.String string, Position position) {
        super(position);
        this.string = new String(string);
    }

    public String value() {
        return string;
    }

    @Override
    public java.lang.String toString() {
        return string.toString();
    }
    
}
