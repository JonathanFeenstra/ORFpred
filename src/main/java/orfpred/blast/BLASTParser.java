/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.blast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.biojava.nbio.core.search.io.Hit;
import org.biojava.nbio.core.search.io.Result;
import org.biojava.nbio.core.search.io.blast.BlastXMLParser;
import org.biojava.nbio.core.search.io.Hsp;

/**
 * Deze class is verantwoordelijk voor het parse van de BLAST resultaten.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class BLASTParser {

    private final File xmlInputFile;
    private File output;
    private final double maxEValue;
    private List<Result> results;
    private ArrayList<ArrayList<Object>> hitsData;

    /**
     * Constructor
     *
     * @param FilexmlInput Het XML bestand dat ingeladen moet worden
     * @param eValCutOff De E-value cut-off.
     */
    public BLASTParser(File FilexmlInput, double eValCutOff) {
        xmlInputFile = FilexmlInput;
        maxEValue = eValCutOff;
    }

    /**
     * Deze methode parsed de resultaten uit het XML bestand waarin de BLAST
     * resultaten zijn opgeslagen.
     *
     * @throws IOException
     * @throws ParseException
     */
    public void parse() throws IOException, ParseException {
        BlastXMLParser parser = new BlastXMLParser();
        parser.setFile(xmlInputFile);
        results = parser.createObjects(maxEValue);
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i));
            System.out.println(results.get(i).getDbFile());
            System.out.println(results.get(i).getHitCounter());
            System.out.println(results.get(i).getProgram());
            System.out.println(results.get(i).getQueryDef());
            System.out.println(results.get(i).getQueryID());
            System.out.println(results.get(i).getQueryID());
            System.out.println(results.get(i).getQueryLength());
            System.out.println(results.get(i).getQuerySequence());
            System.out.println(results.get(i).getReference());
            System.out.println(results.get(i).getVersion());
            ArrayList<Hit> hitList = getTopHits(results.get(i));
            hitsData.addAll(extractHitData(hitList));
        }

    }

    private static ArrayList<ArrayList<Object>> extractHitData(ArrayList<Hit> hitList) {
        Object[] data;
        ArrayList<ArrayList< Object>> dataList = new ArrayList<>();
        Hit hit;
        for (int i = 0; i < hitList.size(); i++) {
            hit = hitList.get(i);
            if (hit.iterator().hasNext()) {
                Hsp specs = hit.iterator().next();
                data = new Object[]{hit.getHitDef(), specs.getHspBitScore(), specs.getHspEvalue(),
                    specs.getHspHseq(),specs.getHspQueryFrom(),specs.getHspQueryTo(), specs.getHspIdentity(), specs.getHspPositive(),
                    specs.getHspGaps(), hit.getHitAccession()};
                dataList.get(i).add(data);

            }
        }
        return dataList;
    }

    /**
     * Deze methode haalt alle top x hits op uit het XML bestand. Deze top Hit
     * objecten worden opgeslagen in een ArryList en geretouneerd.
     *
     * @param result
     * @return Retouneert een ArrayList met x aantal Hit objecten.
     */
    public ArrayList<Hit> getTopHits(Result result) {
        ArrayList<Hit> hitList = new ArrayList<>();
        Result res = result;
        Iterator<Hit> iter = res.iterator();
        while (iter.hasNext()) {
            Hit hit = iter.next();
            hitList.add(hit);
        }
        return hitList;
    }

    public ArrayList<ArrayList<Object>> getHitsData() {
        return hitsData;
    }
}
