package util_project;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class dbconnection {
    private static Connection conn = null;

    public static Connection getConnection(){
        if (conn == null) {
            String filePath = new File("").getAbsolutePath();
            filePath += "/resources/connection.properties";
            try(FileReader input = new FileReader(filePath)) {

                Properties props = new Properties();
                props.load(input);

                String url = props.getProperty("url");
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                conn = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}

