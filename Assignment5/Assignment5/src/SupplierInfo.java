import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Public class to retrieve supplier information
public class SupplierInfo {

    // Public method to load the supplier information in an element
    public static Element supplier(String start, String end, Connection connection) {

        Statement statement = null;
        ResultSet supplierResultSet = null;
        String companyName;
        String sAddress;
        String sCity;
        String sRegion;
        String sPostalCode;
        String sCountry;
        Document dom;
        Element supplierList = null;

        try {

            // Creating a statement
            statement = connection.createStatement();

            // Query to retrieve the supplier information in the given period
            final String query3 =
                    "SELECT\n" +
                            "s.CompanyName,\n" +
                            "s.Address,\n" +
                            "s.City,\n" +
                            "s.Region,\n" +
                            "s.PostalCode,\n" +
                            "s.Country,\n" +
                            "SUM(od.Quantity) AS ProductsSold,\n" +
                            "SUM(od.UnitPrice * od.Quantity - od.Discount) AS TotalDollarValue\n" +
                            "FROM suppliers AS s, products AS p, orderdetails AS od, orders AS o\n" +
                            "WHERE\n" +
                            "s.SupplierID = p.SupplierID AND\n" +
                            "p.ProductID = od.ProductID AND\n" +
                            "o.OrderID = od.OrderID AND\n" +
                            "o.OrderDate BETWEEN '" + start + "' AND '" + end +
                            "' GROUP BY s.SupplierID\n" +
                            "ORDER BY s.CompanyName;";

            // Storing the result in supplierResultSet
            supplierResultSet = statement.executeQuery(query3);

            // Instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                // Using DocumentBuilderFactory to get an instance of DocumentBuilder
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Creating an instance of DOM
                dom = db.newDocument();

                // Creating the root element for this document as supplierList
                supplierList = dom.createElement("supplier_list");

                // Iterating through the result set
                while (supplierResultSet.next()) {

                    // Storing the companyName
                    companyName = supplierResultSet.getString("CompanyName");
                    Element sn = dom.createElement("supplier_name");
                    // Performing check on the data read
                    if (companyName == null || companyName.isEmpty()) {
                        sn.appendChild(dom.createTextNode("null"));
                    } else {
                        sn.appendChild(dom.createTextNode(companyName));
                    }

                    // Storing the streetAddress
                    // Also replacing the "\n" in the data retrieved
                    sAddress = supplierResultSet.getString("Address");
                    sAddress = sAddress.replace("\n", "");
                    Element sa = dom.createElement("street_address");
                    // Performing check on the data read
                    if (sAddress.isEmpty()) {
                        sa.appendChild(dom.createTextNode("null"));
                    } else {
                        sa.appendChild(dom.createTextNode(sAddress));
                    }

                    // Storing the city
                    sCity = supplierResultSet.getString("City");
                    Element c = dom.createElement("city");
                    // Performing check on the data read
                    if (sCity == null || sCity.isEmpty()) {
                        c.appendChild(dom.createTextNode("null"));
                    } else {
                        c.appendChild(dom.createTextNode(sCity));
                    }

                    // Storing the region
                    sRegion = supplierResultSet.getString("Region");
                    Element r = dom.createElement("region");
                    // Performing check on the data read
                    if (sRegion == null || sRegion.isEmpty()) {
                        r.appendChild(dom.createTextNode("null"));
                    } else {
                        r.appendChild(dom.createTextNode(sRegion));
                    }

                    // Storing the postalCode
                    sPostalCode = supplierResultSet.getString("PostalCode");
                    Element pc = dom.createElement("postal_code");
                    // Performing check on the data read
                    if (sPostalCode == null || sPostalCode.isEmpty()) {
                        pc.appendChild(dom.createTextNode("null"));
                    } else {
                        pc.appendChild(dom.createTextNode(sPostalCode));
                    }

                    // Storing the country
                    sCountry = supplierResultSet.getString("Country");
                    Element co = dom.createElement("country");
                    // Performing check on the data read
                    if (sCountry == null || sCountry.isEmpty()) {
                        co.appendChild(dom.createTextNode("null"));
                    } else {
                        co.appendChild(dom.createTextNode(sCountry));
                    }

                    // Storing the number of products supplied by the supplier in the given period
                    Element np = dom.createElement("num_products");
                    np.appendChild(dom.createTextNode(String.valueOf(supplierResultSet.getInt("ProductsSold"))));

                    // Storing the total dollar value of the products supplied by the supplier in the given period
                    Element pv = dom.createElement("product_value");
                    pv.appendChild(dom.createTextNode(String.valueOf(supplierResultSet.getDouble("TotalDollarValue"))));

                    // Appending the complete address in address element
                    Element address = dom.createElement("address");
                    address.appendChild(sa);
                    address.appendChild(c);
                    address.appendChild(r);
                    address.appendChild(pc);
                    address.appendChild(co);

                    // Appending the supplier details in supplier element
                    Element supplier = dom.createElement("supplier");
                    supplier.appendChild(sn);
                    supplier.appendChild(address);
                    supplier.appendChild(np);
                    supplier.appendChild(pv);

                    // Appending all the supplier details within supplierList element
                    supplierList.appendChild(supplier);
                }

                // Appending the supplierList to DOM
                dom.appendChild(supplierList);

            } catch (Exception e) {
                return null;
            }

            // Closing the result set
            supplierResultSet.close();
        }
        // Catching SQLException
        catch (SQLException e) {
            return null;
        }
        // Running the finally block to close the result set & statement
        finally {
            if (supplierResultSet != null) {
                try {
                    supplierResultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // Returning the supplierList element
        return supplierList;
    }
}
