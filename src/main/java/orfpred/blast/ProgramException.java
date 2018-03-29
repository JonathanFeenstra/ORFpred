/*
Datum laatste update: 31-03-17
Projectgroep 12: Enrico Schmitz, Thomas Reinders en Rick Beeloo
Functionaliteit: De gebruiker kan een FASTA bestand inladen. In de sequentie
			     kunnen vervolgens ORF's gezocht worden die verder geannoteerd 
			     kunnen worden door gebruikt te maken van een BLAST search.
Bekende bugs:    Als de gebruiker het tijdelijke BLAST bestand verwijderd zal de
                 data niet opgeslagen kunnen worden in de database.

 */
package orfpred.blast;

/**
 * Deze exceptie wordt gegeooid als de gebruiker een blast programma ingeeft anders dan
 * blastn, blastp, tblastn, tblastx.
 * @author projectgroep 12
 */
public class ProgramException extends Exception {}
