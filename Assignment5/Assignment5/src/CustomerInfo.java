import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Public class to retrieve customer information
public class CustomerInfo {

    // Public method to load the customer information in an element
    public static Element customer(String start, String end, Connection connection) {

        Statement statement = null;
        ResultSet customerResultSet = null;
        String customerName;
        String streetAddress;
        String city;
        String region;
        String postalCode;
        String country;
        Document dom;
        Element customerList = null;

        try {

            // Creating a statement
            statement = connection.createStatement();

            // Query to retrieve the customer information in the given period
            final String query1 =
                    "SELECT\n" +
                            "c.CompanyName,\n" +
                            "c.Address,\n" +
                            "c.City,\n" +
                            "c.Region,\n" +
                            "c.PostalCode,\n" +
                            "c.Country,\n" +
                            "COUNT(DISTINCT o.OrderID) AS NumOfOrders,\n" +
                            "SUM(od.UnitPrice * od.Quantity - od.Discount) AS TotalOrderPrice\n" +
                            "FROM customers AS c, orders AS o, orderdetails AS od\n" +
                            "WHERE\n" +
                            "c.CustomerID = o.CustomerID AND\n" +
                            "o.OrderID = od.OrderID AND\n" +
                            "o.OrderDate BETWEEN '" + start + "' AND '" + end +
                            "' GROUP BY o.CustomerID;";

            // Storing the result in customerResultSet
            customerResultSet = statement.executeQuery(query1);

            // Instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                // Using DocumentBuilderFactory to get an instance of DocumentBuilder
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Creating an instance of DOM
                dom = db.newDocument();

                // Creating the root element for this document as customerList
                customerList = dom.createElement("customer_list");

                // Iterating through the result set
                while (customerResultSet.next()) {

                    // Storing the customerName
                    customerName = customerResultSet.getString("CompanyName");
                    Element cn = dom.createElement("customer_name");
                    // Performing check on the data read
                    if (customerName == null || customerName.isEmpty()) {
                        cn.appendChild(dom.createTextNode("null"));
                    } else {
                        cn.appendChild(dom.createTextNode(customerName));
                    }

                    // Storing the streetAddress
                    streetAddress = customerResultSet.getString("Address");
                    Element sa = dom.createElement("street_address");
                    // Performing check on the data read
                    if (streetAddress == null || streetAddress.isEmpty()) {
                        sa.appendChild(dom.createTextNode("null"));
                    } else {
                        sa.appendChild(dom.createTextNode(streetAddress));
                    }

                    // Storing the city
                    city = customerResultSet.getString("City");
                    Element c = dom.createElement("city");
                    // Performing check on the data read
                    if (city == null || city.isEmpty()) {
                        c.appendChild(dom.createTextNode("null"));
                    } else {
                        c.appendChild(dom.createTextNode(city));
                    }

                    // Storing the region
                    region = customerResultSet.getString("Region");
                    Element r = dom.createElement("region");
                    // Performing check on the data read
                    if (region == null || region.isEmpty()) {
                        r.appendChild(dom.createTextNode("null"));
                    } else {
                        r.appendChild(dom.createTextNode(region));
                    }

                    // Storing the postalCode
                    postalCode = customerResultSet.getString("PostalCode");
                    Element pc = dom.createElement("postal_code");
                    // Performing check on the data read
                    if (postalCode == null || postalCode.isEmpty()) {
                        pc.appendChild(dom.createTextNode("null"));
                    } else {
                        pc.appendChild(dom.createTextNode(postalCode));
                    }

                    // Storing the country
                    country = customerResultSet.getString("Country");
                    Element co = dom.createElement("country");
                    // Performing check on the data read
                    if (country == null || country.isEmpty()) {
                        co.appendChild(dom.createTextNode("null"));
                    } else {
                        co.appendChild(dom.createTextNode(country));
                    }

                    // Storing the number of orders placed in the given period
                    Element no = dom.createElement("num_orders");
                    no.appendChild(dom.createTextNode(String.valueOf(customerResultSet.getInt("NumOfOrders"))));

                    // Storing the total order value of the orders placed in the given period
                    Element ov = dom.createElement("order_value");
                    ov.appendChild(dom.createTextNode(String.valueOf(customerResultSet.getDouble("TotalOrderPrice"))));

                    // Appending the complete address in address element
                    Element address = dom.createElement("address");
                    address.appendChild(sa);
                    address.appendChild(c);
                    address.appendChild(r);
                    address.appendChild(pc);
                    address.appendChild(co);

                    // Appending the customer details in customer element
                    Element customer = dom.createElement("customer");
                    customer.appendChild(cn);
                    customer.appendChild(address);
                    customer.appendChild(no);
                    customer.appendChild(ov);

                    // Appending all the customer details within customerList element
                    customerList.appendChild(customer);
                }

                // Appending the customerList to DOM
                dom.appendChild(customerList);

            } catch (Exception e) {
                return null;
            }

            // Closing the result set
            customerResultSet.close();
        }
        // Catching SQLException
        catch (SQLException e) {
            return null;
        }
        // Running the finally block to close the result set & statement
        finally {
            if (customerResultSet != null) {
                try {
                    customerResultSet.close();
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
        // Returning the customerList element
        return customerList;
    }
}

