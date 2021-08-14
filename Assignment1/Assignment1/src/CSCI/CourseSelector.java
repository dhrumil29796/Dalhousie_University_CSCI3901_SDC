package CSCI;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class CourseSelector {

    static ArrayList<String> uniqueCourses = new ArrayList<>();
    static String[] allUniqueCourses;
    static int rows = 0;
    ArrayList<ArrayList<String>> arraylist = new ArrayList<>();

    // Method to read the contents of the file passed
    public int read(String filename) {

        // Checking whether filename is not empty
        if (!filename.isEmpty()) {
            try {
                // Scanner used to read the contents from the file
                // Using space as a delimiter to separate the courses
                Scanner sc = new Scanner(new File(filename)).useDelimiter(" ");

                // Checks whether there is next line in the file
                while (sc.hasNextLine()) {

                    // New arraylist named line is created
                    ArrayList<String> line = new ArrayList<>();

                    // Reading the next line from the file
                    // Converting the read string to uppercase
                    // Splitting the courses read by 1 or more space
                    // Trimming any extra spaces
                    final String[] items = sc.nextLine().trim().toUpperCase(Locale.ROOT).split("\\s+");

                    // Adding items array to the line arraylist
                    Collections.addAll(line, items);
                    // Adding the line arraylist to the main arraylist
                    arraylist.add(line);

                    // Calculates the no.of rows read from the file
                    rows++;

                    // To clear out the 'items' in the array
                    Arrays.fill(items, null);
                }
            } catch (Exception e) {
                return -1;
            }
            // Returns the no.of rows read from the file
            return rows;
        } else {
            return -1;
        }
    }

    public ArrayList<String> recommend(String taken, int support, int numRec) {

        // Doing input validation for the parameters passed
        if (taken.equals(" ") || (support < 0) || (numRec <= 0)) {
            return null;
        }
        // Converting taken string of courses to an array of string
        String[] str = taken.trim().toUpperCase(Locale.ROOT).split("\\s+");

        // Converting string array to an array list
        ArrayList<String> takenList = new ArrayList<>(Arrays.asList(str));

        // An integer array list of support student indices who have taken all courses from the takenList
        ArrayList<Integer> supportIndices = new ArrayList<>();
        boolean exists = true;

        // Iterating through all the students & courses from the main arraylist
        for (int i = 0; i < arraylist.size(); ++i) {
            ArrayList<String> current = arraylist.get(i);
            for (String s : takenList) {
                if (!current.contains(s)) {
                    exists = false;
                    break;
                }
            }
            // Student index(i) added only if he has has taken all the courses from the takenList
            if (exists) {
                supportIndices.add(i);
            }
        }
        // Comparing supportIndices size to the support parameter passed
        if (supportIndices.size() < support) {
            return null;
        } else {
            // Creating an hash map of the courses & the no.of students who have taken it
            // Key is course name & value is course frequency
            HashMap<String, Integer> recommendCourse = new HashMap<>();

            // Iterating through all the supported courses
            for (Integer sIndex : supportIndices) {
                for (String sCourse : arraylist.get(sIndex)) {
                    if (!takenList.contains(sCourse)) {
                        if (recommendCourse.containsKey(sCourse)) {
                            // Updating the frequency of added course
                            recommendCourse.put(sCourse, (recommendCourse.get(sCourse) + 1));
                        } else {
                            // Adding the course in map & setting the frequency to 1
                            recommendCourse.put(sCourse, 1);
                        }
                    }
                }
            }
            // Create a list from elements of HashMap
            List<Map.Entry<String, Integer>> list = new LinkedList<>(recommendCourse.entrySet());

            // Sort the list using comparator
            list.sort(Map.Entry.comparingByValue());

            // The array list to store all final recommended courses
            ArrayList<String> finalCourses = new ArrayList<>();

            // If numRec is greater than all the available recommended courses then display all the courses
            if (numRec >= list.size()) {
                for (HashMap.Entry<String, Integer> course : list) {
                    finalCourses.add(course.getKey());
                }
            } else {
                // If numRec is less than the total available recommended courses
                int cIndex;
                for (cIndex = 0; cIndex < numRec; ++cIndex) {
                    finalCourses.add(list.get(cIndex).getKey());
                }
                int prevCourseFreq = list.get(cIndex - 1).getValue();
                for (cIndex = numRec; cIndex < list.size(); ++cIndex) {
                    if (prevCourseFreq == list.get(cIndex).getValue()) {
                        finalCourses.add(list.get(cIndex).getKey());
                    }
                }
            }
            // Returning all recommended courses
            return finalCourses;
        }
    }

    /*It is assumed that the courses entered are not duplicate as it is not logical while making a pairwise popularity matrix*/
    public boolean showCommon(String courses) {

        // Returning false if the courses entered is null
        if (courses == null) {
            return false;
        } else {
            // Converting the courses passed to uppercase
            // Splitting the courses read by 1 or more space
            // Trimming any extra spaces
            String[] courseArray = courses.trim().toUpperCase(Locale.ROOT).split("\\s+");

            // Returning false if the no.of courses is less than 2
            if (courseArray.length <= 0) {
                return false;
            }
            // Getting the n x n pairwise popularity matrix of all students courses
            int[][] coursesMatrix = matrix(courseArray);

            // Printing the n x n matrix
            for (int a = 0; a < coursesMatrix.length; a++) {
                System.out.print(courseArray[a] + "\t");
                for (int b = 0; b < coursesMatrix.length; b++) {
                    System.out.print(coursesMatrix[a][b] + "\t");
                }
                System.out.println(); // Change line on console as row comes to end in matrix
            }
            return true;
        }
    }

    public boolean showCommonAll(String filename) {

        // Checking whether filename is not empty
        if (!filename.isEmpty()) {
            // Getting sorted arraylist without duplicates
            uniqueCourses = removeDuplicate();

            // ArrayList to Array Conversion
            allUniqueCourses = uniqueCourses.toArray(new String[uniqueCourses.size()]);

            // Getting the n x n pairwise popularity matrix of all students courses
            int[][] coursesMatrix = matrix(allUniqueCourses);

            // Initializing writer to null
            PrintWriter writer = null;
            try {
                // Checking whether filename is not null
                if (filename != null) {
                    writer = new PrintWriter(filename);
                }
                // Writing the n x n matrix in the file
                for (int a = 0; a < coursesMatrix.length; a++) {
                    writer.print(allUniqueCourses[a] + "\t");
                    for (int b = 0; b < coursesMatrix.length; b++) {
                        writer.print(coursesMatrix[a][b] + "\t");
                    }
                    writer.println();
                }
                // Flushing the writer object
                writer.flush();

                return true;
            } catch (Exception e) {  // Catches any exception faced
                return false;
            }
        } else {
            return false;
        }
    }


    // Method to calculate the n x n matrix
    private int[][] matrix(String[] courseList) {

        // Creates a matrix of n x n, where n is the length of courseList array
        // Similar courses are not paired, so they will always contain 0
        // Each & every cell of the matrix gives the no.of students who have taken both courses
        int[][] cMatrix = new int[courseList.length][courseList.length];

        // Initializing the entire matrix to 0 by using Arrays.fill() method
        for (int[] value : cMatrix) {
            Arrays.fill(value, 0);
        }

        // Iterating through courseList array
        // The c1 holds the first courses index for each course list read from the file
        // Using pre-increment in for loop
        for (int c1 = 0; c1 < courseList.length; ++c1) {

            // The c1Name holds the name of the current course
            String c1Name = courseList[c1];

            // Iterating through the list of students read from the file using for each loop
            // The list is a courses list which is taken by the current student
            for (ArrayList<String> list : arraylist) {

                // Checking if the current student has taken the c1Name course
                if (list.contains(c1Name)) {

                    // Iterating through remaining courseList array
                    // The c2 holds the second courses index for each course list read from the file
                    // Using pre-increment in for loop & starting c2 form (c1+1) index
                    for (int c2 = (c1 + 1); c2 < courseList.length; ++c2) {

                        // The c2Name holds the name of the next course after the current course
                        String c2Name = courseList[c2];

                        // Checking whether c2Name course has been taken by the current student
                        if (list.contains(c2Name)) {

                            // The current student has taken both the course
                            // Diagonally adding 1 to both pairs in the matrix
                            cMatrix[c1][c2] = cMatrix[c1][c2] + 1;
                            cMatrix[c2][c1] = cMatrix[c2][c1] + 1;
                        }
                    }
                }
            }
        }
        // Returning an n x n pairwise popularity matrix
        return cMatrix;
    }

    // Method to remove the duplicate values from the main 2D arraylist
    private ArrayList<String> removeDuplicate() {

        // Creating a new arraylist
        ArrayList<String> uCourses = new ArrayList<>();

        // Iterating through the main 2D arraylist
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < arraylist.get(i).size(); j++) {

                // Checking whether the uCourses arraylist contains the current element from the main 2D arraylist
                if (!uCourses.contains(arraylist.get(i).get(j))) {

                    // Adding the unique current element to uCourses
                    uCourses.add(arraylist.get(i).get(j));
                }
            }
            // Sorting the uCourses lexicographically
            Collections.sort(uCourses);
        }
        // Returning unique set of courses
        return uCourses;
    }
}