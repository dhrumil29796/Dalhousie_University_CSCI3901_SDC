package CSCI3901;

// Node creation
public class Node {

    public int counter;
    public String data;
    public Node left, right, parent;

    // Creates a new node
    public Node(Node left, Node right, Node parent, String elem, int count) {
        this.counter = count;
        this.data = elem;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
