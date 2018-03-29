/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.database;

import java.sql.*;

/**
 * Deze class is verantwoordelijk voor de connectie met de database.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class DatabaseConnector {

    private final String URL = "jdbc:oracle:thin:@cytosine.nl:1521:xe",
            USER = "owe7_pg9",
            PASSWORD = "blaat1234";
    private Connection dbConnection;

    /**
     * Constructor voor de DatabaseConnector
     *
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    protected DatabaseConnector() throws SQLException, ClassNotFoundException {
        this.connect();
    }

    /**
     * Maakt connectie met de database.
     *
     * @throws SQLException bij problemen met de toegang tot de database
     * @throws java.lang.ClassNotFoundException
     */
    final void connect() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Methode om queries te verzenden en de resultaten te retourneren
     *
     * @param query String met daarin de query
     * @return de ResultSet met daarin het resultaat van de query
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    ResultSet sentFeedbackQuery(String query) throws SQLException {
        Statement statement = dbConnection.createStatement();
        return statement.executeQuery(query);
    }

    /**
     * Methode om insertie queries uit te kunnen voeren
     *
     * @param table String met daarin de table naam waar de gegevens in moeten
     * worden opgeslagen
     * @param values String met daarin de gegevens die moeten worden opgeslagen
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    void sentInsertionQuery(String table, String values) throws SQLException {
        Statement statement = dbConnection.createStatement();
        if (values.endsWith(")")) {
            statement.executeQuery("INSERT INTO " + table + " VALUES " + values);
        } else {
            statement.executeQuery("INSERT INTO " + table + " VALUES (" + values + ")");
        }
        statement.executeQuery("COMMIT");
    }

    /**
     * Methode om sequenties te kunnen op slaan die anders te lang zouden zijden voor de gewone Statement
     * @param query String met de query
     * @param sequence String met sequentie die in de statement wordt toegevoegd
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     */
    void sentPreparedSequenceQuery(String query, String sequence) throws SQLException{
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1,sequence);
        statement.executeQuery();
    }

    /**
     * Methode om entries te verwijderen uit db
     * @param table String met de tabel naam waaruit de entry moet worden verwijderd
     * @param conditie String met de voorwaarde waaraan de rij moet voldoen om verwijderd te moeten worden
     * @throws SQLException
     */
    void sentDeleteQuery(String table, String conditie) throws SQLException{
        Statement statement = dbConnection.createStatement();
        statement.executeQuery("DELETE FROM "+table+" WHERE "+conditie);
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
