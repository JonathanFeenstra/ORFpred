/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.sequence;

import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.transcription.Frame;

/**
 * Class om de reading frames te bepalen.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class ReadingFramer {

    /**
     * Geeft eiwit reading frames voor gegeven DNA-sequentie.
     *
     * @param dnaSeq de DNA-sequentie
     * @return array van eiwit reading frames
     */
    public static ProteinSequence[] getProteinFrames(DNASequence dnaSeq) {
        ProteinSequence[] proteinFrames = new ProteinSequence[6];
        for (int i = 0; i <= 5; i++) {
            RNASequence rnaFrame = dnaSeq.getRNASequence(Frame.values()[i]);
            proteinFrames[i] = rnaFrame.getProteinSequence();
        }
        return proteinFrames;
    }

}
