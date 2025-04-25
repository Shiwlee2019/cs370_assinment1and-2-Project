package edu.cs;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTest_Demo {

    public DBTest_Demo() {
        // Default constructor
    }

    public int testconnection_mysql(int hr_offset) {
        String connection_host = "3.145.65.29";
        String dbName = "Dbtest";
        String username = "db_user";
        String password = "Ahmed2019";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        int flag = 0;

        System.out.println("\nTesting connection to MySQL Database...");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // MySQL connection with SSL
            connect = DriverManager.getConnection(
                "jdbc:mysql://" + connection_host + ":3306/" + dbName + "?useSSL=true&requireSSL=true",
                username,
                password
            );

            // Query MySQL server time with offset
            String qry1a = "SELECT CURDATE() + INTERVAL " + hr_offset + " HOUR";
            preparedStatement = connect.prepareStatement(qry1a);
            ResultSet r1 = preparedStatement.executeQuery();

            if (r1.next()) {
                String nt = r1.getString(1);
                System.out.println(hr_offset + " hour(s) ahead of the system clock of MySQL at " + connection_host + " is: " + nt);
            }

            r1.close();
            preparedStatement.close();
        } catch (Exception e) {
            System.out.println("MySQL DB Connection Failed: " + e.getMessage());
            flag = -1;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    public int testConnection(int hr_offset) {
        String oracle_driver = "oracle.jdbc.driver.OracleDriver";
        String dbURL1 = "jdbc:oracle:thin:@oodb.cs.qc.cuny.edu:1521:oodb";
        String userName1 = "SE";
        String userPassword1 = "SE2020";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        int flag = 0;

        System.out.println("\nTesting connection to Oracle Database...");

        try {
            Class.forName(oracle_driver);
            conn = DriverManager.getConnection(dbURL1, userName1, userPassword1);
            String stmtQuery = "SELECT sysdate + " + (float) hr_offset / 24 + " FROM dual";
            pstmt = conn.prepareStatement(stmtQuery);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String newTime = rs.getString(1);
                System.out.println(hr_offset + " hour(s) ahead of the system clock of Oracle is: " + newTime);
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Oracle DB Connection Failed: " + e.getMessage());
            flag = -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Usage: java -jar DBTest.jar <number_of_hr_offset>");
                System.out.println("Success returns errorlevel 0. Error return greater than zero.");
                System.exit(1);
            }

            System.out.println("\n=====================================");
            System.out.println("   Example for Oracle DB connection via Java");
            System.out.println("   Copyright: Bon Sy");
            System.out.println("   Free to use this at your own risk!");
            System.out.println("=====================================\n");

            int hr_offset = Integer.parseInt(args[0]);
            DBTest_Demo DBConnect_instance = new DBTest_Demo();

            if (DBConnect_instance.testConnection(hr_offset) == 0) {
                System.out.println("Oracle Remote Connection Successful Completion!\n");
            } else {
                System.out.println("Oracle DB Connection Failed!\n");
            }

            if (DBConnect_instance.testconnection_mysql(hr_offset) == 0) {
                System.out.println("MySQL Remote Connection Successful Completion!\n");
            } else {
                System.out.println("MySQL DB Connection Failed!\n");
            }

            System.out.println("\n=====================================");
            System.out.println("     Database Connection Completed   ");
            System.out.println("=====================================\n");

        } catch (Exception e) {
            System.out.println("Hmmm... Looks like an input error.");
            e.printStackTrace();
        }
    }
}