package cz.cvut.fel.coursework.GUI;

import cz.cvut.fel.coursework.Controller;
import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVER.StartServer;
import cz.cvut.fel.coursework.SERVER.StopServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Creates program's main window as tabbed pane.<br>
 * Tabs are:
 * <ul>
 *     <li>Welcome tab</li>
 *     <li>Active notifications tab</li>
 *     <li>Settings tab</li>
 *     <li>Information about program tab</li>
 * </ul>
 *
 * @see Globals
 * @author Anastasia Surikova
 */

public class MainWindow extends JPanel {

    private static Controller c = new Controller();
    private static JFrame frame;

    /**
     * Creates main window and calls related functions to create tabs.
     * BorderLayout() is used.
     */
    public MainWindow() {
        super(new BorderLayout());

        // We are going to have tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Welcome tab
        JPanel tab1 = new JPanel();
        JPanel steps = new JPanel();
        steps.add(createWelcomeTabStep1());
        steps.add(createWelcomeTabStep2());
        steps.setLayout(new GridLayout(1,2));
        tab1.add(createWelcomeTabMessage());
        tab1.add(steps);
        tab1.setLayout(new BoxLayout(tab1, BoxLayout.Y_AXIS));
        tabbedPane.addTab("Welcome", tab1);

        // Active notifications tab
        JPanel tab2 = new JPanel();
        tab2.add(createActiveNotificationsTab());
        tabbedPane.addTab("My notifications", tab2);

        // Settings tab
        JPanel tab3 = new JPanel();
        tab3.add(createSettingsTab());
        tabbedPane.addTab("Settings", tab3);

        // Information about program tab
        JPanel tab4 = new JPanel();
        tab4.add(createAboutTab());
        tabbedPane.addTab("About", tab4);

        // Set prefered window size (specified in class Globals)
        tabbedPane.setPreferredSize(new Dimension(Globals.getWIDTH(), Globals.getHEIGHT()));

        // TabbedPane will use BorderLayout
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates welcoming label that will be added to the first tab.
     *
     * @return JPanel with welcoming label.
     */
    public JPanel createWelcomeTabMessage() {
        JLabel label = new JLabel("Welcome to Notification Displayer!");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel pane = new JPanel();
        pane.add(label);
        return pane;
    }

    /**
     * Creates step #1 for user.
     * In this step user is asked to scan a QR code image to connect his smartphone with computer.
     *
     * @return JPanel with guiding label and QR image.
     */
    public JPanel createWelcomeTabStep1() {

        // Create title of pane
        String title = "Step 1";

        // Create guiding label
        JLabel label = new JLabel("Scan this QR code with your mobile phone");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get QR picture
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMG_PATH()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(picLabel);
        return pane;
    }

    /**
     * Creates step #2 for user.
     * In this step user can control the program with control buttons.<br>
     *
     * <b>Buttons:</b>
     * <ul>
     *     <li>Start
     *         <ul>
     *             <li>launches the server. Starts to wait for incoming notifications from smartphone</li>
     *         </ul>
     *     </li>
     *     <li>Stop
     *         <ul>
     *             <li>stops the server</li>
     *         </ul>
     *     </li>
     *     <li>Hide
     *         <ul>
     *             <li>hide application to MACOS Dock, WINDOWS bottom panel, LINUX panel</li>
     *         </ul>
     *     </li>
     * </ul>
     * <b>Labels:</b>
     * <ul>
     *     <li>Guiding label
     *         <ul>
     *             <li>tells user what buttons are for</li>
     *         </ul>
     *     </li>
     *     <li>Status label
     *         <ul>
     *             <li>indicates the status of server: STOPPED, WAITING FOR NOTIFICATIONS</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @return JPanel with guiding label, buttons and status label.
     */
    public JPanel createWelcomeTabStep2() {

        // Create pane title
        String title = "Step 2";

        // Create guide label
        JLabel label = new JLabel("Use control buttons to start application");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create start button
        final JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create stop button
        final JButton stopButton = new JButton("Stop");
        stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // disable it
        stopButton.setEnabled(false);

        // Create hide button
        JButton hideButton = new JButton("Hide");
        hideButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create status label
        final JLabel statusLabel = new JLabel("Stopped.");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // server is stopped at the beginning, so the color is red
        statusLabel.setForeground(Color.red);

        // Start button will start the server in thread and change the label
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Thread startListening = new Thread(new StartServer());

                statusLabel.setText("Waiting for your notifications :)");
                statusLabel.setForeground(Globals.getGREEN());

                startButton.setEnabled(false);
                stopButton.setEnabled(true);

                startListening.start();
            }
        });

        // Stop button will stop the server and change label back to 'Stopped.'
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                StopServer stopListening = new StopServer();

                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                statusLabel.setText("Stopped.");
                statusLabel.setForeground(Color.red);

                stopListening.start();
            }
        });

        // Hide button hides window to application panel
        hideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setState(Frame.ICONIFIED);
            }
        });

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(label);
        pane.add(startButton);
        pane.add(stopButton);
        pane.add(hideButton);
        pane.add(statusLabel);
        return pane;
    }

    /**
     * Creates active notifications tab.<br>
     * <b>This tab has:</b>
     * <ul>
     *     <li>Guiding label</li>
     *     <li>Control buttons:
     *          <ul>
     *              <li>Refresh - refreshes the list of sctive notifications</li>
     *              <li>Delete - removes selected notification from the list</li>
     *          </ul>
     *     </li>
     *     <li>Status label
     *          <ul>
     *              <li>appears only if there are no notifications to show</li>
     *          </ul>
     *     </li>
     *     <li>Table with active notifications</li>
     * </ul>
     *
     * @return JPanel with labels, buttons and table.
     * @see StartServer
     * @see Globals
     */
    public JPanel createActiveNotificationsTab() {

        // Create guiding label and set its position and height
        JLabel label = new JLabel("Hit Refresh button to see your active notifications");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(Globals.getWIDTH()-50, 50));

        // Create status label and set its position and height
        final JLabel statusLabel = new JLabel("You have got no notification yet.");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setPreferredSize(new Dimension(Globals.getWIDTH()-50, 50));

        // Create refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create delete button
        JButton removeButton = new JButton("Delete");
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pack buttons as grid with 1 row and 2 columns
        JPanel buttons = new JPanel();
        buttons.add(refreshButton);
        buttons.add(removeButton);
        buttons.setLayout(new GridLayout(1,2));

        // Set table contents
        String[] columnNames = {"Source","Content"};
        // Globals.getACTIVE_NOTIFICATIONS() provides the list of active notifications
        final DefaultTableModel tableContent = new DefaultTableModel(Globals.getACTIVE_NOTIFICATIONS(),columnNames);

        // Create table and set its preferred size
        final JTable activeNotifications = new JTable(tableContent);
        JScrollPane sp = new JScrollPane(activeNotifications);
        sp.setPreferredSize(new Dimension(Globals.getWIDTH()-50, Globals.getHEIGHT()/2));

        // Refresh button will check if there are new data and adds it to the table
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // New data
                Object[][] data = Globals.getACTIVE_NOTIFICATIONS();
                if (data[0].length > 0) {
                    // Clear old data
                    if (tableContent.getRowCount() > 0) {
                        for (int i = tableContent.getRowCount() - 1; i > -1; i--) {
                            tableContent.removeRow(i);
                        }
                    }
                    // Clear label
                    statusLabel.setText("  ");
                    // Insert data
                    for (int i = 0; i < data.length; i++) {
                        Object[] insertRowData = data[i];
                        tableContent.insertRow(i,insertRowData);
                    }
                    tableContent.fireTableDataChanged();
                    activeNotifications.revalidate();
                }
            }
        });

        // Delete button will remove selected row from the table
        removeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int selRow = activeNotifications.getSelectedRow();
                if(selRow != -1) {
                    DefaultTableModel model = (DefaultTableModel)activeNotifications.getModel();
                    model.removeRow(selRow);
                }
            }
        });

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(buttons);
        pane.add(statusLabel);
        pane.add(sp, BorderLayout.CENTER);
        return pane;
    }

    /**
     * Creates tab with port number setting.<br>
     * <b>This tab has:</b>
     * <ul>
     *     <li>Guiding label</li>
     *     <li>Text field where port number will be entered
     *          <ul>
     *              <li>Port must contain 4 digits and be greater than 1023</li>
     *          </ul>
     *     </li>
     *     <li>Save button</li>
     *     <li>Status label shows:
     *          <ul>
     *              <li><i>error message</i> when port number is invalid</li>
     *              <li><i>success message</i> when port is successfully changed</li>
     *          </ul>
     *     </li>
     *     <li>QR image that changes when new port is set</li>
     * </ul>
     *
     * @return JPanel with labels, textfield, save button and QR image.
     */
    public JPanel createSettingsTab() {

        // Create guiding label
        JLabel labelPort = new JLabel("Change your port number");
        labelPort.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create textfield
        final JTextField textField = new JTextField(String.valueOf(Globals.getPORT()));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setHorizontalAlignment(JTextField.CENTER);

        // Create save button
        JButton button = new JButton("Save");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);;

        // Create status label
        final JLabel doneLabel = new JLabel("   ");
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create label for QR image
        JLabel labelQR = new JLabel("Don't forget to scan new QR code");
        labelQR.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get QR image
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMG_PATH()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        final JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* Save button will get the text from textfield and check it due to requirements.
        /* Also changes status label according to the situation.
         */
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String portString = textField.getText();
                if (portString.length() == 4 && portString.matches("[0-9]+")) {
                    int port = Integer.parseInt(portString);
                    if (port > 1023) {
                        Globals.setPORT(port);
                        c.saveQR();
                        BufferedImage newQR = null;
                        try {
                            newQR = ImageIO.read(new File(Globals.getIMG_PATH()));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        picLabel.setIcon(new ImageIcon(newQR));

                        doneLabel.setText("Port has been changed to " + port);
                        doneLabel.setForeground(Globals.getGREEN());
                    } else {
                        doneLabel.setText("Port must be greater than 1023!");
                        doneLabel.setForeground(Color.red);
                    }
                } else {
                    doneLabel.setText("Port must contain 4 digits!");
                    doneLabel.setForeground(Color.red);
                }
            }

        });

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(labelPort);
        pane.add(textField);
        pane.add(button);
        pane.add(doneLabel);
        pane.add(labelQR);
        pane.add(picLabel);
        return pane;
    }

    /**
     * Creates tab with information about program.
     *
     * @return JPanel with information label.
     * @see Globals where 'about' information is set.
     */
    public JPanel createAboutTab() {

        JLabel label = new JLabel("<html>"+ Globals.getABOUT() +"</html>");

        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        return pane;
    }

    /**
     * Calls <i>MainWindow()</i> constructor to create tabbed pane.
     * Frame is placed in the center of display and can be exited on close.
     */
    public void createWindow() {

        frame = new JFrame("Notification Displayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MainWindow content = new MainWindow();
        content.setOpaque(true);
        frame.setContentPane(content);

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
