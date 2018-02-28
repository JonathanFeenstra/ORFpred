/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred;

import javax.swing.JFileChooser;
import java.io.*;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

/**
 * Class voor het beheren van bestanden.
 * 
 * @author Projectgroep 10
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
        chooser.setFileFilter(new FileNameExtensionFilter("FASTA file", "fasta", "fna", "ffn", "fa"));
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
        // TODO: Andere bestandsformaten inlezen.
        try {
            if (fasta != null)
                return FastaReaderHelper.readFastaDNASequence(fasta);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IOExeption", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    /**
     * Procedure om bestanden in te laden.
     *
     * @param gui
     */
    public static void loadFile(GUI gui) {
        try {
            File fasta = FileHandler.selectFile();
            LinkedHashMap<String, DNASequence> data = FileHandler.getData(fasta);
            gui.getHeaderComboBox().setModel(new DefaultComboBoxModel(data.keySet().toArray()));
            gui.getHeaderComboBox().setEnabled(true);
            gui.getZoekButton().setEnabled(true);
            gui.getSeqEditorPane().setText(data.entrySet().iterator().next().getValue().toString());
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
