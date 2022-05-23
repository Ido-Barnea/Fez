package fez.main.Nodes;

import fez.main.Objects.Token;

public class VariableReassignNode extends VariableAssignNode {

    public VariableReassignNode(Token identifierToken, Node value) {
        super(identifierToken, value);
    }
    
}
