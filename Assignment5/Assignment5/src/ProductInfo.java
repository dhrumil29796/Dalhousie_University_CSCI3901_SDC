import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Public class to retrieve product information
public class ProductInfo {

    // Public method to load the product information in an element
    public static Element product(String start, String end, Connection connection) {

        Statement statement = null;
        ResultSet productResultSet = null;
        String previousCategoryName = null;
        String currentCategoryName = null;
        String productName;
        String supplierName;
        Document dom;
        Element productList = null;
        Element category = null;

        try {

            // Creating a statement
            statement = connection.createStatement();

            // Query to retrieve the product information in the given period
            final String query2 =
                    "SELECT\n" +
                            "c.CategoryName,\n" +
                            "p.ProductName,\n" +
                            "s.CompanyName,\n" +
                            "SUM(od.Quantity) AS UnitsSold,\n" +
                            "SUM(od.UnitPrice * od.Quantity - od.Discount) AS SaleValue\n" +
                            "FROM orderdetails AS od, products AS p, suppliers AS s, categories AS c, orders AS o\n" +
                            "WHERE\n" +
                            "od.ProductID = p.ProductID AND\n" +
                            "p.SupplierID = s.SupplierID AND\n" +
                            "p.CategoryID = c.CategoryID AND\n" +
                            "o.OrderID = od.OrderID AND\n" +
                            "o.OrderDate BETWEEN '" + start + "' AND '" + end +
                            "' GROUP BY od.ProductID\n" +
                            "ORDER BY c.CategoryName;";

            // Storing the result in productResultSet
            productResultSet = statement.executeQuery(query2);

            // Instance of a DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                // Using DocumentBuilderFactory to get an instance of DocumentBuilder
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Creating an instance of DOM
                dom = db.newDocument();

                // Creating the root element for this document as productList
                productList = dom.createElement("product_list");

                // Iterating through the result set
                while (productResultSet.next()) {

                    // Storing the productName
                    productName = productResultSet.getString("ProductName");
                    Element pn = dom.createElement("product_name");
                    // Performing check on the data read
                    if (productName == null || productName.isEmpty()) {
                        pn.appendChild(dom.createTextNode("null"));
                    } else {
                        pn.appendChild(dom.createTextNode(productName));
                    }

                    // Storing the supplierName
                    supplierName = productResultSet.getString("CompanyName");
                    Element sn = dom.createElement("supplier_name");
                    // Performing check on the data read
                    if (supplierName == null || supplierName.isEmpty()) {
                        sn.appendChild(dom.createTextNode("null"));
                    } else {
                        sn.appendChild(dom.createTextNode(supplierName));
                    }

                    // Storing the number of units sold in the given period
                    Element us = dom.createElement("units_sold");
                    us.appendChild(dom.createTextNode(String.valueOf(productResultSet.getInt("UnitsSold"))));

                    // Storing the total sale value done in the given period
                    Element sv = dom.createElement("sale_value");
                    sv.appendChild(dom.createTextNode(String.valueOf(productResultSet.getDouble("SaleValue"))));

                    // Appending all the product details in the product element
                    Element product = dom.createElement("product");
                    product.appendChild(pn);
                    product.appendChild(sn);
                    product.appendChild(us);
                    product.appendChild(sv);

                    // Storing the currentCategoryName
                    currentCategoryName = productResultSet.getString("CategoryName");

                    // Checking whether the previousCategoryName is null
                    if (previousCategoryName == null) {

                        // Creating a new category name element to store the current category name
                        Element categoryName = dom.createElement("category_name");
                        // Performing check on the currentCategoryName read
                        if (currentCategoryName == null || currentCategoryName.isEmpty()) {
                            categoryName.appendChild(dom.createTextNode("null"));
                        } else {
                            categoryName.appendChild(dom.createTextNode(currentCategoryName));
                        }

                        // Creating a new category element
                        category = dom.createElement("category");

                        // Appending the current category name to category element
                        category.appendChild(categoryName);

                        // Appending the product to category element
                        category.appendChild(product);
                    }
                    // Checking whether the previous & current category name are same
                    else if (previousCategoryName.equals(currentCategoryName)) {

                        // If same then appending only the product to category element
                        category.appendChild(product);
                    }
                    // If they are not same then we append the product and category name
                    else {
                        // Appending the category to productList element
                        productList.appendChild(category);

                        // Creating a new category name element
                        Element categoryName = dom.createElement("category_name");

                        // Appending the current category name to categoryName element
                        categoryName.appendChild(dom.createTextNode(currentCategoryName));

                        // Creating a new category element
                        category = dom.createElement("category");

                        // Appending the current category name to category element
                        category.appendChild(categoryName);

                        // Appending the product to category element
                        category.appendChild(product);
                    }
                    // Assigning current category name to previous category name
                    // And moving on to a new iteration
                    previousCategoryName = currentCategoryName;
                }
                // Appending all the categories to productList element
                productList.appendChild(category);

                // Appending the productList to DOM
                dom.appendChild(productList);

            } catch (Exception e) {
                return null;
            }

            // Closing the result set
            productResultSet.close();
        }
        // Catching SQLException
        catch (SQLException e) {
            return null;
        }
        // Running the finally block to close the result set & statement
        finally {
            if (productResultSet != null) {
                try {
                    productResultSet.close();
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
        // Returning the productList element
        return productList;
    }
}
