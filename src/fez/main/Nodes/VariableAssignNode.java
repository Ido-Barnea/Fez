package fez.main.Nodes;

import fez.main.Objects.ModifierType;
import fez.main.Objects.Token;

import java.util.ArrayList;

public class VariableAssignNode extends VariableReferenceNode {

    private final Node value;
    private final ArrayList<ModifierType> modifiers;

    public VariableAssignNode(Token identifierToken, Node value, ArrayList<ModifierType> modifiers) {
        super(identifierToken);
        this.value = value;
        this.modifiers = modifiers;
    }

    public Node value() {
        return value;
    }

    public ArrayList<ModifierType> modifiers() {
        return modifiers;
    }
    
}
