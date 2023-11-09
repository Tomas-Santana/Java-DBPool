package Pool;

import java.sql.DriverManager;  
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

import PropertyHandler.PropertyHandler;

/**
 * DBConn
 */
public class DBConn {
    private static PropertyHandler prop = new PropertyHandler("src/Pool/Pool.properties");
    private Connection conn;
    private String DBID; 
    private ResultSet rs;
    public boolean isAvailable = true;

    protected DBConn() {
    }

    protected boolean Connect(String DBID) {
        String connString = prop.getProp(DBID, "Failed");
        if (connString.equals("Failed")) {
            System.err.println("Invalid DBID: " + DBID);
            return false;
        }

        String url = "jdbc:postgresql://localhost:5432/Militar";
        String user = "postgres";
        String pass = "123456789";
        try {
            this.DBID = DBID;
            conn = DriverManager.getConnection(url, user, pass);
            isAvailable = false;
            return true;
        } catch (SQLException e) {
            System.err.println("Connection Failed: " + url);
            return false;
        }
    }

    protected boolean Disconnect() {
        try {
            conn.close();
            this.DBID = null;
            isAvailable = true;
            return true;

        } catch (SQLException e) {
            System.err.println("Disconnection Failed");
            return false;
        }
    }

    public boolean QueryFromString(String query) {
        try {
            rs = conn.createStatement().executeQuery(query);
            return true;
        } catch (SQLException e) {
            System.err.println("Query Failed: " + query);
            return false;
        }
    }
    public String getID() {
        return DBID;
    }
    public ResultSet getResultSet() {
        return rs;
    }

    
}