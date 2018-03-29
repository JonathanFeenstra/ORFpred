/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.blast;

/**
 * Deze exception wordt gegooid als de gebruiker een BLAST probeerd uit te voeren en deze
 * opdracht al in de rij staat. 
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class QueueException extends Exception{}
