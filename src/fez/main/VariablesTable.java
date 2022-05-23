package fez.main;

import java.util.HashMap;
import java.util.Map;
import fez.main.Subjects.Subject;

public class VariablesTable {

    private final Map<String, Subject> variablesMap;
    private final VariablesTable parent;

    public VariablesTable(Map<String, Subject> globalVariablesMap) {
        variablesMap = globalVariablesMap;
        parent = null;
    }

    public Map<String, Subject> variablesMap() {
        return variablesMap;
    }

    public Subject get(String name) {
        Subject value = variablesMap.get(name);
        if (value == null && parent != null) return parent.get(name);
        return value;
    }

    public void set(String name, Subject value) {
        variablesMap.put(name, value);
    }

    public void remove(String name) {
        variablesMap.remove(name);
    }

    public VariablesTable copy() {
        return new VariablesTable(new HashMap<>(variablesMap));
    }

    @Override
    public String toString() {
        return variablesMap.toString();
    }
}
