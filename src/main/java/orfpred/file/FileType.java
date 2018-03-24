/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred.file;

import javax.swing.filechooser.*;

/**
 * Enum van bestandstypen met bijbehorende filefilters.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public enum FileType {
    FASTA(new FileNameExtensionFilter("FASTA", "fasta", "fna", "ffn", "fa")),
    FASTQ(new FileNameExtensionFilter("FASTQ", "fastq", "fq")),
    GENBANK(new FileNameExtensionFilter("GenBank", "gb", "gbk"));

    private final FileFilter fileFilter;

    /**
     * Constructor.
     *
     * @param ff de FileFilter
     */
    FileType(FileFilter ff) {
        fileFilter = ff;
    }

    /**
     * @return fileFilter
     */
    public FileFilter getFileFilter() {
        return fileFilter;
    }

    @Override
    public String toString() {
        return fileFilter.getDescription();
    }
}
