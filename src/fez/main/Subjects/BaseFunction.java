package fez.main.Subjects;

import java.util.ArrayList;
import fez.main.Exceptions.RuntimeException;
import fez.main.Objects.Context;
import fez.main.Objects.ResultObjects.InterpreterResult;

public abstract class BaseFunction extends Subject {
    
    protected final java.lang.String name;

    public BaseFunction(java.lang.String name) {
        this.name = name;
    }

    protected Context generateNewContext() {
        Context newContext = new Context(name, context, context.entryPosition());
        newContext.setVariablesTable(newContext.parent().variablesTable());
        return newContext;
    }

    private InterpreterResult checkArguments(ArrayList<java.lang.String> parameters, ArrayList<Subject> arguments, Context executionContext) {
        // exception for incorrect amount of arguments
        if (arguments.size() != parameters.size()) return new InterpreterResult(
                new RuntimeException(
                        executionContext,
                        position,
                        java.lang.String.format("\"%s\" requires arguments: %s", name, parameters)
                )
        );

        return new InterpreterResult();
    }

    private void populateArguments(ArrayList<java.lang.String> parameters, ArrayList<Subject> arguments, Context executionContext) {
        // Add arguments to the variables table
        for (int i = 0; i < arguments.size(); i++) {
            java.lang.String parameterName = parameters.get(i);
            Subject argumentValue = arguments.get(i);

            argumentValue.setContext(executionContext);
            executionContext.variablesTable().set(parameterName, argumentValue);
        }
    }

    protected InterpreterResult checkAndPopulateArguments(ArrayList<java.lang.String> parameters, ArrayList<Subject> arguments, Context executionContext) {
        InterpreterResult checkArgumentsResult = checkArguments(parameters, arguments, executionContext);
        if (checkArgumentsResult.exception() != null) return checkArgumentsResult;

        populateArguments(parameters, arguments, executionContext);
        return new InterpreterResult();
    }

    public abstract InterpreterResult execute(ArrayList<Subject> arguments);

    public abstract BaseFunction copy();

}
