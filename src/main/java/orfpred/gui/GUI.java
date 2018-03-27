/*
 * ORFpred - © Projectgroep 9: Damian Bolwerk, Jonathan Feenstra, 
 * Fini De Gruyter, Lotte Houwen & Alex Janse 2018.
 * Functie: Open Reading Frames voorspellen in DNA sequenties.
 * Release datum: 29 maart 2018
 */
package orfpred.gui;

import orfpred.sequence.ORFHighlighter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import orfpred.sequence.ORF;
import orfpred.sequence.ReadingFramer;

/**
 * Class voor het weergeven van de GUI.
 *
 * @author Projectgroep 9
 * @since JDK 1.8
 * @version 1.0
 */
public class GUI implements Runnable {

    private JFrame frame;
    private GUIUpdater guiUpdater;

    private JMenuItem openMenuItem, openDBMenuItem, dbSaveMenuItem, exitMenuItem,
            highlightMenuItem, orfLengteMenuItem, prokaryootMenuItem,
            eukaryootMenuItem;
    private JComboBox<String> headerComboBox;
    private JButton zoekButton;
    private JTextPane seqTextPane;

    /**
     * Lettertype voor JLabels
     */
    public static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    /**
     * Start de applicatie.
     *
     * @param args de command line argumenten
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            System.err.println(ex.toString());
        }
        EventQueue.invokeLater(new GUI());
    }

    @Override
    public void run() {
        frame = new JFrame("ORFpred - Open Reading Frame predictie tool");

        frame.setSize(720, 505);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setIconImage(new ImageIcon(getClass().getResource("/orfpred.png")).getImage());

        guiUpdater = new GUIUpdater(this);
        GUIEventHandler eventHandler = new GUIEventHandler(guiUpdater);

        Container window = frame.getContentPane();

        JMenuBar menuBar = new JMenuBar();

        //<editor-fold defaultstate="collapsed" desc="Bestandmenu aanmaken">
        JMenu bestandMenu = new JMenu("Bestand");
        openMenuItem = new JMenuItem("Open...", new ImageIcon(getClass().getResource("/open.png")));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openMenuItem.addActionListener(eventHandler);
        openDBMenuItem = new JMenuItem("Open vanuit databank", new ImageIcon(getClass().getResource("/open.png")));
        openDBMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        openDBMenuItem.addActionListener(eventHandler);
        dbSaveMenuItem = new JMenuItem("Opslaan in database", new ImageIcon(getClass().getResource("/database.png")));
        dbSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        dbSaveMenuItem.addActionListener(eventHandler);
        JSeparator bestandMenuSeparator = new JSeparator();
        exitMenuItem = new JMenuItem("Afsluiten", new ImageIcon(getClass().getResource("/exit.png")));
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        exitMenuItem.addActionListener(eventHandler);

        bestandMenu.add(openMenuItem);
        bestandMenu.add(openDBMenuItem);
        bestandMenu.add(dbSaveMenuItem);
        bestandMenu.add(bestandMenuSeparator);
        bestandMenu.add(exitMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Weergave menu aanmaken">
        JMenu weergaveMenu = new JMenu("Weergave");
        highlightMenuItem = new JMenuItem("Highlight kleur", new ImageIcon(getClass().getResource("/highlight.png")));
        highlightMenuItem.addActionListener(eventHandler);

        weergaveMenu.add(highlightMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Toolsmenu aanmaken">
        JMenu toolsMenu = new JMenu("Tools"), orfSearchMenu = new JMenu("ORF zoekmodus");
        orfLengteMenuItem = new JMenuItem("Stel minimale ORF lengte in...");
        orfLengteMenuItem.addActionListener(eventHandler);
        
        prokaryootMenuItem = new JRadioButtonMenuItem("Prokaryoot - start tot stop");
        prokaryootMenuItem.addActionListener(eventHandler);
        eukaryootMenuItem = new JRadioButtonMenuItem("Eukaryoot - stop tot stop", true);
        eukaryootMenuItem.addActionListener(eventHandler);
        
        ButtonGroup orfSearchGroup = new ButtonGroup();
        orfSearchGroup.add(prokaryootMenuItem);
        orfSearchGroup.add(eukaryootMenuItem);
        orfSearchMenu.add(prokaryootMenuItem);
        orfSearchMenu.add(eukaryootMenuItem);

        toolsMenu.add(orfLengteMenuItem);
        toolsMenu.add(orfSearchMenu);
        //</editor-fold>

        menuBar.add(bestandMenu);
        menuBar.add(weergaveMenu);
        menuBar.add(toolsMenu);

        frame.setJMenuBar(menuBar);

        JLabel headerLabel = new JLabel(new ImageIcon(getClass().getResource("/header.png")));
        window.add(headerLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);

        //<editor-fold defaultstate="collapsed" desc="Mainpanel componenten aanmaken">
        JLabel seqLabel = new JLabel("Sequentie");
        seqLabel.setFont(LABEL_FONT);

        headerComboBox = new JComboBox();
        headerComboBox.setModel(new DefaultComboBoxModel(new String[]{"Open een bestand..."}));
        headerComboBox.setEnabled(false);
        headerComboBox.addItemListener(eventHandler);

        zoekButton = new JButton("Voorspel ORF's", new ImageIcon(getClass().getResource("/search.png")));
        zoekButton.setEnabled(false);
        zoekButton.addActionListener(eventHandler);

        JLabel readingFrameLabel = new JLabel("<html>+1<br/>+2<br/>+3<br/>Ref<br/>-1<br/>-2<br/>-3<br/>Pos</html>", SwingConstants.CENTER);
        if (System.getProperty("os.name").startsWith("Windows")) {
            readingFrameLabel.setFont(new Font("Lucida Console", Font.BOLD, 12));
        } else {
            readingFrameLabel.setFont(new Font("Courier", Font.BOLD, 12));
        }
        readingFrameLabel.setVerticalAlignment(SwingConstants.TOP);
        
        seqTextPane = new JTextPane() {
            // Zorgt ervoor dat de textpane horizontaal uitbreidt.
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width
                        < getParent().getWidth();
            }
        };

        // Zorgt ervoor dat de scrollbar niet automatisch van positie verandert.
        ((DefaultCaret) seqTextPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        seqTextPane.setEditable(false);
        if (System.getProperty("os.name").startsWith("Windows")) {
            seqTextPane.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        } else {
            seqTextPane.setFont(new Font("Courier", Font.PLAIN, 12));
        }

        JScrollPane seqScrollPane = new JScrollPane(seqTextPane);
        seqScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        JLabel blastLabel = new JLabel("BLAST resultaten");
        blastLabel.setFont(LABEL_FONT);
        JScrollPane blastScrollPane = new JScrollPane();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Layout, overgenomen uit GUI builder">
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(blastScrollPane, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(readingFrameLabel)
                                                .addGap(1, 1, 1)
                                                .addComponent(seqScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(seqScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                        .addComponent(readingFrameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blastLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blastScrollPane, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        //</editor-fold>
        window.add(mainPanel);

        frame.setVisible(true);
    }


    /**
     * Inner class voor het afhandelen van events in de GUI.
     */
    private class GUIEventHandler implements ActionListener, ItemListener {

