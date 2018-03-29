/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.blast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Objects;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

/**
 * Deze class is verantwoordelijk voor het uitvoeren van BLAST searches.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class BLAST {

    private final String sequence;
    private final String program;
    private final String database;
    private final double evalueThreshold;
    private final int hitThreshold;
    private final int resultID;
    private final NCBIQBlastService service;
    private NCBIQBlastAlignmentProperties alignProperties;
    private NCBIQBlastOutputProperties outputProperties;
    private BLASTParser parser;
    private File XMLFile;
    private Thread thread;
    private String requestId;

    private FileWriter writer;
    private BufferedReader reader;

    /**
     * Constructor.
     *
     * @param seq sequentie voor de BLAST.
     * @param blastProgram BLAST algoritme dat gebruikt wordt.
     * @param db gebruikte database
     * @param eValCutOff e-value cut-off.
     * @param hits hoeveelheid hits dat teruggestuurd wordt.
     * @param id de database ID voor de resultaten
     * @throws ParseException als programma ongeldig is
     */
    public BLAST(String seq, String blastProgram, String db, double eValCutOff, int hits, int id) throws ParseException {
        this.sequence = seq;
        this.program = blastProgram;
        this.database = db;
        this.evalueThreshold = eValCutOff;
        this.hitThreshold = hits;
        this.resultID = id;
        this.service = new NCBIQBlastService();
        setAlignmentOptions();
        setOutputOptions();
    }

    /**
     * Deze methode is verantwoordelijk voor het instellen van de alignment
     * opties.
     *
     * @throws ParseException als programma ongeldig is
     */
    private void setAlignmentOptions() throws ParseException {
        alignProperties = new NCBIQBlastAlignmentProperties();
        alignProperties.setBlastProgram(parseBlastProgram(program));
        alignProperties.setBlastDatabase(database);
    }

    /**
     * Deze methode is verantwoordelijk voor het instellen van de output opties.
     */
    private void setOutputOptions() {
        outputProperties = new NCBIQBlastOutputProperties();
        outputProperties.setAlignmentNumber(100);
    }

    /**
     * Deze methode zet een BLAST programma als String om naar een BlastProgram
     * object die gebruikt kan worden door de biojava BLAST service.
     *
     * @param program het gewenste BLAST programma
     * @return BlastProgramEnum object corresponderend met de ingegeven String
     * @throws ParseException als programma ongeldig is
     */
    private BlastProgramEnum parseBlastProgram(String program) throws ParseException {
        switch (program.toLowerCase()) {
            case "blastp":
                return BlastProgramEnum.blastp;
            case "blastn":
                return BlastProgramEnum.blastn;
            case "tblastn":
                return BlastProgramEnum.tblastn;
            case "tblastx":
                return BlastProgramEnum.tblastx;
            default:
                throw new ParseException("Ongeldig BLAST programma: " + program, 0);
        }
    }

    /**
     * Deze methode start een nieuwe Thread waarin een request wordt gestuurd
     * naar de NCBI server. Deze thread blijft "levend" zolang de BLAST server
     * nog geen resultaat heeft geretouneerd.
     */
    public void sendRequest() {
        thread = new Thread(() -> {
            try {
                requestId = service.sendAlignmentRequest(sequence, alignProperties);
                while (!service.isReady(requestId)) {
                    Thread.sleep(5000);
                }
                readResults(requestId);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        });
        thread.start();
    }

    /**
     * Deze methode controleerd of de request Thread de data van de BLAST server
     * heeft ontvangen of niet.
     *
     * @return of de thread nog actief is als boolean
     */
    public boolean checkStatus() {
        return (thread.isAlive());
    }

    /**
     * Deze methode haalt op basis van het door de NCBI server teruggegeven
     * BLAST ID het resultaat op en slaat dit op in een tijdelijk bestand.
     *
     * @param rid BLAST job ID (geretouneerd door de NCBI server)
     * @throws IOException Gooit een exception als er niet naar het bestand kan
     * worden geschreven.
     * @throws Exception Gooi een exception als er een onbekende fout optreed.
     */
    private void readResults(String rid) throws IOException, Exception {
        InputStream inStream = service.getAlignmentResults(rid, outputProperties);
        TempFile file = new TempFile(inStream);
        XMLFile = file.getFile();
        parseFile();
    }

    /**
     * Deze methode zorgt voor het instantiëren van de BLAST parser en het
     * aanroepen van de parse methoden in deze parser.
     *
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public void parseFile() throws FileNotFoundException, IOException, ParseException {
        parser = new BLASTParser(XMLFile, evalueThreshold);
        parser.parse();
        InputStream targetStream = new FileInputStream(XMLFile);
        reader = new BufferedReader(new InputStreamReader(targetStream));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line + System.getProperty("line.separator"));
        }
    }

    /**
     * Deze methode retouneert het BLAST job ID geretouneert door de NCBI server
     *
     * @return BLAST job ID geretouneert door de NCBI server.
     */
    public String getBlastJobID() {
        return requestId;
    }

    public String getSequence() {
        return sequence;
    }

    /**
     * @return resultID
     */
    public int getResultID() {
        return resultID;
    }

    //Overrides zorgen ervoor dat BLAST's op de juiste manier worden vergeleken.
    @Override
    public boolean equals(Object o) {
        if (o instanceof BLAST) {
            BLAST that = (BLAST) o;
            return that.getResultID() == this.getResultID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 37 * 3 + Objects.hashCode(this.getResultID());
    }
}
