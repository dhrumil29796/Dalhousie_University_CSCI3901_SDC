import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

// Public class of Government
// This class interacts with the Database
// It receives the locally stored data from each mobile device on synchronization
// Implementing Singleton Design Pattern for the Government Class
public class Government {

    // Global variables
    final String DATABASE = "database";
    final String USER_NAME = "user";
    final String PASSWORD = "password";
    String initiator;
    Properties govProperties = new Properties();
    Map<String, String> config2 = new HashMap<>();
    ArrayList<String> testList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<>();
    Contact contact = null;
    Document dom1;

    // Private static Government reference for implementing Singleton class design pattern
    private static Government government;

    // Singleton class design pattern is implemented
    // Public static method to get only a single instance of Government class
    public static Government getInstance(String fileName) {

        try {
            // Checks if the instance is null
            // Creates a new instance of Government class by calling the public parameterized constructor
            // Passing the config file to the constructor
            if (government == null) {
                government = new Government(fileName);
            }
            // If an instance already exists
            // Returns that already created instance
            return government;
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public static method to reset the database
    // This method drops the existing tables in the database & also creates the tables
    // This method needs to be run before & after running any test cases
    public static boolean resetDatabase() {

        try {
            // Database variables
            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

            // Instantiating the properties variable used to read the input file
            final Properties governmentProperties = new Properties();

            // Declaring connection variable
            Connection connection = null;

            // Declaring the statement variable
            Statement statement = null;

            // Loading the configFile contents in govProperties
            // Using try with resources as it implements AutoCloseable interface
            try (FileReader reader = new FileReader("g1.properties")) {
                governmentProperties.load(reader);
            }

            // Establish connection between Java program and the Database
            try {
                Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return false;
            }

            try {
                // Database variables
                final String CSID = governmentProperties.getProperty("user");
                final String BANNER_ID = governmentProperties.getProperty("password");
                final String DATABASE_PATH = "jdbc:mysql://db.cs.dal.ca:3306/" + CSID;

                // Getting the connection
                connection = DriverManager.getConnection(DATABASE_PATH, CSID, BANNER_ID);

                // Creating a statement
                statement = connection.createStatement();

                // Queries to drop the tables in the database
                statement.execute("DROP TABLE IF EXISTS Contact;");
                statement.execute("DROP TABLE IF EXISTS MobileDevice_TestResult;");
                statement.execute("DROP TABLE IF EXISTS MobileDevice;");
                statement.execute("DROP TABLE IF EXISTS TestResult;");

                // Queries to create the tables in the database
                statement.execute("""
                        CREATE TABLE MobileDevice (
                        \tMobileDeviceHash VARCHAR(100) PRIMARY KEY
                        );""");
                statement.execute("""
                        CREATE TABLE TestResult (
                        \tTestHash VARCHAR(100) PRIMARY KEY,
                        \tTest_Date DATE,
                        \tTest_Result BOOLEAN
                        );""");
                statement.execute("""
                        CREATE TABLE MobileDevice_TestResult (
                        \tMobileDeviceHash VARCHAR(100) NOT NULL,
                        \tTestHash VARCHAR(100) NOT NULL,
                        \tPRIMARY KEY (MobileDeviceHash,TestHash),
                        \tFOREIGN KEY (MobileDeviceHash) REFERENCES MobileDevice(MobileDeviceHash),
                        \tFOREIGN KEY (TestHash) REFERENCES TestResult(TestHash)
                        );""");
                statement.execute("""
                        CREATE TABLE Contact (
                        \tContactID INT PRIMARY KEY AUTO_INCREMENT,
                        \tMobileDeviceHash VARCHAR(100) NOT NULL,
                        \tContactDeviceHash VARCHAR(100),
                        \tContact_Date DATE NOT NULL,
                        \tContact_Duration INT NOT NULL,
                            Contact_Notified BOOLEAN,
                        \tFOREIGN KEY (MobileDeviceHash) REFERENCES MobileDevice(MobileDeviceHash),
                        \tFOREIGN KEY (ContactDeviceHash) REFERENCES MobileDevice(MobileDeviceHash)
                        );""");

                // Returning true if the database is reset successfully
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            // Running the finally block to close the connection
            finally {

                // Checking the statement & then closing it
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
                // Checking the connection & then closing it
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        // Catching any thrown exception
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }

    }

    // Private constructor of Government class to implement Singleton design pattern
    // Validates & reads the config file
    // The config file contains the configuration parameters to connect to the Dal Database
    private Government(String configFile) {

        try {
            // Validating the configFile name
            // Config file cannot be null or empty
            if (configFile == null || configFile.trim().isEmpty()) {
                throw new IllegalArgumentException("The configFile name passed cannot be null/empty.");
            }

/*        // Validating the extension of configFile
        if (configFile.lastIndexOf(".") != -1 && configFile.lastIndexOf(".") != 0) {
            if (!(configFile.substring(configFile.lastIndexOf(".") + 1)).equals("properties")) {
                throw new IllegalArgumentException("Enter a file with .properties extension only");
            }
        }*/

            // Making an instance of the configFile
            File file = new File(configFile);

            // Validating whether file exists & whether it is a file
            if (!file.exists() || !file.isFile()) {
                throw new FileNotFoundException("The configFile passed does not exist/is not a file.");
            }

            // Loading the configFile contents in govProperties
            // Using try with resources as it implements AutoCloseable interface
            try (FileReader reader = new FileReader(configFile)) {
                govProperties.load(reader);
            }

            // Verifying the database name of the configFile
            if (!govProperties.containsKey(DATABASE)) {
                throw new RuntimeException("The database name is not present in the mobile device configuration file.");
            }

            // Verifying the user name of the configFile
            if (!govProperties.containsKey(USER_NAME)) {
                throw new RuntimeException("The user name is not present in the mobile device configuration file.");
            }

            // Verifying the password of the configFile
            if (!govProperties.containsKey(PASSWORD)) {
                throw new RuntimeException("The password is not present in the mobile device configuration file.");
            }

            // Verifying the database value of the configFile
            if (govProperties.getProperty(DATABASE) == null || govProperties.getProperty(DATABASE).trim().isEmpty()) {
                throw new RuntimeException("The database name passed cannot be null/empty.");
            }

            // Verifying the user name value of the configFile
            if (govProperties.getProperty(USER_NAME) == null || govProperties.getProperty(USER_NAME).trim().isEmpty()) {
                throw new RuntimeException("The user name passed cannot be null/empty.");
            }

            // Verifying the password value of the configFile
            if (govProperties.getProperty(PASSWORD) == null || govProperties.getProperty(PASSWORD).trim().isEmpty()) {
                throw new RuntimeException("The password passed cannot be null/empty.");
            }

            // Mapping the contents in a hash map
            config2.put(DATABASE, govProperties.getProperty(DATABASE));
            config2.put(USER_NAME, govProperties.getProperty(USER_NAME));
            config2.put(PASSWORD, govProperties.getProperty(PASSWORD));
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Private method to read the XML that is passed to the mobileContact method in the form of a String
    // This method is called from the mobileContact method of the Government class
    private boolean readXML(String xml) {

        try {
            // Validating the xml string passed containing the contactInfo
            if (xml == null || xml.trim().isEmpty()) {
                throw new IllegalArgumentException("The passed string containing contactInfo cannot be null/empty.");
            }

            // Instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // Using DocumentBuilderFactory to get an instance of DocumentBuilder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Parsing the passed String into the document object DOM
            dom1 = db.parse(new InputSource(new StringReader(xml)));

            // Normalizing the document object DOM
            dom1.getDocumentElement().normalize();

            // Getting the contact list using NodeList
            NodeList nl1 = dom1.getElementsByTagName("contact");
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node = nl1.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Creating a new Contact object
                    contact = new Contact();

                    // Setting the contacts after reading them from the contactInfo passed
                    contact.setIndividual(element.getElementsByTagName("individual").item(0).getTextContent());
                    contact.setDate(LocalDate.parse(element.getElementsByTagName("date").item(0).getTextContent()));
                    contact.setDuration(Integer.parseInt(element.getElementsByTagName("duration").item(0).getTextContent()));

                    // Adding contact to the contact_list
                    contactList.add(contact);
                }
            }

            // Getting the test hashes using NodeList
            NodeList nl2 = dom1.getElementsByTagName("test_hash");
            for (int i = 0; i < nl2.getLength(); i++) {
                Node node = nl2.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    // Reading the test hashes from the contactInfo
                    String test = node.getTextContent();

                    // Adding test hashes to the testList
                    testList.add(test);
                }
            }
            // Returns true if XML read successfully
            return true;
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public method of mobileContact
    // Records the initiators contactsInfo & stores it in the database
    public boolean mobileContact(String initiator, String contactInfo) {

        try {

            // Database variables
            final String CSID = govProperties.getProperty(USER_NAME);
            final String BANNER_ID = govProperties.getProperty(PASSWORD);
            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            final String DATABASE_PATH = "jdbc:mysql://db.cs.dal.ca:3306/" + CSID;

            // List of contactID's retrieved from the database
            List<Integer> contactIDList = new ArrayList<>();

            // Declaring connection variable
            Connection connection = null;

            // Declaring the statement variable
            Statement statement = null;

            // Declaring result set for for government
            ResultSet govResultSet;

            // Validating the passed initiator
            if (initiator == null || initiator.trim().isEmpty()) {
                throw new IllegalArgumentException("The passed initiator cannot be null/empty.");
            }

            // Validating the passed contactInfo string
            if (contactInfo == null || contactInfo.trim().isEmpty()) {
                throw new IllegalArgumentException("The passed contactInfo string cannot be null/empty.");
            }

            // Assigning the initiator deviceHash whose contacts are read & will be stored in the database
            this.initiator = initiator;

            // Calling the readXml method
            // Validating whether readXml method passed ture/false
            // If passed then we move forward with the mobileContact method's operations
            if (readXML(contactInfo)) {

                // Establish connection between Java program and the Database
                try {
                    Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    return false;
                }

                // Connecting to the Dal Database
                try {
                    // Getting the connection
                    connection = DriverManager.getConnection(DATABASE_PATH, CSID, BANNER_ID);

                    // Creating a statement
                    statement = connection.createStatement();

                    // Inserting initiator in MobileDevice table in the database
                    statement.execute("INSERT IGNORE INTO MobileDevice VALUES (\"" + initiator + "\"); \n");

                    // Printing & storing the arraylist contactList in the database
                    for (Contact contact : contactList) {

                        // Inserting the individual in MobileDevice table in the database if not present
                        statement.execute("INSERT IGNORE INTO MobileDevice VALUES (\"" + contact.getIndividual() + "\"); \n");

                        // Inserting the values of contactList in Contact table in the database
                        // By default setting Contact_Notified false as the contact hasn't been notified yet
                        statement.execute("INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES" +
                                " (\"" + initiator + "\", \"" + contact.getIndividual() + "\", \"" + contact.getDate() + "\", \"" + contact.getDuration() + " \", " + false + "); \n");
                    }

                    // Printing & Storing the testList in the database
                    for (String test : testList) {

                        // Inserting initiator in MobileDevice table in the database
                        statement.execute("INSERT IGNORE INTO MobileDevice VALUES (\"" + initiator + "\"); \n");

                        // Inserting the individual & its testHashes into the MobileDevice_TestResult table in the database
                        statement.execute("INSERT IGNORE INTO MobileDevice_TestResult(MobileDeviceHash, TestHash) VALUES (\"" + initiator + "\", \"" + test + "\"); \n");
                    }

                    // Storing the query in a String
                    final String query1 =
                            "SELECT\n" +
                                    "   \tco.ContactID\n" +
                                    "FROM\n" +
                                    "    Contact AS co, MobileDevice_TestResult AS dtr, TestResult AS tr\n" +
                                    "WHERE\n" +
                                    "   \tco.ContactDeviceHash = dtr.MobileDeviceHash AND\n" +
                                    "    dtr.TestHash = tr.TestHash AND\n" +
                                    "    co.MobileDeviceHash = \"" + initiator + "\" AND\n" +
                                    "    ABS(DATEDIFF(tr.Test_Date,co.Contact_Date)) BETWEEN 0 AND 14 AND \n" +
                                    "    tr.Test_Result = true AND\n" +
                                    "    co.Contact_Notified = false;";

                    // Storing the result in govResultSet
                    govResultSet = statement.executeQuery(query1);

                    // Storing the ContactID from the govResultSet in contactIDList
                    while (govResultSet.next()) {
                        contactIDList.add(govResultSet.getInt("ContactID"));
                    }

                    // If nothing returned then return false
                    if (contactIDList.size() == 0) {
                        return false;
                    } else {
                        // Iterating through the contactIDList that was retrieved from the select query
                        for (Integer integer : contactIDList) {
                            statement.execute("UPDATE Contact SET Contact_Notified = true WHERE ContactID = \"" + integer + "\";");
                        }
                        // Returning true if database updated successfully
                        return true;
                    }
                }
                // Catching any thrown exception
                catch (Exception e) {

                    // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
                    throw new RuntimeException(e.getMessage());
                }
                // Running the finally block to close the connection
                finally {

                    // Checking the statement & then closing it
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    // Checking the connection & then closing it
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            } else {
                System.out.println("The passed contactInfo XMl is not read successfully.");
                return false;
            }
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Private method to get a LocalDate from the noOfDays
    // It returns only the date without time and time zone
    // I am including both the start & end date in the calculation from 1st Jan 2021
    // The date is calculated since 1st Jan 2021
    // The noOfDays passed could be 0, the returned date will be 2021/01/01
    private LocalDate getLocalDate(int noOfDays) {
        try {
            // The default start date is set to 1st Jan 2021
            LocalDate startDate = LocalDate.parse("2021-01-01");

            // Adding the noOfDays to the startDate
            LocalDate newDate = startDate.plusDays(noOfDays);

            // Returning the newDate
            return newDate;
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public method to record the test result by the government
    // Storing the testHashes passed along with the date & result
    // Storing the records read into the database
    public void recordTestResult(String testHash, int date, boolean result) {

        try {
            // Database variables
            final String CSID = govProperties.getProperty(USER_NAME);
            final String BANNER_ID = govProperties.getProperty(PASSWORD);
            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            final String DATABASE_PATH = "jdbc:mysql://db.cs.dal.ca:3306/" + CSID;

            // Declaring connection variable
            Connection connection = null;

            // Declaring statement variable
            Statement statement = null;

            // Validating the testHash string passed
            if (testHash == null || testHash.trim().isEmpty()) {
                throw new IllegalArgumentException("The testHash string passed cannot be null/empty.");
            }

            // Validating the date passed
            // The date passed could be 0 as I ma considering the days since 1st Jan 2021
            // So if 0 is passed as noOfDays then, date returned will be 2021/01/01
            if (date < 0) {
                throw new IllegalArgumentException("The date passed cannot be nul/empty.");
            }

            // Validating the result passed for the testHash
            if (!(result == true || result == false)) {
                throw new IllegalArgumentException("The passed result cannot be anything other than true/false.");
            }

            // Creating an instance of Message Digest of SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Storing the SHA-256 hash of testHash into a string tHash
            String tHash = String.format("%064x", new BigInteger(1, md.digest(testHash.getBytes(StandardCharsets.UTF_8)))) + testHash;

            // Establish connection between Java program and the Database
            try {
                Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            // Connecting to the Dal Database
            try {
                // Getting the connection
                connection = DriverManager.getConnection(DATABASE_PATH, CSID, BANNER_ID);

                // Creating a statement
                statement = connection.createStatement();

                // Inserting the testHash, date & result in the database
                statement.execute("INSERT IGNORE INTO TestResult VALUES (\"" + tHash + "\", \"" + getLocalDate(date) + "\", " + result + "); \n");

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            // Running the finally block to close the connection
            finally {

                // Checking the statement & then closing it
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
                // Checking the connection & then closing it
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Private method contactSearch to search the contact from allIndividualPairs
    private ArrayList<String> contactSearch(String person1,
                                            String person2, ArrayList<ArrayList<String>> allContactPairs) {

        try {
            // Iterating through the allContactPairs
            // This is ideally the allIndividualPairs
            for (ArrayList<String> pair : allContactPairs) {

                // Comparing the current pair with the passed person1 & person2 in both order
                // Normal & reverse order
                if (pair.get(0).equals(person1) && pair.get(1).equals(person2)) {
                    return pair;
                } else if (pair.get(1).equals(person1) && pair.get(0).equals(person2)) {
                    return pair;
                }
            }
            // Returning null if no contact pair found
            return null;
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public method to find the gatherings among the connections on a particular date
    public int findGatherings(int date, int minSize, int minTime, float density) {

        try {
            // Database variables
            final String CSID = govProperties.getProperty(USER_NAME);
            final String BANNER_ID = govProperties.getProperty(PASSWORD);
            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            final String DATABASE_PATH = "jdbc:mysql://db.cs.dal.ca:3306/" + CSID;

            // ArrayList of ArrayList to store all the pairs
            ArrayList<ArrayList<String>> allIndividualPairs = new ArrayList<>();

            // Declaring connection variable
            Connection connection = null;

            // Declaring the statement variable
            Statement statement = null;

            // Declaring resultSet variable
            ResultSet gatheringResultSet;

            // Validating the date passed
            // The date passed could be 0 as I ma considering the days since 1st Jan 2021
            // So if 0 is passed as noOfDays then, date returned will be 2021/01/01
            if (date < 0) {
                throw new IllegalArgumentException("The passed date is not valid.");
            }

            // Validating the minSize passed
            // The minSize of the gathering should be >=2
            if (minSize < 2) {
                throw new IllegalArgumentException("The passed minSize is not valid.");
            }

            // Validating the minTime passed
            // The minTime should be >=0
            if (minTime <= 0) {
                throw new IllegalArgumentException("The passed minTime is not valid.");
            }

            // Validating the density passed
            // The density should always be between 0 & 1, both inclusive
            if (density < 0f || density > 1f) {
                throw new IllegalArgumentException("The passed density is not valid");
            }

            // Establish connection between Java program and the Database
            try {
                Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            // Connecting to the Dal Database
            try {
                connection = DriverManager.getConnection(DATABASE_PATH, CSID, BANNER_ID);

                // Creating a statement
                statement = connection.createStatement();

                // Storing the query in a String
                final String query1 =
                        "SELECT\n" +
                                "   \tco.MobileDeviceHash,\n" +
                                "    co.ContactDeviceHash,\n" +
                                "    SUM(co.Contact_Duration) AS Total\n" +
                                "FROM\n" +
                                "   \tContact AS co\n" +
                                "WHERE\n" +
                                "   \tco.Contact_Date = \"" + getLocalDate(date) + "\"\n" +
                                "GROUP BY\n" +
                                "   \tco.MobileDeviceHash,co.ContactDeviceHash\n" +
                                "HAVING\n" +
                                "   \tSUM(co.Contact_Duration) >=\"" + minTime + "\";";

                // Storing the result in govResultSet
                gatheringResultSet = statement.executeQuery(query1);

                // Iterating through the gatheringResultSet
                while (gatheringResultSet.next()) {

                    // Creating an arraylist to store normal pairs
                    ArrayList<String> normPair = new ArrayList<>();

                    // Creating an arraylist to store reverse pairs for the read pair
                    ArrayList<String> revPair = new ArrayList<>();

                    // Reading the individual pair from gatheringResultSet
                    // Adding the read pair to normPair arraylist
                    normPair.add(gatheringResultSet.getString("MobileDeviceHash"));
                    normPair.add(gatheringResultSet.getString("ContactDeviceHash"));

                    // Reading the individual pair from gatheringResultSet
                    // Adding the read pair to revPair arraylist
                    revPair.add(gatheringResultSet.getString("ContactDeviceHash"));
                    revPair.add(gatheringResultSet.getString("MobileDeviceHash"));

                    // Checking if allIndividualPairs does not contain the pair or revPair
                    if (!allIndividualPairs.contains(normPair) && !allIndividualPairs.contains(revPair)) {

                        // Adding the normPair to the allIndividualPairs
                        allIndividualPairs.add(normPair);
                    }
                }
                // Moving forward if allIndividualPairs contains
                if (allIndividualPairs.size() > 0) {

                    // Creating an arraylist of already seenPairs from the allIndividualPairs
                    ArrayList<ArrayList<String>> seenPairs = new ArrayList<>();

                    // Setting gatheringCount to 0
                    int gatheringCount = 0;

                    // Iterating through the allIndividualPairs arraylist
                    for (ArrayList<String> pair : allIndividualPairs) {

                        // Checking if seenPairs does not contain the current pair from allIndividualPairs
                        if (!seenPairs.contains(pair)) {

                            // Initializing an iterator to 0
                            int iterator = 0;

                            // A boolean flag to track the searching of pairs
                            // Setting flag as true
                            boolean flag = true;

                            // Storing the person1 & person2 of current pair in p1 & p2 variables
                            String p1 = pair.get(0);
                            String p2 = pair.get(1);

                            // Creating a hashmap of arraylist to store the gatherings
                            // Using a hashmap is because I can refer this & see if the gathering is present than don't report it again
                            HashMap<Integer, ArrayList<String>> gathering = new HashMap<>();

                            // Creating an arraylist of individuals
                            // This arraylist will store the individuals of the current pair
                            ArrayList<String> individuals = new ArrayList<>();

                            // Mapping the current pair against an integer
                            // Starting the key with 0 and keep on incrementing as new pairs are being added
                            // This hashmap will store the pairs of a gathering as value against integer key
                            gathering.put(0, pair);

                            // Adding the current pair to the individuals arraylist
                            // This arraylist contains all the individual pairs
                            individuals.add(p1);
                            individuals.add(p2);

                            // Looping using the flag
                            // If it is tue we keep iterating & stop when it is set to false
                            while (flag) {

                                // Getting arraylist form gathering hashmap & storing it in a new arraylist
                                // Creating gatheringPairs as we iterate through the gathering hashmap
                                ArrayList<String> gatheringPairs = gathering.get(iterator);

                                // Getting each pair form the gatheringPairs & storing it in variables
                                String gP1 = gatheringPairs.get(0);
                                String gP2 = gatheringPairs.get(1);

                                // Iterating through the allIndividualPairs
                                for (ArrayList<String> individualPair : allIndividualPairs) {

                                    // Checking if the gathering & seenPairs contains the current individualPair
                                    if (!gathering.containsValue(individualPair) && !seenPairs.contains(individualPair)) {

                                        // Declaring variables to store the individuals
                                        String a, b;

                                        // Comparing the 1st entry of individualPair with the gP1 from gatheringPairs
                                        if (individualPair.get(0).equals(gP1)) {
                                            a = individualPair.get(1);
                                            b = gP2;

                                            // Comparing the 2nd entry of individualPair with the gP1 from gatheringPairs
                                        } else if (individualPair.get(1).equals(gP1)) {
                                            a = individualPair.get(0);
                                            b = gP2;

                                            // Comparing the 1st entry of individualPair with the gP2 from gatheringPairs
                                        } else if (individualPair.get(0).equals(gP2)) {
                                            a = individualPair.get(1);
                                            b = gP1;

                                            // Comparing the 2nd entry of individualPair with the gP2 from gatheringPairs
                                        } else if (individualPair.get(1).equals(gP2)) {
                                            a = individualPair.get(0);
                                            b = gP1;

                                            // If not found then set a & b to empty strings
                                        } else {
                                            a = "";
                                            b = "";
                                        }
                                        // Checking if the strings a & b are not empty
                                        // Then we move forward with searching the allIndividualPairs
                                        if (!a.equals("") && !b.equals("")) {

                                            // Checking if individuals does not contain string a
                                            // a is the 1st individual that we go from the individualPairs derived from allIndividualPairs hashmap
                                            if (!individuals.contains(a)) {

                                                // Creating an arraylist to store any newPair's if found
                                                // Calling the contactSearch method to find any new pairs from the allIndividualPairs
                                                ArrayList<String> newPair = contactSearch(a, b, allIndividualPairs);

                                                // If no new pair is found then we add the 1st individual i.e. string a in this case
                                                if (newPair != null) {

                                                    // String a is added to the individuals arraylist
                                                    // a is the 1st individual that we go from the individualPairs derived from allIndividualPairs hashmap
                                                    individuals.add(a);

                                                    // If gathering does not contain the individualPair
                                                    // Then we map that individualPair in the gathering
                                                    if (!gathering.containsValue(individualPair)) {

                                                        // Getting the gatheringSize
                                                        int gatheringSize = gathering.size();

                                                        // Mapping the individualPair to the gathering hashmap
                                                        gathering.put(gatheringSize, individualPair);
                                                    }

                                                    // If new pair is found then we add the new pair to the gathering
                                                    if (!gathering.containsValue(newPair)) {

                                                        // Getting the gathering size
                                                        int gatheringSize = gathering.size();

                                                        // Mapping the individualPair to the gathering hashmap
                                                        gathering.put(gatheringSize, newPair);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                // Incrementing the iterator
                                iterator++;

                                // Storing the size of the gathering
                                int gatheringSize = gathering.size();

                                // Setting the flag as false if the iterator size is greater then the size of gathering
                                // Exhausted searching the gatherings formed while iterating through the allIndividualPairs
                                if (iterator >= gatheringSize) {

                                    // Setting the flag as false to come out of the while loop
                                    // This is set to false only when we have iterated the entire gathering hashmap
                                    flag = false;
                                }
                            }
                            // If the gathering size is > 0 & individuals arraylist of the gathering >= passed minSize
                            // Then we consider this as a gathering & moe forward to calculations
                            if (gathering.size() > 0 && individuals.size() >= minSize) {

                                // Getting the no.of individuals in the gathering
                                int n = individuals.size();

                                // Getting the no.of pairs of individuals
                                int c = gathering.size();

                                // Calculating m by using the below formula
                                int m = n * (n - 1) / 2;

                                // Calculating d by using below formula
                                float d = (float) c / m;

                                // Comparing d with density passed
                                // Density & d both should be between 0 & 1
                                if (d >= density && d >= 0 && d <= 1) {

                                    // Increasing the gatheringCount as we consider the gathering
                                    gatheringCount++;

                                    // Checking whether the seenPairs contains the gathering
                                    // If yes then we don't consider that gathering anymore
                                    // Storing the gathering size
                                    int gatheringSize = gathering.size();

                                    // Iterating through the gathering hashmap
                                    for (int i = 0; i < gatheringSize; i++) {

                                        // Creating a new arraylist to store the pairs considered in the gatherings
                                        ArrayList<String> consideredGatherings = gathering.get(i);

                                        // Checking if the seenPairs does not contain the consideredGatherings pair
                                        // If not present then we add it in the seenPairs 2D arraylist
                                        if (!seenPairs.contains(consideredGatherings)) {
                                            seenPairs.add(consideredGatherings);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Returning the gatheringCount as the no.of gatherings
                    return gatheringCount;
                } else {

                    // Returning 0 if allIndividualPairs does not contain any pairs for the given date and duration
                    return 0;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error faced in finding the gatherings");
            }

            // Running the finally block to close the connection
            finally {

                // Checking the statement & then closing it
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
                // Checking the connection & then closing it
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }
}