        private final GUIUpdater updater;

        /**
         * Constructor.
         *
         * @param guiUpdater de GUIUpdater van de betreffende GUI
         */
        public GUIEventHandler(GUIUpdater guiUpdater) {
            this.updater = guiUpdater;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == openMenuItem) {
                EventQueue.invokeLater(() -> {
                    updater.loadFile();
                });
            } else if (evt.getSource() == openDBMenuItem){
                EventQueue.invokeLater(() -> {
                    new DBFileChooser(updater).setVisible(true);
                });
            } else if (evt.getSource() == dbSaveMenuItem) {
                // TODO: Opslaan in database
            } else if (evt.getSource() == exitMenuItem) {
                System.exit(0);
            } else if (evt.getSource() == highlightMenuItem) {
                ORFHighlighter.setHighlightKleur(JColorChooser.showDialog(GUI.this.frame, "Highlight kleur", ORFHighlighter.getHighlightKleur()));
            } else if (evt.getSource() == orfLengteMenuItem) {
                // TODO: Toon pop-up waar ORF lengte kan worden ingesteld https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers#11093360
            } else if (evt.getSource() == zoekButton) {
                EventQueue.invokeLater(new ORFHighlighter(updater.getShownReadingFrames(), GUI.this));
            } else if (evt.getSource() == prokaryootMenuItem || evt.getSource() == eukaryootMenuItem) {
                ORFHighlighter.setSearchMode(prokaryootMenuItem.isSelected());
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == headerComboBox) {
                EventQueue.invokeLater(() -> {
                    updater.showReadingFrames(ReadingFramer.getProteinFrames(updater.getHeaderToSeq().get(headerComboBox.getSelectedItem().toString())));
                });
            }
        }
    }
    
    public void showORFPopUp(ORF orf) {
        new ORFPopUp(guiUpdater.getShownReadingFrames(), orf);
    }

    /**
     * Toont foutmelding pop-up.
     *
     * @param ex de exception
     * @param msg de boodschap
     */
    public void showErrorMessage(Exception ex, String msg) {
        JOptionPane.showMessageDialog(this.getFrame(), msg, ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * @return frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * @return headerComboBox
     */
    public JComboBox<String> getHeaderComboBox() {
        return headerComboBox;
    }

    /**
     * @return zoekButton
     */
    public JButton getZoekButton() {
        return zoekButton;
    }

    /**
     * @return seqTextPane
     */
    public JTextPane getSeqTextPane() {
        return seqTextPane;
    }

}
