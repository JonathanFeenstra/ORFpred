/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra,
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.database;

import orfpred.sequence.ORF;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.transcription.Frame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;

public class DatabaseSaver {

    private DatabaseConnector connector;

    public static void main(String[] args) {
        try{
            DatabaseSaver saver = new DatabaseSaver();
            saver.saveORF(new ORF(Frame.REVERSED_ONE,"agagagagagagagattt",2,5),5);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public DatabaseSaver() throws SQLException, ClassNotFoundException, MissingResourceException {
        this.connector = new DatabaseConnector();
    }

    public void saveBestand(String naamBestand) throws SQLException{
        int id = getUniqueID("BESTAND");
        connector.sentOneWayQuery("BESTAND",""+id+",'"+naamBestand+"'");
    }

    public void saveSequentie(String header, DNASequence sequentie, int bestandID) throws SQLException, NoSuchFieldError{
        int id = getUniqueID("SEQUENTIE");
        connector.sentOneWayQuery("SEQUENTIE",""+id+",'"+header+
                "','"+sequentie.toString()+"',"+bestandID);
    }

    public void saveORF(ORF orf, int sequentieID) throws SQLException{
        int id = getUniqueID("ORF");
        String frame = parseFrameToString(orf.getReadingFrame());
        connector.sentOneWayQuery("ORF",""+id+",'"+frame+
                "',"+orf.getStart()+","+orf.getStop()+","+sequentieID);
    }

    public void saveBLASTResults(String description, float bitScore, float queryCoverage,
                                 String eValue, float identity, float positives, int gaps, int orfID) throws SQLException{
        int id = getUniqueID("BLAST_RESULTAAT");
        connector.sentOneWayQuery("BLAST_RESULTAAT",""+id+",'"+description+
                "',"+bitScore+","+queryCoverage+",'"+eValue+"',"+identity+","+positives+","+gaps+","+orfID);
    }

    public int getUniqueID(String table) throws SQLException{
        ResultSet resultset = connector.sentFeedbackQuery("SELECT * FROM "+table);
        int maxID = 0;
        int id;
        while (resultset.next()){
            id = Integer.parseInt(resultset.getString(1));
            if (id > maxID){
                maxID = id;
            }
        }
        return maxID+1;
    }

    public int findID(String table, String column, String search) throws SQLException, NoSuchFieldError{
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM "+table);
        while (resultSet.next()){
            if (resultSet.getString(column).equals(search)){
                return Integer.parseInt(resultSet.getString(1));
            }
        }
        throw new NoSuchFieldError("Error: Searched field doesn't exist");
    }

    public String parseFrameToString(Frame frame){
        if(frame == Frame.ONE){
            return "+1";
        } else if (frame == Frame.TWO){
            return "+2";
        } else if (frame == Frame.THREE){
            return "+3";
        } else if (frame == Frame.REVERSED_ONE){
            return "-1";
        } else if (frame == Frame.REVERSED_TWO){
            return "-2";
        } else {
            return "-3";
        }
    }
}
