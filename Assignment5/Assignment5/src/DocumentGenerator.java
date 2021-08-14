import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


// Public class to establish the connection & generate the XML document
public class DocumentGenerator {

    // Public method to load the data & connect to Dal database
    public boolean loadData(String JDBC_DRIVER, String DATABASE_PATH, String CSID,
                            String BANNER_ID, String start, String end, String fileName) {

        // Declaring connection variable
        Connection connection = null;

        // Establish connection between Java program and the Database
        try {
            Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Error connecting to jdbc.");
            return false;
        }

        // Connect to Dal Database
        try {
            connection = DriverManager.getConnection(DATABASE_PATH, CSID, BANNER_ID);

            // Creating a document reference dom that will store the retrieved data
            Document dom;

            // Instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                // Using DocumentBuilderFactory to get an instance of DocumentBuilder
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Creating an instance of DOM
                dom = db.newDocument();

                // Creating the root element of the DOM
                Element rootEle = dom.createElement("period_summary");

                // Creating a start date element
                Element sd = dom.createElement("start_date");
                sd.appendChild(dom.createTextNode(start));

                // Creating an end date element
                Element ed = dom.createElement("end_date");
                ed.appendChild(dom.createTextNode(end));

                // Creating a period element
                Element period = dom.createElement("period");

                // Appending the start and end date in the period element
                period.appendChild(sd);
                period.appendChild(ed);

                // Appending the period element in the root element
                rootEle.appendChild(period);

                // Calling customer method in CustomerInfo class to retrieve the customer information
                Element customerList = CustomerInfo.customer(start, end, connection);

                // Adopting the returned element in the current DOM
                dom.adoptNode(customerList);

                // Appending the customerList in the root element
                rootEle.appendChild(customerList);

                // Calling product method in ProductInfo class to retrieve the product information
                Element productList = ProductInfo.product(start, end, connection);

                // Adopting the returned element in the current DOM
                dom.adoptNode(productList);

                // Appending the productList in the root element
                rootEle.appendChild(productList);

                // Calling supplier method in SupplierInfo class to retrieve the supplier information
                Element supplierList = SupplierInfo.supplier(start, end, connection);

                // Adopting the returned element in the current DOM
                dom.adoptNode(supplierList);

                // Appending the supplierList in the root element
                rootEle.appendChild(supplierList);

                // Finally appending the root element to the DOM
                dom.appendChild(rootEle);

                try {
                    // Writing the DOM in the xml output file
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.setOutputProperty(OutputKeys.METHOD, "xml");
                    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileName)));

                } catch (Exception e) {
                    // Returns false if error faced
                    return false;
                }
            } catch (Exception e) {
                // Returns false if error faced
                return false;
            }
        } catch (SQLException e) {
            // Returns false if error faced
            return false;
        }
        // Running the finally block to close the connection
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // Returning true
        return true;
    }
}
