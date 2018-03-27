package orfpred.gui;

/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */


import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import orfpred.sequence.ORF;

/**
 * Deze class toont een popup met informatie over het aangeklikte ORF.
 *
 * @author Projectgroep 9
 */
public class ORFPopUp extends JFrame {

    private final ORF selectedORF;
    private final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    /**
     * Constructor
     *
     * @param orf het aangeklikte ORF
     */
    public ORFPopUp(ORF orf) {
        this.selectedORF = orf;
        this.showPopUp();
    }

    /**
     * Methode toont een popup die informatie geeft over het ORF en een optie om
     * te BLASTen.
     */
    public final void showPopUp() {
        setSize(400, 260);
        setTitle("Eigenschappen ORF");
        setIconImage(new ImageIcon(getClass().getResource("/orfpred.png")).getImage());
        setResizable(false);
        setLocationByPlatform(true);
        
        Container window = getContentPane();
        window.setLayout(new FlowLayout());

        JLabel selectedORFLabel = new JLabel("Eiwitsequentie van het geselecteerde ORF: ");
        selectedORFLabel.setFont(LABEL_FONT);
        window.add(selectedORFLabel);
        
        JTextArea seqArea = new JTextArea(selectedORF.getProteinSequence().toString());
        seqArea.setPreferredSize(new Dimension(330, 150));
        seqArea.setLineWrap(true);
        
        JScrollPane seqScrollPane = new JScrollPane(seqArea);
        window.add(seqScrollPane);
        
        JLabel startLabel = new JLabel("Startpositie ORF: " + Integer.toString(selectedORF.getStart())+ "     ");
        window.add(startLabel);

        JLabel eindLabel = new JLabel("Eindpositie ORF: " + Integer.toString(selectedORF.getStop()) + "     ");
        window.add(eindLabel);
        
        JLabel lengteLabel = new JLabel("Lengte ORF: " + Integer.toString(selectedORF.getProteinSequence().getLength()) + "     ");
        window.add(lengteLabel);
        
        JLabel frameLabel = new JLabel("Het ORF is gevonden in frame: " + ORF.parseFrameToString(selectedORF.getReadingFrame())+ "     ");
        window.add(frameLabel);

        JButton buttonBLAST = new JButton("BLAST ORF");
        buttonBLAST.addActionListener((ActionEvent e) -> {
            EventQueue.invokeLater(() -> {
                new BLASTPopUp(selectedORF);
            });
        });
        window.add(buttonBLAST); 
        
        setVisible(true);
    }

}
