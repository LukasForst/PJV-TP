package cz.cvut.fel.coursework.GUI;

import cz.cvut.fel.coursework.GLOBAL;
import cz.cvut.fel.coursework.SERVER.IPIdentifier;
import cz.cvut.fel.coursework.SERVER.JSONReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

        tabbedPane.setPreferredSize(new Dimension(GLOBAL.WIDTH, GLOBAL.HEIGHT));

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

        final JLabel statusLabel = new JLabel("Stopped.");
        statusLabel.setForeground(Color.red);

        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        statusLabel.setAlignmentY(BOTTOM_ALIGNMENT);

        final JButton button1 = new JButton("Start");
        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
        button1.setHorizontalTextPosition(AbstractButton.CENTER);

        final JButton button2 = new JButton("Stop");
        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
        button2.setHorizontalTextPosition(AbstractButton.CENTER);

        // Saves IP to database and starts server
        button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                // TODO: not working because of shitty database
//                statusLabel.setText("Saving IP address to database...");
//                IPIdentifier ip = new IPIdentifier();
//                ip.getIP();
//                ip.insertIntoDatabase();
//                statusLabel.setText("Done.");

                statusLabel.setText("Creating server connection...");
                try {
                    button1.setEnabled(false);
                    button2.setEnabled(true);
                    statusLabel.setText("Waiting for your notifications :)");
                    statusLabel.setForeground(GLOBAL.GREEN);

                    // TODO: handle vlákna first

//                    JSONReceiver r = new JSONReceiver();
//                    r.startListening();

                } catch (Exception ignored){
                    statusLabel.setText("Error");
                    statusLabel.setForeground(Color.red);
                }
            }
        });

        // Saves IP to database and starts server
        button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                button2.setEnabled(false);
                statusLabel.setText("Stopping...");
                try {

                    // TODO: handle stopping the server
                    button1.setEnabled(true);
                    statusLabel.setText("Stopped.");
                    statusLabel.setForeground(Color.red);

                } catch (Exception ignored){
                    statusLabel.setText("Error");
                    statusLabel.setForeground(Color.red);
                }
            }
        });

        JButton button3 = new JButton("Hide to toolbar");
        button3.setVerticalTextPosition(AbstractButton.BOTTOM);
        button3.setHorizontalTextPosition(AbstractButton.CENTER);

        // Saves IP to database and starts server
        button3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

//                setNormalScreen(); //disable fullscreen mode
//                //this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//                setVisible(false);
//                this.setState(JFrame.ICONIFIED);
            }
        });

        String title = "Main";
        button1.setAlignmentX(CENTER_ALIGNMENT);
        button1.setAlignmentY(BOTTOM_ALIGNMENT);
        button2.setAlignmentX(CENTER_ALIGNMENT);
        button2.setAlignmentY(BOTTOM_ALIGNMENT);
        button3.setAlignmentY(BOTTOM_ALIGNMENT);
        button3.setAlignmentX(CENTER_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(button1);
        pane.add(button2);
        pane.add(button3);
        pane.add(statusLabel);
        pane.setPreferredSize(new Dimension(GLOBAL.WIDTH-20, 150));
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
//        pane.setPreferredSize(new Dimension(GLOBAL.WIDTH-20, 50));
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
        pane.setPreferredSize(new Dimension(GLOBAL.WIDTH-20, GLOBAL.HEIGHT-50));
        return pane;
    }

    public JPanel createAbout() {

        JLabel label = new JLabel("<html>"+ GLOBAL.ABOUT +"</html>");

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

        final JTextField textField = new JTextField(String.valueOf(GLOBAL.PORT));
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
                    GLOBAL.setPORT(Integer.valueOf(newPort));
                    doneLabel.setText("Port has been changed to " + newPort);
                    doneLabel.setForeground(GLOBAL.GREEN);
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
        pane.setPreferredSize(new Dimension(GLOBAL.WIDTH/2, GLOBAL.HEIGHT/2));
        return pane;
    }


}
