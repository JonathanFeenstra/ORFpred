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
        this.createIDsArrays();
        this.deleteData();

    }

    private void createIDsArrays() throws SQLException{
        this.sequentieIDs = getIDsToRemove(bestandID,"SEQUENTIE","BESTAND_ID");
        for(String seqid : this.sequentieIDs){
            System.out.println(5);
            this.orfIDs.addAll(getIDsToRemove(seqid,"ORF","SEQ_ID"));
        }
        for(String orfid : this.orfIDs){
            System.out.println(6);
            this.blastIDs.addAll(getIDsToRemove(orfid,"BLAST_RESULTAAT","ORF_ID"));
        }
    }

    private void deleteData() throws SQLException{
        deleteRowsByArray(blastIDs,"BLAST_RESULTAAT","RESULT_ID");
        System.out.println(2);
        deleteRowsByArray(orfIDs,"ORF","ORF_ID");
        System.out.println(3);
        deleteRowsByArray(sequentieIDs,"SEQUENTIE","SEQ_ID");
        System.out.println(4);
        connector.sentDeleteQuery("BESTAND","BESTAND_ID ="+bestandID);
    }

    private ArrayList<String> getIDsToRemove(String lookUpID, String table, String lookUpColumn) throws SQLException{
        ArrayList<String> foundIDs = new ArrayList<>();
        ResultSet resultSet = connector.sentFeedbackQuery("SELECT * FROM "+table+" WHERE "+lookUpColumn+" = "+lookUpID);
        while (resultSet.next()){
            System.out.println(8);
            System.out.println(1);
            foundIDs.add(resultSet.getString(1));
        }
        return foundIDs;
    }

    private void deleteRowsByArray(ArrayList<String> ids, String table, String column) throws SQLException{
        for(String id : ids){
            connector.sentDeleteQuery(table,column+" = "+id);
        }
    }
}
