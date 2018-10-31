/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package OracleConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

/**
 * Třída pro vytvoření spojení do databázového systému Oracle.
 * Vytvořeno v návrhovém vzoru singleton.
 * @author Zechmeister Jiří
 * @version 1.2
 */
public class OracleConnector {

    private static Connection connection = null;
    private static String userName;
    private static String password;
    private static String serverName;
    private static int portNumber;
    private static String dbms;
    private static String sid;
    private static Connection conn = null;

    /**
     * Metoda vrátí navázané spojení do databáze
     * @return java.sql.Connection Navázané spojení
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException {
        if (OracleConnector.conn == null) {
            throw new SQLException("Session is not established");
        }
        return OracleConnector.conn;
    }

    /**
     * Metoda pro sestavení connection stringu na základě znalosti TNS
     * @param tnsAlias Alias do tnsnames.ora
     * @param userName Uživatelské jméno do db
     * @param password Heslo do db
     * @throws SQLException 
     */
    public static void setUpConnection(String tnsAlias, String userName, String password) throws SQLException {
        OracleConnector.dbms = "oracle:thin";
        OracleConnector.userName = userName;
        OracleConnector.password = password;

        Properties connectionProps = new Properties();
        connectionProps.put("user", OracleConnector.userName);
        connectionProps.put("password", OracleConnector.password);
        
        OracleConnector.conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@"+tnsAlias,
                connectionProps);

        OracleConnector.conn.setAutoCommit(false);

        System.out.println("Connected to database via TNS");
    }
    
    /**
     * Metoda pro sestavení connection stringu na základě eZConnect
     * @param serverName    Adresa serveru (host)
     * @param portNumber    Port instance
     * @param sid           Sid
     * @param userName      Uživatelské jméno do db
     * @param password      Heslo do db
     * @throws SQLException 
     */
    public static void setUpConnection(String serverName, int portNumber, String sid, String userName, String password) throws SQLException {

        OracleConnector.serverName = serverName;
        OracleConnector.portNumber = portNumber;
        OracleConnector.dbms = "oracle:thin";
        OracleConnector.sid = sid;
        OracleConnector.userName = userName;
        OracleConnector.password = password;

        Properties connectionProps = new Properties();
        connectionProps.put("user", OracleConnector.userName);
        connectionProps.put("password", OracleConnector.password);

        OracleConnector.conn = DriverManager.getConnection(
                "jdbc:" + OracleConnector.dbms + ":@"
                + OracleConnector.serverName
                + ":" + OracleConnector.portNumber + ":" + OracleConnector.sid,
                connectionProps);

        OracleConnector.conn.setAutoCommit(false);

        System.out.println("Connected to database");
    }
    
    /**
     * Metoda sestaví a vrátí connection tring
     * @return Sestavený connection string pro jdbc
     */
    public static String getConnectionString() {
        return "//jdbc:" + OracleConnector.dbms + ":@" + OracleConnector.serverName + ":" + OracleConnector.portNumber + ":" + OracleConnector.sid;
    }
    
    /**
     * Metoda ukončí spojení s databází
     * @param commit    Potvrdit měněná data (commit)
     * @throws SQLException 
     */
    public static void closeConnection(boolean commit) throws SQLException {

        if (conn != null) {

            if (commit) {
                conn.commit();
            }

            conn.close();
            conn = null;
            System.out.println("Connection closed");
        }
    }
    
    /**
     * Metoda převede datum (String) na typ java.sql.Date
     * @param date  POžadované datum
     * @param format Požadovaný formát
     * @return  java.sql.Date Datum v požadovaném formátu
     * @throws ParseException 
     */
    public static java.sql.Date parseDate(String date, String format) throws ParseException{
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        java.sql.Date result = null; 
        try {
            result = new java.sql.Date(df.parse(date).getTime());
        } catch (ParseException ex) {
            throw ex;
        }
        return result;
    }
}
