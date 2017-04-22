package cz.cvut.fel.coursework.GUI;

import javax.swing.*;

public class MainWindow {

    public void createWindow() {
        Gui okno = new Gui();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setVisible(true);
        okno.setSize(300, 200);
        okno.setLocationRelativeTo(null);
    }

}
