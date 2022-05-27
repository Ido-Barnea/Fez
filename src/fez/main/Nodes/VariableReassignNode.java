package fez.main.Nodes;

import fez.main.Objects.Token;
import java.util.ArrayList;

public class VariableReassignNode extends VariableAssignNode {

    public VariableReassignNode(Token identifierToken, Node value) {
        super(identifierToken, value, new ArrayList<>());
    }
    
}
