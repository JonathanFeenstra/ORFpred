/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.database;

import java.sql.SQLException;
import java.util.MissingResourceException;

/**
 * Class om data uit de database in te laden.
 * 
 * @author Projectgroep 10
 */
public class DatabaseLoader {

    private final DatabaseConnector connector;

    /**
     * Constructor
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws MissingResourceException 
     */
    public DatabaseLoader() throws SQLException, ClassNotFoundException, MissingResourceException {
        this.connector = new DatabaseConnector("","","");
    }

    /**
     * 
     * @param search
     * @param type 
     */
    public void getData(String search, SearchType type){

    }
}
