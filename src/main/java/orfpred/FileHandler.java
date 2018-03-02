/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred;

import java.awt.Component;
import java.io.*;
import javax.swing.JFileChooser;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.*;
import org.biojava.nbio.sequencing.io.fastq.*;

/**
 * Class voor het beheren van bestanden.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class FileHandler {

    private static FileFilter selectedFileFilter;

    private static LinkedHashMap<String, DNASequence> headerToSeq;

    /**
     * Opent een JFileChooser om een bestand te selecteren.
     *
     * @param parent de parent voor de JFileChooser opendialog
     * @return het geselecteerde bestand of null
     * @throws FileNotFoundException als het bestand niet gevonden is
     */
    public static File selectFile(Component parent) throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("FASTA file", "fasta", "fna", "ffn", "fa"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("FASTQ file", "fastq", "fq"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("GenBank file", "gb", "gbk"));
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFileFilter = chooser.getFileFilter();
            if (chooser.getSelectedFile().exists()) {
                return chooser.getSelectedFile();
            }
            throw new FileNotFoundException();
        }
        return null;
    }

    /**
     * Procedure om bestanden in te laden.
     *
     * @param gui de betreffende GUI
     */
    public static void loadFile(GUI gui) {
        try {
            File selectedFile = FileHandler.selectFile(gui.getFrame());
            if (selectedFile != null) {
                FileHandler.setHeaderToSeq(selectedFile);
                gui.getHeaderComboBox().setModel(new DefaultComboBoxModel(headerToSeq.keySet().toArray()));
                gui.getHeaderComboBox().setEnabled(true);
                gui.getZoekButton().setEnabled(true);
                gui.getSeqTextPane().setText(headerToSeq.entrySet().iterator().next().getValue().toString().toUpperCase());
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(gui.getFrame(), ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Leest een bestand met DNA-sequenteie(s) en stopt data in headerToSeq.
     *
     * @param file een bestand met DNA-sequentie(s)
     */
    public static void setHeaderToSeq(File file) {
        try {
            switch (selectedFileFilter.getDescription()) {
                case "FASTA file":
                    headerToSeq = FastaReaderHelper.readFastaDNASequence(file);
                    break;
                case "FASTQ file":
                    headerToSeq = readFastqDNASequence(file);
                    break;
                case "GenBank file":
                    headerToSeq = GenbankReaderHelper.readGenbankDNASequence(file);
                    break;
                default:
                    // TODO: File type bepalen op een andere manier
                    break;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IOExeption", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return headerToSeq
     */
    public static LinkedHashMap<String, DNASequence> getHeaderToSeq() {
        return headerToSeq;
    }

    /**
     * Maakt LinkedHashMap van FASTQ header(s) en DNA sequentie(s).
     *
     * @param file FASTQ bestand
     * @return LinkedHashMap van FASTQ header(s) en DNA sequentie(s)
     * @throws IOException bij problemen met het bestand openen
     */
    public static LinkedHashMap<String, DNASequence> readFastqDNASequence(File file) throws IOException {
        FastqReader fastqReader = new SangerFastqReader();
        LinkedHashMap<String, DNASequence> fastqHeaderToSeq = new LinkedHashMap<>();
        for (Fastq fastq : fastqReader.read(file)) {
            try {
                fastqHeaderToSeq.put(fastq.getDescription(), new DNASequence(fastq.getSequence()));
            } catch (CompoundNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        return fastqHeaderToSeq;
    }
}
