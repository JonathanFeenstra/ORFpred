/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.RNASequence;
import org.biojava.nbio.core.sequence.transcription.Frame;

/**
 * Class om de reading frames te bepalen.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class ReadingFramer {

    /**
     * Geeft RNA reading frames voor gegeven DNA sequentie.
     *
     * @param dnaSeq de DNA sequentie
     * @return array van reading frames
     */
    public static RNASequence[] getReadingFrames(DNASequence dnaSeq) {
        RNASequence[] readingFrames = new RNASequence[6];
        for (int i = 0; i <= 5; i++) {
            readingFrames[i] = dnaSeq.getRNASequence(Frame.getAllFrames()[i]);
        }
        return readingFrames;
    }
    
    /**
     * Geeft eiwit reading frames voor gegeven DNA sequentie.
     *
     * @param dnaSeq de DNA sequentie
     * @return array van eiwit reading frames
     */
    public static ProteinSequence[] getProteinFrames(DNASequence dnaSeq) {
        ProteinSequence[] proteinFrames = new ProteinSequence[6];
        RNASequence[] rnaFrames = getReadingFrames(dnaSeq);
        for (int i = 0; i <= 5; i++) {
            proteinFrames[i] = rnaFrames[i].getProteinSequence();
        }
        return proteinFrames;
    }

}

