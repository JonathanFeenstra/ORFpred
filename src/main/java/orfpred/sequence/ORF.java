/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.sequence;

import org.biojava.nbio.core.sequence.transcription.Frame;

/**
 * Deze class slaat attributen op per Open Reading Frame.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class ORF { // TODO: Overerven uit BioJava sequentie: eiwit of nucleotide

    private Frame readingFrame;
    private int start, stop;
    // TODO: Meer attributen opslaan

    /**
     * Constructor.
     *
     * @param frame de reading frame
     * @param startPos de startpositie
     * @param stopPos de stoppositie
     * @throws InvalidORFLengthException als lengte niet deelbaar is door 3.
     */
    public ORF(Frame frame, int startPos, int stopPos) throws InvalidORFLengthException {
        if ((stopPos - startPos) % 3 != 0) {
            throw new InvalidORFLengthException(startPos, stopPos);
        }
        this.readingFrame = frame;
        this.start = startPos;
        this.stop = stopPos;
        // TODO: Meer attributen
    }

    /**
     * Exception bij ORF lengtes die niet deelbaar zijn door 3.
     */
    class InvalidORFLengthException extends Exception {

        /**
         * Parameterloze Constructor.
         */
        public InvalidORFLengthException() {
            super("De ORF lengte moet deelbaar zijn door 3");
        }

        /**
         * Constructor met start en eindpositie.
         *
         * @param start startpositie van ORF
         * @param stop stoppositie van ORF
         */
        public InvalidORFLengthException(int start, int stop) {
            super("De ORF lengte moet deelbaar zijn door 3.\n"
                    + "Startpositie " + start + " en stoppositie " + stop
                    + "resulteren in een lengte van " + (stop - start) + ".");
        }
    }
}
