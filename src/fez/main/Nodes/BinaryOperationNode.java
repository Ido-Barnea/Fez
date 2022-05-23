package fez.main.Nodes;

import fez.main.Objects.TokenType;

public class BinaryOperationNode extends Node {

    private Node leftNode;
    private TokenType operator;
    private Node rightNode;

    public BinaryOperationNode(Node leftNode, TokenType operator, Node rightNode) {
        super(rightNode.copyPosition());
        
        this.leftNode = leftNode;
        this.operator = operator;
        this.rightNode = rightNode;
    }

    public BinaryOperationNode(Node leftNode) {
        super(leftNode.copyPosition());
        
        this.leftNode = leftNode;
        this.operator = null;
        this.rightNode = null;
    }

    public Node leftNode() {
        return leftNode;
    }

    public Node rightNode() {
        return rightNode;
    }

    public TokenType operator() {
        return operator;
    }

    public void setRight(Node subject) {
        rightNode = subject;
    }

    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", leftNode, operator, rightNode);
    }

}
