/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.gui;

import orfpred.sequence.ReadingFramer;
import orfpred.file.FileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
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
                    showReadingFrames(gui, ReadingFramer.getProteinFrames(FileHandler.getHeaderToSeq().entrySet().iterator().next().getValue()));
                }
            }
        } catch (FileNotFoundException ex) {
            GUI.showErrorMessage(ex, ex.getMessage());
        } catch (Exception ex) {
            GUI.showErrorMessage(ex, ex.getMessage());
        }
    }

    /**
     * Toont de reading frames in de GUI.
     *
     * @param gui de betreffende GUI
     * @param readingFrames de te weergeven reading frames
     */
    public static void showReadingFrames(GUI gui, ProteinSequence[] readingFrames) {
        Document seqDocument = gui.getSeqTextPane().getDocument();
        for (ProteinSequence readingFrame : readingFrames) {
            try {
                seqDocument.insertString(seqDocument.getLength(), readingFrame.toString() + "\n", new SimpleAttributeSet());
            } catch (BadLocationException ex) {
                GUI.showErrorMessage(ex, ex.getMessage());
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
