package cz.cvut.fel.coursework;

public class Notification {

    Enum os = OSDetection.getOS();

    public void notificate() {

        if (os.toString().equals("MAC")) {
            System.out.println("This is MAC");
            macNotificator();
        }

    }

    public void macNotificator() {

    }
}
