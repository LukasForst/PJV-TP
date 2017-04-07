/**
 * @[ile        IPIndentifier
 * @date        26.3.17
 * @author      Anastasia Surikova
 * @project     IPIndentifier
 */

package cz.cvut.fel.coursework.computer;

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
//                    System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
            for (int j = 0; j < ip_types.length; j++) {
                System.out.println(ip_types[j] + " " + ips[j]);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertInDatabase() {
/*  deprecated, it is better to use newer version of mysql driver library
    use: mysql:mysql-connector-java:6.0.6
*/
        try {
            /* alternative which is working
            Server: sql11.freemysqlhosting.net
            Name: sql11165963
            Username: sql11165963
            Password: V1NQqt6CgL
            Port number: 3306
            usage as:
            url = "jdbc:mysql://" + serverName + ":"+ port + "/" + mydatabase;
             */
            String serverName = "uvdb28.active24.cz";
            String mydatabase = "anastasias";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
            String username = "anastasias";
            String password = "nuFGxBE2";
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ip_addresses");
            while (rs.next()) System.out.println(rs.getString(1));
        } catch(Exception e) {
            System.out.println("Chyba volani, neprihlaseno");
            System.out.println(e.toString());
        }
//        com.dbaccess.BasicDataSource dataSource = new com.dbaccess.BasicDataSource();
//        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setUser("anastasias");
//        dataSource.setPassword("nuFGxBE2");
//        dataSource.setServerName("uvdb28.active24.cz");
//        try {
//
//            // instantiating and configuring DataSource from database driver
//
//            String driverName = "org.gjt.mm.mysql.Driver";
//            Class.forName(driverName);
//
//            String serverName = "localhost";
//            String mydatabase = "anastasias";
//            String url = "jdbc:mysql :// " + serverName + "/" + mydatabase;
//
//            String username = "anastasias";
//            String password = "nuFGxBE2";
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//
//            // obtain connections from it
//
////            Connection conn = dataSource.getConnection();
//            Statement stmt = connection.createStatement();
//
//            stmt.executeUpdate("INSERT INTO ip_addresses " +
//                    "VALUES (1, " + ip_types[0] + ", " + ips[0] + ")");
//            stmt.executeUpdate("INSERT INTO ip_addresses " +
//                    "VALUES (2, " + ip_types[1] + ", " + ips[1] + ")");
//            stmt.executeUpdate("INSERT INTO ip_addresses " +
//                    "VALUES (3, " + ip_types[2] + ", " + ips[2] + ")");
//            stmt.executeUpdate("INSERT INTO ip_addresses " +
//                    "VALUES (4, " + ip_types[3] + ", " + ips[3] + ")");
//
//            stmt.close();
//            connection.close();
//
//        } catch (Exception e) {
//            System.err.println("Got an exception! ");
//            System.err.println(e.getMessage());
//        }
    }
}