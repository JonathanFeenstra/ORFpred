/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.blast;

import java.util.Objects;

/**
 * Deze class dient als wrapper voor een Blast object naardat deze als request
 * is verstuurd naar de NCBI server.
 *
 * @author projectogroep 12
 */
public class BLASTSearch {

    //instantie variabele
    private BLAST BLASTObj;
    private String jobID;

    /**
     * Constructor
     *
     * @param BLASTdata Een BLAST ojbect waarvan de de status bijgehouden moet
     * worden.
     * @param ID Het ID waaran de BLASTdata gekoppeld moet worden.
     */
    public BLASTSearch(BLAST BLASTdata, String ID) {
        BLASTObj = BLASTdata;
        jobID = ID;
    }

    /**
     * @return Retouneert de status van de BLAST job. Dus of de NCBI server de
     * resultaten al heeft teruggestuurd naar de applicatie of niet.
     */
    public Boolean checkStatus() {
        return BLASTObj.checkStatus();
    }

    /**
     * @return Retouneert het job ID.
     */
    public String getID() {
        return jobID;
    }

    /**
     * @return Retouneert het Blast object
     */
    public BLAST getBlastObj() {
        return BLASTObj;
    }

    /**
     * Deze methode vergelijkt twee Blast objecten op basis van hun job ID, dit
     * job ID is afgeleid van het ORF ID.
     *
     * @param o Een Blast object om mee te vergelijken.
     * @return Retouneert True als beide objecten hetzelfde Blast job ID hebben.
     */
    @Override
    public boolean equals(Object o) {
        BLASTSearch compareObj = (BLASTSearch) o;
        return (compareObj.getID().equals(this.getID()));
    }

    /**
     * @return Retouneert de hashcode van een BlastJob object op basis van het Blast job
     * ID.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.getID());
        return hash;
    }
}
