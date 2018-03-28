/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra,
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.database;

import orfpred.gui.GUI;
import orfpred.gui.GUIUpdater;
import orfpred.sequence.ORF;
import org.biojava.nbio.core.sequence.DNASequence;
import sun.dc.pr.PRError;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Class om de ORFpredDB aan te vullen
 *
 * @author Projectgroep 9
 */
public class DatabaseSaver {

    private DatabaseConnector connector;
    private DatabaseLoader loader;
    private int bestandID, seqID, orfID, blastID;

    /**
     * Constructor voor de DatabaseSaver
     *
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     * @throws ClassNotFoundException wordt opgegooid als het programma geen
     * link heeft met de ojdbc8.jar file
     */
    public DatabaseSaver(GUIUpdater updater, GUI gui) throws SQLException, ClassNotFoundException {
        this.connector = new DatabaseConnector();
        this.loader = new DatabaseLoader();
        bestandID = -1;
        String bestandnaam = updater.getFileName();
        ArrayList<ArrayList<String>> bestaandeFiles = loader.getStoredFiles();
        for(ArrayList<String> array : bestaandeFiles){
            if(array.get(1).equals(bestandnaam)){
                bestandID = Integer.parseInt(array.get(0));
            }
        }
        if(bestandID == -1){
            saveBestand(bestandnaam);
            bestandID = findID("BESTAND","BESTAND_ID",bestandnaam);
        }

        LinkedHashMap<String, DNASequence> headersAndSeq = updater.getHeaderToSeq();
        ArrayList<ArrayList<String>> alOpgeslagenHeaders = loader.getHeadersFromFile(bestandID);
        ArrayList<String> alleHeaders = new ArrayList<>();
        for(ArrayList<String> headerArray : alOpgeslagenHeaders){
            alleHeaders.add(headerArray.get(1));
        }
        for(String header : headersAndSeq.keySet()){
            if(alleHeaders.contains(header)){
                int index = alleHeaders.indexOf(header);
                seqID = Integer.parseInt(alOpgeslagenHeaders.get(index).get(0));
            } else {
                saveSequentie(header,headersAndSeq.get(header),bestandID);
            }
            if(header.equals(gui.getHeaderComboBox().getSelectedItem())){

            }
        }
    }

    /**
     * methode om bestand gegevens op te slaan in de db
     *
     * @param naamBestand String met de naam van het bestand
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public void saveBestand(String naamBestand) throws SQLException {
        int id = getUniqueID("BESTAND");

        connector.sentInsertionQuery("BESTAND", "" + id + ",'" + naamBestand + "'");
    }

    /**
     * methode om de sequentie en gegevens op te slaan
     *
     * @param header String met de header erin
     * @param sequentie DNASequentie (van biojava) met daarin de sequentie
     * @param bestandID int met de bestandID van het bestand die bij de
     * sequentie hoort
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public void saveSequentie(String header, DNASequence sequentie, int bestandID) throws SQLException {
        this.seqID = getUniqueID("SEQUENTIE");
        connector.sentInsertionQuery("SEQUENTIE", "" + seqID + ",'" + header
                + "','" + sequentie.toString() + "'," + bestandID);

    }

    /**
     * Methode om de ORF met gegevens op te slaan
     *
     * @param orf ORF object met daarin de gegevens
     * @param sequentieID int met de sequentieID van de sequentie waar de ORF
     * vandaan komt
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public void saveORF(ORF orf, int sequentieID) throws SQLException {
        int id = getUniqueID("ORF");
        String frame = ORF.parseFrameToString(orf.getReadingFrame());
        connector.sentInsertionQuery("ORF", "" + id + ",'" + frame
                + "'," + orf.getStart() + "," + orf.getStop() + "," + sequentieID);
    }

    /**
     * Methode om de blast resultaat gegevens op te slaan
     *
     * @param description String met de omschrijving van de hit
     * @param bitScore float met daarin de bitscore
     * @param queryCoverage float met daarin de query coverage (mag max. 2
     * getallen voor de komma hebben anders SQLException)
     * @param eValue String met de eValue
     * @param identity float met de idenetity percentage (mag max. 2 getallen
     * voor de komma hebben anders SQLException)
     * @param positives float met de positives percentage (mag max. 2 getallen
     * voor de komma hebben anders SQLException)
     * @param gaps int met het aantal gaps
     * @param orfID int met de orfID die bij de ORF hoort waar de resultaten van
     * zijn
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public void saveBLASTResults(String description, float bitScore, float queryCoverage,
            String eValue, float identity, float positives, int gaps, int orfID) throws SQLException {
        int id = getUniqueID("BLAST_RESULTAAT");
        connector.sentInsertionQuery("BLAST_RESULTAAT", "" + id + ",'" + description
                + "'," + bitScore + "," + queryCoverage + ",'" + eValue + "'," + identity + "," + positives + "," + gaps + "," + orfID);
    }

    /**
     * Methode om een unieke ID te maken. (*)Kan mogelijk problemen leveren als
     * meerdere gebruikers te gelijkertijd ID's ophalen.
     *
     * @param table String met daarin de naam van de table waarin gekeken moet
     * worden
     * @return een int met een unieke(*) ID
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public int getUniqueID(String table) throws SQLException {
        ResultSet resultset = connector.sentFeedbackQuery("SELECT * FROM " + table);
        int maxID = 0;
        int id;
        while (resultset.next()) {
            id = Integer.parseInt(resultset.getString(1));
            if (id > maxID) {
                maxID = id;
            }
        }
        return maxID + 1;
    }

    /**
     * Hulp methode om ID's te vinden
     *
     * @param table String met de table naam waarin gezocht moet worden
     * @param column String met de column naam waarin gezocht moet worden
     * @param search String met de zoek term waarnaar gezocht moet worden
     * @return een int met daarin de ID die bij de rij hoort van het zoek
     * resultaat
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     * @throws NoSuchFieldError wordt opgegegooid als er geen resultaat is
     * gevonden
     */
    public Integer findID(String table, String column, String search) throws SQLException {
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM " + table);
        while (resultSet.next()) {
            if (resultSet.getString(column).equals(search)) {
                return Integer.parseInt(resultSet.getString(1));
            }
        }
        return null;
    }

}
