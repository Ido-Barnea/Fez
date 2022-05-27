package fez.main;

import java.util.ArrayList;
import fez.main.Exceptions.InvalidSyntaxException;
import fez.main.Exceptions.NullPointerException;
import fez.main.Exceptions.RuntimeException;
import fez.main.Nodes.*;
import fez.main.Objects.Context;
import fez.main.Objects.ModifierType;
import fez.main.Objects.TokenType;
import fez.main.Objects.ResultObjects.InterpreterResult;
import fez.main.Subjects.BaseFunction;
import fez.main.Subjects.Float;
import fez.main.Subjects.Function;
import fez.main.Subjects.Int;
import fez.main.Subjects.List;
import fez.main.Subjects.Subject;
import java.util.AbstractMap.SimpleEntry;

public class Interpreter {
    
    public InterpreterResult visit(Node node, Context context) {
        if (node == null) return new InterpreterResult();
        else if (node instanceof ReturnNode) return visit(((ReturnNode) node).returnValue(), context);
        else if (node instanceof VariableReassignNode) return visitVariableReassignNode((VariableReassignNode) node, context);
        else if (node instanceof VariableAssignNode) return visitVariableAssignNode((VariableAssignNode) node, context);
        else if (node instanceof VariableReferenceNode) return visitVariableReferenceNode((VariableReferenceNode) node, context);
        else if (node instanceof IfNode) return visitIfNode((IfNode) node, context);
        else if (node instanceof ForNode) return visitForNode((ForNode) node, context);
        else if (node instanceof WhileNode) return visitWhileNode((WhileNode) node, context);
        else if (node instanceof UnaryOperationNode) return visitUnaryOperationNode((UnaryOperationNode) node, context);
        else if (node instanceof BinaryOperationNode) return visitBinaryOperationNode((BinaryOperationNode) node, context);
        else if (node instanceof NumberNode) return visitNumberNode((NumberNode) node, context);
        else if (node instanceof StringNode) return visitStringNode((StringNode) node);
        else if (node instanceof ListNode) return visitListNode((ListNode) node, context);
        else if (node instanceof FunctionDefinitionNode) return visitFunctionDefinitionNode((FunctionDefinitionNode) node, context);
        else if (node instanceof FunctionCallNode) return visitFunctionCallNode((FunctionCallNode) node, context);
        else return new InterpreterResult(new InvalidSyntaxException(node.copyPosition(), "Expected Operation"));
    }

    public InterpreterResult visitFunction(Node node, Context context) {
        if (node == null) return new InterpreterResult();
        else if (node instanceof ReturnNode) return visit(((ReturnNode) node).returnValue(), context);
        else if (node instanceof ListNode) return visitFunctionListNode((ListNode) node, context);
        return new InterpreterResult();
    }

    private InterpreterResult visitVariableReferenceNode(VariableReferenceNode variableReferenceNode, Context context) {
        String variableName = variableReferenceNode.identifier().value().toString();

        if (!context.variablesTable().variablesMap().containsKey(variableName)) // Variable key isn't in the variables table
            return new InterpreterResult(new RuntimeException(context, variableReferenceNode.copyPosition(), String.format("\"%s\" is not defined", variableName)));
        else return new InterpreterResult(context.variablesTable().getValue(variableName));
    }

    private InterpreterResult visitVariableAssignNode(VariableAssignNode variableAssignmentNode, Context context) {
        String variableName = variableAssignmentNode.identifier().value().toString();
        Node variableValueNode = variableAssignmentNode.value();

        // Check if variable is already defined
        if (context.variablesTable().variablesMap().containsKey(variableName)) {
            return new InterpreterResult(
                new RuntimeException(
                    context,
                    variableAssignmentNode.copyPosition(),
                    String.format("\"%s\" is already defined", variableName)
                )
            );
        }

        InterpreterResult variableValueSmartResult = visit(variableValueNode, context);
        if (variableValueSmartResult.hasException()) return variableValueSmartResult;

        Subject variableValue = variableValueSmartResult.result();
        context.variablesTable().set(variableName, new SimpleEntry<>(variableValue, variableAssignmentNode.modifiers()));

        return new InterpreterResult();
    }

