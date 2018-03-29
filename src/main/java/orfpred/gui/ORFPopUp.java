package orfpred.gui;

/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import orfpred.blast.BLASTTable;
import orfpred.sequence.ORF;
import org.biojava.nbio.core.sequence.ProteinSequence;

/**
 * Deze class maakt een pop-up met informatie over het aangeklikte ORF.
 *
 * @author Projectgroep 9
 */
public class ORFPopUp extends JFrame {

    private final ProteinSequence[] shownReadingFrames;
    private final ORF selectedORF;
    private final BLASTTable blastTable;
    private final GUI gui;

    /**
     * Constructor.
     *
     * @param readingFrames de ingeladen reading frames
     * @param orf het aangeklikte ORF
     * @param table de BLAST resultaten tabel
     * @param gui de parent GUI
     */
    public ORFPopUp(ProteinSequence[] readingFrames, ORF orf, BLASTTable table, GUI gui) {
        this.shownReadingFrames = readingFrames;
        this.selectedORF = orf;
        this.blastTable = table;
        this.gui = gui;
        this.createPopUp();

    }

    /**
     * Methode toont een popup die informatie geeft over het ORF en een optie om
     * te BLASTen.
     */
    public final void createPopUp() {
        setSize(400, 270);
        setTitle("Eigenschappen ORF");
        setIconImage(new ImageIcon(getClass().getResource("/orfpred.png")).getImage());
        setResizable(false);
        setLocationByPlatform(true);

        Container window = getContentPane();
        window.setLayout(new FlowLayout());

        JLabel selectedORFLabel = new JLabel("Eiwitsequentie:");
        selectedORFLabel.setFont(GUI.LABEL_FONT);
        window.add(selectedORFLabel);

        JTextArea seqArea = new JTextArea(shownReadingFrames[selectedORF.getReadingFrame().ordinal()].toString().substring(selectedORF.getStart(), selectedORF.getStop()));
        seqArea.setPreferredSize(new Dimension(350, 150));
        seqArea.setLineWrap(true);

        JScrollPane seqScrollPane = new JScrollPane(seqArea);
        window.add(seqScrollPane);

        JLabel startLabel = new JLabel("Startpositie: " + Integer.toString(selectedORF.getStart()) + "    ");
        window.add(startLabel);

        JLabel eindLabel = new JLabel("Eindpositie: " + Integer.toString(selectedORF.getStop()) + "    ");
        window.add(eindLabel);

        JLabel lengteLabel = new JLabel("Lengte: " + Integer.toString(selectedORF.getStop() - selectedORF.getStart()) + "    ");
        window.add(lengteLabel);

        JLabel frameLabel = new JLabel("Frame: " + ORF.parseFrameToString(selectedORF.getReadingFrame()) + "    ");
        window.add(frameLabel);

        JButton buttonBLAST = new JButton("BLAST ORF");
        buttonBLAST.addActionListener((ActionEvent e) -> {
            EventQueue.invokeLater(() -> {
                new BLASTPopUp(selectedORF, blastTable, gui).setVisible(true);
                dispose();
            });
        });
        window.add(buttonBLAST);
    }

}
