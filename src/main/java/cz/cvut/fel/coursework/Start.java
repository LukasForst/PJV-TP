package cz.cvut.fel.coursework;

public class Start {
    public static void main(String[] args) {
        IPIdentifier ipIdentifier = new IPIdentifier();
        ipIdentifier.getIP();
        ipIdentifier.insertIntoDatabase();
    }
}
