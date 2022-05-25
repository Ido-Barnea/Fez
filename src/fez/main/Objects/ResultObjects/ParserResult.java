package fez.main.Objects.ResultObjects;

import fez.main.Exceptions.Exception;
import fez.main.Nodes.ListNode;
import fez.main.Nodes.Node;

public class ParserResult extends BaseResult {

    private final ListNode syntaxTree;

    public ParserResult(ListNode syntaxTree, Exception exception) {
        super(exception);
        this.syntaxTree = syntaxTree;
    }

    public ParserResult(Exception exception) {
        super(exception);
        this.syntaxTree = null;
    }

    public ListNode syntaxTree() {
        return syntaxTree;
    }
    
}
