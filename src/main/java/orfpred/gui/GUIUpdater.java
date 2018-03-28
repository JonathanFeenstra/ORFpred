/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.gui;

import java.io.File;

import orfpred.database.DatabaseLoader;
import orfpred.sequence.ReadingFramer;
import orfpred.file.FileHandler;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.*;
import orfpred.sequence.ORFHighlighter;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;

/**
 * Class om de GUI te updaten. Methoden uit deze class dienen aangeropen te
 * worden via EventQueue.invokeLater().
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class GUIUpdater {

    private final GUI targetGUI;
    private DatabaseLoader loader;
    private LinkedHashMap<String, DNASequence> headerToSeq;
    private ProteinSequence[] shownReadingFrames;
    private String fileName;
    private ArrayList<Integer> seqIDs;

    /**
     * Constructor.
     *
     * @param gui
     */
    public GUIUpdater(GUI gui) {
        this.targetGUI = gui;
    }

    /**
     * Procedure om lokale bestanden in te laden.
     */
    public void loadFile() {
        try {
            File selectedFile = FileHandler.selectFile(targetGUI.getFrame());
            if (selectedFile != null) {
                this.fileName = selectedFile.getName();
                headerToSeq = FileHandler.readHeaderToSeq(selectedFile);
                if (headerToSeq != null) {
                    showHeaders(headerToSeq.keySet().toArray(new String[headerToSeq.size()]));
                    showReadingFrames(ReadingFramer.getProteinFrames(headerToSeq.entrySet().iterator().next().getValue()));
                }
            }
            targetGUI.setDbFileLoaded(false);
        } catch (FileNotFoundException ex) {
            targetGUI.showErrorMessage(ex, ex.getMessage());
        } catch (NoSuchElementException ex) {
            targetGUI.showErrorMessage(ex, "Onjuist bestandsformaat. Controleer of het bestand headers (beginnend met '>') en DNA-sequenties bevat.");
        } catch (Exception ex) {
            targetGUI.showErrorMessage(ex, ex.getMessage());
        }
    }

    /**
     * Procedure om bestanden uit de database in te laden.
     *
     * @param bestandsID het ID van het geselecteerde bestand
     */
    public void loadDBFile(int bestandsID) {
        try {
            loader = new DatabaseLoader(this, targetGUI);
            headerToSeq = new LinkedHashMap<>();
            ArrayList<ArrayList<String>> headerAndSeqData = loader.getHeadersFromFile(bestandsID);
            seqIDs = new ArrayList<>();
            headerAndSeqData.forEach((row) -> {
                try {
                    seqIDs.add(Integer.parseInt(row.get(0)));
                    headerToSeq.put(row.get(1), new DNASequence(row.get(2)));
                } catch (CompoundNotFoundException ex) {
                    targetGUI.showErrorMessage(ex, "De volgende sequentie bevat een ongeldig karakter:\n"
                            + row.get(1) + "\n" + ex.getMessage());
                }
            });
            showHeaders(headerToSeq.keySet().toArray(new String[headerToSeq.size()]));
            showLoadedSeq();
            targetGUI.setDbFileLoaded(true);
        } catch (SQLException | ClassNotFoundException ex) {
            targetGUI.showErrorMessage(ex, ex.getMessage());
        } catch (NoSuchElementException ex) {
            targetGUI.showErrorMessage(ex, "Ingeladen bestand bevat geen headers.");
        } catch (CompoundNotFoundException ex) {
            targetGUI.showErrorMessage(ex, "Sequentie bevat ongeldig karakter.\n" + ex.getMessage());
        }
    }

    /**
     * Toont ingeladen sequentie.
     *
     * @throws SQLException bij problemen met de connectie met de database
     * @throws CompoundNotFoundException als sequentie ongeldig karakter bevat
     */
    public void showLoadedSeq() throws SQLException, CompoundNotFoundException {
        DNASequence loadedSeq = headerToSeq.get(targetGUI.getHeaderComboBox().getSelectedItem().toString());
        showHeaders(headerToSeq.keySet().toArray(new String[headerToSeq.size()]));
        showReadingFrames(ReadingFramer.getProteinFrames(loadedSeq));
        ORFHighlighter highlighter = new ORFHighlighter(shownReadingFrames, targetGUI);
        highlighter.highlightSavedORFs(loader.getORFFromDB(seqIDs.get(targetGUI.getHeaderComboBox().getSelectedIndex()), loadedSeq.toString()).values());
    }

    /**
     * Toont de reading frames en DNA sequentie in de GUI.
     *
     * @param readingFrames de te weergeven reading frames
     */
    public void showReadingFrames(ProteinSequence[] readingFrames) {
        targetGUI.getSeqTextPane().setText("");
        Document seqDocument = targetGUI.getSeqTextPane().getDocument();
        try {
            int lineNum = 0;
            for (ProteinSequence readingFrame : readingFrames) {
                lineNum++;
                String readingFrameWithSpaces = readingFrame.toString().replace("", "  ").trim();
                switch (lineNum) {
                    case 1:
                    case 4:
                        seqDocument.insertString(seqDocument.getLength(), " " + readingFrameWithSpaces + "\n", new SimpleAttributeSet());
                        break;
                    case 2:
                    case 5:
                        seqDocument.insertString(seqDocument.getLength(), "  " + readingFrameWithSpaces + "\n", new SimpleAttributeSet());
                        break;
                    case 3:
                        seqDocument.insertString(seqDocument.getLength(), "   " + readingFrameWithSpaces + "\n", new SimpleAttributeSet());
                        seqDocument.insertString(seqDocument.getLength(), headerToSeq.get(targetGUI.getHeaderComboBox().getSelectedItem().toString()).toString().toUpperCase() + "          \n", new SimpleAttributeSet());
                        break;
                    case 6:
                        seqDocument.insertString(seqDocument.getLength(), "   " + readingFrameWithSpaces + "\n1        ", new SimpleAttributeSet());
                        for (int i = 10; i <= headerToSeq.get(targetGUI.getHeaderComboBox().getSelectedItem().toString()).toString().length(); i += 10) {
                            seqDocument.insertString(seqDocument.getLength(), String.valueOf(i) + new String(new char[10 - Integer.toString(i).length()]).replace("\0", " "), new SimpleAttributeSet());
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (BadLocationException ex) {
            targetGUI.showErrorMessage(ex, ex.getMessage());
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

}
