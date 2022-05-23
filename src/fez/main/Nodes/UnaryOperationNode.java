package fez.main.Nodes;

import fez.main.Objects.TokenType;

public class UnaryOperationNode extends Node {
    private final TokenType operator;
    private final Node node;

    public UnaryOperationNode(TokenType operator, Node node) {
        super(node.copyPosition());

        this.operator = operator;
        this.node = node;
    }

    public Node node() {
        return node;
    }

    public TokenType operator() {
        return operator;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", operator, node);
    }

}
