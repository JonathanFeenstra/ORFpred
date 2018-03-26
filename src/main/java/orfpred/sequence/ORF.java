/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.sequence;

import org.biojava.nbio.core.exceptions.ParserException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.transcription.Frame;

/**
 * Deze class slaat attributen op per Open Reading Frame.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class ORF extends DNASequence {

    private final Frame readingFrame;
    private final String sequence;
    private final int start, stop;

    /**
     * Constructor.
     *
     * @param frame de reading frame
     * @param seq de sequentie
     * @param startPos de startpositie
     * @param stopPos de stoppositie
     */
    public ORF(Frame frame, String seq, int startPos, int stopPos) {
        this.readingFrame = frame;
        this.start = startPos;
        this.stop = stopPos;
        this.sequence = seq;
    }

    /**
     * @return readingFrame
     */
    public Frame getReadingFrame() {
        return readingFrame;
    }

    /**
     * @return sequence
     */
    public String getSequence() {
        return sequence;
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

    /**
     * Methode om van een Frame object een frame String te maken
     *
     * @param frame Frame object die geparsed moet worden
     * @return String met daarin de gewenste String van het object
     * @throws ParserException bij onbekende frames
     */
    public static String parseFrameToString(Frame frame) throws ParserException {
        if (null == frame) {
            return "-3";
        } else {
            switch (frame) {
                case ONE:
                    return "+1";
                case TWO:
                    return "+2";
                case THREE:
                    return "+3";
                case REVERSED_ONE:
                    return "-1";
                case REVERSED_TWO:
                    return "-2";
                case REVERSED_THREE:
                    return "-3";
                default:
                    break;
            }
        }
        throw new ParserException("Error: Unkown frame found!");
    }
}
