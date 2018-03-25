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

/**
 * Class om de ORFPredDB aan te vullen
 * @author Projectgroep 9
 */
public class DatabaseSaver {

    private DatabaseConnector connector;

    /**
     * Constructor voor de DatabaseSaver
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     * @throws ClassNotFoundException wordt opgegooid als het programma geen link heeft met de ojdbc8.jar file
     */
    public DatabaseSaver() throws SQLException, ClassNotFoundException {
        this.connector = new DatabaseConnector();
    }

    /**
     * methode om bestand gegevens op te slaan in de db
     * @param naamBestand String met de naam van het bestand
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     */
    public void saveBestand(String naamBestand) throws SQLException{
        int id = getUniqueID("BESTAND");
        connector.sentInsertionQuery("BESTAND",""+id+",'"+naamBestand+"'");
    }

    /**
     * methode om de sequentie en gegevens op te slaan
     * @param header String met de header erin
     * @param sequentie DNASequentie (van biojava) met daarin de sequentie
     * @param bestandID int met de bestandID van het bestand die bij de sequentie hoort
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     */
    public void saveSequentie(String header, DNASequence sequentie, int bestandID) throws SQLException{
        int id = getUniqueID("SEQUENTIE");
        connector.sentInsertionQuery("SEQUENTIE",""+id+",'"+header+
                "','"+sequentie.toString()+"',"+bestandID);
    }

    /**
     * Methode om de ORF met gegevens op te slaan
     * @param orf ORF object met daarin de gegevens
     * @param sequentieID int met de sequentieID van de sequentie waar de ORF vandaan komt
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     */
    public void saveORF(ORF orf, int sequentieID) throws SQLException{
        int id = getUniqueID("ORF");
        String frame = parseFrameToString(orf.getReadingFrame());
        connector.sentInsertionQuery("ORF",""+id+",'"+frame+
                "',"+orf.getStart()+","+orf.getStop()+","+sequentieID);
    }


    /**
     * Methode om de blast resultaat gegevens op te slaan
     * @param description String met de omschrijving van de hit
     * @param bitScore float met daarin de bitscore
     * @param queryCoverage float met daarin de query coverage (mag max. 2 getallen voor de komma hebben anders SQLException)
     * @param eValue String met de eValue
     * @param identity float met de idenetity percentage (mag max. 2 getallen voor de komma hebben anders SQLException)
     * @param positives float met de positives percentage (mag max. 2 getallen voor de komma hebben anders SQLException)
     * @param gaps int met het aantal gaps
     * @param orfID int met de orfID die bij de ORF hoort waar de resultaten van zijn
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     */
    public void saveBLASTResults(String description, float bitScore, float queryCoverage,
                                 String eValue, float identity, float positives, int gaps, int orfID) throws SQLException{
        int id = getUniqueID("BLAST_RESULTAAT");
        connector.sentInsertionQuery("BLAST_RESULTAAT",""+id+",'"+description+
                "',"+bitScore+","+queryCoverage+",'"+eValue+"',"+identity+","+positives+","+gaps+","+orfID);
    }

    /**
     * Methode om een unieke ID te maken.
     * (*)Kan mogelijk problemen leveren als meerdere gebruikers te gelijkertijd ID's ophalen.
     * @param table String met daarin de naam van de table waarin gekeken moet worden
     * @return een int met een unieke(*) ID
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     */
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

    /**
     * Hulp methode om ID's te vinden
     * @param table String met de table naam waarin gezocht moet worden
     * @param column String met de column naam waarin gezocht moet worden
     * @param search String met de zoek term waarnaar gezocht moet worden
     * @return een int met daarin de ID die bij de rij hoort van het zoek resultaat
     * @throws SQLException wordt opgegooid als er een exception optreed bij de SQL server
     * @throws NoSuchFieldError wordt opgegegooid als er geen resultaat is gevonden
     */
    public int findID(String table, String column, String search) throws SQLException, NoSuchFieldError{
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM "+table);
        while (resultSet.next()){
            if (resultSet.getString(column).equals(search)){
                return Integer.parseInt(resultSet.getString(1));
            }
        }
        throw new NoSuchFieldError("Error: Searched field doesn't exist");
    }

    /**
     * Methode om van een Frame object een frame String te maken
     * @param frame Frame object die geparsed moet worden
     * @return String met daarin de gewenste String van het object
     */
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