    private InterpreterResult visitVariableReassignNode(VariableReassignNode variableReassignmentNode, Context context) {
        String variableName = variableReassignmentNode.identifier().value().toString();
        Node variableValueNode = variableReassignmentNode.value();

        // Check if variable is defined
        if (!context.variablesTable().variablesMap().containsKey(variableName)) {
            return new InterpreterResult(
                new RuntimeException(
                    context,
                    variableReassignmentNode.copyPosition(),
                    String.format("\"%s\" is not defined", variableName)
                )
            );
        } else {
            ArrayList<ModifierType> modifiers = context.variablesTable().getModifiers(variableName);
            if (modifiers.contains(ModifierType.IMMUTABLE)) {
                return new InterpreterResult(
                        new RuntimeException(
                                context,
                                variableReassignmentNode.copyPosition(),
                                String.format("Cannot change value of constant \"%s\"", variableName)
                        )
                );
            }
        }

        InterpreterResult variableValueSmartResult = visit(variableValueNode, context);
        if (variableValueSmartResult.hasException()) return variableValueSmartResult;

        Subject variableValue = variableValueSmartResult.result();
        context.variablesTable().set(variableName, variableValue);

        return new InterpreterResult();
    }

    private InterpreterResult visitFunctionDefinitionNode(FunctionDefinitionNode functionDefinitionNode, Context context) {
        String functionName = functionDefinitionNode.name();
        ArrayList<String> parameters = functionDefinitionNode.parameters();
        Node functionBody = functionDefinitionNode.body();

        if (context.variablesTable().variablesMap().containsKey(functionName)) { // Function is already defined in variables table
            return new InterpreterResult(
                new RuntimeException(context, functionDefinitionNode.copyPosition(),
                String.format("\"%s\" is already defined", functionName))
            );
        }

        // Create function object
        Function function = new Function(functionName, parameters, functionBody);
        function.setContext(context.copy());
        function.setPosition(functionDefinitionNode.copyPosition());
        
        // Add function to variables table
        context.variablesTable().set(functionName, function);
        return new InterpreterResult();
    }

    private InterpreterResult visitFunctionCallNode(FunctionCallNode functionCallNode, Context context) {
        InterpreterResult valueToCall = visit(functionCallNode.functionNode(), context);
        if (valueToCall.hasException()) return new InterpreterResult(valueToCall.exception());
        if (valueToCall.result() == null) {
            return new InterpreterResult(
                    new NullPointerException(
                            context,
                            functionCallNode.copyPosition(),
                            "Cannot perform operation on null value"
                    )
            );
        }

        BaseFunction function = ((BaseFunction) valueToCall.result()).copy();

        Context functionContext = context.copy();
        functionContext.setEntryPosition(functionCallNode.copyPosition());
        if (function.context() != null) functionContext.setParent(function.context());
        function.setContext(functionContext);

        function.setPosition(functionCallNode.copyPosition());

        // Add arguments
        ArrayList<Subject> arguments = new ArrayList<>();
        for (int i = 0; i < functionCallNode.arguments().size(); i++) {
            Node argumentNode = functionCallNode.arguments().get(i);
            InterpreterResult argumentValueInterpreterSmartResult = visit(argumentNode, context);

            if (argumentValueInterpreterSmartResult.hasException()) return argumentValueInterpreterSmartResult; // Return exception
            else arguments.add(argumentValueInterpreterSmartResult.result()); // Add argument to arguments ArrayList
        }

        return function.execute(arguments);
    }

    private InterpreterResult visitIfNode(IfNode ifNode, Context context) {
        Int condition = (Int) visit(ifNode.condition(), context).result();
        if (condition.isTrue()) {
            Subject ifExpression = visit(ifNode.ifStatements(), context).result();
            return new InterpreterResult(ifExpression);
        } else {
            Subject elseExpression = visit(ifNode.elseStatements(), context).result();
            return new InterpreterResult(elseExpression);
        }
    }

    private InterpreterResult visitForNode(ForNode forNode, Context context) {
        Int startValue = (Int) visit(forNode.startValue(), context).result();
        Int endValue = (Int) visit(forNode.endValue(), context).result();

        for (int i = startValue.intValue(); i < endValue.intValue(); i++) {
            context.variablesTable().set(forNode.variableName(), new Int(i)); // Update for loop variable in the variables table
            InterpreterResult visitResult = visit(forNode.statements(), context);
            if (visitResult.hasException()) return visitResult;
        }

        context.variablesTable().remove(forNode.variableName());
        return new InterpreterResult();
    }

    private InterpreterResult visitWhileNode(WhileNode whileNode, Context context) {
        InterpreterResult conditionVisitResult = visit(whileNode.condition(), context);
        if (conditionVisitResult.hasException()) return conditionVisitResult;
        Int condition = (Int) conditionVisitResult.result();
        
        while (condition.isTrue()) {
            InterpreterResult statementVisitResult = visit(whileNode.expression(), context);
            if (statementVisitResult.hasException()) return statementVisitResult;

            conditionVisitResult = visit(whileNode.condition(), context);
            if (conditionVisitResult.hasException()) return conditionVisitResult;
            else condition = (Int) conditionVisitResult.result();
        }
        return new InterpreterResult();
    }

