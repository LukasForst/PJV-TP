package cz.cvut.fel.coursework.GUI;

import cz.cvut.fel.coursework.GLOBAL;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JPanel {

    public MainWindow() {
        super(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel tab1 = new JPanel();
        //Use default FlowLayout.
        tab1.add(createTab1());
        tab1.add(createStatus());
        tabbedPane.addTab("Main", tab1);

        JPanel tab2 = new JPanel();
        //Use default FlowLayout.
        tab2.add(createTab2());
        tabbedPane.addTab("Settings", tab2);

        //Add tabbedPane to this panel.
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void createWindow() {

        JFrame frame = new JFrame("Notificator Displayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MainWindow content = new MainWindow();
        content.setOpaque(true);
        frame.setContentPane(content);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    public JPanel createTab1() {
        JButton button1 = new JButton("Start");
        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
        button1.setHorizontalTextPosition(AbstractButton.CENTER);

        JButton button2 = new JButton("Stop");
        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
        button2.setHorizontalTextPosition(AbstractButton.CENTER);

        JButton button3 = new JButton("Hide to toolbar");
        button3.setVerticalTextPosition(AbstractButton.BOTTOM);
        button3.setHorizontalTextPosition(AbstractButton.CENTER);

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
        return pane;
    }

    public JPanel createStatus() {

        // TODO: add getStatus()
        JLabel label = new JLabel("Stopped.");

        String title = "Status";
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        return pane;
    }

    public JPanel createTab2() {

        JTextField textField = new JTextField(String.valueOf(GLOBAL.PORT), 10);

        JButton button = new JButton("Save");
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);

        String title = "Port";
        textField.setAlignmentX(CENTER_ALIGNMENT);
        textField.setHorizontalAlignment(JTextField.CENTER);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(textField);
        pane.add(button);
        return pane;
    }


}
