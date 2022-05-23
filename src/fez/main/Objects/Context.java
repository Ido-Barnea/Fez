package fez.main.Objects;

import fez.main.VariablesTable;

public class Context {

    private final String displayName;
    private Context parent;
    private Position entryPosition;
    private VariablesTable variablesTable;

    public Context(String displayName) {
        this.displayName = displayName;
        parent = null;
        entryPosition = null;
        variablesTable= null;
    }

    public Context(String displayName, Context parent, Position entryPosition) {
        this.displayName = displayName;
        this.parent = parent;
        this.entryPosition = entryPosition;
        variablesTable= null;
    }

    public String displayName() {
        return displayName;
    }

    public Context parent() {
        return parent;
    }

    public void setParent(Context context) {
        parent = context;
    }

    public Position entryPosition() {
        return entryPosition;
    }

    public void setEntryPosition(Position position) {
        entryPosition = position;
    }

    public VariablesTable variablesTable() {
        return variablesTable;
    }

    public void setVariablesTable(VariablesTable variablesTable) {
        this.variablesTable = variablesTable;
    }

    public Context copy() {
        Context copy = new Context(displayName, parent, entryPosition);
        copy.setVariablesTable(variablesTable.copy());
        return copy;
    }
    
}
