package orfpred.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseDeleter {

    private DatabaseConnector connector;
    private String bestandID;
    private ArrayList<String> sequentieIDs, orfIDs = new ArrayList<>(), blastIDs = new ArrayList<>();

    public DatabaseDeleter(String bestandID) throws SQLException, ClassNotFoundException{
        this.connector = new DatabaseConnector();
        this.bestandID = bestandID;
        this.sequentieIDs = getIDsToRemove(bestandID,"SEQUENTIE","BESTAND_ID");
        for(String seqid : this.sequentieIDs){
            this.orfIDs.addAll(getIDsToRemove(seqid,"ORF","SEQ_ID"));
        }
        for(String orfid : this.orfIDs){
            this.blastIDs.addAll(getIDsToRemove(orfid,"BLAST_RESULTAAT","ORF_ID"));
        }

    }

    public ArrayList<String> getIDsToRemove(String lookUpID, String table, String lookUpColumn) throws SQLException{
        ArrayList<String> foundIDs = new ArrayList<>();
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM "+table+" WHERE "+lookUpColumn+" = "+lookUpID);
        while (resultSet.next()){
            foundIDs.add(resultSet.getString(1));
        }
        return foundIDs;
    }

    public void deleteRowsByArray(ArrayList<String> ids, String table, String column) throws SQLException{
        for(String id : ids){
            connector.sentDeleteQuery(table,column+" = "+id);
        }
    }
}
