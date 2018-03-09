/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */

package orfpred.file;

import java.awt.Component;
import java.io.*;
import javax.swing.JFileChooser;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.*;
import org.biojava.nbio.sequencing.io.fastq.*;

/**
 * Deze class bestaat exclusief uit static methoden om bestanden te beheren.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class FileHandler {

    private static LinkedHashMap<String, DNASequence> headerToSeq;
    private static File selectedFile;

    /**
     * Opent een JFileChooser om een bestand te selecteren.
     *
     * @param parent de parent voor de JFileChooser opendialog
     * @throws FileNotFoundException als het bestand niet gevonden is
     */
    public static void selectFile(Component parent) throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser();
        for (FileType ft : FileType.values()) {
            chooser.addChoosableFileFilter(ft.getFileFilter());
        }
        chooser.setFileFilter(chooser.getChoosableFileFilters()[1]);
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists()) {
                selectedFile = chooser.getSelectedFile();
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    /**
     * Leest het geselecteerde bestand en stopt data in headerToSeq.
     *
     * @throws Exception bij exceptions uit BioJava
     */
    public static void readHeaderToSeq() throws Exception {
        if (FileType.FASTA.getFileFilter().accept(selectedFile)) {
            headerToSeq = FastaReaderHelper.readFastaDNASequence(selectedFile);
        } else if (FileType.FASTQ.getFileFilter().accept(selectedFile)) {
            headerToSeq = readFastqDNASequence(selectedFile);
        } else if (FileType.GENBANK.getFileFilter().accept(selectedFile)) {
            headerToSeq = GenbankReaderHelper.readGenbankDNASequence(selectedFile);
        } else {
            headerToSeq = askFileTypeAndRead(selectedFile);
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
     * @throws CompoundNotFoundException als karakter geen nucleotide is.
     */
    public static LinkedHashMap<String, DNASequence> readFastqDNASequence(File file) throws IOException, CompoundNotFoundException {
        FastqReader fastqReader = new SangerFastqReader();
        LinkedHashMap<String, DNASequence> fastqHeaderToSeq = new LinkedHashMap<>();
        for (Fastq fastq : fastqReader.read(file)) {
            fastqHeaderToSeq.put(fastq.getDescription(), new DNASequence(fastq.getSequence()));
        }
        return fastqHeaderToSeq;
    }
    
    /**
     * @return selectedFile
     */
    public static File getSelectedFile() {
        return selectedFile;
    }

    /**
     * @return headerToSeq
     */
    public static LinkedHashMap<String, DNASequence> getHeaderToSeq() {
        return headerToSeq;
    }

    /**
     * @return headers
     */
    public static String[] getHeaders() {
        return headerToSeq.keySet().toArray(new String[FileHandler.getHeaderToSeq().keySet().size()]);
    }
}
