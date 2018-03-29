/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra,
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.database;

import orfpred.blast.BLAST;
import orfpred.blast.BLASTParser;
import orfpred.blast.BLASTTable;
import orfpred.gui.GUI;
import orfpred.gui.GUIUpdater;
import orfpred.sequence.ORF;
import orfpred.sequence.ORFHighlighter;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class om de ORFpredDB aan te vullen
 *
 * @author Projectgroep 9
 */
public class DatabaseSaver {

    private DatabaseConnector connector;
    private DatabaseLoader loader;
    private GUIUpdater updater;
    private GUI gui;
    private int bestandID, seqID, orfID, blastID;
    private LinkedHashMap<String, DNASequence> headersAndSeq;
    private ArrayList<ArrayList<String>> alOpgeslagenHeaders;
    private ArrayList<String> alleOudeHeaders;
    private BLASTTable blastTable;
    private String currentHeader;

    /**
     * Constructor voor de DatabaseSaver
     *
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     * @throws ClassNotFoundException wordt opgegooid als het programma geen
     * link heeft met de ojdbc8.jar file
     */
    public DatabaseSaver(GUIUpdater updater, GUI gui) throws SQLException, ClassNotFoundException, CompoundNotFoundException {
        this.connector = new DatabaseConnector();
        this.loader = new DatabaseLoader(updater, gui);
        this.updater = updater;
        this.gui = gui;
        this.blastTable = gui.getBlastTable();

    }

    public void saveBestandData() throws SQLException, CompoundNotFoundException {
        saveBestand();
        findOldHeaders();
        orfHandler();
    }

    /**
     * methode om bestand gegevens op te slaan in de db
     *
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    private void saveBestand() throws SQLException {
        bestandID = -1;
        String bestandnaam = updater.getFileName();
        ArrayList<ArrayList<String>> bestaandeFiles = loader.getStoredFiles();
        for (ArrayList<String> array : bestaandeFiles) {
            if (array.get(1).equals(bestandnaam)) {
                bestandID = Integer.parseInt(array.get(0));
            }
        }
        if (bestandID == -1) {
            int id = getUniqueID("BESTAND");
            connector.sentInsertionQuery("BESTAND", "" + id + ",'" + bestandnaam + "'");
            bestandID = findID("BESTAND", "NAAM", bestandnaam);
        }

    }

    /**
     * methode om de sequentie en gegevens op te slaan
     *
     * sequentie hoort
     *
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    private void saveSequentie(String header, DNASequence sequentie) throws SQLException {
        if (alleOudeHeaders.contains(header)) {
            int index = alleOudeHeaders.indexOf(header);
            seqID = Integer.parseInt(alOpgeslagenHeaders.get(index).get(0));
        } else {
            this.seqID = getUniqueID("SEQUENTIE");
            connector.sentPreparedSequenceQuery("INSERT INTO SEQUENTIE VALUES("+seqID + ",'" + header
                    + "',?,"+bestandID+")",sequentie.toString().toUpperCase());
        }
    }


    /**
     * Methode om de ORF met gegevens op te slaan
     *
     * @param orfList ArrayList met ORF objecten met daarin de gegevens
     * @param sequentieID int met de sequentieID van de sequentie waar de ORF
     * vandaan komt
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    private void saveORFs(ArrayList<ORF> orfList, int sequentieID) throws SQLException {
        for (ORF orf : orfList) {
            int id = getUniqueID("ORF");
            String frame = ORF.parseFrameToString(orf.getReadingFrame());
            connector.sentInsertionQuery("ORF", "" + id + ",'" + frame
                    + "'," + orf.getStart() + "," + orf.getStop() + "," + sequentieID);
            //blastHandler(id);
        }
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
    private int getUniqueID(String table) throws SQLException {
        ResultSet resultset = connector.sentFeedbackQuery("SELECT * FROM " + table);
        int maxID = 0;
        int id;
        while (resultset.next()) {
            id = Integer.parseInt(resultset.getString(1));
            if (id > maxID) {
                maxID = id;
            }
        }
        resultset.close();
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
    private Integer findID(String table, String column, String search) throws SQLException {
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM " + table);
        while (resultSet.next()) {
            if (resultSet.getString(column).equals(search)) {
                return Integer.parseInt(resultSet.getString(1));
            }
        }
        resultSet.close();
        return null;
    }

    /**
     * Methode om de oude orfs te verwijderen
     * @throws SQLException
     * @throws CompoundNotFoundException
     */
    private void deleteOldORFandBLAST() throws SQLException, CompoundNotFoundException {
        HashMap<Integer, ORF> oudeORFd = loader.getORFFromDB(seqID);
        for (Integer id : oudeORFd.keySet()) {
            connector.sentDeleteQuery("BLAST_RESULTAAT", "ORF_ID = " + id);
        }
        connector.sentDeleteQuery("ORF", "SEQ_ID = " + seqID);
    }

