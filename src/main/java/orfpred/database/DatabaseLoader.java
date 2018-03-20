package orfpred.database;

import java.sql.SQLException;
import java.util.MissingResourceException;

public class DatabaseLoader {

    private DatabaseConnector connector;

    public DatabaseLoader() throws SQLException, ClassNotFoundException, MissingResourceException {
        this.connector = new DatabaseConnector("","","");
    }

    public void getData(String search, SearchType type){

    }
}
