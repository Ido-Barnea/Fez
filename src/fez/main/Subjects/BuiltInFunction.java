package fez.main.Subjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import fez.Shell;
import fez.main.Exceptions.InvalidSyntaxException;
import fez.main.Exceptions.RuntimeException;
import fez.main.Objects.Context;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class BuiltInFunction extends BaseFunction {

    private boolean needsContext;

    public BuiltInFunction(java.lang.String name) {
        super(name);
        needsContext = true;
    }

    @Override
    public InterpreterResult execute(ArrayList<Subject> arguments) {
        Context executionContext = generateNewContext();
        Method function = searchForFunctionByName(name);
        if (function == null) { // Show exception if no function is found
            return new InterpreterResult(
                    new RuntimeException(
                            executionContext,
                            position,
                            java.lang.String.format("Couldn't find function named \"%s\"", name)
                    )
            );
        }

        // Check if function parameters match function arguments and populate function arguments to the variables table
        InterpreterResult checkAndPopulateResult = checkAndPopulateArguments(getFunctionParametersByName(name), arguments, executionContext);
        if (checkAndPopulateResult.exception() != null) return checkAndPopulateResult;

        try {
            if (needsContext) return (InterpreterResult) function.invoke(this, executionContext);
            else return (InterpreterResult) function.invoke(this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return new InterpreterResult();
        }
    }

    private Method searchForFunctionByName(java.lang.String name) {
        try {
            if (needsContext) return this.getClass().getMethod(name, Context.class);
            else return this.getClass().getMethod(name);
        } catch (NoSuchMethodException e) {
            if (needsContext) {
                needsContext = false;
                return searchForFunctionByName(name);
            }
            return null;
        }
    }

    private ArrayList<java.lang.String> getFunctionParametersByName(java.lang.String name) {
        ArrayList<java.lang.String> parameters = new ArrayList<>();

        switch (name) {
            case "print", "println", "abs", "ceil", "floor" -> parameters.add("value");
            case "min", "max" -> parameters.addAll(Arrays.asList("first", "second"));
        }

        return parameters;
    }

    public BuiltInFunction copy() {
        BuiltInFunction copy = new BuiltInFunction(name);
        copy.setContext(context);
        copy.setPosition(position);
        return copy;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.format("<Built-in function \"%s\">", name);
    }


    /*  ----------------------------------------
        Built-in Functions
    ----------------------------------------  */

    public InterpreterResult print(Context executionContext) {
        System.out.print(executionContext.variablesTable().get("value"));
        executionContext.variablesTable().remove("value");
        return new InterpreterResult();
    }

    public InterpreterResult println(Context executionContext) {
        System.out.println(executionContext.variablesTable().get("value"));
        executionContext.variablesTable().remove("value");
        return new InterpreterResult();
    }

    public InterpreterResult input() {
        System.out.println();
        return new InterpreterResult(new String(Shell.scanner.nextLine()));
    }

    public InterpreterResult abs(Context executionContext) {
        Subject subject = executionContext.variablesTable().get("value");
        executionContext.variablesTable().remove("value");

        if (subject instanceof Int) return new InterpreterResult(new Int(Math.abs(subject.intValue())));
        else if (subject instanceof Float) return new InterpreterResult(new Float(Math.abs(subject.floatValue())));
        else return illegalOperation(position);
    }

    public InterpreterResult ceil(Context executionContext) {
        Subject subject = executionContext.variablesTable().get("value");
        executionContext.variablesTable().remove("value");

        if (subject instanceof Number) return new InterpreterResult(new Float((float) Math.ceil(subject.floatValue())));
        else return illegalOperation(position);
    }

    public InterpreterResult floor(Context executionContext) {
        Subject subject = executionContext.variablesTable().get("value");
        executionContext.variablesTable().remove("value");

        if (subject instanceof Number) return new InterpreterResult(new Float((float) Math.floor(subject.floatValue())));
        else return illegalOperation(position);
    }

    public InterpreterResult min(Context executionContext) {
        Subject first = executionContext.variablesTable().get("first");
        Subject second = executionContext.variablesTable().get("second");
        executionContext.variablesTable().remove("first");
        executionContext.variablesTable().remove("second");

        // Make sure both values are numbers
        if (!(first instanceof Number && second instanceof Number)) return illegalOperation(position);

        float value = Math.min(first.floatValue(), second.floatValue());
        if (value == Math.floor(value)) return new InterpreterResult(new Int((int) value));
        else return new InterpreterResult(new Float(value));
    }

    public InterpreterResult max(Context executionContext) {
        Subject first = executionContext.variablesTable().get("first");
        Subject second = executionContext.variablesTable().get("second");
        executionContext.variablesTable().remove("first");
        executionContext.variablesTable().remove("second");

        // Make sure both values are numbers
        if (!(first instanceof Number && second instanceof Number)) return illegalOperation(position);

        float value = Math.max(first.floatValue(), second.floatValue());
        if (value == Math.floor(value)) return new InterpreterResult(new Int((int) value));
        else return new InterpreterResult(new Float(value));
    }
}