    /**
     * Methode om de headers uit het headertoseq file te kunnen vergelijken met de nieuwe headers
     * @throws SQLException
     */
    private void findOldHeaders() throws SQLException {
        headersAndSeq = updater.getHeaderToSeq();
        alOpgeslagenHeaders = loader.getHeadersFromFile(bestandID);
        alleOudeHeaders = new ArrayList<>();
        alOpgeslagenHeaders.forEach((headerArray) -> {
            alleOudeHeaders.add(headerArray.get(1));
        });
    }

    /**
     * Aangezien er duplicaten in de oorsprongelijke object zitten z
     * orgt deze methode ervoor dat alleen de unieke overblijven
     * @return
     */
    private HashSet<ORF> createUniqueORFset() {
        HashSet<ORF> uniqueORFset = new HashSet<>();
        HashMap<Integer, ORF> alleORFs = ORFHighlighter.getPositionToORF();
        alleORFs.keySet().forEach((key) -> {
            uniqueORFset.add(alleORFs.get(key));
        });
        return uniqueORFset;
    }

    /**
     * Methode die regelt dat de orf's bij de juiste headers worden opgeslagen
     * @throws SQLException
     * @throws CompoundNotFoundException
     */
    private void orfHandler() throws SQLException, CompoundNotFoundException {
        for (String header : headersAndSeq.keySet()) {
            currentHeader = header;
            saveSequentie(header, headersAndSeq.get(header));
            if (header.equals(gui.getHeaderComboBox().getSelectedItem())) {
                ArrayList<ORF> uniqueORFList = new ArrayList<>(createUniqueORFset()); // nodig aangezien er duplicaten van ORF opgeslagen zijn in de origine HashMap
                if (!uniqueORFList.isEmpty()) {
                    if (uniqueORFList.get(0).getHeaderHerkomst().equals(header)) {    // Twee losse if statements gemaakt aangezien hij beide statements gaat testen ondanks de && expressie
                        if (findID("ORF", "SEQ_ID", "" + seqID) != null) {
                            if (JOptionPane.showConfirmDialog(null,
                                    "Let op! Voor deze sequentie zijn "
                                    + "er al ORF's opgeslagen.\nWeet u "
                                    + "zeker dat u deze wilt vervangen?")
                                    == JOptionPane.YES_OPTION) {
                                deleteOldORFandBLAST();
                                saveORFs(uniqueORFList, seqID);
                            }
                        } else {
                            saveORFs(uniqueORFList, seqID);
                        }
                    }
                }
            }

        }
    }

    /**
     * @deprecated is niet gelukt om op een juiste manier te blasten en gegevens op te halen om op te slaan
     * methode om de blast resultaten op te slaan
     * @param ORFid
     */
    private void blastHandler(int ORFid){
//        for(BLAST blast : blastTable.getCurrectBLASTs()){
//            if(blast.getOrf().getHeaderHerkomst().equals(currentHeader)){
//                try {
//                    blast.parseFile();
//                    BLASTParser parser = blast.getParser();
//                    ArrayList<ArrayList<Object>> list = parser.getHitsData();
//                    for(ArrayList arrayList : list){
//                        for (Object o : arrayList){
//                            System.out.println((String)o);
//                        }
//                        int i = 1;
////                        saveBLASTResults((String)arrayList.get(0),
////                                (float)arrayList.get(0),
////                                (float)arrayList.get(0),
////                                (String)arrayList.get(0),
////                                (float)arrayList.get(0),
////                                (float)arrayList.get(0),
////                                (int)arrayList.get(0),orfID);
//                    }
//
//                } catch (Exception e){
//                    System.out.println(e.toString());
//                }
//            }
//        }
    }
}
