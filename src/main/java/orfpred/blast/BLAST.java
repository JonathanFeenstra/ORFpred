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
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.biojava.nbio.core.search.io.Hit;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

/**
 * Deze class is verantwoordelijk voor het uitvoeren van BLAST searches tegen de
 * NCBI database.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class BLAST {

    //instantie variabele
    private final String sequence;
    private final String blastProgram;
    private final String blastDatabase;
    private NCBIQBlastService service;
    private NCBIQBlastAlignmentProperties props;
    private NCBIQBlastOutputProperties outputProps;
    private BLASTParser parser;
    private File XMLFile;
    private Thread t;
    private String rid;
    private final double maxEval;
    private final int top;
    private  FileWriter writer;
    private BufferedReader reader;

    /**
     * Constructor.
     *
     * @param seq sequentie voor de BLAST.
     * @param program BLAST algoritme dat gebruikt wordt.
     * @param db gebruikte database
     * @param eValCutOff e-value cut-off.
     * @param numberTopHits hoeveelheid hits dat teruggestuurd wordt.
     */
    public BLAST(String seq, String program, String db, double eValCutOff, int numberTopHits) {
        sequence = seq;
        blastProgram = program;
        blastDatabase = db;
        maxEval = eValCutOff;
        top = numberTopHits;
        setServices();
    }

    /**
     * Deze methode zorgt voor het opstellen van de alignment opties (database
     * en BLAST programma) en het instellen van het maximaal aantal alignments
     * dat gemaakt moet worden.
     */
    private void setServices() {
        try {
            service = new NCBIQBlastService();
            setAlignmentOptions();
            setOutputOptions();
        } catch (BLASTException ex) {
            showError("please provide a valid datbase: \n blastn, blastp, tblastn, tblastx");
        } catch (Exception ex) {
            showError("Cannot connenct to NCBI database!");
        }
    }

    /**
     * Deze methode is verantwoordelijk voor het instellen van de alignment
     * opties.
     *
     * @throws BLASTException
     */
    private void setAlignmentOptions() throws BLASTException {
        props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(getBlastProgram(blastProgram));
        props.setBlastDatabase(blastDatabase);
    }

    /**
     * Deze methode is verantwoordelijk voor het instellen van de output opties.
     */
    private void setOutputOptions() throws Exception {
        outputProps = new NCBIQBlastOutputProperties();
        outputProps.setAlignmentNumber(100); //this is used as default
         
    }

    /**
     * Deze methode zet een BLAST programma als String om naar een BlastProgram
     * object die gebruikt kan worden door de biojava BLAST service.
     *
     * @param program Een String object dat het gewenste BLAST programma bevat.
     * @return Geeft een BlastProgramEnum object terug corresponderend met de
     * ingegeven String.
     * @throws BLASTException Gooit een exception als het String object niet
     * kan worden omgezet naar een BlastProgramEnum object.
     */
    private BlastProgramEnum getBlastProgram(String program) throws BLASTException {
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
                throw new BLASTException();
        }
    }

    /**
     * Deze methode start een nieuwe Thread waarin een request wordt gestuurd
     * naar de NCBI server. Deze thread blijft "levend" zolang de BLAST server
     * nog geen resultaat heeft geretouneerd.
     *
     * @throws Exception Gooit een Exception als er geen verbinding gemaakt kan
     * worden met de NCBI server.
     */
    public  void sendRequest() throws Exception {
        t = new Thread(() -> {
            try {
                rid = null;
                //stuur een BLAST request en sla het ID op.
                rid = service.sendAlignmentRequest(sequence, props);
                while (!service.isReady(rid)) {
                          System.out.println("hoi");
                    Thread.sleep(5000);
                }
                readResults(rid);
            } catch (Exception ex) {
                System.out.println(ex);
               // showError("Cannot connect to NCBI server");
            }
        });
        t.start();
    }

    /**
     * Deze methode controleerd of de request Thread de data van de BLAST server
     * heeft ontvangen of niet.
     *
     * @return Retouneert of de thread nog actief is als Boolean
     */
    public boolean checkStatus() {
        return (t.isAlive());
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
        System.out.println("hello");
        InputStream inStream = service.getAlignmentResults(rid, outputProps);
        System.out.println("ik ben hier");
        TempFile file = new TempFile(inStream);
        XMLFile = file.getFile();
        parseFile();
    }

    /**
     * Deze methode zorgt voor het instantiëren van de BLAST parser en het
     * aanroepen van de parse methoden in deze parser.
     */
    public void parseFile() throws FileNotFoundException, IOException {
        parser = new BLASTParser(XMLFile, maxEval);
        System.out.println("yoyo");
        parser.parse();
        System.out.println("kek");
         InputStream targetStream = new FileInputStream(XMLFile);
        reader = new BufferedReader(new InputStreamReader(targetStream));
        String line;
        System.out.println("ik ben hier2");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            writer.write(line + System.getProperty("line.separator"));
        }  
        //XMLFile.delete(); 
    }

    /**
     * Deze methode retouneert de gevonden top x BLAST hits. Waarbij x een
     * meegegeven getal is bij instantiatie van deze class.
     *
     * @return Retouneert een ArrayList met daarin de top x Hit objecten.
     */
    

    /**
     * Deze methode retouneert het BLAST job ID geretouneert door de NCBI server
     *
     * @return BLAST job ID geretouneert door de NCBI server.
     */
    public String getBlastJobID() {
        return rid;
    }
    public String getSequence(){
        return sequence;
    }

    /**
     * Deze methode laat een Error pop-up zien met daarin het meegegeven
     * bericht.
     *
     * @param mssg Het bericht dat weergegeven moet worden in de pop-up.
     */
    private void showError(String mssg) {
        JOptionPane.showMessageDialog(null, mssg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
