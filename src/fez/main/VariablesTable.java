package fez.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import fez.main.Objects.ModifierType;
import fez.main.Subjects.Subject;
import java.util.AbstractMap.SimpleEntry;

public class VariablesTable {

    private final Map<String, Entry<Subject, ArrayList<ModifierType>>> variablesMap;
    private final VariablesTable parent;

    public VariablesTable(Map<String, Entry<Subject, ArrayList<ModifierType>>> globalVariablesMap) {
        variablesMap = globalVariablesMap;
        parent = null;
    }

    public Map<String, Entry<Subject, ArrayList<ModifierType>>> variablesMap() {
        return variablesMap;
    }

    public Subject getValue(String name) {
        Subject value = variablesMap.get(name).getKey();
        if (value == null && parent != null) return parent.getValue(name);
        return value;
    }

    public ArrayList<ModifierType> getModifiers(String name) {
        ArrayList<ModifierType> value = variablesMap.get(name).getValue();
        if (value == null && parent != null) return parent.getModifiers(name);
        return value;
    }

    public void set(String name, Subject value) {
        variablesMap.put(name, new SimpleEntry<>(value, new ArrayList<>()));
    }

    public void set(String name, Entry<Subject, ArrayList<ModifierType>> entry) {
        variablesMap.put(name, entry);
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
