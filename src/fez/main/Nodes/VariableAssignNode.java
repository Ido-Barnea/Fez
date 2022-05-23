package fez.main.Nodes;

import fez.main.Objects.Token;

public class VariableAssignNode extends VariableReferenceNode {

    private final Node value;

    public VariableAssignNode(Token identifierToken, Node value) {
        super(identifierToken);
        this.value = value;
    }

    public Node value() {
        return value;
    }
    
}
