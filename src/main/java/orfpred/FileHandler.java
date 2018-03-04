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
        for (FileType ft : FileType.values()) {
            chooser.addChoosableFileFilter(ft.getFileFilter());
        }
        chooser.setFileFilter(chooser.getChoosableFileFilters()[1]);
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
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
                if (headerToSeq != null) {
                    // TODO: Naar GUI updater class
                    gui.getHeaderComboBox().setModel(new DefaultComboBoxModel(headerToSeq.keySet().toArray()));
                    gui.getHeaderComboBox().setEnabled(true);
                    gui.getZoekButton().setEnabled(true);
                    gui.getSeqTextPane().setText(headerToSeq.entrySet().iterator().next().getValue().toString().toUpperCase());
                }
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
            if (FileType.FASTA.getFileFilter().accept(file)) {
                headerToSeq = FastaReaderHelper.readFastaDNASequence(file);
            } else if (FileType.FASTQ.getFileFilter().accept(file)) {
                headerToSeq = readFastqDNASequence(file);
            } else if (FileType.GENBANK.getFileFilter().accept(file)) {
                headerToSeq = GenbankReaderHelper.readGenbankDNASequence(file);
            } else {
                headerToSeq = askFileTypeAndRead(file);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "IOExeption", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opent venster om de gebruiker om het bestandstype te vragen en gebruikt
     * de adequate leesmethode om de data in te laden.
     *
     * @param file het geselecteerde bestand
     * @return LinkedHashMap<header, DNA sequentie>
     * @throws Exception bij fouten tijdens het inlezen
     */
    public static LinkedHashMap<String, DNASequence> askFileTypeAndRead(File file) throws Exception {
        switch (JOptionPane.showOptionDialog(null, "Selecteer het bestandstype:",
                "Bestandstype", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, FileType.values(),
                FileType.FASTA)) {
            case 0:
                return FastaReaderHelper.readFastaDNASequence(file);
            case 1:
                return readFastqDNASequence(file);
            case 2:
                return GenbankReaderHelper.readGenbankDNASequence(file);
            default:
                return askFileTypeAndRead(file);
        }
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
    
    /**
     * @return headerToSeq
     */
    public static LinkedHashMap<String, DNASequence> getHeaderToSeq() {
        return headerToSeq;
    }
}
