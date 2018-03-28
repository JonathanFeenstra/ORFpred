/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.gui;

import orfpred.database.DatabaseDeleter;
import orfpred.database.DatabaseLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Venster waarmee bestanden uit de database kunnen worden ingeladen.
 *
 * @author Projectgroep 9
 */
public class DBFileChooser extends JFrame implements ActionListener {

    private JList<String> fileList;
    private String[] bestandArray;
    private JButton openButton, deleteButton;
    private ArrayList<ArrayList<String>> bestandList = null;
    private final GUIUpdater guiUpdater;

    /**
     * Constructor.
     *
     * @param updater de betreffende GUIUpdater
     */
    public DBFileChooser(GUIUpdater updater) {
        this.guiUpdater = updater;
        getBestanden();
        if (bestandArray.length > 0) {
            createComponents();
        } else {
            JOptionPane.showMessageDialog(null, "Er zijn geen bestanden gevonden in de DB");
        }
    }

    /**
     * Maakt DBFileChooser componenten aan.
     */
    private void createComponents() {
        setTitle("Kies uw bestand uit de databank");
        setSize(500, 500);
        setLocationByPlatform(true);
        setResizable(false);

        Container window = getContentPane();
        window.setLayout(new FlowLayout());

        JLabel uitlegLabel = new JLabel("Kies een bestand:");
        window.add(uitlegLabel);

        fileList = new JList<>(bestandArray);
        fileList.setFont(new Font("Courier new", Font.BOLD, 15));
        fileList.setPreferredSize(new Dimension(500, 300));
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        window.add(fileList);

        openButton = new JButton("Open bestand");
        openButton.addActionListener(this);
        window.add(openButton);

        deleteButton = new JButton("Verwijder bestand");
        deleteButton.addActionListener(this);
        window.add(deleteButton);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (fileList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Kies een bestand of sluit het venster.");
        } else {
            String bestandNaam = fileList.getSelectedValue();
            bestandList.stream().filter((lijst) -> (lijst.get(1).equals(bestandNaam))).map((lijst) -> {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                return lijst;
            }).forEachOrdered((lijst) -> {
                if (event.getSource() == openButton) {
                    guiUpdater.setFileName(bestandNaam);
                    guiUpdater.loadDBFile(Integer.parseInt(lijst.get(0)));
                } else {
                    verwijderBestand(bestandNaam, lijst);
                }
            }
            );
        }
    }

    /**
     * Laadt bestanden in uit de database.
     */
    public final void getBestanden() {
        try {
            bestandList = new DatabaseLoader().getStoredFiles();
            bestandArray = new String[bestandList.size()];
            for (int index = 0; index < bestandList.size(); index++) {
                bestandArray[index] = bestandList.get(index).get(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "De volgende error is opgetreden: " + e.toString());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "ORFPred kan de ojbc.jar file niet vinden.");
        }
    }

    /**
     * Verdqijdert een bestand uit de database.
     *
     * @param bestandNaam naam van het te verwijderen bestand
     * @param lijst
     */
    public void verwijderBestand(String bestandNaam, ArrayList<String> lijst) {
        try {
            if (JOptionPane.showConfirmDialog(null, "Weet u zeker dat u " + bestandNaam + " wilt verwijderen?") == JOptionPane.YES_OPTION) {
                DatabaseDeleter deleter = new DatabaseDeleter(lijst.get(0));
                JOptionPane.showMessageDialog(null, bestandNaam + " is verwijderd.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error: Er is een fout opgetreden bij verwijderen van het bestand: "
                    + e.toString());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: Oracle driver niet gevonden.");
        }
    }
}
