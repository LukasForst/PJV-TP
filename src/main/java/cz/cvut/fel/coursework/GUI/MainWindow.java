package cz.cvut.fel.coursework.GUI;

import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVER.StartServer;
import cz.cvut.fel.coursework.SERVER.StopServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MainWindow extends JPanel {

    public MainWindow() {
        super(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel tab1 = new JPanel();
//        tab1.setLayout(new BoxLayout(tab1, BoxLayout.X_AXIS));
        tab1.add(createControlButtons());
//        tab1.add(createStatus());
        tabbedPane.addTab("Main", tab1);

        JPanel tab2 = new JPanel();
        tab2.add(createNotifications());
        tabbedPane.addTab("My notifications", tab2);

        JPanel tab3 = new JPanel();
        tab3.add(createConfiguration());
        tabbedPane.addTab("Settings", tab3);

        JPanel tab4 = new JPanel();
        tab4.add(createAbout());
        tabbedPane.addTab("About", tab4);

        tabbedPane.setPreferredSize(new Dimension(Globals.WIDTH, Globals.HEIGHT));

        //Add tabbedPane to this panel.
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void createWindow() {

        JFrame frame = new JFrame("Notification Displayer");
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

    public JPanel createControlButtons() {

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File(Globals.getIMGPATH()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        final JButton startButton = new JButton("Start");
        final JButton stopButton = new JButton("Stop");
        JButton hideButton = new JButton("Hide to toolbar");
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
                statusLabel.setForeground(Globals.GREEN);

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
                // TODO: hide to tray
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
        pane.setPreferredSize(new Dimension(Globals.WIDTH-20, Globals.HEIGHT));
        return pane;
    }

//    public JPanel createStatus() {
//
//        // TODO: add getStatus()
//        JLabel label = new JLabel("Stopped.");
//
//        String title = "Status";
//        label.setAlignmentX(CENTER_ALIGNMENT);
//        label.setAlignmentY(BOTTOM_ALIGNMENT);
//
//        JPanel pane = new JPanel();
//        pane.setBorder(BorderFactory.createTitledBorder(title));
//        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
//        pane.setPreferredSize(new Dimension(Globals.WIDTH-20, 50));
//        pane.add(label);
//        return pane;
//    }

    public JPanel createNotifications() {

        // TODO: handle active notifications
        String title = "Active notifications";

        JLabel label = new JLabel("None");
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.setPreferredSize(new Dimension(Globals.WIDTH-20, Globals.HEIGHT-50));
        return pane;
    }

    public JPanel createAbout() {

        JLabel label = new JLabel("<html>"+ Globals.ABOUT +"</html>");

        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        return pane;
    }

    public JPanel createConfiguration() {

        String title = "Port";

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
                String newPort = textField.getText();
                if (newPort.length() == 4 && newPort.matches("[0-9]+")) {
                    Globals.setPORT(Integer.valueOf(newPort));
                    doneLabel.setText("Port has been changed to " + newPort);
                    doneLabel.setForeground(Globals.GREEN);
                } else {
                    doneLabel.setText("Port must contain 4 digits!");
                    doneLabel.setForeground(Color.red);
                }
            }

        });

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(textField);
        pane.add(button);
        pane.add(doneLabel);
        pane.setPreferredSize(new Dimension(Globals.WIDTH/2, Globals.HEIGHT/2));
        return pane;
    }


}
