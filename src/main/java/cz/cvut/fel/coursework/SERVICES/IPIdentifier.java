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

    private static ArrayList<String> ip_types = new ArrayList<String>();
    private static ArrayList<String> value = new ArrayList<String>();

    /**
     * Gets IPv4 address of machine.
     * @return String IPv4 address
     */

    public String getIP(){

        try {
            String ip;
            int i = 0;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    ip = ip.replace("%" + iface.getDisplayName(), "");
                    ip_types.add(iface.getDisplayName());
                    value.add(ip);
                    i++;
                }
            }
            for (NetworkInterface ni :
                    Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress address : Collections.list(ni.getInetAddresses())) {
                    if (address instanceof Inet4Address) {
                        System.out.println(address);
                    }
                }
            }
//            for (int j = 0; j < ip_types.size(); j++) {
//                if (value.get(j) instanceof Inet4Address) {
//                    System.out.println(value.get(j));
//                }
//                System.out.println(ip_types.get(j) + " " + value.get(j));
//            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return "";

//        String finalIP;
//
//        InetAddress detectedIP = null;
//
//        try {
//            detectedIP = InetAddress.getByAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//
//        finalIP = detectedIP.toString();
//        finalIP = finalIP.substring(finalIP.lastIndexOf("/") + 1);
//
//        return finalIP;

    }
}