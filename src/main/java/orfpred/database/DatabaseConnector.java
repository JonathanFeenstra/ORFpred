/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */

package orfpred.database;

import java.sql.*;

/**
 * Deze class is verantwoordelijk voor de connectie met de database.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class DatabaseConnector {

    private static String url, user, password;
    private static Connection dbConnection;

    /**
     * Maakt connectie met de database.
     *
     * @param dbURL de database url
     * @param usr de database user
     * @param pass de database password
     * @throws SQLException bij problemen met de toegang tot de database
     */
    public void connect(String dbURL, String usr, String pass) throws SQLException {
        url = dbURL;
        user = usr;
        password = pass;
        if (url != null) {
            dbConnection = DriverManager.getConnection(url, user, password);
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
