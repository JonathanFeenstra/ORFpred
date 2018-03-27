package orfpred.gui;

import orfpred.database.DatabaseLoader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBFileChooser extends JFrame implements ActionListener{

    private JList<String> fileList;
    private static String[] bestandArray;
    private JButton openButton, deleteButton;
    private JLabel uitlegLabel;
    private static ArrayList<ArrayList<String>> bestandList = null;
    private static GUIUpdater updater;

    public static void runFrame() {
        getBestanden();
        if(bestandArray.length > 0) {
            DBFileChooser frame = new DBFileChooser();
            frame.setTitle("Kies uw bestand uit de databank");
            frame.setSize(500, 500);
            frame.setResizable(false);
            frame.createGUI();
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null,"Er zijn geen bestanden gevonden in de DB");
        }
    }

    private void createGUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container window = getContentPane();
        window.setLayout(new FlowLayout());

        uitlegLabel = new JLabel("Kies een bestand:");
        window.add(uitlegLabel);

        fileList = new JList<>(bestandArray);
        fileList.setFont(new Font("Courier new",Font.BOLD,15));
        fileList.setPreferredSize(new Dimension(500,300));
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        window.add(fileList);

        openButton = new JButton("Open bestand");
        openButton.addActionListener(this);
        window.add(openButton);

        deleteButton = new JButton("Verwijder bestand");
        deleteButton.addActionListener(this);
        window.add(deleteButton);

    }

    public void actionPerformed(ActionEvent event){
        if (fileList.isSelectionEmpty()){
            JOptionPane.showMessageDialog(null,"Error: Kies een bestand of sluit het venster.");
        } else {
            String bestandNaam = fileList.getSelectedValue();
            for(ArrayList<String> lijst : bestandList){
                if(lijst.get(1).equals(bestandNaam)){
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    if(event.getSource() == openButton) {
                        updater.loadDBFile(Integer.parseInt(lijst.get(0)));
                    } else {

                    }
                }
            }
        }
    }

    public static void getBestanden(){
        try {
            DatabaseLoader loader = new DatabaseLoader();
            bestandList = loader.getStoredFileNames();
            bestandArray = new String[bestandList.size()];
            for(int index = 0; index < bestandList.size(); index++){
                bestandArray[index] = bestandList.get(index).get(1);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "De volgende error is opgetreden: "+e.toString());
        } catch (ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "ORFPred kan geen de ojbc.jar file niet vinden.");
        }
    }

    public static void setGuiUpdater(GUIUpdater guiUpdater){
        updater = guiUpdater;
    }

}
