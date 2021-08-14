import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

// Class to load and solve the Mathdoku puzzle
public class Mathdoku {

    // Class to store the puzzle cell values
    public static class PuzzleCell {

        // Variables to store value & group of each puzzle cell
        int cellValue;
        char cellGroup;

        // Parameterized Constructor of puzzle cell
        public PuzzleCell(int value, char group) {
            cellValue = value;
            cellGroup = group;
        }
    }

    // Class to store grid locations a and b
    public static class Grid {

        // Variables to store the grid location
        int a;
        int b;

        // Parameterized constructor of grid
        public Grid(int xAxis, int yAxis) {
            a = xAxis;
            b = yAxis;
        }
    }

    // Class to store the constraints of each group
    public static class PuzzleConstraint {

        // Variables to store each groups outcome & it's operator
        int outcome;
        char operator;

        // Arraylist to store the vertices list of each group
        ArrayList<Grid> Grid;

        // Parameterized constructor of constraint
        public PuzzleConstraint(int outcome, char operator, ArrayList<Grid> grid) {
            this.outcome = outcome;
            this.operator = operator;
            Grid = grid;
        }
    }

    // The mathdoku puzzle (mPuzzle) is a 2D array of type PuzzleCell
    // Each PuzzleCell contains a cellValue & cellGroup
    PuzzleCell[][] mPuzzle;

    // The puzzleGroups is a map with key as Character & value as type PuzzleConstraint
    Map<Character, PuzzleConstraint> puzzleGroups;

    // Stores the choices made while solving the puzzle
    int choices;

    // Final variables storing the operators
    final private char equal = '=';
    final private char multiply = '*';
    final private char add = '+';
    final private char subtract = '-';
    final private char divide = '/';

    // Mathdoku class constructor
    public Mathdoku() {
        mPuzzle = null;
        puzzleGroups = null;
        choices = 0;
    }

