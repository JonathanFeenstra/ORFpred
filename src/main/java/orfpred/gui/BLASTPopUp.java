/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.gui;

import orfpred.sequence.ORF;

/**
     * Deze class toont een popup waarin de parameters voor het BLASTen
     * ingesteld kunnen worden.
     *
     * @author Projectgroep 9
     */
public class BLASTPopUp extends javax.swing.JFrame {
    
    private final ORF selectedORF;

    /**
     * Constructor
     * @param orf het geselecteerde ORF
     */
    public BLASTPopUp(ORF orf) {
        this.selectedORF = orf;
        initComponents();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        algoritmeComboBox = new javax.swing.JComboBox<>();
        databaseComboBox = new javax.swing.JComboBox<>();
        algoritmeLabel = new javax.swing.JLabel();
        databaseLabel = new javax.swing.JLabel();
        evalueLabel = new javax.swing.JLabel();
        evalueTekstField = new javax.swing.JTextField();
        BLASTButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BLAST instellingen");
        setLocationByPlatform(true);

        algoritmeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blastn", "Blastx" }));
        algoritmeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algoritmeComboBoxActionPerformed(evt);
            }
        });

        databaseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Non-redundant protein sequences (NR)", "Swiss-Prot" }));

        algoritmeLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        algoritmeLabel.setText("Algoritme:");

        databaseLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        databaseLabel.setText("Database:");

        evalueLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        evalueLabel.setText("E-value cut-off:");

        evalueTekstField.setText(" ");
        evalueTekstField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evalueTekstFieldActionPerformed(evt);
            }
        });

        BLASTButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        BLASTButton.setText("BLAST");
        BLASTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BLASTButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(databaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(algoritmeLabel)
                            .addComponent(evalueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(algoritmeComboBox, 0, 222, Short.MAX_VALUE)
                            .addComponent(evalueTekstField)
                            .addComponent(databaseComboBox, 0, 222, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BLASTButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algoritmeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(algoritmeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(databaseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(databaseLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(evalueLabel)
                    .addComponent(evalueTekstField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BLASTButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void algoritmeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algoritmeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_algoritmeComboBoxActionPerformed

    private void evalueTekstFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evalueTekstFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_evalueTekstFieldActionPerformed

    private void BLASTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BLASTButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BLASTButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BLASTButton;
    private javax.swing.JComboBox<String> algoritmeComboBox;
    private javax.swing.JLabel algoritmeLabel;
    private javax.swing.JComboBox<String> databaseComboBox;
    private javax.swing.JLabel databaseLabel;
    private javax.swing.JLabel evalueLabel;
    private javax.swing.JTextField evalueTekstField;
    // End of variables declaration//GEN-END:variables
}