/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.database;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;

/**
 * Class om data uit de database in te laden.
 * 
 * @author Projectgroep 10
 */
public class DatabaseLoader {

    private final DatabaseConnector connector;


    public static void main(String[] args) {
        try {
            DatabaseLoader loader = new DatabaseLoader();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }
    /**
     * Constructor
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws MissingResourceException 
     */
    public DatabaseLoader() throws SQLException, ClassNotFoundException, MissingResourceException {
        this.connector = new DatabaseConnector();
        ResultSet set = connector.sentFeedbackQuery("SELECT * " +
                "FROM BESTAND " +
                "INNER JOIN SEQUENTIE ON BESTAND.BESTAND_ID = SEQUENTIE.BESTAND_ID " +
                "INNER JOIN ORF ON SEQUENTIE.SEQ_ID = ORF.SEQ_ID");
        System.out.println(set.getString("BESTAND_ID"));

    }

    /**
     * 
     * @param search
     * @param type 
     */
    public void getData(String search, SearchType type){

    }
}
