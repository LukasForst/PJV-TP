/**
 * @[ile        IPIndentifier
 * @date        26.3.17
 * @author      Anastasia Surikova
 * @project     IPIndentifier
 */

package cz.cvut.fel.coursework;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

public class IPIdentifier {
    /*
    * do not use array, use ArrayList instead, because of we don't know the size of array before
    * */
    public static String[] ip_types = new String[4];
    public static String[] ips = new String[4];

    public void getIP() {
        try {
            String ip;
            int i = 0;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    ip = ip.replace("%" + iface.getDisplayName(), "");
                    ip_types[i] = iface.getDisplayName();
                    ips[i] = ip;
                    i++;
                }
            }
            for (int j = 0; j < ip_types.length; j++) {
                System.out.println(ip_types[j] + " " + ips[j]);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertIntoDatabase() {

        try {

            // Create connection with database
            String serverName = "sql11.freemysqlhosting.net";
            String mydatabase = "sql11169171";
            String url = "jdbc:mysql://" + serverName + ":3306" + "/" + mydatabase;
            String username = "sql11169171";
            String password = "WRXL46jEFg";
            Connection conn = DriverManager.getConnection(url, username, password);

            // Create table
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE ip_addresses (id int, type varchar(255), value varchar(255))");

            // Insert data
            for (int i = 0; i < ip_types.length; i++) {
                st.executeUpdate("INSERT INTO ip_addresses (id, type, value)" +
                        "VALUES (" + i + ", '" + ip_types[i] + "', '" + ips[i] + "')");
            }

            conn.close();

        } catch(Exception e) {
            System.out.println("Oops, something went wrong :(");
            System.out.println(e.toString());
        }
    }
}