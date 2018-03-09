/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */

package orfpred.gui;

import orfpred.sequence.ReadingFramer;
import orfpred.file.FileHandler;
import java.io.FileNotFoundException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.*;
import org.biojava.nbio.core.sequence.ProteinSequence;

/**
 * Class om de GUI te updaten. Methoden uit deze class dienen aangeropen te
 * worden via EventQueue.invokeLater().
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class GUIUpdater {

    private final GUI targetGUI;
            
    /**
     * Constructor.
     * 
     * @param gui
     */
    public GUIUpdater(GUI gui) {
        this.targetGUI = gui;
    }
    
    /**
     * Procedure om bestanden in te laden.
     */
    public void loadFile() {
        try {
            FileHandler.selectFile(targetGUI.getFrame());
            if (FileHandler.getSelectedFile() != null) {
                FileHandler.readHeaderToSeq();
                if (FileHandler.getHeaderToSeq() != null) {
                    showHeaders(FileHandler.getHeaders());
                    // TODO: Dit is niet nodig als ze al enabled staan, misschien zorgen dat dit alleen de eerste keer gebeurt
                    targetGUI.getHeaderComboBox().setEnabled(true);
                    targetGUI.getZoekButton().setEnabled(true);
                    showReadingFrames(ReadingFramer.getProteinFrames(FileHandler.getHeaderToSeq().entrySet().iterator().next().getValue()));
                }
            }
        } catch (FileNotFoundException ex) {
            targetGUI.showErrorMessage(ex, ex.getMessage());
        } catch (Exception ex) {
            targetGUI.showErrorMessage(ex, ex.getMessage());
        }
    }

    /**
     * Toont de reading frames in de GUI.
     *
     * @param readingFrames de te weergeven reading frames
     */
    public void showReadingFrames(ProteinSequence[] readingFrames) {
        Document seqDocument = targetGUI.getSeqTextPane().getDocument();
        for (ProteinSequence readingFrame : readingFrames) {
            try {
                seqDocument.insertString(seqDocument.getLength(), readingFrame.toString() + "\n", new SimpleAttributeSet());
            } catch (BadLocationException ex) {
                targetGUI.showErrorMessage(ex, ex.getMessage());
            }
        }
    }

    /**
     * Toont headers in het drop-down menu.
     *
     * @param headers de header(s) uit het ingeladen bestand
     */
    public void showHeaders(String[] headers) {
        targetGUI.getHeaderComboBox().setModel(new DefaultComboBoxModel<>(headers));
    }
}
