package fez.main.Nodes;

public class ForNode extends Node {

    private final String variableName;
    private final Node startValueNode;
    private final Node endValueNode;
    private final Node expression;

    public ForNode(String variableName, Node startValueNode, Node endValueNode, Node expression) {
        super(endValueNode.copyPosition());
        this.variableName = variableName;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.expression = expression;
    }

    public String variableName() {
        return variableName;
    }

    public Node startValue() {
        return startValueNode;
    }

    public Node endValue() {
        return endValueNode;
    }

    public Node statements() {
        return expression;
    }

    @Override
    public String toString() {
        return String.format("for (%s in %s:%s) %s", variableName, startValueNode, endValueNode, expression);
    }
    
}
