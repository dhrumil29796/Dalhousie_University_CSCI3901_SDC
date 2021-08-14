import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String start = "";
        String end = "";
        String fileName = "";

        System.out.println("Enter a start date of format YYYY-MM-DD:");
        start = sc.nextLine();
        System.out.println("Enter an end date of format YYYY-MM-DD:");
        end = sc.nextLine();
        System.out.println("Enter the name of the file to store output:");
        fileName = sc.nextLine();

        // Validating for an empty string
        if(start.trim().equals("") || end.trim().equals("")){
            return;
        }

        String[] startDate = start.split("-");
        String[] endDate = end.split("-");
        for (int i = 0; i < startDate.length; i++) {
            System.out.println(startDate[i]);
        }

        // Validating the length of YYYY-MM-DD individually for start and end date
        if (startDate[0].length() < 4 || startDate[1].length() < 2 || startDate[2].length() < 2
                || endDate[0].length() < 4 || endDate[1].length() < 2 || endDate[2].length() < 2) {
            return;
        }


/*        final String CS_ID = "drshah";
        final String BANNER_ID = "B00870600";
        Scanner sc = new Scanner(System.in);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String start = "";
        String end = "";
        String fileName = "";

        // Establish connection between the Java program & Database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Error connecting to JDBC.");
        }

        System.out.println("Enter a start date of format YYYY-MM-DD:");
        start = sc.nextLine();
        System.out.println("Enter an end date of format YYYY-MM-DD:");
        end = sc.nextLine();
        System.out.println("Enter the name of the file to store output:");
        fileName = sc.nextLine();

        Date ft1 = null;
        Date ft2 = null;
        try {
            ft1 = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            ft2 = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        } catch (ParseException e) {
            System.out.println("Enter date in the mentioned format");
        }

        System.out.println("Start Date: " + ft1);
        System.out.println("End Date: " + ft2);
        System.out.println("File name to store the output: " + fileName);*/
        
/*        String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<period_summary>\n" +
                "<period>\n" +
                "<start_date> 1996-01-30 </start_date>\n" +
                "<end_date> 1996-02-02 </end_date>\n" +
                "</period>\n" +
                "</period_summary>";

        File newFile = new File("sample.xml");
        try{
            newFile.createNewFile();
            FileWriter fw = new FileWriter(newFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sample);
            bw.close();
        }catch(Exception e){
            System.out.println("Exception faced while writing to a file");
        }*/

    }
}