    private InterpreterResult visitNumberNode(NumberNode numberNode, Context context) {
        if (numberNode.value() instanceof Int) return new InterpreterResult(new Int(numberNode.value().intValue()));
        else if (numberNode.value() instanceof Float) return new InterpreterResult(new Float(numberNode.value().floatValue()));
        return new InterpreterResult(new RuntimeException(context, numberNode.copyPosition(), "Unrecognized type for operation"));
    }

    private InterpreterResult visitStringNode(StringNode stringNode) {
        return new InterpreterResult(stringNode.value());
    }

    private InterpreterResult visitListNode(ListNode listNode, Context context) {
        ArrayList<Subject> elements = new ArrayList<>();
        for (Node node : listNode.nodes()) {
            InterpreterResult getElementResult = visit(node, context);
            if (getElementResult.hasException()) return getElementResult;
            else if (getElementResult.result() != null) elements.add(getElementResult.result());
        }

        if (elements.size() == 0) return new InterpreterResult();
        return new InterpreterResult(new List(elements));
    }

    private InterpreterResult visitFunctionListNode(ListNode listNode, Context context) {
        for (Node node : listNode.nodes()) {
            InterpreterResult getElementResult = visit(node, context);
            if (node instanceof ReturnNode) return getElementResult;
        }

        return new InterpreterResult();
    }

    private InterpreterResult visitBinaryOperationNode(BinaryOperationNode binaryOperationNode, Context context) {
        InterpreterResult leftInterpreterSmartResult = visit(binaryOperationNode.leftNode(), context);
        InterpreterResult rightInterpreterSmartResult = visit(binaryOperationNode.rightNode(), context);

        // Check for errors
        if (leftInterpreterSmartResult.hasException()) return leftInterpreterSmartResult;
        else if (rightInterpreterSmartResult.hasException()) return rightInterpreterSmartResult;
        
        Subject left = leftInterpreterSmartResult.result();
        Subject right = rightInterpreterSmartResult.result();

        if (left == null) {
            return new InterpreterResult(
                    new NullPointerException(
                            context,
                            binaryOperationNode.leftNode().copyPosition(),
                            "Cannot perform operation on null value"
                    )
            );
        }
        else if (right == null) {
            return new InterpreterResult(
                    new NullPointerException(
                            context,
                            binaryOperationNode.rightNode().copyPosition(),
                            "Cannot perform operation on null value"
                    )
            );
        }

        return switch (binaryOperationNode.operator()) {
            case PLUS -> left.add(right, binaryOperationNode.copyPosition());
            case MINUS -> left.subtract(right, binaryOperationNode.copyPosition());
            case MUL -> left.multiply(right, binaryOperationNode.copyPosition());
            case DIV -> left.divide(right, binaryOperationNode.copyPosition());
            case POW -> left.power(right, binaryOperationNode.copyPosition());
            case MODULO -> left.modulo(right, binaryOperationNode.copyPosition());
            case EQUAL_EQUAL -> new InterpreterResult(left.equals(right));
            case NOT_EQUAL -> left.equals(right).not(binaryOperationNode.copyPosition());
            case GREATER_THAN -> left.greaterThan(right, binaryOperationNode.copyPosition());
            case LESS_THAN -> left.lessThan(right, binaryOperationNode.copyPosition());
            case GREATER_OR_EQUAL -> left.greaterOrEqualTo(right, binaryOperationNode.copyPosition());
            case LESS_OR_EQUAL -> left.lessOrEqualTo(right, binaryOperationNode.copyPosition());
            case AND -> left.and(right, binaryOperationNode.copyPosition());
            case OR -> left.or(right, binaryOperationNode.copyPosition());
            case RETRIEVE -> left.retrieve(right, binaryOperationNode.copyPosition());
            default -> new InterpreterResult(new InvalidSyntaxException(binaryOperationNode.leftNode().copyPosition(), "Illegal Operation"));
        };
    }

    private InterpreterResult visitUnaryOperationNode(UnaryOperationNode unaryOperationNode, Context context) {
        Subject subject = visit(unaryOperationNode.node(), context).result();
        TokenType operator = unaryOperationNode.operator();

        if (operator.equals(TokenType.MINUS)) return subject.multiply(new Int(-1), unaryOperationNode.copyPosition());
        else if (operator.equals(TokenType.NOT)) return subject.not(unaryOperationNode.copyPosition());
        else return new InterpreterResult(subject);
    }

}
