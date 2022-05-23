package fez.main.Subjects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

import fez.Shell;
import fez.main.Exceptions.RuntimeException;
import fez.main.Objects.Context;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class BuiltInFunction extends BaseFunction {

    public BuiltInFunction(java.lang.String name) {
        super(name);
    }

    @Override
    public InterpreterResult execute(ArrayList<Subject> arguments) {
        Context executionContext = generateNewContext();
        Method function;
        try {
            function = this.getClass().getMethod(name, Context.class);
        } catch (NoSuchMethodException e) {
            // Show exception if no function is found
            return new InterpreterResult(new RuntimeException(executionContext, position, java.lang.String.format("Couldn't find function named \"%s\"", name)));
        }

        // Check if function parameters match function arguments and populate function arguments to the variables table
        InterpreterResult checkAndPopulateResult = checkAndPopulateArguments(getFunctionParametersByName(name), arguments, executionContext);
        if (checkAndPopulateResult.exception() != null) return checkAndPopulateResult;

        try {
            return new InterpreterResult((Subject) function.invoke(null, executionContext));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // Show exception if function value can't be converted to a subject
            return new InterpreterResult(new RuntimeException(executionContext, position, java.lang.String.format("Cannot convert \"%s\" return value to Subject", name)));
        }
    }

    private ArrayList<java.lang.String> getFunctionParametersByName(java.lang.String name) {
        ArrayList<java.lang.String> parameters = new ArrayList<>();

        switch (name) {
            case "print", "println" -> parameters.add("value");
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
    
    public static void print(Context executionContext) {
        System.out.print(executionContext.variablesTable().get("value"));
        executionContext.variablesTable().remove("value");
    }

    public static void println(Context executionContext) {
        System.out.println(executionContext.variablesTable().get("value"));
        executionContext.variablesTable().remove("value");
    }

    public static String input(Context executionContext) {
        System.out.println();
        return new String(Shell.scanner.nextLine());
    }
}
