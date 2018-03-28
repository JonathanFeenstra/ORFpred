/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class om data uit de database te verwijderen.
 *
 * @author Projectgroep 9
 */
public class DatabaseDeleter {

    private final DatabaseConnector CONNECTOR = new DatabaseConnector();
    private final String bestandID;
    private ArrayList<String> sequentieIDs, orfIDs = new ArrayList<>(), blastIDs = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param bestandID het ID van het te verwijderen bestand
     * @throws SQLException bij problemen met de connectie met de database
     * @throws ClassNotFoundException als de Oracle JDBC driver niet gevonden
     * kan worden
     */
    public DatabaseDeleter(String bestandID) throws SQLException, ClassNotFoundException {
        this.bestandID = bestandID;
        this.createIDsArrays();
        this.deleteData();

    }

    /**
     * Maakt arrays van ID's.
     *
     * @throws SQLException bij problemen met de connectie met de database
     */
    private void createIDsArrays() throws SQLException {
        this.sequentieIDs = getIDsToRemove(bestandID, "SEQUENTIE", "BESTAND_ID");
        for (String seqid : this.sequentieIDs) {
            this.orfIDs.addAll(getIDsToRemove(seqid, "ORF", "SEQ_ID"));
        }
        for (String orfid : this.orfIDs) {
            this.blastIDs.addAll(getIDsToRemove(orfid, "BLAST_RESULTAAT", "ORF_ID"));
        }
    }

    /**
     * Verwijdert data uit de database.
     * 
     * @throws SQLException bij problemen met de connectie met de database
     */
    private void deleteData() throws SQLException {
        deleteRowsByArray(blastIDs, "BLAST_RESULTAAT", "RESULT_ID");
        deleteRowsByArray(orfIDs, "ORF", "ORF_ID");
        deleteRowsByArray(sequentieIDs, "SEQUENTIE", "SEQ_ID");
        CONNECTOR.sentDeleteQuery("BESTAND", "BESTAND_ID =" + bestandID);
    }

    /**
     * Retourneert ArrayList met de te verwijderen ID's.
     * 
     * @param lookUpID de te zoeken ID
     * @param table de te doorzoeken tabel
     * @param lookUpColumn de te doorzoeken kolom
     * @return foundIDs de te verwijderen ID's
     * @throws SQLException bij problemen met de connectie met de database
     */
    private ArrayList<String> getIDsToRemove(String lookUpID, String table, String lookUpColumn) throws SQLException {
        ArrayList<String> foundIDs = new ArrayList<>();
        ResultSet resultSet = CONNECTOR.sentFeedbackQuery("SELECT * FROM " + table + " WHERE " + lookUpColumn + " = " + lookUpID);
        while (resultSet.next()) {
            foundIDs.add(resultSet.getString(1));
        }
        return foundIDs;
    }

    /**
     * Verwijdert de meegegeven rijen.
     * 
     * @param ids de te verwijderen ID's
     * @param table de tabel waaruit verwijderd moet worden.
     * @param column de kolom waaruit verwijderd moet worden
     * @throws SQLException bij problemen met de connectie met de database
     */
    private void deleteRowsByArray(ArrayList<String> ids, String table, String column) throws SQLException {
        for (String id : ids) {
            CONNECTOR.sentDeleteQuery(table, column + " = " + id);
        }
    }
}
