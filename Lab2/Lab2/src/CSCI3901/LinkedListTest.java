package CSCI3901;

/**
 * An implementation of a linked list
 * @param <T> The type of objects to store
 */
class LinkedList<T> {

    /** The root of the list */
    private Node<T> root;

    /** Adds an item to the head of the list */
    public void add(T item) {
        root = new Node<T>(item, root);
    }

    /** Removes the head of the list if non-empty, does nothing otherwise */
    public void remove() {
        if (root != null) {
            root = root.tail;
        }
    }

    /** Add an item to the head of the list */
    public void append(LinkedList<T> list) {
        if (root == null) {
            root = list.root;
        } else {
            Node<T> tmp = root;

            // Iterate through the list to find the end
            while (tmp.tail != null) {
                tmp = tmp.tail;
            }

            // Assign the end of the list
            tmp.tail = list.root;
        }
    }
    /* Method to add new node at last, making deep copy */
    public void addLast(T item){
        if(root == null){
            root = new Node<T>(item, null);
        } else {
            Node<T> temp = root;
            while (temp.tail != null){
                temp = temp.tail;
            }
            temp.tail = new Node<>(item, null);
        }
    }

    /** Copies a linked list */
    public LinkedList<T> copy() {
        LinkedList<T> tmp = new LinkedList<T>();

        // Copies the entire list
        // tmp.root = this.root.copy();

        /* changes made to make a deep copy by calling addLast()*/
        Node<T> node = this.root;
        while (node != null) {
            tmp.addLast(node.head);
            node = node.tail;
        }

        return tmp;
    }

    /** Prints the contents of a list */
    public void print() {
        Node<T> tmp = root;

        // Iterate through the list
        while (tmp != null) {
            System.out.print(tmp.head);
            tmp = tmp.tail;
        }

        // Print a new line
        System.out.println();
    }

    /**
     * Private internal node class storing the actual linked list
     * @param <T> The type of objects to store
     */
    private static class Node<T> {
        public T head;
        public Node<T> tail;

        // Singleton list constructor
        public Node(T head) {
            this.head = head;
            this.tail = null;
        }

        // Standard "cons" constructor
        public Node(T head, Node<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        // Creates a copy of the node
        public Node<T> copy() {
            Node<T> tmp = new Node<T>(this.head);

            tmp.head = this.head;
            if (this.tail == null) {
                tmp.tail = null;
            } else {
                tmp.tail = this.tail;
            }

            return tmp;
        }

    }
}

/**
 * Main program
 */
public class LinkedListTest {

    public static void main(String[] args) {

        LinkedList<Integer> lst1 = new LinkedList<Integer>();

        // Populate a list
        for (int i = 0; i < 10; i++) {
            lst1.add(i);
        }
        LinkedList<Integer> lst2 = lst1.copy();
        lst2.append(lst1);

        // Test cases
        lst1.print(); // Should be 9876543210
        lst2.print(); // Should be 98765432109876543210

    }
}