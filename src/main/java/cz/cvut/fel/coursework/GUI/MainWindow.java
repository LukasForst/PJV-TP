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


public class MainWindow extends JPanel {

    private static Controller c = new Controller();
    private static JFrame frame;

    public MainWindow() {
        super(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel tab1 = new JPanel();
        JPanel steps = new JPanel();
        steps.add(createWelcomeTabStep1());
        steps.add(createWelcomeTabStep2());
        steps.setLayout(new GridLayout(1,2));
        tab1.add(createWelcomeTabMessage());
        tab1.add(steps);
        tab1.setLayout(new BoxLayout(tab1, BoxLayout.Y_AXIS));
        tabbedPane.addTab("Welcome", tab1);

        JPanel tab2 = new JPanel();
        tab2.add(createActiveNotificationsTab());
        tabbedPane.addTab("My notifications", tab2);

        JPanel tab3 = new JPanel();
        tab3.add(createSettingsTab());
        tabbedPane.addTab("Settings", tab3);

        JPanel tab4 = new JPanel();
        tab4.add(createAboutTab());
        tabbedPane.addTab("About", tab4);

        tabbedPane.setPreferredSize(new Dimension(Globals.getWIDTH(), Globals.getHEIGHT()));

        add(tabbedPane, BorderLayout.CENTER);
    }

    public JPanel createWelcomeTabMessage() {
        JLabel label = new JLabel("Welcome to Notification Displayer!");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel pane = new JPanel();
        pane.add(label);
        return pane;
    }

    public JPanel createWelcomeTabStep1() {

        String title = "Step 1";
        JLabel label = new JLabel("Scan this QR code with your mobile phone");

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get QR picture
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMGPATH()));
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

    public JPanel createWelcomeTabStep2() {

        String title = "Step 2";
        JLabel label = new JLabel("Use control buttons to start application");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        final JButton stopButton = new JButton("Stop");
        stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        stopButton.setEnabled(false);

        JButton hideButton = new JButton("Hide");
        hideButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel statusLabel = new JLabel("Stopped.");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.red);

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

    public JPanel createActiveNotificationsTab() {

        JLabel label = new JLabel("Hit Refresh button to see your active notifications");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(Globals.getWIDTH()-50, 50));

        final JLabel statusLabel = new JLabel("You have got no notification yet.");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setPreferredSize(new Dimension(Globals.getWIDTH()-50, 50));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton removeButton = new JButton("Delete");
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttons = new JPanel();
        buttons.add(refreshButton);
        buttons.add(removeButton);
        buttons.setLayout(new GridLayout(1,2));

        String[] columnNames = {"Source","Content"};
        DefaultTableModel tableContent = new DefaultTableModel(Globals.getData(),columnNames);

        JTable activeNotifications = new JTable(tableContent);
        JScrollPane sp = new JScrollPane(activeNotifications);
        sp.setPreferredSize(new Dimension(Globals.getWIDTH()-50, Globals.getHEIGHT()/2));

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // New data
                Object[][] data = Globals.getData();
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

    public JPanel createSettingsTab() {

        JLabel labelPort = new JLabel("Change your port number");
        labelPort.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JTextField textField = new JTextField(String.valueOf(Globals.getPORT()));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setHorizontalAlignment(JTextField.CENTER);

        JButton button = new JButton("Save");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);;

        final JLabel doneLabel = new JLabel("   ");
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelQR = new JLabel("Don't forget to scan new QR code");
        labelQR.setAlignmentX(Component.CENTER_ALIGNMENT);

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMGPATH()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
                            newQR = ImageIO.read(new File(Globals.getIMGPATH()));
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
        pane.add(Box.createRigidArea(new Dimension(5,0)));
        pane.add(textField);
        pane.add(button);
        pane.add(doneLabel);
        pane.add(labelQR);
        pane.add(picLabel);
        return pane;
    }

    public JPanel createAboutTab() {

        JLabel label = new JLabel("<html>"+ Globals.getABOUT() +"</html>");

        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        return pane;
    }

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
