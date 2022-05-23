package fez.main.Nodes;

import java.util.ArrayList;
import fez.main.Objects.Position;

public class FunctionDefinitionNode extends Node {

    private final String name;
    private final ArrayList<String> parameters;
    private final Node body;

    public FunctionDefinitionNode(String name, ArrayList<String> parameters, Node body, Position position) {
        super(position);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String name() {
        return name;
    }

    public ArrayList<String> parameters() {
        return parameters;
    }

    public Node body() {
        return body;
    }
    
}
