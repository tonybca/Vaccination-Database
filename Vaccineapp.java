import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


class simpleJDBC
{
    public static void main ( String [ ] args ) throws SQLException
    {
        // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
        String tableName = "";
        int sqlCode=0;      // Variable to hold SQLCODE
        String sqlState="00000";  // Variable to hold SQLSTATE

        if ( args.length > 0 )
            tableName += args [ 0 ] ;
        else
            tableName += "exampletbl";

        // Register the driver.  You must register the driver before you can use it.
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        // This is the url you must use for DB2.
        //Note: This url may not valid now !
        String url = "jdbc:db2://winter2021-comp421.cs.mcgill.ca:50000/cs421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "";
        String your_password = "";
        //AS AN ALTERNATIVE, you can just set your password in the shell environment in the Unix (as shown below) and read it from there.
        //$  export SOCSPASSWD=yoursocspasswd
        if(your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null)
        {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        if(your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null)
        {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement ( ) ;
        int exit = 0;
        while(exit != 1)
        {
        Scanner s = new Scanner(System.in);
        System.out.println("VaccineApp Main Menu\n" +
                "   1. Add a Person\n" +
                "   2. Assign a slot to a Person\n" +
                "   3. Enter Vaccination information\n" +
                "   4. Exit Application\n" +
                "Please Enter Your Option(Please enter the corresponding index number):");
        int i = s.nextInt();
        s.nextLine();
        if (i == 1)
        {
            System.out.println("Please enter the person's health insurance number");
            String hin = s.nextLine();
            try
            {

                String s1 = "SELECT rname FROM Person WHERE health_insurance_number = " + "'"+hin+"'";
                System.out.println (s1) ; // to be deleted
                java.sql.ResultSet r2 = statement.executeQuery ( s1 ) ;
                if(r2.next())
                {
                    // if the health insurance number already exists
                    String name1 =  r2.getString (1);
                    System.out.println("The health insurance number you entered is associated with "+name1+". Do you want to change it? Type yes or no.");
                    String choice = s.nextLine();
                    if (choice.equalsIgnoreCase("yes"))
                    {
                        try
                        {
                            // delete the old record

                            String createSQL = "DELETE FROM Person " +  " WHERE health_insurance_number ="+ "'"+ hin+"'";
                            System.out.println (createSQL ) ;
                            statement.executeUpdate (createSQL ) ;
                            System.out.println ("DONE");
                        }
                        catch (SQLException e)
                        {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                            System.out.println(e);
                        }
                    }
                    else
                    {
                        System.out.println("The person's information won't be updated.");
                        continue;
                    }

                }
                System.out.println ("DONE");

            }
            catch (SQLException e)
            {
                sqlCode = e.getErrorCode(); // Get SQLCODE
                sqlState = e.getSQLState(); // Get SQLSTATE

                // Your code to handle errors comes here;
                // something more meaningful than a print would be good
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                System.out.println(e);
            }

            System.out.println("Please enter the person's name");
            String name = s.nextLine();

            System.out.println("Please enter the person's gender, 'F' for female and 'M' for male");
            String gender = s.nextLine();
            System.out.println("Please enter the person's category with the highest priority");
            String cat = s.nextLine();
            System.out.println("Please enter the date of registration");
            String date_reg = s.nextLine();
            System.out.println("Please enter the date of birth");
            String date_birth = s.nextLine();
            System.out.println("Please enter the phone number");
            String phone = s.nextLine();
            System.out.println("Please enter the address");
            String address = s.nextLine();
            try
            {
                //String createSQL = "INSERT INTO Person " +  " VALUES (65432, 'jake', '2000-09-20', '2000-09-20', 'F', 'Teacher', 6543224567, 'gugu') ";

                String createSQL = "INSERT INTO Person " +  " VALUES ("+ "'"+ hin+"'"+","+ "'"+name+"'"+","+"'"+ date_reg+"'"+","+"'"+date_birth+"'"+","+"'"+gender+"'"+","+"'"+cat+"'"+","+"'"+phone+"'"+","+"'"+address+"'"+")";
                System.out.println (createSQL ) ;
                statement.executeUpdate (createSQL ) ;
                System.out.println ("DONE");
            }
            catch (SQLException e)
            {
                sqlCode = e.getErrorCode(); // Get SQLCODE
                sqlState = e.getSQLState(); // Get SQLSTATE

                // Your code to handle errors comes here;
                // something more meaningful than a print would be good
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                System.out.println(e);
            }




        }else if (i == 2)
        {
            System.out.println("Please enter the person's health insurance number");
            String hin = s.nextLine();
            try
            {
                int number = 0;
                String type ="";
                String querySQL = "SELECT manufacturer from " + "Vaccination_Slot" + " WHERE health_insurance_number = " +"'"+hin+"'" +" and manufacturer IS NOT NULL";
                System.out.println (querySQL) ;
                java.sql.ResultSet r3 = statement.executeQuery ( querySQL ) ;
                while (r3.next())
                {
                    // get the number of shots already got and the vaccine type
                    type = r3.getString (1);
                    number++;
                }
                if(number>0)
                {
                    try
                    {

                        String s1 = "SELECT no_of_required_doses FROM Vaccine_Type WHERE manufacturer = " +"'"+type+"'";
                        System.out.println (s1) ;
                        java.sql.ResultSet r2 = statement.executeQuery ( s1 ) ;
                        int requiredDoses = 999;
                        while(r2.next()) {
                            requiredDoses = r2.getInt(1);
                        }
                        if (number >= requiredDoses)
                        {
                            System.out.println("This person has received enough doses");
                            continue;
                        }
                        System.out.println ("DONE");

                    }
                    catch (SQLException e)
                    {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE

                        // Your code to handle errors comes here;
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                    }
                }

                    System.out.println();
                    System.out.println("Please enter the vaccination venue name");
                    String vname = s.nextLine();
                    System.out.println("Please enter the date of the slot in the format of yyyy-MM-dd");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String entered = s.nextLine();
                    Date enteredFormatted = sdf.parse(entered);
                    if (enteredFormatted.before(date))
                    {
                        // if the date entered is before the current date
                        System.out.println("The date you entered is before the current date!");
                        continue;
                    }

                    System.out.println("Please enter the starting time of the slot in the format of (hour:minute) in 24h");
                    String start_time = s.nextLine();
                    System.out.println("Please enter the ending time of the slot in the format of (hour:minute) in 24h");
                    String end_time = s.nextLine();
                    System.out.println("Please enter the room number of the slot");
                    String rnumber = s.nextLine();
                try
                {

                    String s1 = "SELECT vname,date,start_time,end_time,room_number FROM Vaccination_Slot WHERE vname = " + "'" + vname + "'"+ "and date ="+ "'"+ entered +"'"+ "and start_time = "
                            +"'"+ start_time +"'"+ "and end_time =" + "'"+end_time +"'"+ "and health_insurance_number IS NOT NULL";
                    System.out.println (s1) ;
                    java.sql.ResultSet r2 = statement.executeQuery ( s1 ) ;
                    int size =0;

                        while (r2.next()) {
                            size++;
                        }

                    if (size>0)
                    {
                        System.out.println("This slot has already been assigned!");
                        continue;
                    }
                    System.out.println ("DONE");

                }
                catch (SQLException e)
                {
                    sqlCode = e.getErrorCode();
                    sqlState = e.getSQLState();


                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    System.out.println(e);
                }

                    try
                    {

                        String createSQL = "UPDATE Vaccination_Slot " +  " SET health_insurance_number = " + "'" + hin +"'"
                                + " WHERE  vname = " + "'" + vname + "'"+ "and date ="+ "'"+ entered +"'"+ "and start_time = "
                            +"'"+ start_time +"'"+ "and end_time =" + "'"+end_time +"'";
                        System.out.println (createSQL ) ;
                        statement.executeUpdate (createSQL ) ;
                        System.out.println ("DONE");
                    }
                    catch (SQLException e)
                    {
                        sqlCode = e.getErrorCode();
                        sqlState = e.getSQLState();


                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                    }



                System.out.println ("DONE");
            }
            catch (SQLException e )
            {
                sqlCode = e.getErrorCode();
                sqlState = e.getSQLState();


                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                System.out.println(e);
            }
            catch (ParseException e1)
            {
                System.out.println(e1);
            }

        }else if (i == 3)
        {
            System.out.println("Please enter the person's health insurance number");
            String hin = s.nextLine();
            System.out.println("Please enter the vial's manufacturer");
            String type = s.nextLine();
            //String querySQL = "SELECT manufacturer from " + "Injection" + " WHERE health_insurance_numbner = " +hin +")";
            //System.out.println (querySQL) ;

            try
            {
                String querySQL = "SELECT manufacturer from " + "Vaccination_Slot" + " WHERE health_insurance_number = " +"'"+hin+"'" + "and Manufacturer IS NOT NULL";
                System.out.println (querySQL) ;
                java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
                String name ="";
                if(rs.next())
                {
                    name = rs.getString (1);
                    System.out.println(name);
                }
                if (!name.isBlank() && !name.equalsIgnoreCase(type))
                {
                    System.out.println("The manufacturer you entered is not the same as the person's first dose. The first does is made by" + name);
                    continue;
                }
                System.out.println ("DONE");
            }
            catch (SQLException e)
            {
                sqlCode = e.getErrorCode(); // Get SQLCODE
                sqlState = e.getSQLState(); // Get SQLSTATE

                // Your code to handle errors comes here;
                // something more meaningful than a print would be good
                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                System.out.println(e);
            }

            System.out.println("Please enter the license number of the nurse");
            String ln = s.nextLine();
            System.out.println("Please enter the batch number of the shot");
            int batch_no = s.nextInt();
            s.nextLine();
            System.out.println("Please enter the vial number of the shot");
            int vial_no = s.nextInt();
            s.nextLine();
            System.out.println("Please enter the date in the form of yyyy-MM-dd");
            String date = s.nextLine();
            System.out.println("Please enter the name of the vaccination venue");
            String vname = s.nextLine();
            System.out.println("Please enter the start time of the slot");
            String start_time = s.nextLine();
            System.out.println("Please enter the end time of the slot");
            String end_time = s.nextLine();
            System.out.println("Please enter the room number of the slot");
            int room_no = s.nextInt();
            s.nextLine();


            try
            {

                String createSQL = "UPDATE VACCINATION_SLOT " +  " SET Batch_no =" + "'"+batch_no+"', "+ " vial_no = '"+vial_no
                +"',"+ "license_number = '" + ln +"'" + ", Manufacturer = '"+ type +"'"+" WHERE health_insurance_number =" + "'" +hin +"'" + " and date = '"+date
                        + "'" + " and start_time = '" + start_time + "'" + " and end_time = '" +end_time +"'" + "and room_number ='"
                        + room_no + "'";
                System.out.println (createSQL ) ;
                statement.executeUpdate (createSQL ) ;
                System.out.println ("DONE");
            }
            catch (SQLException e)
            {
                sqlCode = e.getErrorCode();
                sqlState = e.getSQLState();


                System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                System.out.println(e);
            }





        }else if (i == 4)
        {
            exit = 1;
            System.out.println("You have quitted the system");
        }

}


        // Finally but importantly close the statement and connection
        statement.close ( ) ;
        con.close ( ) ;
    }
}
