/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.blast;

/**
 * Deze exceptie wordt gegeooid als de gebruiker een blast programma ingeeft anders dan
 * blastn, blastp, tblastn, tblastx.
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class BLASTException extends Exception {}
