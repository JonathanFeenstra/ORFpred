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


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.biojava.nbio.core.search.io.Hit;
import org.biojava.nbio.core.search.io.Result;
import org.biojava.nbio.core.search.io.blast.BlastXMLParser;
import org.biojava.nbio.core.search.io.Hsp;


/**
 * Deze class is verantwoordelijk voor het parse van de BLAST resultaten.
 * @author projectgroep 12
 */
public class BlastParser {
    
    //instantie variabele
    private File xmlInputFile, output ;
    private double maxEValue;
    private List<Result> results;
    private ArrayList<Hit> hits;
    
    /**
     * Constructor
     * @param FilexmlInput Het XML bestand dat ingeladen moet worden
     * @param eValCutOff De E-value cut-off.
     */
    public BlastParser(File FilexmlInput, double eValCutOff) {
       xmlInputFile = FilexmlInput;
       maxEValue = eValCutOff;
    }
    
    /**
     * Deze methode parsed de resultaten uit het XML bestand waarin de BLAST 
     * resultaten zijn opgeslagen. 
     */
    public void parse() {
        try {
            BlastXMLParser parser = new BlastXMLParser();
            System.out.println("1");
            parser.setFile(xmlInputFile);
            System.out.println("2");
            results = parser.createObjects(maxEValue);
            for ( int i = 0 ; i < results.size();i++ ){
                System.out.println(results.get(i));
                System.out.println(results.get(i).getDbFile());
                System.out.println (results.get(i).getHitCounter());
                 System.out.println( results.get(i).getProgram());
                   System.out.println(results.get(i).getQueryDef());
                    System.out.println(results.get(i).getQueryID());
                     System.out.println(results.get(i).getQueryID());
                     System.out.println(results.get(i).getQueryLength());
                    System.out.println( results.get(i).getQuerySequence());
                    System.out.println( results.get(i).getReference());
                     System.out.println(results.get(i).getVersion());
                  ArrayList<Hit> hit_lijst=  getTopHits(results.get(i));
                  extractHitData(hit_lijst);
                   
                     
                       
            }
            
           
          
            
            
            /* System.out.println("3");
             File output = new File ("output.txt");
            parser.setFile(output);
            parser.storeObjects(results);*/
            
            
        } catch (IOException ex) {
            showError("Cannot open XML file");
        } catch (ParseException ex) {
            showError("Wrong XML format");
        }
        
    }
     private static ArrayList< Object> extractHitData( ArrayList<Hit> hit_lijst) {
        Object[] data = null;
        ArrayList< Object> datalijst = new ArrayList< Object>();
        Hit hit_object;
         for ( int i = 0 ; i < hit_lijst.size();i++ ){
             hit_object = hit_lijst.get(i);
              Boolean next = hit_object.iterator().hasNext();
        if (next) {
             Hsp specs =hit_object.iterator().next();
             data = new Object[]{specs.getHspBitScore(), specs.getHspEvalue(), 
                 specs.getHspHseq(),specs.getHspIdentity(), specs.getHspPositive(), 
                 specs.getHspGaps(),  hit_object.getHitAccession()};
             
             datalijst.add(data);
             
        }
         }
       
        return datalijst;
    }

    
    /**
     * Deze methode haalt alle top x hits op uit het XML bestand. Deze top Hit 
     * objecten worden opgeslagen in een ArryList en geretouneerd. 
     * @param top Het aantal top hits dat weergegeven moet worden
     * @return Retouneert een ArrayList met x aantal Hit objecten.
     */
    public ArrayList<Hit> getTopHits(Result result_object) {
        ArrayList<Hit> hits = new ArrayList<>();
        Result res = result_object;
        Iterator<Hit> iter = res.iterator();
        while (iter.hasNext()) {
            Hit hit = iter.next();
            hits.add(hit);            
        } 
        return hits;
    }
    
    /**
     * Deze methode laat een Error pop-up zien met daarin het meegegeven bericht.
     * @param mssg Het bericht dat weergegeven moet worden in de pop-up.
     */
    private static void showError(String mssg) {
        JOptionPane.showMessageDialog(null, mssg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
}