    // Will load the puzzle from a stream
    public boolean loadPuzzle(BufferedReader stream) {

        // Returns false if the stream is null
        if (stream == null) {
            return false;
        }
        try {
            // Reads the size of the puzzle
            String line = stream.readLine();

            // Returns false if the read line is null
            if (line == null) {
                return false;
            } else {

                // Checking for next line if any empty lines found
                while (line.trim().isEmpty()) {
                    line = stream.readLine().trim();
                }
            }
            // Storing the puzzle size
            int size = Integer.parseInt(line);

            // Returns false if puzzle size is <= 1
            if (size <= 1) {
                return false;
            }
            // Create a puzzle of size n x n  where n is equal to the size passed
            mPuzzle = new PuzzleCell[size][size];

            // Reading & trimming the next line in the stream
            line = stream.readLine().trim();

            // Iterating through the puzzle to store values & groups
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {

                    // Storing the values of read line in the puzzle for row i
                    mPuzzle[i][j] = new PuzzleCell(0, line.charAt(j));
                }
                // Break if row no is equal to size of the puzzle
                if (i == size - 1) {
                    break;
                }
                // Reading & trimming next line in the stream
                line = stream.readLine().trim();

                // Checking for next line if any empty lines found
                while (line.trim().isEmpty()) {
                    line = stream.readLine().trim();
                }
                // Checking no.of characters for the read line
                // If found different then return false & set puzzle to null
                if (line.length() != size) {
                    mPuzzle = null;
                    return false;
                }
            }
            // Reading & trimming the next line in the stream, containing the constraints
            line = stream.readLine().trim();

            // Declaring new constraints StringBuilder
            StringBuilder constraints = new StringBuilder();

            // Iterating using a while loop to
            // Store constraints in constraints StringBuilder
            while (line != null) {

                // If the line is empty, check for the next line
                if (line.trim().isEmpty()) {
                    line = stream.readLine().trim();
                    continue;
                }
                // Appending the trimmed line and "\n" to constraints
                constraints.append(line.trim()).append("\n");

                // Reading next line from the stream
                line = stream.readLine();
            }
            // Calling loadConstraints method to load the constraints with constraints
            boolean validConstraints = loadingConstraints(constraints.toString());

            // Returns false if constraints are not valid
            if (!validConstraints) {
                return false;
            }
        } catch (Exception e) {
            mPuzzle = null;
            puzzleGroups = null;
            return false;
        }
        return true;
    }

    // Private method to load the constrains into the puzzleGroups Map
    private boolean loadingConstraints(String constraints) {

        // Checking whether constraints is not empty
        if (!constraints.isEmpty()) {

            // Declaring new puzzleGroups Map
            puzzleGroups = new HashMap<>();

            // Adding operators list to an arraylist
            ArrayList<Character> validOperators = new ArrayList<>(Arrays.asList(add, subtract, multiply, divide, equal));

            // Splitting & storing constraints into a constraintsLines string array
            String[] constraintLines = constraints.split("\n");

            // Iterating through the string array constraintLines
            for (String s : constraintLines) {

                // Splitting & storing each index of constraintLines array in constraintParams array
                String[] constraintParams = s.split("\\s+");

                // Checking whether each constraint contains 3 things
                // The group, outcome & operator
                if (constraintParams.length != 3) {
                    puzzleGroups = null;
                    return false;
                }
                // Getting the group from the constraintParams array
                char group = constraintParams[0].charAt(0);

                // Variable to store the outcome
                int outcome;

                try {
                    // Getting the outcome
                    outcome = Integer.parseInt(constraintParams[1]);
                } catch (Exception e) {
                    puzzleGroups = null;
                    return false;
                }
                // Getting the operator from the constraintParams array
                char operator = constraintParams[2].charAt(0);

                // Checking whether validOperators arraylist contains the read operator
                // Returns false if not present & sets puzzleGroup to null
                if (!validOperators.contains(operator)) {
                    puzzleGroups = null;
                    return false;
                }
                // Arraylist to store the grid locations
                ArrayList<Grid> gridLocations = new ArrayList<>();

                // Iterating through the puzzle
                for (int i = 0; i < mPuzzle.length; i++) {
                    for (int j = 0; j < mPuzzle.length; j++) {
                        if (mPuzzle[i][j].cellGroup == group) {

                            // Storing the Grids of the group
                            gridLocations.add(new Grid(i, j));
                        }
                    }
                }
                // Storing the outcome, operator and grid locations for each puzzleGroup in the Map
                puzzleGroups.put(group, new PuzzleConstraint(outcome, operator, gridLocations));
            }
        } else {
            return false;
        }
        return true;
    }

    // Private method to check if the puzzle is solved
    private boolean solvedPuzzle() {

        // Returns false if puzzle is null
        if (mPuzzle == null) {
            return false;
        }
        // Iterating through the puzzle
        // Checking whether any of the cell contains 0
        for (PuzzleCell[] cells : mPuzzle) {
            for (int j = 0; j < mPuzzle.length; j++) {
                if (cells[j].cellValue == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Public method to validate the loaded puzzle
    public boolean validate() {

        // Returns false if puzzle is null or puzzleGroups Map is null
        if (mPuzzle == null || puzzleGroups == null) {
            return false;
        }

        // Returns false if puzzle is already complete
        if (solvedPuzzle()) {
            return true;
        }

        // Creating a character set to store all the groups
        Set<Character> groupSet = new HashSet<>();

        // Iterating through the puzzle & adding the cell group to the set
        for (PuzzleCell[] cells : mPuzzle) {
            for (int j = 0; j < mPuzzle.length; j++) {
                groupSet.add(cells[j].cellGroup);
            }
        }

        // Checking if all the groups that belong to the puzzle are provided in the constraints
        if (!puzzleGroups.keySet().containsAll(groupSet)) {
            return false;
        }

        // Calling the validateGroupings method
        if (validateGroupings()) {
            return false;
        }

        // Outcome of any cell should never be <= zero
        for (char c : puzzleGroups.keySet()) {
            if (puzzleGroups.get(c).outcome <= 0) {
                return false;
            }
        }
        return true;
    }

    // Private method to validate the groupings of the puzzle according to its operator
    private boolean validateGroupings() {

        // Iterating through the puzzleGroups map's key set containing groups
        for (char c : puzzleGroups.keySet()) {

            // Every grouping with '=' operator has exactly one cell
            if (puzzleGroups.get(c).operator == equal) {
                if (puzzleGroups.get(c).Grid.size() != 1) {
                    return true;
                }
            }

            // Every grouping with '-' or '/' operator has exactly two cells
            if ((puzzleGroups.get(c).operator == subtract) || (puzzleGroups.get(c).operator == divide)) {
                if (puzzleGroups.get(c).Grid.size() != 2) {
                    return true;
                }
            }

            // Every grouping with '+' or '*' operator has at least two cells
            if ((puzzleGroups.get(c).operator == add) || (puzzleGroups.get(c).operator == multiply)) {
                if (puzzleGroups.get(c).Grid.size() <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // Public method to solve the puzzle
    public boolean solve() {

        // First validating the puzzle by calling validate method
        // If true, then only move on with solving the puzzle
        if (validate()) {

            // Checking if the puzzle or puzzleGroups Map is null
            if (mPuzzle == null || puzzleGroups == null) {
                return false;
            }

            // Returns true if the puzzle is already solved
            if (solvedPuzzle()) {
                return true;
            }

            // Resetting the choices
            choices = 0;

            // Creating a Character set of '=' operator groups
            Set<Character> equalOpGroups = new HashSet<>();

            // Inserting the values in the group with '=' operator constraint
            // Iterating through the puzzleGroups key set of groups
            for (char c : puzzleGroups.keySet()) {
                if (puzzleGroups.get(c).operator == equal) {

                    // Retrieving the grid location of the current puzzleGroups group
                    Grid loc = puzzleGroups.get(c).Grid.get(0);

                    // Storing the puzzle's grid location into two variables
                    int x = loc.a;
                    int y = loc.b;

                    // Storing the outcome from the puzzleGroups map for the current group
                    int outcome = puzzleGroups.get(c).outcome;

                    // Checking for any collisions in row & column before setting values
                    if (checkConflict(x, y, outcome)) {

                        // If conflict found, then reset the puzzle & return false
                        resetPuzzle();
                        return false;
                    }
                    // Resetting the puzzle if outcome is greater than puzzle size
                    else if (outcome > mPuzzle.length) {
                        resetPuzzle();
                        return false;
                    }
                    // Setting outcome in the cell value of current grid's location
                    mPuzzle[x][y].cellValue = outcome;

                    // Incrementing the choices
                    choices++;

                    // Adding to the equal operator groups
                    equalOpGroups.add(c);
                }
            }
            // Removing the '=' operator groups from the puzzleGroups as they have been filled
            puzzleGroups.keySet().removeAll(equalOpGroups);

            // 2D arraylist of type Grid to store the group points
            ArrayList<ArrayList<Grid>> groupPoints = new ArrayList<>();

            // Storing the Grid locations of each group
            // Iterating through the puzzleGroups map
            for (char c : puzzleGroups.keySet()) {
                groupPoints.add(new ArrayList<>(puzzleGroups.get(c).Grid));
            }

            // Creating a new array of the size of the puzzle
            int[] possibleValues = new int[mPuzzle.length];

            // Storing all the possible values that can come in a cell in possibleValues array
            for (int i = 0; i < mPuzzle.length; i++) {
                possibleValues[i] = i + 1;
            }

            // Solving the puzzle
            if (groupPoints.size() != 0) {
                solvePuzzle(groupPoints, possibleValues, 0);
            }

            // Returns ture if the puzzle is solved
            if (solvedPuzzle()) {
                return true;
            }
            // If puzzle is not solved then
            // Return false
            // Reset the puzzle & choices
            else {
                choices = 0;
                resetPuzzle();
                return false;
            }
        } else {
            return false;
        }
    }

    // Private method to solve the puzzle
    private void solvePuzzle(ArrayList<ArrayList<Grid>> groupPoints, int[] possibleValues, int index) {

        // If all the groups visited then return
        if (index == groupPoints.size()) {
            return;
        }
        // Getting the first point for the current index of groupPoints arraylist
        int x = groupPoints.get(index).get(0).a;
        int y = groupPoints.get(index).get(0).b;

        // Getting the group from the current point
        char group = mPuzzle[x][y].cellGroup;

        // Getting the operator for the current group
        char operator = puzzleGroups.get(group).operator;

        // Getting the outcome for the current group
        int outcome = puzzleGroups.get(group).outcome;

        // Getting the grid locations for the current group
        ArrayList<Grid> Grids = puzzleGroups.get(group).Grid;

        // Storing the number of grids
        int noOfGrids = Grids.size();

        // Storing the list of all possible pairs that can be put into a group in the puzzle
        ArrayList<ArrayList<Integer>> allPossiblePairs = new ArrayList<>();

        // Checking the current operator using switch case
        switch (operator) {
            case add:
                allPossiblePairs = add(possibleValues, noOfGrids, outcome, new ArrayList<>(), allPossiblePairs);
                break;

            case subtract:
                if (noOfGrids <= 2) {
                    allPossiblePairs = subtract(possibleValues, outcome, allPossiblePairs);
                }
                break;

            case multiply:
                allPossiblePairs = multiply(possibleValues, noOfGrids, outcome, new ArrayList<>(), allPossiblePairs);
                break;

            case divide:
                if (noOfGrids <= 2) {
                    allPossiblePairs = division(possibleValues, outcome, allPossiblePairs);
                }
                break;
        }
        // Iterating through the allPossiblePairs
        for (ArrayList<Integer> allPossiblePair : allPossiblePairs) {

            // Setting pairExists to true
            boolean pairExists = true;

            // Iterating through the Grids
            for (int j = 0; j < Grids.size(); j++) {

                // Getting the Grid locations row index
                int row = Grids.get(j).a;

                // Getting the Grid locations column index
                int column = Grids.get(j).b;

                // Getting the cell value from the allPossiblePairs
                int cellValue = allPossiblePair.get(j);

                // Checking whether a conflict exists for the current cell value
                // If yes, then reset the cell values & return false & break
                if (checkConflict(row, column, cellValue)) {
                    resetCellValues(Grids);
                    pairExists = false;
                    break;
                }
                // Inserting the cell value in the puzzle if it's unique
                mPuzzle[row][column].cellValue = cellValue;

                // Incrementing the choices
                choices++;
            }
            // Pair found that fits in the current group without any conflicts
            if (pairExists) {

                // Calling solvePuzzle method to check for the next group & incrementing the index by 1
                solvePuzzle(groupPoints, possibleValues, index + 1);

                // Exiting the loop if the puzzle is solved
                if (solvedPuzzle()) {
                    break;
                }
                // If puzzle is not solved
                // Resetting the cell values & trying a different combination
                else {
                    resetCellValues(Grids);
                }
            }
        }
    }


    // Private recursive method to get the possible pairs for the '+' operator
    private ArrayList<ArrayList<Integer>> add(int[] list, int size, int outcome,
                                              ArrayList<Integer> ans, ArrayList<ArrayList<Integer>> allAns) {

        // Checking whether the pair satisfies the constraints
        if (ans.size() == size) {

            // Initializing sum
            int sum = 0;

            // Iterating through the solution arraylist
            for (int value : ans) {

                // Adding the value to sum
                sum += value;
            }
            // Adding pair if the condition is satisfied
            if (sum == outcome) {
                allAns.add(new ArrayList<>(ans));
            }
            // Returning all the solutions
            return allAns;
        }
        // Trying all the values in the list
        for (int i : list) {

            // Adding a number to the solution
            ans.add(i);

            // Recursively calling the add method
            add(list, size, outcome, ans, allAns);

            // Removes the last element
            ans.remove(ans.size() - 1);
        }
        // Returning all the solutions
        return allAns;
    }

    // Private recursive method to get the possible pairs for the '*' operator
    private ArrayList<ArrayList<Integer>> multiply(int[] list, int size, int outcome,
                                                   ArrayList<Integer> ans, ArrayList<ArrayList<Integer>> allAns) {

        // Checking whether the pair satisfies the constraints
        if (ans.size() == size) {

            // Initializing prod
            int prod = 1;

            // Iterating through the solution arraylist
            for (int value : ans) {
                prod *= value;
            }
            // Adding pair if the condition is satisfied
            if (prod == outcome) {
                allAns.add(new ArrayList<>(ans));
            }
            // Returning all the solutions
            return allAns;
        }
        // Trying all the values in the list
        for (int i : list) {

            // Adding a number to the solution
            ans.add(i);

            // Recursively calling the multiply method
            multiply(list, size, outcome, ans, allAns);

            // Removes the last element
            ans.remove(ans.size() - 1);
        }
        // Returning all the solutions
        return allAns;
    }

    // Private method to get the possible pairs for the "-" operator
    private ArrayList<ArrayList<Integer>> subtract(int[] list, int outcome,
                                                   ArrayList<ArrayList<Integer>> allAns) {
        // Iterating through the list
        for (int i = 0; i < list.length; i++) {
            for (int j = i; j < list.length; j++) {

                // Difference of the numbers = outcome
                // Adding the pair to the solution list & also its reverse form
                if (list[j] - list[i] == outcome) {
                    allAns.add(new ArrayList<>(Arrays.asList(list[i], list[j])));
                    allAns.add(new ArrayList<>(Arrays.asList(list[j], list[i])));
                }
            }
        }
        // Returning all the solutions
        return allAns;
    }

    // Private method to get the possible pairs for the "/" operator
    private ArrayList<ArrayList<Integer>> division(int[] list, int outcome, ArrayList<ArrayList<Integer>> allAns) {
        // Iterating through the list
        for (int i = 0; i < list.length; i++) {
            for (int j = i; j < list.length; j++) {

                // Division of the numbers = outcome
                // Adding the pair to the solution list & also its reverse form
                if (list[j] % list[i] == 0 && list[j] / list[i] == outcome) {
                    allAns.add(new ArrayList<>(Arrays.asList(list[i], list[j])));
                    allAns.add(new ArrayList<>(Arrays.asList(list[j], list[i])));
                }
            }
        }
        // Returning all the solutions
        return allAns;
    }

    // Private method to check conflicts
    private boolean checkConflict(int row, int column, int value) {

        // Checking in the row
        // Iterating through the mPuzzle
        for (int i = 0; i < mPuzzle.length; i++) {
            if (mPuzzle[row][i].cellValue == value) {
                return true;
            }
        }
        // Checking in the column
        // Iterating through the mPuzzle
        for (PuzzleCell[] cells : mPuzzle) {
            if (cells[column].cellValue == value) {
                return true;
            }
        }
        return false;
    }

    // Private method to reset the puzzle
    private void resetPuzzle() {

        // Iterating through the puzzle & setting the cell value to 0
        for (PuzzleCell[] cells : mPuzzle) {
            for (int j = 0; j < mPuzzle.length; j++) {
                cells[j].cellValue = 0;
            }
        }
    }

    // Private method to reset the cell values to zero for the input grid locations
    private void resetCellValues(ArrayList<Grid> Grids) {
        for (Grid loc : Grids) {
            mPuzzle[loc.a][loc.b].cellValue = 0;
        }
    }

    // Public method to print the puzzle as a string
    public String print() {

        // Returns null if puzzle is null
        if (mPuzzle == null) {
            return null;
        }
        // Instantiating a new string builder
        StringBuilder s = new StringBuilder();

        // Iterating through the puzzle
        for (PuzzleCell[] puzzleCells : mPuzzle) {
            for (int j = 0; j < mPuzzle.length; j++) {
                char ch = puzzleCells[j].cellGroup;
                if (puzzleCells[j].cellValue >= 1 && puzzleCells[j].cellValue <= mPuzzle.length) {
                    s.append(puzzleCells[j].cellValue);
                } else if (puzzleGroups != null && puzzleGroups.get(ch) != null && puzzleGroups.get(ch).operator == equal) {
                    s.append(puzzleGroups.get(ch).outcome);
                } else {
                    s.append(puzzleCells[j].cellGroup);
                }
            }
            s.append("\n");
        }

        // Returning the string builder as a string
        return s.toString();
    }

    // Returns the no.of choices made to solve the puzzle
    public int choices() {
        // Returning the choices made while solving the puzzle
        return Math.max(choices, 0);

    }

    // Driver method
    public static void main(String[] args) throws IOException {

        // Instantiating the mathdoku class
        Mathdoku md = new Mathdoku();

        // Instantiating BufferedReader to read the puzzle file
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Asking the user to enter the puzzle file name
        System.out.println("Enter the Mathdoku Puzzle file name: ");

        // Reading the puzzle file name
        String fileName = br.readLine();

        // Checking whether puzzle file name is not empty
        if (!fileName.isEmpty()) {
            try {

                // Reading the file name using BufferedReader into a stream
                br = new BufferedReader(new FileReader(fileName));

                // Calling the loadPuzzle method
                System.out.println("Load Puzzle: " + md.loadPuzzle(br));

                System.out.println("Print: \n" + md.print());

                // Calling the solve method
                System.out.println("Solve: " + md.solve());

                // Calling the choices method
                System.out.println("Choices: " + md.choices());

                // Calling the print method
                System.out.println("Print: \n" + md.print());
            } catch (Exception e) {
                System.out.println("Oops. The file could not be read");
            }
        } else {
            System.out.println("Enter a valid puzzle file name");
        }

    }
}