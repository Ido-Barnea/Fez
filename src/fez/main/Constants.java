package fez.main;

import java.util.*;

import fez.main.Objects.ModifierType;
import fez.main.Subjects.BuiltInFunction;
import fez.main.Subjects.Int;
import fez.main.Subjects.Subject;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public final class Constants {
    public static final String DIGITS = "0123456789";
    public static final String LETTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static final String POSSIBLE_IDENTIFIER_CHARACTERS = LETTERS + DIGITS + "_";
    public static final List<String> KEYWORDS = Arrays.asList("var", "const", "if", "else", "for", "in", "while", "fun", "return");

    public static final Int TRUE = new Int(1);
    public static final Int FALSE = new Int(0);

    public static final Map<String, Entry<Subject, ArrayList<ModifierType>>> defaultVariables = new HashMap<>();
    static {
        defaultVariables.put("null", null);
        defaultVariables.put("true", new SimpleEntry<>(TRUE, new ArrayList<>()));
        defaultVariables.put("false", new SimpleEntry<>(FALSE, new ArrayList<>()));
        defaultVariables.put("print", new SimpleEntry<>(new BuiltInFunction("print"), new ArrayList<>()));
        defaultVariables.put("println", new SimpleEntry<>(new BuiltInFunction("println"), new ArrayList<>()));
        defaultVariables.put("input", new SimpleEntry<>(new BuiltInFunction("input"), new ArrayList<>()));
        defaultVariables.put("abs", new SimpleEntry<>(new BuiltInFunction("abs"), new ArrayList<>()));
        defaultVariables.put("ceil", new SimpleEntry<>(new BuiltInFunction("ceil"), new ArrayList<>()));
        defaultVariables.put("floor", new SimpleEntry<>(new BuiltInFunction("floor"), new ArrayList<>()));
        defaultVariables.put("min", new SimpleEntry<>(new BuiltInFunction("min"), new ArrayList<>()));
        defaultVariables.put("max", new SimpleEntry<>(new BuiltInFunction("max"), new ArrayList<>()));
    }
    public static final Map<String, Entry<Subject, ArrayList<ModifierType>>> globalVariables = new HashMap<>(defaultVariables);

    public static final Map<Character, Character> escapeCharacters = Map.of(
            '"', '\"',
            'n', '\n',
            't', '\t'
        );
}
