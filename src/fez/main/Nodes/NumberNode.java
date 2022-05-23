package fez.main.Nodes;

import fez.main.Objects.Position;
import fez.main.Subjects.Float;
import fez.main.Subjects.Int;
import fez.main.Subjects.Number;

public class NumberNode extends Node {

    private final Number number;

    public NumberNode(Number number, Position position) {
        super(position);
        this.number = number;
    }

    public Number value() {
        return number;
    }

    @Override
    public String toString() {
        if (number instanceof Int) return Integer.toString(number.intValue());
        else if (number instanceof Float) return java.lang.Float.toString(number.floatValue());
        else return number.toString();
    }
}
