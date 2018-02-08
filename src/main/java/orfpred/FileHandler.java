/*
 * ORFpred - © Damian Bolwerk, Jonathan Feenstra, Fini De Gruyter, Lotte Houwen 
 * & Alex Janse.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * 
 */
package orfpred;

import javax.swing.JFileChooser;
import java.io.*;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

/**
 *
 * 
 * @author Damian Bolwerk, Jonathan Feenstra, Fini De Gruyter, Lotte Houwen 
 * & Alex Janse
 * @since JDK 1.8
 * @version 1.0
 */
public class FileHandler {

    /**
     * Opent een JFileChooser om een bestand te selecteren.
     *
     * @return het geselecteerde bestand of null
     * @throws FileNotFoundException als het bestand niet gevonden is
     */
    public static File selectFile() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("FASTA file", "fasta", "fa"));
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists()) {
                return chooser.getSelectedFile();
            } else {
                throw new FileNotFoundException();
            }
        }
        return null;
    }

    /**
     * Leest een FASTA bestand metd DNA-sequenteie(s) en stopt data in een
     * LinkedHashMap.
     *
     * @param fasta een FASTA bestand met DNA-sequentie(s)
     * @return een LinkedHashMap met de FASTA headers als keys en de sequenties
     * als values of null bij exceptions
     */
    public static LinkedHashMap<String, DNASequence> getData(File fasta) {
        try {
            if (fasta != null)
                return FastaReaderHelper.readFastaDNASequence(fasta);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IOExeption", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    /**
     * Procedure om FASTA-bestanden in te laden.
     * 
     * @param headerComboBox 
     */
    public static void loadFile(JComboBox headerComboBox) {
        try {
            File fasta = FileHandler.selectFile();
            LinkedHashMap<String, DNASequence> data = FileHandler.getData(fasta);
            headerComboBox.setModel(new DefaultComboBoxModel(data.keySet().toArray()));
            headerComboBox.setEnabled(true);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
