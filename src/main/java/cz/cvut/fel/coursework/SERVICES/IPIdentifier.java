package cz.cvut.fel.coursework.SERVICES;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Finds out current IPv4 address of the machine.
 * The class has only one <b>getIP()</b> method.
 * @author Anastasia Surikova
 */

public class IPIdentifier {

    /**
     * Finds out all the InetAddresses of machine and returns the IPv4 one.
     * @return String IPv4 address
     */

    public String getIP() {
        try {

            String ip;

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {

                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();

                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    ip = ip.replace("%" + iface.getDisplayName(), "");
                    if (ip.startsWith("192.168.")) {
                        return ip;
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}