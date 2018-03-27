/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.gui;

import orfpred.sequence.ORF;
import java.awt.Font;
import javax.swing.*;

/**
 * Deze class toont een popup waarin de parameters voor het BLASTen ingesteld
 * kunnen worden.
 *
 * @author Projectgroep 9
 */
public class BLASTPopUp extends javax.swing.JFrame {

    private final ORF selectedORF;

    private JButton BLASTButton;
    private JComboBox<String> algoritmeComboBox, databaseComboBox;
    private JTextField evalueTekstField;
    
    private final Font LABEL_FONT = new Font("Arial", 1, 11);

    /**
     * Constructor.
     *
     * @param orf het geselecteerde ORF
     */
    public BLASTPopUp(ORF orf) {
        this.selectedORF = orf;
        initComponents();
    }

    /**
     * Maakt popup componenten aan.
     */
    private void initComponents() {

        algoritmeComboBox = new JComboBox<>();
        databaseComboBox = new JComboBox<>();
        JLabel algoritmeLabel = new JLabel(), databaseLabel = new JLabel(), evalueLabel = new JLabel();
        evalueTekstField = new JTextField();
        BLASTButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BLAST instellingen");
        setLocationByPlatform(true);

        algoritmeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"blastp", "blastn", "tblastn", "tblastx"}));
        databaseComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Non-redundant protein sequences (NR)", "Swiss-Prot"}));

        algoritmeLabel.setFont(LABEL_FONT);
        algoritmeLabel.setText("Algoritme:");

        databaseLabel.setFont(LABEL_FONT);
        databaseLabel.setText("Database:");

        evalueLabel.setFont(LABEL_FONT);
        evalueLabel.setText("E-value cut-off:");

        evalueTekstField.setText(" ");
        // evalueTekstField.addActionListener();

        BLASTButton.setFont(LABEL_FONT);
        BLASTButton.setText("BLAST");
        // BLASTButton.addActionListener();
        
        //<editor-fold defaultstate="collapsed" desc="Layout">
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(databaseLabel, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(algoritmeLabel)
                                                        .addComponent(evalueLabel, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(evalueTekstField)
                                                        .addComponent(databaseComboBox, 0, 230, Short.MAX_VALUE)
                                                        .addComponent(algoritmeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(BLASTButton, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(algoritmeComboBox, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(algoritmeLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(databaseComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(databaseLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(evalueLabel)
                                        .addComponent(evalueTekstField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BLASTButton)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    //</editor-fold>

        pack();
    }

}
