package fez.main.Nodes;

public class ListItemAssignNode extends Node{

    private final Node indexesToAssignNode;
    private final Node listNode;
    private final Node valueNode;

    public ListItemAssignNode(Node listNode, Node indexesToAssignNode, Node valueNode) {
        super(listNode.copyPosition());
        this.listNode = listNode;
        this.indexesToAssignNode = indexesToAssignNode;
        this.valueNode = valueNode;
    }

    public Node nodeToAssign() {
        return indexesToAssignNode;
    }

    public Node listNode() {
        return listNode;
    }

    public Node valueNode() {
        return valueNode;
    }
}
