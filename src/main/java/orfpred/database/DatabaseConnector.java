/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
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
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class DatabaseConnector {

    private String url, user, password;
    private Connection dbConnection;

    /**
     * Constructor voor de DatabaseConnector
     *
     * @param dbURL de database url
     * @param usr de database user
     * @param pass de database password
     */
    public DatabaseConnector(String dbURL, String usr, String pass)throws SQLException, ClassNotFoundException, MissingResourceException{
        this.url = dbURL;
        this.user = usr;
        this.password = pass;
        this.connect();
    }
    /**
     * Maakt connectie met de database.
     *
     * @throws SQLException bij problemen met de toegang tot de database
     */
    public void connect() throws SQLException, ClassNotFoundException, MissingResourceException {

        if (url != null) {
            Class.forName("com.mysql.jdbc.Driver");
            this.dbConnection = DriverManager.getConnection(url, user, password);
        } else {
            throw new MissingResourceException("URL-missing","DatabadeConnector","URL-missing");
        }
    }

    /**
     * Sluit connectie met de database.
     *
     * @throws SQLException bij problemen met de toegang tot de database
     */
    public void closeConnection() throws SQLException {
        if (dbConnection != null) {
            dbConnection.close();
        }
    }
}
