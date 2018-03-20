/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.sequence;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import orfpred.gui.GUI;
import orfpred.gui.GUIUpdater;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.transcription.Frame;

/**
 * Class voor het highlighten van ORF's.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class ORFHighlighter implements Runnable {

    private static Color highlightKleur = Color.CYAN;
    private static DefaultHighlightPainter painter;
    private static HashMap<Integer, ORF> positionToORF;
    private static int minORFLength = 30;
    private final ProteinSequence[] readingFrames;
    private final GUI targetGUI;
    private boolean[] isHighlighted;

    /**
     * Constructor.
     *
     * @param rfs de te doorzoeken reading frames.
     * @param gui de betreffende GUI
     */
    public ORFHighlighter(ProteinSequence[] rfs, GUI gui) {
        this.readingFrames = rfs;
        this.targetGUI = gui;
        painter = new DefaultHighlightPainter(highlightKleur);
    }

    @Override
    public void run() {
        targetGUI.getSeqTextPane().getHighlighter().removeAllHighlights();
        isHighlighted = new boolean[targetGUI.getSeqTextPane().getText().length()];
        positionToORF = new HashMap<>();
        predictORFs(readingFrames).forEach((ORF orf) -> {
            try {
                highlightORF(targetGUI.getSeqTextPane(), orf);
            } catch (BadLocationException ex) {
                targetGUI.showErrorMessage(ex, ex.getMessage());
            }
        });
        addClickListener(targetGUI.getSeqTextPane());
    }

    /**
     * Voorspelt ORF's in een set reading frames.
     *
     * @param readingFrames de te doorzoeken reading frames
     * @return ArrayList van ORF's
     */
    public static ArrayList<ORF> predictORFs(ProteinSequence[] readingFrames) {
        ArrayList<ORF> predictedORFs = new ArrayList<>();
        int frameNum = 0;
        if (readingFrames != null) {
            for (ProteinSequence readingFrame : readingFrames) {
                Matcher matcher = Pattern.compile("\\*[^*]+?\\*").matcher(readingFrame.toString());
                while (matcher.find()) {
                    if (matcher.group().length() - 2 >= minORFLength) {
                        predictedORFs.add(new ORF(Frame.values()[frameNum], matcher.group().substring(1, matcher.group().length() - 1), matcher.start() + 1, matcher.end() - 1));
                    }
                }
                frameNum++;
            }
        }
        return predictedORFs;
    }

    /**
     * Highlight een ORF.
     *
     * @param seqPane de JTextPane om in te highlighten
     * @param orf het te highlighten ORF
     * @throws BadLocationException bij verkeerde locaties
     */
    public void highlightORF(JTextPane seqPane, ORF orf) throws BadLocationException {
        int offset = 0;
        for (int i = 0; i < orf.getReadingFrame().ordinal(); i++) {
            offset += readingFrames[i].getLength() * 3 + 1;
            switch (i) {
                case 1:
                    offset++;
                    break;
                case 2:
                    offset += seqPane.getText().split("\n")[3].length();
                    break;
                case 4:
                    offset++;
                    break;
            }
        }
        int start = offset + orf.getStart() * 3, stop = offset + orf.getStop() * 3;
        seqPane.getHighlighter().addHighlight(start, stop, painter);
        for (int pos = start; pos < stop; pos++) {
            isHighlighted[pos] = true;
            positionToORF.put(pos, orf);
        }
    }

    /**
     * Voegt CaretListener toe aan JTextPane die het BLAST dialoogvenster opent
     * bij het klikken op een gemarkeerd ORF.
     *
     * @param seqTextPane de te beluisteren JTextPane
     */
    public void addClickListener(JTextPane seqTextPane) {
        for (CaretListener clickListener : seqTextPane.getCaretListeners()) {
            seqTextPane.removeCaretListener(clickListener);
        }
        seqTextPane.addCaretListener((CaretEvent e) -> {
            try {
                if (isHighlighted[e.getDot()]) {
                    JOptionPane.showInputDialog(positionToORF.get(e.getDot()).getSequence()); // TODO: Placeholder, zorg dat je echt kan blasten
                }
            } catch (Exception ex) {
                // Negeer: er wordt buiten de tekst geklikt
            }
        });
    }

    /**
     * highlightKleur setter.
     *
     * @param kleur
     */
    public static void setHighlightKleur(Color kleur) {
        highlightKleur = kleur;
    }

    /**
     * @return highlightKleur
     */
    public static Color getHighlightKleur() {
        return highlightKleur;
    }

    /**
     * minORFLength setter.
     *
     * @param length
     */
    public static void setMinORFLength(int length) {
        minORFLength = length;
    }

}
