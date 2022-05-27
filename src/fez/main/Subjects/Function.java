package fez.main.Subjects;

import java.util.ArrayList;

import fez.main.Exceptions.RuntimeException;
import fez.main.Interpreter;
import fez.main.Nodes.Node;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class Function extends BaseFunction {
    
    private final ArrayList<java.lang.String> parameters;
    private final Node body;

    public Function(java.lang.String name, ArrayList<java.lang.String> parameters, Node body) {
        super(name);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public InterpreterResult execute(ArrayList<Subject> arguments) {
        Interpreter interpreter = new Interpreter();

        // Check if function parameters match function arguments and populate function arguments to the variables table
        InterpreterResult checkAndPopulateResult = checkAndPopulateArguments(parameters, arguments, context);
        if (checkAndPopulateResult.exception() != null) return checkAndPopulateResult;

        InterpreterResult result;
        // Make sure there isn't a stackoverflow exception
        try {
            result = interpreter.visitFunction(body, context);
        } catch (StackOverflowError e) {
            result = new InterpreterResult(new RuntimeException(context, position, "StackOverflow"));
        }

        if (result.result() != null && result.result() instanceof List statements) return new InterpreterResult(statements.get(0));
        return result;
    }

    public Function copy() {
        Function copy = new Function(name, parameters, body);
        copy.setContext(context);
        copy.setPosition(position);
        return copy;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.format("<function \"%s\">", name);
    }

}
