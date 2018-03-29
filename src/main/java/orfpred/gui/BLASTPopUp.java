/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.gui;

import orfpred.sequence.ORF;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.HashMap;
import javax.swing.*;
import orfpred.blast.BLAST;
import orfpred.blast.BLASTTable;

/**
 * Deze class toont een popup waarin de parameters voor het BLASTen ingesteld
 * kunnen worden.
 *
 * @author Projectgroep 9
 */
public class BLASTPopUp extends javax.swing.JFrame {

    private final ORF selectedORF;
    private final BLASTTable blastTable;
    private final GUI gui;

    private JButton BLASTButton;
    private JComboBox<String> algoritmeComboBox, databaseComboBox;
    private JTextField evalueTekstField;
    private static HashMap<Integer, Integer> versionControl = new HashMap<>();


    /**
     * Constructor.
     *
     * @param orf het geselecteerde ORF
     * @param table de BLAST resultaten tabel
     */
    public BLASTPopUp(ORF orf, BLASTTable table, GUI gui) {
        this.selectedORF = orf;
        this.blastTable = table;
        this.gui = gui;
        initComponents();
    }

    /**
     * Maakt popup componenten aan.
     */
    private void initComponents() {
        setIconImage(new ImageIcon(getClass().getResource("/orfpred.png")).getImage());

        algoritmeComboBox = new JComboBox<>();
        databaseComboBox = new JComboBox<>();
        JLabel algoritmeLabel = new JLabel(), databaseLabel = new JLabel(), evalueLabel = new JLabel();
        evalueTekstField = new JTextField();
        BLASTButton = new JButton("BLAST", new ImageIcon(getClass().getResource("/blast.png")));

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BLAST instellingen");
        setLocationByPlatform(true);

        algoritmeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"blastp", "blastn", "tblastn", "tblastx"}));
        databaseComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Non-redundant protein sequences (NR)", "Swiss-Prot"}));

        algoritmeLabel.setFont(GUI.LABEL_FONT);
        algoritmeLabel.setText("Algoritme:");

        databaseLabel.setFont(GUI.LABEL_FONT);
        databaseLabel.setText("Database:");

        evalueLabel.setFont(GUI.LABEL_FONT);
        evalueLabel.setText("E-value cut-off:");

        BLASTButton.setFont(GUI.LABEL_FONT);
        BLASTButton.addActionListener((ActionEvent e) -> {
            try {
                blastTable.addBLAST(new BLAST(selectedORF.toString(),
                        algoritmeComboBox.getSelectedItem().toString(),
                        databaseComboBox.getSelectedItem().toString(),
                        Double.parseDouble(evalueTekstField.getText()),
                        20, getVersion()));
            } catch (NumberFormatException ex) {
                // TODO: catch
            } catch (ParseException ex) {
                // TODO: catch
            } catch (Exception ex) {
                // TODO: catch
            }
        });
        
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
                                                .addComponent(BLASTButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
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

    /**
     * Methode om een unieke id te maken waarbij de herkomst en versie aan af te leiden zijn.
     * @return Unieke id
     */
    private String getVersion(){
        Integer headerIndex = gui.getHeaderComboBox().getSelectedIndex(),
                versie = 0;
        if(versionControl.keySet().contains(headerIndex)){
            versie = versionControl.get(headerIndex)+1;
            versionControl.put(headerIndex,versie);

        } else {
            versionControl.put(headerIndex,versie);
        }
        return ""+headerIndex+"-"+versie;
    }

}
