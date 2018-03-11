/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.gui;

import java.io.File;
import orfpred.sequence.ReadingFramer;
import orfpred.file.FileHandler;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.*;
import org.biojava.nbio.core.sequence.DNASequence;
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
    private LinkedHashMap<String, DNASequence> headerToSeq;
    private ProteinSequence[] shownReadingFrames;

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
            File selectedFile = FileHandler.selectFile(targetGUI.getFrame());
            if (selectedFile != null) {
                headerToSeq = FileHandler.readHeaderToSeq(selectedFile);
                if (headerToSeq != null) {
                    showHeaders(headerToSeq.keySet().toArray(new String[headerToSeq.size()]));
                    showReadingFrames(ReadingFramer.getProteinFrames(headerToSeq.entrySet().iterator().next().getValue()));
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
        targetGUI.getSeqTextPane().setText("");
        Document seqDocument = targetGUI.getSeqTextPane().getDocument();
        for (ProteinSequence readingFrame : readingFrames) {
            try {
                seqDocument.insertString(seqDocument.getLength(), readingFrame.toString() + "\n", new SimpleAttributeSet());
            } catch (BadLocationException ex) {
                targetGUI.showErrorMessage(ex, ex.getMessage());
            }
        }
        if (!targetGUI.getZoekButton().isEnabled()) {
            targetGUI.getZoekButton().setEnabled(true);
        }
        shownReadingFrames = readingFrames;
    }

    /**
     * Toont headers in het drop-down menu.
     *
     * @param headers de header(s) uit het ingeladen bestand
     */
    public void showHeaders(String[] headers) {
        targetGUI.getHeaderComboBox().setModel(new DefaultComboBoxModel<>(headers));
        if (!targetGUI.getHeaderComboBox().isEnabled()) {
            targetGUI.getHeaderComboBox().setEnabled(true);
        }
    }

    /**
     * @return headerToSeq
     */
    public LinkedHashMap<String, DNASequence> getHeaderToSeq() {
        return headerToSeq;
    }

    /**
     * @return shownReadingFrames
     */
    public ProteinSequence[] getShownReadingFrames() {
        return shownReadingFrames;
    }
}