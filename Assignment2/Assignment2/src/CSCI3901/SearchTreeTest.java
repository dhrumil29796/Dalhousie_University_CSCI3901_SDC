package CSCI3901;

/**
 * A simple test harness
 */
public class SearchTreeTest {

    // The tree from figure 2
    public static void baseTest() {
        SearchTree bst = new SearchTree();

        // Add everything from figure 2
        assert (bst.add("Egg"));
        assert (bst.add("Carrot"));
        assert (bst.add("Lentil"));
        assert (bst.add("Apple"));
        assert (bst.add("Date"));
        assert (bst.add("Fig"));
        assert (bst.add("Yam"));

        assert (1 == bst.find("Egg"));
        assert (1 == bst.find("Egg"));
        assert (2 == bst.find("Carrot"));
        assert (2 == bst.find("Lentil"));
        assert (3 == bst.find("Fig"));

        // Initial tree
        assert (bst.printTree().equals("Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n"));

        // After finding carrot twice
        assert (2 == bst.find("Carrot"));
        assert (2 == bst.find("Carrot"));
        assert (bst.printTree().equals("Apple 2\nCarrot 1\nDate 3\nEgg 2\nFig 4\nLentil 3\nYam 4\n"));
    }

    // Testing the output of an empty tree
    public static void emptyTest() {
        SearchTree bst = new SearchTree();

        assert (bst.printTree().equals(""));
    }

    // Runs the tests
    public static void main(String[] args) {
        baseTest();
        emptyTest();
    }
}
