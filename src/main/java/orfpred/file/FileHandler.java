/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
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
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class FileHandler {

    // Stopt default constuctor zodat de class niet kan worden geïnstantieerd.
    private FileHandler() {
    }

    /**
     * Opent een JFileChooser om een bestand te selecteren.
     *
     * @param parent de parent voor de JFileChooser opendialog
     * @return het geselecteerde bestand (of null)
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
     * Leest een bestand en retourneert LinkedHashMap van headers naar
     * DNASequence objecten.
     *
     * @param file het te lezen bestand
     * @return LinkedHashMap<header, DNASequence>
     * @throws Exception bij exceptions uit BioJava
     */
    public static LinkedHashMap<String, DNASequence> readHeaderToSeq(File file) throws Exception {
        if (FileType.FASTA.getFileFilter().accept(file)) {
            return FastaReaderHelper.readFastaDNASequence(file);
        } else if (FileType.FASTQ.getFileFilter().accept(file)) {
            return readFastqDNASequence(file);
        } else if (FileType.GENBANK.getFileFilter().accept(file)) {
            return GenbankReaderHelper.readGenbankDNASequence(file);
        }
        return askFileTypeAndRead(file);
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

}
