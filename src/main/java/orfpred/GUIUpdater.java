/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred;

import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import org.biojava.nbio.core.sequence.RNASequence;

/**
 * Class om de GUI te updaten.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class GUIUpdater { // TODO: Misschien handig om de GUI als variabele op te slaan zodat het niet steeds als argument hoeft te worden meegegeven

    /**
     * Procedure om bestanden in te laden.
     *
     * @param gui de betreffende GUI
     */
    public static void loadFile(GUI gui) {
        try {
            File selectedFile = FileHandler.selectFile(gui.getFrame());
            if (selectedFile != null) {
                FileHandler.setHeaderToSeq(selectedFile);
                if (FileHandler.getHeaderToSeq() != null) {
                    showHeaders(gui.getHeaderComboBox(), FileHandler.getHeaderToSeq().keySet().toArray(new String[FileHandler.getHeaderToSeq().keySet().size()]));
                    // TODO: Dit is niet echt nodig als ze al enabled staan, misschien zorgen dat dit alleen de eerste keer gebeurt.
                    gui.getHeaderComboBox().setEnabled(true);
                    gui.getZoekButton().setEnabled(true);
                    showReadingFrames(gui, ReadingFramer.getReadingFrames(FileHandler.getHeaderToSeq().entrySet().iterator().next().getValue()));
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(gui.getFrame(), ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Toont de reading frames in de GUI.
     *
     * @param gui de betreffende GUI
     * @param readingFrames de te weergeven reading frames
     */
    public static void showReadingFrames(GUI gui, RNASequence[] readingFrames) {
        for (RNASequence readingFrame : readingFrames) {
            Document seqDocument = gui.getSeqTextPane().getDocument();
            try {
                seqDocument.insertString(seqDocument.getLength(), readingFrame.toString().replace('U', 'T') + "\n", new SimpleAttributeSet());
            } catch (BadLocationException ex) {
                JOptionPane.showMessageDialog(gui.getFrame(), ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Toont headers in het drop-down menu.
     *
     * @param headerComboBox
     * @param headers de header(s) uit het ingeladen bestand
     */
    public static void showHeaders(JComboBox<String> headerComboBox, String[] headers) {
        headerComboBox.setModel(new DefaultComboBoxModel<>(headers));
    }
}
