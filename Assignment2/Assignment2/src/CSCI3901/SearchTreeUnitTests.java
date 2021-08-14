package CSCI3901;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A more advanced test harness using JUnit, a unit testing framework.
 * <p>
 * To use this test harness, you'll have to add JUnit to your classpath. You can look up how
 * to do this, or if you're using an IDE like IntelliJ chances are it will just ask you if
 * you want to do this.
 * <p>
 * A unit testing framework is morally equivalent to a main function that runs a set
 * of testing functions, but has more flexibility and can report more information
 * or hook into your version control for the purpose of doing regression tests, automatically
 * separate your tests into a hierarchy of test cases, etc.
 */
class SearchTreeUnitTests {

    @Test
    @DisplayName("Test tree from Figure 2")
    public void baseTest() {
        SearchTree bst = new SearchTree();

        assertTrue(bst.add("Egg"));
        assertTrue(bst.add("Carrot"));
        assertTrue(bst.add("Lentil"));
        assertTrue(bst.add("Apple"));
        assertTrue(bst.add("Date"));
        assertTrue(bst.add("Fig"));
        assertTrue(bst.add("Yam"));

        assertEquals(1, bst.find("Egg"));
        assertEquals(1, bst.find("Egg"));
        assertEquals(2, bst.find("Carrot"));
        assertEquals(2, bst.find("Lentil"));
        assertEquals(3, bst.find("Fig"));

        // Initial tree
        String tree = "Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n";
        assertTrue(tree.equals(bst.printTree()));

        // After finding carrot twice
        assertEquals(2, bst.find("Carrot"));
        assertEquals(2, bst.find("Carrot"));
        tree = "Apple 2\nCarrot 1\nDate 3\nEgg 2\nFig 4\nLentil 3\nYam 4\n";
        assertTrue(tree.equals(bst.printTree()));
    }

    @Test
    @DisplayName("Printing an empty tree")
    void emptyTest() {
        SearchTree bst = new SearchTree();

        String tree = "";
        assertTrue(tree.equals(bst.printTree()));
    }
}