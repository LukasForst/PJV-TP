package cz.cvut.fel.coursework.SERVICES;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPIdentifier {

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