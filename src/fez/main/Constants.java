package fez.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fez.main.Subjects.Boolean;
import fez.main.Subjects.BuiltInFunction;
import fez.main.Subjects.Subject;

public final class Constants {
    public static final String DIGITS = "0123456789";
    public static final String LETTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static final String POSSIBLE_IDENTIFIER_CHARACTERS = LETTERS + DIGITS + "_";
    public static final List<String> KEYWORDS = Arrays.asList("var", "if", "else", "for", "in", ":", "while", "fun", "return");

    public static final Map<String, Subject> defaultVariables = new HashMap<>();
    static {
        defaultVariables.put("null", null);
        defaultVariables.put("true", new Boolean(1));
        defaultVariables.put("false", new Boolean(0));
        defaultVariables.put("print", new BuiltInFunction("print"));
        defaultVariables.put("println", new BuiltInFunction("println"));
        defaultVariables.put("input", new BuiltInFunction("input"));
    }
    public static final Map<String, Subject> variables = new HashMap<>(defaultVariables);

    public static final Map<Character, Character> escapeCharacters = Map.of(
            '"', '\"',
            'n', '\n',
            't', '\t'
        );
}
