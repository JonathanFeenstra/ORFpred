
/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.database;

/**
 *
 *
 * @author Projectgroep 10
 */
public enum SearchType {
    HEADER(new String[]{"header", "Header"}),
    BESTAND(new String[]{"bestand", "file"});

    private final String[] filter;

    /**
     * Constructor
     *
     * @param s1 array van benamingen
     */
    SearchType(String[] s1) {
        filter = s1;
    }

    /**
     * @return type
     */
    public String getType() {
        return this.filter[0];
    }
}
