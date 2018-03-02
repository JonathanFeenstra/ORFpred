/*
 * ORFpred - © Projectgroep 10: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 28 maart 2018
 */
package orfpred;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GUI class met actionlistener voor de aanroep van methoden.
 *
 * @author Projectgroep 10
 * @since JDK 1.8
 * @version 1.0
 */
public class GUI extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu bestandMenu, weergaveMenu, toolsMenu, seqMenu;
    private JMenuItem openMenuItem, dbSaveMenuItem, exitMenuItem,
            highlightMenuItem, blastMenuItem,
            orfLengteMenuItem;
    private JRadioButtonMenuItem eiwitMenuItem, dnaMenuItem;
    private JSeparator bestandMenuSeparator;
    private ButtonGroup seqTypeGroup;
    private JPanel mainPanel;
    private GroupLayout mainPanelLayout;
    private JComboBox headerComboBox;
    private JButton zoekButton;
    private JLabel headerLabel, seqLabel, blastLabel;
    private JScrollPane seqScrollPane, blastScrollPane;
    private JEditorPane seqEditorPane;
    private final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    /**
     * Start de applicatie.
     *
     * @param args de command line argumenten
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(); // TODO: Adequate exception handling
        }

        GUI frame = new GUI();
        frame.setSize(720, 505);
        frame.setResizable(false);
        frame.setTitle("ORFpred - Open Reading Frame predictie tool");
        frame.createGUI();
        frame.setVisible(true);
    }

    /**
     * Creëert de GUI.
     */
    private void createGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setIconImage(new ImageIcon(getClass().getResource("/orfpred.png")).getImage());

        Container window = getContentPane();

        menuBar = new JMenuBar();

        //<editor-fold defaultstate="collapsed" desc="Bestandmenu aanmaken">
        bestandMenu = new JMenu("Bestand");
        openMenuItem = new JMenuItem("Open...", new ImageIcon(getClass().getResource("/open.png")));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openMenuItem.addActionListener(this);
        dbSaveMenuItem = new JMenuItem("Opslaan in database", new ImageIcon(getClass().getResource("/database.png")));
        dbSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        dbSaveMenuItem.addActionListener(this);
        bestandMenuSeparator = new JSeparator();
        exitMenuItem = new JMenuItem("Afsluiten", new ImageIcon(getClass().getResource("/exit.png")));
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        exitMenuItem.addActionListener(this);

        bestandMenu.add(openMenuItem);
        bestandMenu.add(dbSaveMenuItem);
        bestandMenu.add(bestandMenuSeparator);
        bestandMenu.add(exitMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Weergave menu aanmaken">
        weergaveMenu = new JMenu("Weergave");
        seqMenu = new JMenu("Type sequentie...");
        eiwitMenuItem = new JRadioButtonMenuItem("Eiwit", true);
        dnaMenuItem = new JRadioButtonMenuItem("DNA");
        highlightMenuItem = new JMenuItem("Highlight kleur", new ImageIcon(getClass().getResource("/highlight.png")));
        highlightMenuItem.addActionListener(this);

        seqTypeGroup = new ButtonGroup();
        seqTypeGroup.add(eiwitMenuItem);
        seqTypeGroup.add(dnaMenuItem);

        seqMenu.add(eiwitMenuItem);
        seqMenu.add(dnaMenuItem);

        weergaveMenu.add(seqMenu);
        weergaveMenu.add(highlightMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Toolsmenu aanmaken">
        toolsMenu = new JMenu("Tools");
        blastMenuItem = new JMenuItem("BLAST hele sequentie", new ImageIcon(getClass().getResource("/blast.png")));
        blastMenuItem.addActionListener(this);
        orfLengteMenuItem = new JMenuItem("Stel minimale ORF lengte in...");
        orfLengteMenuItem.addActionListener(this);

        toolsMenu.add(blastMenuItem);
        toolsMenu.add(orfLengteMenuItem);
        //</editor-fold>

        menuBar.add(bestandMenu);
        menuBar.add(weergaveMenu);
        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);

        headerLabel = new JLabel(new ImageIcon(getClass().getResource("/header.png")));
        window.add(headerLabel, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);

        //<editor-fold defaultstate="collapsed" desc="Mainpanel componenten aanmaken">
        seqLabel = new JLabel("Sequentie");
        seqLabel.setFont(LABEL_FONT);
        headerComboBox = new JComboBox();
        headerComboBox.setModel(new DefaultComboBoxModel(new String[]{"Open een bestand..."}));
        headerComboBox.setEnabled(false);
        zoekButton = new JButton("Voorspel ORF's", new ImageIcon(getClass().getResource("/search.png")));
        zoekButton.setEnabled(false);
        zoekButton.addActionListener(this);

        seqEditorPane = new JEditorPane();
        seqEditorPane.setEditable(false);
        seqScrollPane = new JScrollPane(seqEditorPane);
        seqScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        blastLabel = new JLabel("BLAST resultaten");
        blastLabel.setFont(LABEL_FONT);
        blastScrollPane = new JScrollPane();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Layout, overgenomen uit GUI builder">
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(blastScrollPane, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                                        .addComponent(seqScrollPane, GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                                .addComponent(blastLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                                .addComponent(seqLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(headerComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(zoekButton)))
                                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(seqLabel)
                                        .addComponent(zoekButton)
                                        .addComponent(headerComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(seqScrollPane, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blastLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blastScrollPane, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        //</editor-fold>

        window.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == openMenuItem) {
            FileHandler.loadFile(this);
        } else if (evt.getSource() == dbSaveMenuItem) {
            // TODO: Opslaan in database
        } else if (evt.getSource() == exitMenuItem) {
            System.exit(0);
        } else if (evt.getSource() == eiwitMenuItem) {
            // TODO: Zet sequentiemodus op eiwit
        } else if (evt.getSource() == dnaMenuItem) {
            // TODO: Zet sequentiemodus op DNA
        } else if (evt.getSource() == highlightMenuItem) {
            ORFHighlighter.setHighlightKleur(JColorChooser.showDialog(null, "Highlight kleur", ORFHighlighter.getHighlightKleur()));
        } else if (evt.getSource() == blastMenuItem) {
            // TODO: Toon pop-up met BLAST settings
        } else if (evt.getSource() == orfLengteMenuItem) {
            // TODO: Toon pop-up waar ORF lengte kan worden ingesteld https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers#11093360
        } else if (evt.getSource() == zoekButton) {
            // TODO: Zoek en highlight ORF's in sequentie
        }
    }

    /**
     * @return headerComboBox
     */
    public JComboBox getHeaderComboBox() {
        return headerComboBox;
    }

    /**
     * @return zoekButton
     */
    public JButton getZoekButton() {
        return zoekButton;
    }

    /**
     * @return seqEditorPane
     */
    public JEditorPane getSeqEditorPane() {
        return seqEditorPane;
    }
}
