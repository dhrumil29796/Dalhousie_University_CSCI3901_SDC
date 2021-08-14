import com.mysql.cj.jdbc.result.ResultSetImpl;

import java.io.*;
import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Sql {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in); //creating scanner object
        LocalDate currentDate, endate; //creating Local Date instance to parse given String as Date
        //Try block for parsing Date and throwing a error if Incorrect Date is entered
        try {
            //Taking user Input
            System.out.println("Enter start date ");
            String strdate, enddate;
            strdate = sc.nextLine();
            //Input validation
            if(strdate == null || strdate.equals(" "))
            {
                System.out.println("Incorrect Date Input ");
                System.exit(0);
            }
            currentDate = LocalDate.parse(strdate);  //Automatically throws an error for incorrect date format
            System.out.println("Enter End date ");
            enddate = sc.nextLine();
            //Input validation
            if(enddate == null || enddate.equals(" "))
            {
                System.out.println("Incorrect Date Input ");
                System.exit(0);
            }
            endate = LocalDate.parse(enddate);
            //Input validation
            if (strdate.compareTo(enddate) > 0 )
            {
                System.out.println("Invalid Date Inputs start date greater than EndDate ");
                System.exit(0);
            }
            System.out.println("Enter the final file name with .xml in the end");
            String output = sc.nextLine();
            //Input Validation
            if ( output == null || output.equals("") || !output.endsWith(".xml") )
            {
                System.out.println("Wrong output file name see ya later ");
                System.exit(0);
            }
            //Getting the main Data from The sql queries function
            String h= sqlpart1(currentDate,endate);
            //checking if there was a connection established at all
            if (h == null)
            {
                System.out.println("Error connecting");
                System.exit(0);
            }
            //Adding the xml part through the printstring function which adds the starting and end part to the string
            String temp = printString(h,currentDate,endate);
            //Validating if the write file function was able to successfully create a file
            Boolean b = Writexml(temp,output);
            if (b)
            {
                System.out.println("Successfully Written");
            }
            else {
                System.out.println("Not  Written try again ");
                System.exit(0);
            }

        } catch (DateTimeException d) {  // Throwing an error if Date entered was incorrect
            System.out.println("Invalid Input date ");
        }


    }

    private static String sqlpart1(LocalDate str, LocalDate end)    {
        Statement statement = null;
        ResultSet outcome = null;
        Connection connection = null;

        //Trying to establish a connection to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("Error connecting to jdbc");
        }
        //Writing every query inside the try block so as to catch any Sql exemption or Connection error
        try {
            // Connect to the Dal database
            connection = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306/csci3901",
                    "rbhargava", "B00866496");
            statement = connection.createStatement();
            //Question 1 query 1
            outcome = statement.executeQuery("SELECT c.CompanyName,c.Address,c.City,c.Region,c.PostalCode,c.Country,Sum(od.UnitPrice*od.Quantity) as TotalPrice, count(Distinct o.orderID) as D From" +
                    " customers as c join orders as o on (c.CustomerId = o.CustomerId) join orderdetails AS od ON (o.OrderId = od.OrderId) where" +
                    " o.OrderDate between" + "'" + str + "'" + "and " + "'" + end + "' group by c.CustomerID ;");
            //creating Dynamic Array lists to store the Data
            ArrayList<String> CompanyName = new ArrayList<>();
            ArrayList<String> Address = new ArrayList<>();
            ArrayList<String> City = new ArrayList<>();
            ArrayList<String> Region = new ArrayList<>();
            ArrayList<String> PostalCode = new ArrayList<>();
            ArrayList<String> Country = new ArrayList<>();
            ArrayList<Integer> count = new ArrayList<Integer>();
            ArrayList<Double> TotalPrice = new ArrayList<>();
            //Going through the result set to store the data
            while (outcome.next()) {
                CompanyName.add(outcome.getString("CompanyName"));
                String h =  outcome.getString("Address").replace("\n"," ");
                Address.add(h);
                City.add(outcome.getString("City"));
                Region.add(outcome.getString("Region"));
                PostalCode.add(outcome.getString("PostalCode"));
                Country.add(outcome.getString("Country"));
                TotalPrice.add(outcome.getDouble("TotalPrice"));
                count.add(outcome.getInt("D"));
            }
            //Storing the result as a String in the form of xml
            String result1 = "";
            String q1 = "\t<customer_list>\n";
            String q2 = "";
            for (int i = 0; i < CompanyName.size(); i++) {

                 q2 = q2 + "\t\t<customer>\n" +
                        "\t\t\t<customer_name> " + CompanyName.get(i) + "</customer_name>\n" +
                        "\t\t\t<address>\n" +
                        "\t\t\t\t<street_address>"+ Address.get(i)+" </street_address>\n" +
                        "\t\t\t\t<city> "+ City.get(i)+" </city>\n" +
                        "\t\t\t\t<region> " + Region.get(i) + "</region>\n" +
                        "\t\t\t\t<postal_code>"+PostalCode.get(i) +" </postal_code>\n" +
                        "\t\t\t\t<country>"+ Country.get(i)+" </country>\n" +
                        "\t\t\t</address>\n" +
                        "\t\t\t<num_orders>"+ Integer.toString(count.get(i))+" </num_orders>\n" +
                        "\t\t\t<order_value>"+Double.toString(TotalPrice.get(i)) +"</order_value>\n" +
                        "\t\t</customer>\n";

            }
            q1 = q1 + q2 +"\t</customer_list>\n";
            result1 = result1 + q1;
            //Resetting temporary String
            q1 = "";
            q2= "";
            //Question 2 Query 2
            outcome = statement.executeQuery("select p.ProductName,c.CategoryName,sum(od.Quantity) as q,s.CompanyName,sum(od.UnitPrice*od.Quantity) as total " +
                    " from products as p join categories as c on (p.CategoryID = c.CategoryID) join " +
                    " suppliers as s on (s.SupplierID = p.SupplierID) " +
                    " join orderdetails as od on (p.ProductID = od.ProductID) join orders as o on (o.OrderID = od.OrderID)" +
                    "where o.OrderDate between '"+ str+ "'" +"and" +"'" + end+"' group by od.ProductId;");
            //Creating hash map for each Category and arraylist for the rest of the data
            HashMap<String,ArrayList<Integer>> c = new HashMap<>();
            ArrayList<String> pname = new ArrayList<String>();
            ArrayList<String> catname = new ArrayList<String>();
            ArrayList<String> CompanyName2 = new ArrayList<String>();
            ArrayList<Double> Total = new ArrayList<>();
            ArrayList<Integer> co = new ArrayList<>();
            //Looping through the whole Result set ot populate the data
            while (outcome.next()) {
                if (! catname.contains(outcome.getString("CategoryName"))) {
                    catname.add(outcome.getString("CategoryName"));
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(0,pname.size());
                    pname.add(outcome.getString("ProductName"));
                    CompanyName2.add(outcome.getString("CompanyName"));
                    Total.add(outcome.getDouble("total"));
                    co.add(outcome.getInt("q"));
                    c.put(catname.get(catname.size() -1),temp);
                }
                else{
                    ArrayList<Integer> temp = new ArrayList<>();
                    String key = "";
                    key = outcome.getString("CategoryName");
                    temp = c.get(key);
                    temp.add(pname.size());
                    pname.add(outcome.getString("ProductName"));
                    CompanyName2.add(outcome.getString("CompanyName"));
                    Total.add(outcome.getDouble("total"));
                    co.add(outcome.getInt("q"));
                    c.put(key,temp);
                }
            }
            //Adding the data to Xml Strign format
            String result2 = "";
             q1 = "\t<product_list>\n";
            for (String key : c.keySet())
            {
                 q2 = q2 +  "\t\t<category>\n" +
                        "\t\t\t<category_name> " + key + "</category_name>\n";
                 String q3 = "";
                 for(int i : c.get(key)) {
                     q3 =  q3 + "\t\t<product>\n" +
                             "\t\t\t<product_name>" + pname.get(i) + " </product_name>\n" +
                             "\t\t\t<supplier_name> " + CompanyName2.get(i) + " </supplier_name>\n" +
                             "\t\t\t<units_sold> " + co.get(i) + "</units_sold>\n" +
                             "\t\t\t<sale_value>" + Total.get(i) + " </sale_value>\n" +
                             "\t\t</product>\n";
                 }
                 q2 = q2 + q3 + "\t\t</category>\n";
            }
            q1 = q1 + q2+ "\t</product_list>\n";
            result2 = result2 + q1;
            //Resetting temporary String
            q1= "";
            q2 = "";
            // question 3 query 3
            outcome = statement.executeQuery("select s.CompanyName,sum(od.Quantity) as y,s.Address,s.City,s.Region,s.PostalCode,s.Country,Count(od.UnitPrice*od.Quantity) as TotalDollars" +
                    " FROM products AS p join categories as c on (p.CategoryID = c.CategoryID) join" +
                    " suppliers as s on (s.SupplierID = p.SupplierID)" +
                    "join orderdetails as od on (p.ProductID = od.ProductID) join orders as o on (o.OrderID = od.OrderID) where" +
                    " o.OrderDate between '" + str + "'" +  " and " +"'"+ end + "' group by s.SupplierID;");
            //creating Dynamic Array lists to store the Data
            ArrayList<String> Cn = new ArrayList<String>();
            ArrayList<String> sAD = new ArrayList<String>();
            ArrayList<String> SCITY = new ArrayList<String>();
            ArrayList<String> sregion = new ArrayList<String>();
            ArrayList<String> spincode = new ArrayList<String>();
            ArrayList<String> scountry = new ArrayList<String>();
            ArrayList<Double> TotalDollars = new ArrayList<>();
            ArrayList<Integer> count2 = new ArrayList<>();
            //Looping through th data set to populate the Data
            while (outcome.next()) {
                    Cn.add(outcome.getString("CompanyName"));
                    String h =  outcome.getString("Address").replace("\n"," ");
                    sAD.add(h);
                    SCITY.add(outcome.getString("City"));
                    sregion.add(outcome.getString("Region"));
                    spincode.add(outcome.getString("PostalCode"));
                    scountry.add(outcome.getString("Country"));
                    TotalDollars.add(outcome.getDouble("TotalDollars"));
                    count2.add(outcome.getInt("y"));
            }
            //Closing the result set
            outcome.close();
            //Concvertign teh data to xml string format
            String result3 = "";
            q1 = "\t<supplier_list>\n";
            q2 = "";
            for (int i = 0; i < Cn.size(); i++) {

                q2 = q2 + "\t\t<supplier>\n" +
                        "\t\t\t<supplier_name> " + Cn.get(i) + "</supplier_name>\n" +
                        "\t\t\t<address>\n" +
                        "\t\t\t\t<street_address> "+ sAD.get(i)+" </street_address>\n" +
                        "\t\t\t\t<city> "+ SCITY.get(i)+" </city>\n" +
                        "\t\t\t\t<region> " + sregion.get(i) + " </region>\n" +
                        "\t\t\t\t<postal_code> "+spincode.get(i) +" </postal_code>\n" +
                        "\t\t\t\t<country> "+ scountry.get(i)+" </country>\n" +
                        "\t\t\t</address>\n" +
                        "\t\t\t<num_products> "+ Integer.toString(count2.get(i))+" </num_products>\n" +
                        "\t\t\t<product_value> "+Double.toString(TotalDollars.get(i)) +" </product_value>\n" +
                        "\t\t</supplier>\n";

            }
            q1 = q1 + q2 +"\t</supplier_list>\n";
            result3 = result3 + q1;
            return  result1 + result2 + result3;  //Adding the result sets and returning the Data part in XmlString format


        } catch (SQLException throwables) { //Catching any sql exceptions and returning null if so
            throwables.printStackTrace();
            System.out.println("Unxpected Sql exception occured I guess I lose Marks");
            return null;
        }
    }

        private static String printString(String fin,LocalDate str,LocalDate enddate)
        {
            //The starting part of the xml String format
            String fin1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n" ;
            //The period part of the xml String
             String fin2= "<period_summary> \n" +
	                           "\t<period> \n" +
		                       "\t\t<start_date>" + str + " </start_date> \n"+
		                       "\t\t<end_date>" + enddate + "</end_date> \n"+
                           "\t</period> \n";
             //Adding all the strings needed to create an end result
            return fin1+ fin2 +fin + "</period_summary> \n";
        }
        private static boolean Writexml(String w,String file)
        {
            //Creating a new File
            File newf = new File(file);
            try { //Writting a try block to catch if file already present
                //Writting the Given String into the File named by the given name
                newf.createNewFile();
                FileWriter fw= new FileWriter(newf);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(w);
                bw.close();
                return true ;

            } catch (IOException e) {  //Throwing error if Write was unsuccessfully
                System.out.println("Unable to write to file Maybe there is already one ");
                e.printStackTrace();
                return false;
            }

        }

    }

