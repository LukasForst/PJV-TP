package cz.cvut.fel.coursework.SERVICES;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
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

        try {
            String ip;

            for (NetworkInterface ni :
                    Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress address : Collections.list(ni.getInetAddresses())) {
                    if (address instanceof Inet4Address) {
                        ip = address.toString();
                        if(!ip.startsWith("127.")) {
                            return ip.substring(ip.lastIndexOf("/") + 1);
                        }
                    }
                }
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return "";

    }
}