/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.blast;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Deze class is verantwoordelijk voor het bijhouden van alles wat te maken
 * heeft met BLAST request. Deze class laat de jobs zien aan de gebruiker, houdt
 * bij of er BLAST jobs klaar zijn en als de BLAST job klaar is wordt de data
 * opgelsagen in de database.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class BLASTTable extends JTable {

    private ArrayList<BLAST> blasts;
    private ArrayList<Boolean> finished;
    private HashMap<Integer, Integer> rowManager;
    private int row;
    
    private static final String URL_PREFIX = "https://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Get&RID=";

    /**
     * Deze methode ontvangt een referentie JTable waarin alle BLAST jobs komen
     * te staan.
     */
    public void setOutputTable() {
        finished = new ArrayList<>();
        blasts = new ArrayList<>();
        rowManager = new HashMap<>();
        setListener();
    }

    /**
     * Deze methode voegt een listener toe aan de tabel. Als de BLAST job klaar
     * is kan de gebruiker op deze link klikken om naar de resultaat pagina te
     * gaan in de webbrowser.
     */
    private void setListener() {
        removeMouseListener(getMouseListeners()[0]);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = getSelectedRow(), column = getSelectedColumn();
                if (column == 1) {
                    String value = (String) getValueAt(row, column);
                    String url = value.split("\"")[1];
                    try {
                        openURL(url);
                    } catch (URISyntaxException | IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }

            }
        });
    }

    /**
     * Deze methode opent de resultaat pagina in de webbrowser.
     *
     * @param url de URL die geopend moet worden in de webbrowser.
     * @throws MalformedURLException
     * @throws URISyntaxException
     * @throws IOException
     */
    private void openURL(String url) throws MalformedURLException, URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URL(url).toURI());

    }

    /**
     * Deze methode controleerd of de gebruiker de sequentie al heeft geblast zo
     * niet dan wordt de BLAST job toegevoegd aan de tabel.
     *
     * @param blast Het Blast object dat toegevoegd moet worden aan de tabel.
     * @throws Exception Gooit een exception als deze Job al in de tabel staat.
     */
    public void addBLAST(BLAST blast) throws Exception {
        if (!blasts.contains(blast)) {
            blasts.add(blast);
            finished.add(false);
            rowManager.put(blast.getResultID(), row++);
            showUnfinishedBLASTs(blast);
        } else {
            throw new Exception();
        }
    }

    /**
     * @return retouneert een Boolean die aangeeft of alle jobs in de wachtrij
     * (op dit moment) klaar zijn.
     */
    public boolean everyThingDone() {
        HashSet<Boolean> set = new HashSet<>(finished);
        if (set.size() == 1) {
            return set.iterator().next();
        } else {
            return false;
        }
    }

    /**
     * Deze methode contorleert of een Job een in de wachtrij klaar is.
     */
    public void isBLASTReady() {
        for (int i = 0; i < blasts.size(); i++) {
            BLAST blast = blasts.get(i);
            if (blast.checkStatus() == false && finished.get(i) == false) {
                finished.set(i, true);
            }
        }
    }

    /**
     * @return retouneert een ArrayList met alle Jobs die op dit moment in de
     * wachtrij staan.
     */
    public ArrayList<BLAST> getCurrectBLASTs() {
        return blasts;
    }

    /**
     * Deze methode laat een Job in de tabel zien als deze nog niet klaar is.
     *
     * @param blast
     */
    private void showUnfinishedBLASTs(BLAST blast) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(new Object[]{blast.getResultID(), "Bezig met BLAST..."});
    }

    /**
     * Deze methode update een rij in de tabel met de gegeven status
     *
     * @param status Een String object met daarin de status van de job
     * @param row De rij waarin de status moet worden weergegeven.
     */
    private void updateStatus(String status, int row) {
        setValueAt(status, row, 1);
    }

    /**
     * Deze methode maakt een hyperlink naar de resultaten.
     *
     * @param ID Het BLAST ID die geretouneerd is door de NCBI server.
     * @return Een String object die de hyerplink bevat.
     */
    private static String createURL(String ID) {
        String adres = URL_PREFIX + ID;
        String hyperLink = "<html><a href=\"" + adres + "\">GO TO RESULTS</a></html>";
        return hyperLink;
    }

}
