package fez.main.Nodes;

import java.util.ArrayList;
import fez.main.Objects.Position;

public class ListNode extends Node {

    private final ArrayList<Node> nodes;

    public ListNode(ArrayList<Node> nodes, Position position) {
        super(position);
        this.nodes = nodes;
    }

    public ArrayList<Node> nodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
