package fez.main.Nodes;

import fez.main.Objects.Position;

public class Node {

    private final Position position;

    public Node(Position position) {
        this.position = position;
    }

    public Position copyPosition() {
        return position.copy();
    }
    
}
