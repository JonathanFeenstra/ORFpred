/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orfpred.blast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author damia
 */
public class Blast_file {
    private static final String SUFFIX = ".XML";
    private static int numb = 0;

    //instatie variabele
    private InputStream inStream;
    private File tempFile;
    private  FileWriter writer;
private BufferedReader reader; 

public Blast_file (InputStream inputStream) throws IOException {
        numb++;
        inStream = inputStream;
        write();
    }
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
        writer.close();
        reader.close();
        inStream.close();
}
public File getFile() {
        return tempFile;
}
}
