package fez.main.Nodes;

import fez.main.Objects.Token;

public class VariableReferenceNode extends Node {

    protected Token identifierToken;

    public VariableReferenceNode(Token identifierToken) {
        super(identifierToken.copyPosition());
        this.identifierToken = identifierToken;
    }

    public Token identifier() {
        return identifierToken;
    }

    @Override
    public String toString() {
        return identifierToken.toString();
    }
    
}
