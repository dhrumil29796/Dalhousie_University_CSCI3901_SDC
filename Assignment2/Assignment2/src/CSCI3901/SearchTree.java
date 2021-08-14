package CSCI3901;

// Class implementing an unbalanced BST
public class SearchTree {

    // String to store the BST nodes along with their depths
    public String tree = "";

    // Maintaining the root of the BST
    public Node root;

    // Constructor to initialize the BST root node
    public SearchTree() {
        root = null;
    }

    // Method to add an element to the BST
    // Returns true if key is added successfully
    public boolean add(String key) {

        // Checking if the key is null or empty
        if (key == null || key.equals("")) {
            return false;
        } else {

            // Checking if the value already exists in the BST by calling contains method
            if (contains(root, key)) {
                return false;

                // Else add this element to the BST
            } else {
                // Calling the recursive add method
                root = add(root, key);
                return true;
            }
        }
    }

    // Private method to recursively add a value in the BST
    private Node add(Node node, String value) {

        // Base case: found a leaf node
        if (node == null) {

            // Creating a new node with a 0 counter
            node = new Node(null, null, null, value, 0);

        } else {
            // Key comparison is case insensitive
            // Dig into the left subtree
            if (value.compareToIgnoreCase(node.data) < 0) {
                Node leftChild = add(node.left, value);
                node.left = leftChild;

                // Assigning the parent
                leftChild.parent = node;
            } else {
                // Dig into the right subtree
                Node rightChild = add(node.right, value);
                node.right = rightChild;

                // Assigning the parent
                rightChild.parent = node;
            }
        }
        // Returning the node that's been added to the BST
        return node;
    }

    // Private recursive method to find an element in the BST
    private boolean contains(Node node, String value) {

        // Base case: reached bottom, value not found in the BST
        if (node == null) {
            return false;
        }
        // Key comparison is case insensitive
        int cmp = value.compareToIgnoreCase(node.data);

        // Dig into the left subtree because the value we are
        // looking for is smaller than the current value
        if (cmp < 0) {
            return contains(node.left, value);
        }
        // Dig into the right subtree because the value we are
        // looking for is greater than the current value
        else if (cmp > 0) {
            return contains(node.right, value);
        }
        // Value found in the BST
        else {
            return true;
        }
    }

    // Method to find a key & return its depth if it exists in the BST
    public int find(String key) {

        // Checking if the key is null or empty
        if (key == null || key.equals("")) {
            return 0;
        } else {

            // Calling recursive function findDepth
            // Depth of the root node is 1
            return findDepth(root, key, 1);
        }
    }

