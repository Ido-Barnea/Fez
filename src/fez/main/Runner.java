package fez.main;

import static fez.main.Constants.*;
import java.util.HashMap;
import fez.main.Objects.Context;
import fez.main.Objects.ResultObjects.InterpreterResult;
import fez.main.Objects.ResultObjects.LexerResult;
import fez.main.Objects.ResultObjects.ParserResult;

public class Runner {

    public InterpreterResult run(String fileName, String text, Boolean reset) {
        Context context = new Context(fileName);
        if (reset) context.setVariablesTable(new VariablesTable(new HashMap<>(defaultVariables)));
        else context.setVariablesTable(new VariablesTable(variables));
        
        // generate tokens
        Lexer lexer = new Lexer(fileName, text);
        LexerResult lexerResult = lexer.createTokens();
        if (lexerResult.exception() != null) return new InterpreterResult(lexerResult.exception());

        // Generate syntax tree
        Parser parser = new Parser(lexerResult.tokens());
        ParserResult parserResult = parser.parse();
        if (parserResult.exception() != null) return new InterpreterResult(parserResult.exception());

        // Run program
        Interpreter interpreter = new Interpreter();
        return interpreter.visit(parserResult.syntaxTree(), context);
    }
}
