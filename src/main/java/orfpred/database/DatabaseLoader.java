/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.database;

import orfpred.sequence.ORF;
import org.biojava.nbio.core.exceptions.*;
import org.biojava.nbio.core.sequence.transcription.Frame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class om data uit de database in te laden.
 *
 * @author Projectgroep 9
 */
public class DatabaseLoader {

    private final DatabaseConnector connector;

    /**
     * Constructor.
     *
     * @throws SQLException bij problemen met de connectie
     * @throws ClassNotFoundException als de vereiste class mist
     * @throws MissingResourceException als een vereist argument mist
     */
    public DatabaseLoader() throws SQLException, ClassNotFoundException {
        this.connector = new DatabaseConnector();
    }

    /**
     * Methode om de opgeslagen bestand namen op te halen
     *
     * @return Een genested ArrayList met daarin de bestand id en naam per
     * ArrayList bijv. [["1","bestand1"],["2","bestand2"]]
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public ArrayList<ArrayList<String>> getStoredFiles() throws SQLException {

        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM BESTAND");
        return parseResultSetToArray(new String[]{"BESTAND_ID", "NAAM"}, resultSet);
    }

    /**
     * Methode om de headers en sequentie op te halen die bij een bestandsID
     * horen
     *
     * @param bestandID int met daarin de bestandsID
     * @return Een genested ArrayList met daarin de sequentieID, header en de
     * sequentie per ArrayList bijv.
     * [["1",">header","agaga"],["2",">header2","tatata"]]
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public ArrayList<ArrayList<String>> getHeadersFromFile(int bestandID) throws SQLException {
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT SEQ_ID, HEADER, "
                + "SEQUENTIE FROM SEQUENTIE WHERE BESTAND_ID = " + bestandID);
        return parseResultSetToArray(new String[]{"SEQ_ID", "HEADER", "SEQUENTIE"}, resultSet);
    }

    /**
     * Methode om de ORF's op te halen uit de db die bij een sequentie ID horen
     *
     * @param seqID int met de sequentie ID waarvan de ORF's moet worden
     * opgehaald
     * @param seq String met daarin de sequentie waaruit de orf sequentie moet
     * worden gehaald voor het ORF object
     * @return Een HashMap met als key de ORF ID en als value een ORF object
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     * @throws CompoundNotFoundException als karakter geen aminozuur/nucleotide
     * is
     */
    public HashMap<Integer, ORF> getORFFromDB(int seqID, String seq) throws SQLException, CompoundNotFoundException {
        HashMap<Integer, ORF> orfList = new HashMap<>();
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT ORF_ID, FRAME, "
                + "START_POS, END_POS FROM ORF WHERE SEQ_ID = " + seqID);
        ArrayList<Integer> orfIDMetBlast = getORFIDMetBLAST();
        while (resultSet.next()) {
            int start = Integer.parseInt(resultSet.getString("START_POS")),
                    end = Integer.parseInt(resultSet.getString("END_POS"));
            Frame frame = getFrame(resultSet);
            Integer orfID = Integer.parseInt(resultSet.getString("ORF_ID"));
            if (orfIDMetBlast.contains(orfID)) {
                orfList.put(orfID, new ORF(orfID, frame, start, end));
            } else {
                orfList.put(orfID, new ORF(frame, start, end));
            }
        }
        return orfList;
    }

    /**
     * Methode die van de frame string de bijbehoordende Frame object ophaald
     *
     * @param resultSet De ResultSet waarin de frame String zich bevindt
     * @return de Frame die bij de frame String past
     * @throws SQLException wordt opgegooid als de FRAME colomn niet wordt
     * gevonden
     * @throws ParserException wordt opgegooid als de inhoud van de FRAME colomn
     * niet overeenkomt met de bestaande frames
     */
    public Frame getFrame(ResultSet resultSet) throws SQLException, ParserException {
        String frame = resultSet.getString("FRAME");
        switch (frame) {
            case "+1":
                return Frame.ONE;
            case "+2":
                return Frame.TWO;
            case "+3":
                return Frame.THREE;
            case "-1":
                return Frame.REVERSED_ONE;
            case "-2":
                return Frame.REVERSED_TWO;
            case "-3":
                return Frame.REVERSED_THREE;
            default:
                break;
        }
        throw new ParserException("Error: Unkown frame found!");
    }

    /**
     * Methode om de opgeslagen BLAST resultaten op te halen uit de db
     *
     * @param orfID int met daarin de ORF ID waarvan de BLAST resultaten moet
     * worden op gehaald
     * @return een genested ArrayList met daarin resultaat ID, omschrijving,
     * bitscore, query coverage, e-value, identity, positives en gaps per
     * gevonde resultaat
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public ArrayList<ArrayList<String>> getBlastResult(int orfID) throws SQLException {
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM BLAST_RESULTAAT WHERE ORF_ID = " + orfID);
        return parseResultSetToArray(new String[]{"RESULT_ID", "DESCRIPTION", "BITSCORE",
            "QUERY_COVERAGE", "EVALUE", "IDENTITY", "POSITIVES", "GAPS"}, resultSet);
    }

    /**
     * Methode om de gevonden resultaten te verwerken in een genestde ArrayList
     *
     * @param columns String Array met de kolom namen waarvan de resultaten
     * moeten worden verzameld
     * @param resultSet ResultSet waarin de resultaten van de SQL query in
     * bevinden
     * @return een genested ArrayList met de gewenste resultaten
     * @throws SQLException wordt opgegooid als er een exception optreed bij de
     * SQL server
     */
    public ArrayList<ArrayList<String>> parseResultSetToArray(String[] columns, ResultSet resultSet) throws SQLException {
        ArrayList<ArrayList<String>> createdArray = new ArrayList<>();
        int counter = 0;
        while (resultSet.next()) {
            createdArray.add(new ArrayList<>());
            for (String column : columns) {
                createdArray.get(counter).add(resultSet.getString(column));
            }
            counter++;
        }
        return createdArray;
    }

    public ArrayList<Integer> getORFIDMetBLAST() throws SQLException {
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT ORF_ID FROM BLAST_RESULTAAT");
        ArrayList<Integer> idList = new ArrayList<>();
        while (resultSet.next()) {
            idList.add(Integer.parseInt(resultSet.getString(1)));
        }
        return idList;
    }
}
