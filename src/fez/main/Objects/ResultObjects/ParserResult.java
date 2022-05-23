package fez.main.Objects.ResultObjects;

import fez.main.Exceptions.Exception;
import fez.main.Nodes.Node;

public class ParserResult extends BaseResult {

    private final Node syntaxTree;

    public ParserResult(Node syntaxTree, Exception exception) {
        super(exception);
        this.syntaxTree = syntaxTree;
    }

    public ParserResult(Exception exception) {
        super(exception);
        this.syntaxTree = null;
    }

    public Node syntaxTree() {
        return syntaxTree;
    }
    
}
