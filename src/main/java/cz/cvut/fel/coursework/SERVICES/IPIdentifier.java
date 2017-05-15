package cz.cvut.fel.coursework.SERVICES;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Finds out current IPv4 address of the machine.
 * The class has only one <b>getIP()</b> method.
 * @author Anastasia Surikova
 */

public class IPIdentifier {

    /**
     * Gets IPv4 address of machine.
     * @return String IPv4 address
     */

    public String getIP(){

        String finalIP;

        InetAddress detectedIP = null;

        try {
            detectedIP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        finalIP = detectedIP.toString();
        finalIP = finalIP.substring(finalIP.lastIndexOf("/") + 1);

        return finalIP;

    }
}