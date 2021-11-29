import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;

// Public class of MobileDevice
// All the data of a particular mobile device has the scope of this class
// The information is passed to the Government class as a contactInfo string in an XML format
// The synchronize method then returns a true or false if the current mobileDevice has come in contact with someone positive
public class MobileDevice {

    // Global variables
    private final String ADDRESS = "address";
    private final String DEVICE_NAME = "deviceName";
    private String deviceHash;
    String contactInfo;
    Properties deviceProperties = new Properties();
    Map<String, String> config1 = new HashMap<>();
    List<String> testHashList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<>();
    Government contactTracer;
    File deviceFile = null;

    // Private method to get the SHA-256 hash of the device's configurations
    private void getDeviceHash() {

        try {
            // Appending the deviceName and device address
            String deviceConfig = deviceProperties.getProperty(ADDRESS) + deviceProperties.getProperty(DEVICE_NAME);

            // Instantiating MessageDigest & getting an instance of SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Storing the hash of the device in global variable deviceHash
            // Using BigInteger and MessageDigest
            // Formatting the string in %064x form
            deviceHash = String.format("%064x", new BigInteger(1, md.digest(deviceConfig.getBytes(StandardCharsets.UTF_8))));
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public method to get the deviceHash
    // As deviceHash is a private string so it directly can't be accessed from outside
    // The deviceHash can only be read, but not modified from outside
    public String getHash() {
        return deviceHash;
    }

    // Constructor of MobileDevice class
    // Validates & reads the config file of each device
    // Then calls the hash method to get Device's hash
    public MobileDevice(String configFile, Government contactTracer) {

        try {
            // Government object cannot be null
            if (contactTracer == null) {
                throw new IllegalArgumentException("Government object contactTracer cannot be null.");
            }
            // Assigning the Government object
            this.contactTracer = contactTracer;

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

            // Making a new file instance of the configFile
            File file = new File(configFile);

            // Validating whether file exists & whether it is a file
            if (!file.exists() || !file.isFile()) {
                throw new FileNotFoundException("The configFile passed does not exist/is not a file.");
            }

            // Loading the configFile contents using FileReader
            // Using try with resources as it implements AutoCloseable interface
            try (FileReader reader = new FileReader(file)) {
                deviceProperties.load(reader);
            }

            // Verifying the device address of the configFile
            if (!deviceProperties.containsKey(ADDRESS)) {
                throw new RuntimeException("The device address is not present in the mobile device configuration file.");
            }

            // Verifying the device name of the configFile
            if (!deviceProperties.containsKey(DEVICE_NAME)) {
                throw new RuntimeException("The deviceName is not present in the mobile device configuration file.");
            }

            // Verifying the device address value of the configFile
            if (deviceProperties.getProperty(ADDRESS) == null || deviceProperties.getProperty(ADDRESS).trim().isEmpty()) {
                throw new RuntimeException("The device address passed cannot be null/empty.");
            }

            // Verifying the device name value of the configFile
            if (deviceProperties.getProperty(DEVICE_NAME) == null || deviceProperties.getProperty(DEVICE_NAME).trim().isEmpty()) {
                throw new RuntimeException("The deviceName passed cannot be null/empty.");
            }

            // Mapping the contents of the configFile in a config1 hash map
            config1.put(ADDRESS, deviceProperties.getProperty(ADDRESS));
            config1.put(DEVICE_NAME, deviceProperties.getProperty(DEVICE_NAME));

            // Calling the getDeviceHash method to convert the device configurations to SHA-256 hash
            getDeviceHash();
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

    // Public method to record contact between the individuals
    public void recordContact(String individual, int date, int duration) {

        try {
            // Validating the individual string passed
            if (individual == null || individual.trim().isEmpty()) {
                throw new IllegalArgumentException("The passed individual cannot be null/empty.");
            }

            // Validating the date passed
            // The date passed could be 0 as I ma considering the days since 1st Jan 2021
            // So if 0 is passed as noOfDays then, date returned will be 2021/01/01
            if (date < 0) {
                throw new IllegalArgumentException("The passed date is not valid.");
            }

            // Validating the duration passed
            if (duration <= 0) {
                throw new IllegalArgumentException("The passed duration is not valid.");
            }

            // Creating a new contact by calling Contact class
            // Adding the newly created contact to arraylist contactList
            contactList.add(new Contact(individual, getLocalDate(date), duration));
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public method to record the positive testHash for the current mobileDevice
    public void positiveTest(String testHash) {

        try {
            // Validating the testHash string passed
            if (testHash == null || testHash.trim().isEmpty()) {
                throw new IllegalArgumentException("The testHash cannot be null/empty.");
            }

            // Creating an instance of Message Digest of SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Storing the SHA-256 hash of testHash into a string tHash
            // Appending the passed testHash at the end of tHash just for reference
            String tHash = String.format("%064x", new BigInteger(1, md.digest(testHash.getBytes(StandardCharsets.UTF_8)))) + testHash;

            // Adding the hashed tHash to arraylist testHashList
            testHashList.add(tHash);
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }

    // Public method to synchronize the data with the Government
    // Calls the mobileContact method of the Government class passing the currentDevice's hash & contactInfo string
    public boolean synchronizeData() {

        try {
            // Creating a document reference DOM that will store the retrieved data
            Document dom;

            // Instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // Using DocumentBuilderFactory to get an instance of DocumentBuilder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Creating an instance of DOM
            dom = db.newDocument();

            // Creating the root element of the DOM
            Element rootEle = dom.createElement("contact_info");

            // Creating a contacts element
            Element con = dom.createElement("contact_list");

            // Creating a test_hash element
            Element allTestHash = dom.createElement("test_hash_list");

            // Iterating through the contactList
            for (Contact contact : contactList) {

                // Storing the individual
                String individual = contact.getIndividual();
                Element in = dom.createElement("individual");
                in.appendChild(dom.createTextNode(individual));

                // Storing the date
                LocalDate date = contact.getDate();
                Element da = dom.createElement("date");
                da.appendChild(dom.createTextNode(date.toString()));

                // Storing the duration
                int duration = contact.getDuration();
                Element du = dom.createElement("duration");
                du.appendChild(dom.createTextNode(String.valueOf(duration)));

                // Creating a contacts element
                Element contacts = dom.createElement("contact");

                // Appending the individual, date & duration in the contacts element
                contacts.appendChild(in);
                contacts.appendChild(da);
                contacts.appendChild(du);

                // Appending the contacts element to the contact_list
                con.appendChild(contacts);
            }
            // Appending the contact_list to the root
            rootEle.appendChild(con);

            // Iterating through the testHashList
            for (String hash : testHashList) {

                // Creating a test hash element
                Element ha = dom.createElement("test_hash");
                ha.appendChild(dom.createTextNode(hash));

                // Appending  the test hash to the test_hash_list
                allTestHash.appendChild(ha);
            }
            // Appending the test_hash_list to the root
            rootEle.appendChild(allTestHash);

            // Finally appending the root element to the DOM
            dom.appendChild(rootEle);

            // Converting the DOM to a contactInfo String
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Creating an instance of the StringWriter
            // Using try with resources as it implements AutoCloseable interface
            try (StringWriter writer = new StringWriter()) {
                // Using the writer to transform the DOM
                tr.transform(new DOMSource(dom), new StreamResult(writer));

                // Converting the writer to String named contactInfo
                contactInfo = writer.getBuffer().toString();
            }
            // Catching the exception & returning false
            catch (Exception e) {
                return false;
            }

            // Calling the mobileContact method of Government class
            // Passing the current deviceHash and the contactInfo string
            // Returning a boolean as true or false
            // Clearing the contactList & testHashList
            // Also clearing the testList & contactList of the Government class
            if (contactTracer.mobileContact(deviceHash, contactInfo)) {
                contactList.clear();
                testHashList.clear();
                contactTracer.testList.clear();
                contactTracer.contactList.clear();
                return true;
            }
            // If mobileContact returned false then also clearing all the resources lists and returning false
            else {
                contactList.clear();
                testHashList.clear();
                contactTracer.testList.clear();
                contactTracer.contactList.clear();
                return false;
            }
        }
        // Catching any thrown exceptions
        catch (Exception e) {

            // Wrapping all the exceptions and throwing a RuntTimeException instead with the exception message
            throw new RuntimeException(e.getMessage());
        }
    }
}

