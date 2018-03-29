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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Deze class kan gebruikt worden voor het maken van een tijdelijk bestand (in de Temp map).
 * @author projectgroep 12
 */
public class TempFile {

    //class variabele
    private static final String SUFFIX = ".XML";
    private static int numb = 0;

    //instatie variabele
    private InputStream inStream;
    private File tempFile;
    private  FileWriter writer;
    private BufferedReader reader;

    /**
     * Constructor
     * @param inputStream Een InputStream die de gegevens bevat die opgeslagen moeten worden in het tijdelijke bestand.
     * @throws IOException Gooit een exception als er geen tijdelijk bestand aangemaakt kan worden.
     */
    public TempFile(InputStream inputStream) throws IOException {
        numb++;
        inStream = inputStream;
        write();
    }
   
    /**
     * @return Retouneert het tijdleijk bestand als File object.
     */
    public File getFile() {
        return tempFile;
    }

    /**
     * Deze methode schrijft de data uit de InputStream weg naar een tijdelijk bestand.
     * @throws IOException Gooit een Exception als de data niet kan worden weggeschreven naar het tijdelijk bestand.
     */
    private void write() throws IOException {
        String prefix = "temp" + numb; //om ervoor te zorgen dat BLAST resultaten nooit door elkaar gehaald kunnen worden.
        tempFile = File.createTempFile(prefix, SUFFIX);
        tempFile.deleteOnExit();
        
        reader = new BufferedReader(new InputStreamReader(inStream));
        writer = new FileWriter(tempFile);
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line + System.getProperty("line.separator"));
        }  
        closeStreams();
    }

    /**
     * Deze methode sluit alle streams af (inputStrea, writer en reader)
     * @throws IOException Gooit een Excpetion als een van de streams niet bereikt kan worden.
     */
    public void closeStreams()throws IOException {
        writer.close();
        reader.close();
        inStream.close();
    }
    
    /**
     * Deze methode verwijderd het tijdelijke bestand.
     */
    public void delete() {
        tempFile.delete();
    }
}