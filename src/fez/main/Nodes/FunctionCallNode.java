package fez.main.Nodes;

import java.util.ArrayList;
import fez.main.Objects.Position;

public class FunctionCallNode extends Node {

    private final Node functionNode;
    private final ArrayList<Node> arguments;

    public FunctionCallNode(Node functionNode, ArrayList<Node> arguments, Position position) {
        super(position);
        this.functionNode = functionNode;
        this.arguments = arguments;
    }

    public Node functionNode() {
        return functionNode;
    }

    public ArrayList<Node> arguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return functionNode.toString();
    }
}
