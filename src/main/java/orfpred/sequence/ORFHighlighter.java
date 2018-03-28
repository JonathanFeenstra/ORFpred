/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.sequence;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import orfpred.gui.GUI;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.transcription.Frame;

/**
 * Class voor het highlighten van ORF's.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class ORFHighlighter implements Runnable {

    private static Color highlightKleur = Color.CYAN;
    private static DefaultHighlightPainter painter;
    private static HashMap<Integer, ORF> positionToORF;
    private static boolean searchMode;
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
        try {
            predictORFs(readingFrames).forEach((ORF orf) -> {
                try {
                    highlightORF(targetGUI.getSeqTextPane(), orf);
                } catch (BadLocationException ex) {
                    targetGUI.showErrorMessage(ex, ex.getMessage());
                }
            });
        } catch (CompoundNotFoundException ex) {
            targetGUI.showErrorMessage(ex, "Sequentie bevat ongeldige karakters.");
        }
        addClickListener(targetGUI.getSeqTextPane());
    }

    /**
     * Voorspelt ORF's in een set reading frames.
     *
     * @param readingFrames de te doorzoeken reading frames
     * @return ArrayList van ORF's
     * @throws CompoundNotFoundException als karakter geen aminozuur is
     */
    public static ArrayList<ORF> predictORFs(ProteinSequence[] readingFrames) throws CompoundNotFoundException {
        ArrayList<ORF> predictedORFs = new ArrayList<>();
        int frameNum = 0;
        if (readingFrames != null) {
            for (ProteinSequence readingFrame : readingFrames) {
                Matcher matcher = searchMode ? Pattern.compile("M[^X*]+").matcher(readingFrame.toString()) : Pattern.compile("[^X*]+").matcher(readingFrame.toString());
                while (matcher.find()) {
                    if (matcher.group().length() - 2 >= minORFLength) {
                        predictedORFs.add(new ORF(Frame.values()[frameNum], searchMode ? matcher.start() : matcher.start(), matcher.end()));
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
                    offset += seqPane.getText().split(System.getProperty("line.separator"))[3].length();
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
                    EventQueue.invokeLater(() -> {
                        if (positionToORF.containsKey(e.getDot())) {
                            targetGUI.showORFPopUp(positionToORF.get(e.getDot()));
                        }
                    });
                }
            } catch (Exception ex) {
                // Negeer: er wordt buiten de tekst geklikt
            }
        });
        addHoverListener(seqTextPane);
    }

    /**
     * Voegt MouseMotionListener toe om het cursoricoon te veranderen bij het
     * hoveren over een gemarkeerd ORF.
     *
     * @param seqTextPane de te beluisteren JTextPane
     */
    public void addHoverListener(JTextPane seqTextPane) {
        for (MouseMotionListener hoverListener : seqTextPane.getMouseMotionListeners()) {
            seqTextPane.removeMouseMotionListener(hoverListener);
        }
        seqTextPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    if (isHighlighted[seqTextPane.viewToModel(e.getPoint())]) {
                        seqTextPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        seqTextPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // Negeer: muis valt buiten tekst
                }
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
     * @param length de in te stellen lengte
     * @throws NumberFormatException als getal te laag is
     */
    public static void setMinORFLength(int length) throws NumberFormatException {
        if (length > 1) {
            minORFLength = length;
        } else {
            throw new NumberFormatException();
        }
    }

    /**
     * searchMode setter
     *
     * @param sM zoekmodus: true = prokaryoot, false = eukaryoot.
     */
    public static void setSearchMode(boolean sM) {
        searchMode = sM;
    }

}
