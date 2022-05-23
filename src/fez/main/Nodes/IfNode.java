package fez.main.Nodes;

public class IfNode extends Node {

    private final Node condition;
    private final Node ifExpression;
    private final Node elseExpression;

    public IfNode(Node condition, Node ifExpression, Node elseExpression) {
        super(condition.copyPosition());

        this.condition = condition;
        this.ifExpression = ifExpression;
        this.elseExpression = elseExpression;
    }

    public Node condition() {
        return condition;
    }

    public Node ifStatements() {
        return ifExpression;
    }

    public Node elseStatements() {
        return elseExpression;
    }

    @Override
    public String toString() {
        if (elseExpression == null)
            return String.format("if (%s) %s", condition, ifExpression);
        return String.format("if (%s) %s else %s", condition, ifExpression, elseExpression);
    }
    
}
