/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.database;

import orfpred.sequence.ORF;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.exceptions.ParserException;
import org.biojava.nbio.core.sequence.transcription.Frame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
        this.connector = new DatabaseConnector();
    }


    public ArrayList<ArrayList<String>> getStoredFileNames()throws SQLException{

        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM BESTAND");
        return parseResultSetToArray(new String[]{"BESTAND_ID","NAAM"},resultSet);
    }

    public ArrayList<ArrayList<String>> getHeadersFromFile(String bestandID)throws SQLException, CompoundNotFoundException{
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT SEQ_ID, HEADER, " +
                "SEQUENTIE FROM SEQUENTIE WHERE BESTAND_ID = "+bestandID);
        return parseResultSetToArray(new String[]{"SEQ_ID","HEADER","SEQUENTIE"},resultSet);
    }

    public HashMap<String, ORF> getORFFromDB(String seqID, String seq) throws SQLException, ParserException{
        HashMap<String, ORF> orfList = new HashMap<>();
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT ORF_ID, FRAME, " +
                "START_POS, END_POS FROM ORF WHERE SEQ_ID = "+seqID);
        while (resultSet.next()){
            int start = Integer.parseInt(resultSet.getString("START_POS")),
                    end = Integer.parseInt(resultSet.getString("END_POS"));
            Frame frame = getFrame(resultSet);
            String orfID = resultSet.getString("ORF_ID");
            orfList.put(orfID, new ORF(frame, seq.substring(start,end+1), start, end));
        }
        return orfList;
    }

    public Frame getFrame(ResultSet resultSet) throws SQLException, ParserException{
        String frame = resultSet.getString("FRAME");
        switch (frame){
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

    public ArrayList<ArrayList<String>> getBlastResult(String orfID) throws SQLException{
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM BLAST_RESULTAAT WHERE ORF_ID = "+orfID);
        return parseResultSetToArray(new String[]{"RESULT_ID","DESCRIPTION","BITSCORE",
                "QUERY_COVERAGE","EVALUE","IDENTITY","POSITIVES","GAPS"},resultSet);
    }

    public ArrayList<ArrayList<String>> parseResultSetToArray(String[] headers, ResultSet resultSet) throws SQLException{
        ArrayList<ArrayList<String>> createdArray = new ArrayList<>();
        int counter = 0;
        while (resultSet.next()){
            createdArray.add(new ArrayList<>());
            for(String header : headers){
                createdArray.get(counter).add(resultSet.getString(header));
            }
            counter++;
        }
        return createdArray;
    }
}
