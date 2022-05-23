package fez.main.Nodes;

public class WhileNode extends Node {

    private final Node condition;
    private final Node statements;

    public WhileNode(Node condition, Node statements) {
        super(condition.copyPosition());
        this.condition = condition;
        this.statements = statements;
    }

    public Node condition() {
        return condition;
    }

    public Node expression() {
        return statements;
    }
    
}