    // Private recursive method to find the depth of desired key in the BST
    // It also updates the counter for each key found in the BST
    private int findDepth(Node node, String value, int depth) {

        try {
            // Base case: reached bottom, value not found
            if (node == null) {
                return 0;
            }
            // Key comparison is case insensitive
            int cmp = value.compareToIgnoreCase(node.data);

            // Dig into the left subtree because the value we are
            // looking for is smaller than the current value
            if (cmp < 0) {
                return findDepth(node.left, value, depth + 1);
            }
            // Dig into the right subtree because the value we are
            // looking for is greater than the current value
            else if (cmp > 0) {
                return findDepth(node.right, value, depth + 1);
            } else {
                // Updating the current node's counter
                node.counter++;

                // Calling tree rotation method
                treeRotation(node);

                // Depth returned of the desired key in the BST
                return depth;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // Private method to perform rotation on the BST
    private void treeRotation(Node node) {

        // Pointer to the parent of the current node
        Node p = node.parent;

        // Two temporary nodes declared
        Node tmp1;
        Node tmp2;

        // Checking if the parent of the current node is not null
        if (p != null) {

            // Checking if the counter of the node is greater than its parent's counter
            if (node.counter > p.counter) {

                // Checks whether the node is on the left of the parent
                // Does right rotation on the BST
                if (p.left == node) {
                    tmp1 = node.right;
                    node.right = p;

                    // Checking if the right of current node is not null
                    if (tmp1 != null) {
                        tmp1.parent = p;
                    }
                    // Checking if the current node's parent's parent is null
                    // If yes then we assign the root to the current node
                    if (p.parent == null) {
                        root = node;
                    } else if (p.parent.left == p) {
                        p.parent.left = node;
                    } else if (p.parent.right == p) {
                        p.parent.right = node;
                    }
                    // Updating parent down link
                    tmp2 = p.parent;
                    p.parent = node;
                    node.parent = tmp2;
                    p.left = tmp1;

                    // Checks whether the node is on the right of the parent
                    // Does left rotation on the BST
                } else if (p.right == node) {
                    tmp1 = node.left;
                    node.left = p;

                    // Checking if the left of current node is not null
                    if (tmp1 != null) {
                        tmp1.parent = p;
                    }
                    // Checking if the current node's parent's parent is null
                    // If yes then we assign the root to the current node
                    if (p.parent == null) {
                        root = node;
                    } else if (p.parent.left == p) {
                        p.parent.left = node;
                    } else if (p.parent.right == p) {
                        p.parent.right = node;
                    }
                    // Updating parent down link
                    tmp2 = p.parent;
                    p.parent = node;
                    node.parent = tmp2;
                    p.right = tmp1;
                }
            }
        }
    }

    // Resets the counter assigned to each key in the BST
    public void reset() {
        // Calling the recursive method resetCounter
        resetCounter(root);
    }

    // Private recursive method to reset each key counter in the BST
    private void resetCounter(Node node) {
        if (node != null) {
            resetCounter(node.left);

            // Sets the node counter as 0
            node.counter = 0;

            resetCounter(node.right);
        }
    }

    // Returns the depth of the key if it exists in the BST
    private int finding(String key) {

        // Checking if the key is null or empty
        if (key == null || key.equals("")) {
            return 0;
        } else {

            // Depth of the root node is 1
            return getDepth(root, key, 1);
        }
    }

    // Private recursive method to find the depth of desired key in the BST
    private int getDepth(Node node, String value, int depth) {

        try {
            // Base case: reached bottom, value not found
            if (node == null) {
                return 0;
            }
            // Key comparison is case insensitive
            int cmp = value.compareToIgnoreCase(node.data);

            // Dig into the left subtree because the value we are
            // looking for is smaller than the current value
            if (cmp < 0) {
                return getDepth(node.left, value, depth + 1);
            }
            // Dig into the right subtree because the value we are
            // looking for is greater than the current value
            else if (cmp > 0) {
                return getDepth(node.right, value, depth + 1);
            }
            // Depth returned of the desired key in the BST
            else {
                return depth;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // Prints the sorted BST along with depth of each node
    public String printTree() {

        // Resetting the string tree after every printTree() is called
        tree = "";
        return printSortedTree(root);
    }

    // Private recursive method to print the sorted BST along with depth of each node
    // The depth returned is of updated BST (i.e, after rotation)
    private String printSortedTree(Node node) {
        try {
            if (node != null) {
                printSortedTree(node.left);

                // Gets the depth of the searched node
                int nodeDepth = finding(node.data);

                // Forming the string tree to print the keys along with their depth
                tree = tree + (node.data + " " + nodeDepth + "\n");
                printSortedTree(node.right);
            }
            // Returning the final string tree
            return tree;
        } catch (Exception e) {
            return null;
        }
    }

    // Private recursive method for Inorder traversal of the BST
    private void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            System.out.print(" " + node.data + " " + node.counter);

            // To avoid NullPointerException for when the parent is null
            if (node.parent == null) {
                System.out.print(" Parent:Null | ");
            } else {
                System.out.print(" Parent:" + node.parent.data + " | ");
            }
            traverseInOrder(node.right);
        }
    }

    // Driver method
    public static void main(String[] args) {

        // Instantiating the class
        SearchTree tree = new SearchTree();

/**
 * Tried to cover as many test cases as possible mentioned in the Assignment 2.pdf file
 * I have also written an additional traverseInOrder() method to print
 * the Inorder Traversal of the tree along with the parent of each node
 */

        // Finding in an empty tree
        tree.find("apple");

        // Printing an empty tree
        System.out.println(tree.printTree());

        tree.add("Egg Apple");
        tree.add("Egg");
        tree.add(" ");
        tree.add("  ");
        tree.add("           ");
        tree.add("Apple1");

        System.out.println("Depth: " + tree.find(" "));
        System.out.println("Depth: " + tree.find("  "));

        tree.traverseInOrder(tree.root);
        System.out.println("----------------------");

        System.out.println(tree.printTree());
        System.out.println("----------------------");

        tree.add("Egg");
        tree.add("Date");
        tree.add("Lentil");
        tree.add("Carrot");
        tree.add("Eda");
        tree.add("Grape");
        tree.add("Yam");
        tree.add("Apple");
        tree.add("Dam");
        tree.add("Doll");
        tree.add("Edb");
        tree.add("Fig");
        tree.add("Hat");
        tree.add("Tub");
        tree.add("Zoo");

        tree.traverseInOrder(tree.root);
        System.out.println("----------------------");

        System.out.println(tree.printTree());
        System.out.println("----------------------");

        System.out.println("Depth: " + tree.find("   Egg"));
        System.out.println("Depth: " + tree.find("   date"));
        System.out.println("Depth: " + tree.find("   lentil"));
        System.out.println("Depth: " + tree.find("   carrot"));
        System.out.println("Depth: " + tree.find("   eda"));
        System.out.println("Depth: " + tree.find("   grape"));
        System.out.println("Depth: " + tree.find("   yam"));
        System.out.println("Depth: " + tree.find("   apple"));
        System.out.println("Depth: " + tree.find("   dam"));
        System.out.println("Depth: " + tree.find("   doll"));
        System.out.println("Depth: " + tree.find("   edb"));
        System.out.println("Depth: " + tree.find("   fig"));
        System.out.println("Depth: " + tree.find("   hat"));
        System.out.println("Depth: " + tree.find("   tub"));
        System.out.println("Depth: " + tree.find("   Zoo"));

        System.out.println("Depth: " + tree.find("   apple"));
        System.out.println("Depth: " + tree.find("   apple"));
        System.out.println("Depth: " + tree.find("   apple"));

        tree.traverseInOrder(tree.root);
        System.out.println("----------------------");

        // Printing a large tree
        System.out.println(tree.printTree());
        System.out.println("----------------------");

        tree.add("Ape");
        tree.add("Igloo");
        tree.add("fat");

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());
        System.out.println("----------------------");

        System.out.println("Depth: " + tree.find("   ape"));
        System.out.println("Depth: " + tree.find("   ape"));
        System.out.println("Depth: " + tree.find("   ape"));
        System.out.println("Depth: " + tree.find("   ape"));
        System.out.println("Depth: " + tree.find("   ape"));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());
        System.out.println("----------------------");

        // Calling reset twice in a row
        tree.reset();
        tree.reset();

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());
        System.out.println("----------------------");


        // Adding a very small string
        tree.add("a");

        // Adding a very long string
        tree.add("asdfafofbsfibsfbsbfosbfosbfobsuofbsdf");

        // Finding a very small string
        System.out.println(tree.find("a"));

        //Finding a very long string
        System.out.println(tree.find("asdfafofbsfibsfbsbfosbfosbfobsuofbsdf"));

        // Tree that is a linked list
        tree.add("Apple");
        tree.add("Apple");
        tree.add("Ball");
        tree.add("Carrot");
        tree.add("Date");
        tree.add("Egg");


        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        // Printing a tree that is a linked list
        System.out.println(tree.printTree());

        tree.add("a");

        // Printing a tree with one node
        System.out.println(tree.printTree());

        tree.reset();

        // Adding a null string
        tree.add(null);

        // Adding an empty string
        tree.add("");

        // Sending a null string
        System.out.println(tree.find(null));

        // Sending an empty string
        System.out.println(tree.find(""));


        tree.add("Egg");
        tree.add("Date");
        tree.add("Lentil");
        tree.add("Carrot");
        tree.add("Eda");
        tree.add("Grape");
        tree.add("Yam");
        tree.add("Apple");
        tree.add("Dam");
        tree.add("Doll");
        tree.add("Edb");
        tree.add("Fig");
        tree.add("Hat");
        tree.add("Tub");
        tree.add("Zoo");

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());
        System.out.println("----------------------");

        System.out.println("Depth: " + tree.find("   apple"));
        System.out.println("Depth: " + tree.find("   apple"));
        System.out.println("Depth: " + tree.find("   apple"));
        System.out.println("Depth: " + tree.find("   apple"));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        // Printing a big tree
        System.out.println(tree.printTree());
        System.out.println("----------------------");

        tree.add("Egg");
        tree.add("Carrot");
        tree.add("Apple");
        tree.add("Date");
        tree.add("Lentil");
        tree.add("Fig");
        tree.add("Yam");

        System.out.println("Depth: " + tree.find("egg"));
        System.out.println("Depth: " + tree.find("egg"));
        System.out.println("Depth: " + tree.find("lentil"));
        System.out.println("Depth: " + tree.find("shah"));
        System.out.println("Depth: " + tree.find("fig"));
        System.out.println("Depth: " + tree.find("CARROT"));
        System.out.println("Depth: " + tree.find("CARROT"));
        System.out.println("Depth: " + tree.find("CARROT"));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());

        System.out.print("\n Reset: ");
        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());

        // Trying to right rotate the tree
        System.out.println("Depth: " + tree.find("APPLE"));
        System.out.println("Depth: " + tree.find("APPLE"));
        System.out.println("Depth: " + tree.find("  APPLE  "));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println("Depth: " + tree.find("  APPLE  "));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println("Depth: " + tree.find("  APPLE  "));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        tree.reset();

        System.out.print("\n Reset: ");
        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println(tree.printTree());

        System.out.println("Depth: " + tree.find("egg"));
        System.out.println("Depth: " + tree.find("egg"));
        System.out.println("Depth: " + tree.find("lentil"));
        System.out.println("Depth: " + tree.find("shah"));
        System.out.println("Depth: " + tree.find("fig"));
        System.out.println("Depth: " + tree.find("CARROT"));
        System.out.println("Depth: " + tree.find("CARROT"));

        tree.traverseInOrder(tree.root);
        System.out.println("\n----------------------");

        System.out.println("The root element of the BST is: " + tree.root.data);

    }
}
