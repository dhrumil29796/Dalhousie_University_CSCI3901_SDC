import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

// Public class Test
public class Test {

    // Driver method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        final String CSID = "drshah";
        final String BANNER_ID = "B00870600";
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DATABASE_PATH = "jdbc:mysql://db.cs.dal.ca:3306/csci3901";
        String start = null;
        String end = null;
        String fileName = null;

        try {
            // Reading start date from user
            System.out.println("Enter a start date of format YYYY-MM-DD:");
            start = sc.nextLine();

            // Validating the start date
            if (start == null || start.trim().isEmpty()) {
                System.out.println("Enter a valid start date");
                System.exit(0);
            }

            // Parsing start date using LocalDate
            // Throws an exception for incorrect date format
            LocalDate.parse(start);

            // Reading end date from user
            System.out.println("Enter an end date of format YYYY-MM-DD:");
            end = sc.nextLine();

            // Validating the end date
            if (end == null || end.trim().isEmpty()) {
                System.out.println("Enter a valid end date");
                System.exit(0);
            }

            // Parsing end date using LocalDate
            // Throws an exception for incorrect date format
            LocalDate.parse(end);

            // Comparing start date and end date
            if (start.compareTo(end) > 0) {
                System.out.println("Start date cannot be greater than end date");
                System.exit(0);
            }

            // Reading file name
            System.out.println("Enter the name of the file wih .xml extension to store output:");
            fileName = sc.nextLine();

            // Validating the file name
            if (fileName == null || fileName.trim().isEmpty() || !fileName.contains(".")) {
                System.out.println("Enter a valid file name");
                System.exit(0);
            }

            // Validating the file name extension
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                if (!(fileName.substring(fileName.lastIndexOf(".") + 1)).equals("xml")) {
                    System.out.println("Enter a file with .xml extension only");
                    System.exit(0);
                }
            }

            // Catching any exception in date input
        } catch (DateTimeException d) {
            System.out.println("Date entered is invalid. Please enter a valid date");
            System.exit(0);
        }

        // Creating an instance of DocumentGenerator class
        DocumentGenerator doc = new DocumentGenerator();

        // Calling the loadData method in DocumentGenerator class
        if (doc.loadData(JDBC_DRIVER, DATABASE_PATH, CSID, BANNER_ID, start, end, fileName)) {
            System.out.println("Data loaded successfully");
        } else {
            System.out.println("Error occurred in loading data");
            System.exit(0);

        }
    }
}
