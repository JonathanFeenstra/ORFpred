/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orfpred;

import javax.swing.JFileChooser;
import java.io.*;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

/**
 *
 * @author Jonathan Feenstra
 * @since JDK 1.8
 * @version 1.0
 */
public class FileHandler {

    /**
     * Opens a window where a file can be selected.
     *
     * @return the selected file or null if nothing was selected or found
     * @throws FileNotFoundException if the selected file was not found
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
     * Reads a FASTA file with DNA-sequence(s) and puts data in a LinkedHashMap.
     *
     * @param fasta a FASTA file containing DNA-sequence(s)
     * @return a LinkedHashMap with the FASTA headers as keys and sequences as
     * values or null if an exception occurred
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
}
