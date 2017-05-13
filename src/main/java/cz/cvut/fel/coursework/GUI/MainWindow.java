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
        tab1.add(createWelcomeTab());
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

    public JPanel createWelcomeTab() {

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMGPATH()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        final JButton startButton = new JButton("Start");
        final JButton stopButton = new JButton("Stop");
        JButton hideButton = new JButton("Hide");

        final JLabel statusLabel = new JLabel("Stopped.");

        statusLabel.setForeground(Color.red);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        statusLabel.setAlignmentY(BOTTOM_ALIGNMENT);

        startButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        startButton.setHorizontalTextPosition(AbstractButton.CENTER);

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

        stopButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        stopButton.setHorizontalTextPosition(AbstractButton.CENTER);
        stopButton.setEnabled(false);

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

        hideButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        hideButton.setHorizontalTextPosition(AbstractButton.CENTER);

        hideButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.setState(Frame.ICONIFIED);
            }
        });

        String title = "Main";
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setAlignmentY(BOTTOM_ALIGNMENT);
        stopButton.setAlignmentX(CENTER_ALIGNMENT);
        stopButton.setAlignmentY(BOTTOM_ALIGNMENT);
        hideButton.setAlignmentY(BOTTOM_ALIGNMENT);
        hideButton.setAlignmentX(CENTER_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(picLabel);
        pane.add(startButton);
        pane.add(stopButton);
        pane.add(hideButton);
        pane.add(statusLabel);
        pane.setPreferredSize(new Dimension(Globals.getWIDTH()-20, Globals.getHEIGHT()));
        return pane;
    }

    public JPanel createActiveNotificationsTab() {

        String title = "Active notifications";

        final JLabel label = new JLabel("You have got no notification yet.");

        String[] columnNames = {"Source","Content"};
        DefaultTableModel tableContent = new DefaultTableModel(Globals.getData(),columnNames);

        JTable activeNotifications = new JTable(tableContent);
        JScrollPane sp = new JScrollPane(activeNotifications);

        JButton refreshButton = new JButton("Refresh");

        refreshButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        refreshButton.setHorizontalTextPosition(AbstractButton.CENTER);

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Clear old data
                if (tableContent.getRowCount() > 0) {
                    for (int i = tableContent.getRowCount() - 1; i > -1; i--) {
                        tableContent.removeRow(i);
                    }
                }

                // New data
                Object[][] data = Globals.getData();
                if (data[0].length > 0) {

                    // Clear label
                    label.setText("  ");

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

        JButton removeButton = new JButton("Delete");

        removeButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        removeButton.setHorizontalTextPosition(AbstractButton.CENTER);

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
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(refreshButton);
        pane.add(removeButton);
        pane.add(sp, BorderLayout.SOUTH);
        pane.setPreferredSize(new Dimension(Globals.getWIDTH()-20, Globals.getHEIGHT()-50));
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

    public JPanel createSettingsTab() {

        String title = "Port";

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMGPATH()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        final JLabel doneLabel = new JLabel("   ");
        doneLabel.setAlignmentX(CENTER_ALIGNMENT);
        doneLabel.setHorizontalAlignment(JTextField.CENTER);

        final JTextField textField = new JTextField(String.valueOf(Globals.getPORT()));
        textField.setAlignmentX(CENTER_ALIGNMENT);
        textField.setHorizontalAlignment(JTextField.CENTER);

        JButton button = new JButton("Save");
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

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

        JButton genQR = new JButton("Generate new QR code");
        genQR.setVerticalTextPosition(AbstractButton.BOTTOM);
        genQR.setHorizontalTextPosition(AbstractButton.CENTER);
        genQR.setAlignmentX(CENTER_ALIGNMENT);
        genQR.setAlignmentY(BOTTOM_ALIGNMENT);


        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(textField);
        pane.add(button);
        pane.add(doneLabel);
        pane.add(picLabel);
        pane.setPreferredSize(new Dimension(Globals.getWIDTH()/2, Globals.getHEIGHT()/2));
        return pane;
    }


}
