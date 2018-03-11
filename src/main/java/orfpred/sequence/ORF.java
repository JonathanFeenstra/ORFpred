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

    private final Frame readingFrame;
    private final int start;
    private final int stop;
    // TODO: Meer attributen opslaan

    /**
     * Constructor.
     *
     * @param frame de reading frame
     * @param startPos de startpositie
     * @param stopPos de stoppositie
     */
    public ORF(Frame frame, int startPos, int stopPos) {
        this.readingFrame = frame;
        this.start = startPos;
        this.stop = stopPos;
        // TODO: Meer attributen
    }

    /**
     * @return readingFrame
     */
    public Frame getReadingFrame() {
        return readingFrame;
    }

    /**
     * @return start
     */
    public int getStart() {
        return start;
    }

    /**
     * @return stop
     */
    public int getStop() {
        return stop;
    }
}