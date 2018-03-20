package orfpred.database;

public enum SearchType {
    HEADER (new String[]{"header","Header"}),
    BESTAND(new String[]{"bestand","file"});

    private final String[] filter;

    SearchType(String[] s1){
        filter = s1;
    }

    public String getType(){
        return this.filter[0];
    }
}
