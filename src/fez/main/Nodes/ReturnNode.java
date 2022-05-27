package fez.main.Nodes;

public class ReturnNode extends Node {

    private final Node returnValue;

    public ReturnNode(Node returnValue) {
        super(returnValue.copyPosition());
        this.returnValue = returnValue;
    }

    public Node returnValue() {
        return returnValue;
    }
}
