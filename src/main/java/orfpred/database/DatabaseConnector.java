/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.database;

import java.sql.*;
import java.util.MissingResourceException;

/**
 * Deze class is verantwoordelijk voor de connectie met de database.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class DatabaseConnector {

    private final String url = "jdbc:oracle:thin:@cytosine.nl:1521:xe",
            user = "owe7_pg9",
            password = "blaat1234";
    private Connection dbConnection;

    /**
     * Constructor voor de DatabaseConnector
     *
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    protected DatabaseConnector()throws SQLException, ClassNotFoundException, MissingResourceException{
        //this.url = dbURL;
        //this.user = usr;
        //this.password = pass;
        this.connect();
    }
    
    /**
     * Maakt connectie met de database.
     *
     * @throws SQLException bij problemen met de toegang tot de database
     * @throws java.lang.ClassNotFoundException
     */
    protected final void connect() throws SQLException, ClassNotFoundException, MissingResourceException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.dbConnection = DriverManager.getConnection(url, user, password);
    }

    protected ResultSet sentFeedbackQuery(String query)throws SQLException{
        Statement statement = dbConnection.createStatement();
        return statement.executeQuery(query);
    }

    protected void sentOneWayQuery(String table, String values) throws SQLException{
        Statement statement = dbConnection.createStatement();
        if (values.endsWith(")")){
            statement.executeQuery("INSERT INTO "+table+" VALUES "+values);
        } else {
            statement.executeQuery("INSERT INTO " + table + " VALUES (" + values + ")");
        }
        statement.executeQuery("COMMIT");
    }

    /**
     * Sluit connectie met de database.
     *
     * @throws SQLException bij problemen met de toegang tot de database
     */
    protected void closeConnection() throws SQLException {
        if (dbConnection != null) {
            dbConnection.close();
        }
    }
}
