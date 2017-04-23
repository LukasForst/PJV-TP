/**
 * @[ile        IPIndentifier
 * @date        26.3.17
 * @author      Anastasia Surikova
 * @project     IPIndentifier
 */

package cz.cvut.fel.coursework.SERVER;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

public class IPIdentifier {

    public static ArrayList<String> type = new ArrayList<String>();
    public static ArrayList<String> value = new ArrayList<String>();

    public void getIP() {
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
                    type.add(iface.getDisplayName());
                    value.add(ip);
                    i++;

                }
            }
            for (int j = 0; j < type.size(); j++) {
                System.out.println(type.get(j) + " " + value.get(j));
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertIntoDatabase() {

        try {

            // TODO: figure out the database, free one in not an option
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
            for (int i = 0; i < type.size(); i++) {
                st.executeUpdate("INSERT INTO ip_addresses (id, type, value)" +
                        "VALUES (" + i + ", '" + type.get(i) + "', '" + value.get(i) + "')");
            }

            conn.close();

        } catch(Exception e) {
            System.out.println("Oops, something went wrong :(");
            System.out.println(e.toString());
        }
    }
}