/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred;

import java.awt.Color;

/**
 * Class voor het highlighten van ORF's.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class ORFHighlighter {

    private static Color highlightKleur = Color.CYAN;

    /**
     * Highlight een ORF.
     */
    public static void highlightORF() {
        // TODO: ORF highlighten
    }

    /**
     *
     * @param highlightKleur
     */
    public static void setHighlightKleur(Color highlightKleur) {
        ORFHighlighter.highlightKleur = highlightKleur;
    }

    /**
     * @return de highlightkleur
     */
    public static Color getHighlightKleur() {
        return highlightKleur;
    }
}
