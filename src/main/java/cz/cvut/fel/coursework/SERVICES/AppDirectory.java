package cz.cvut.fel.coursework.SERVICES;

import java.io.File;

/**
 * Creates program's directory in user's home.
 * The class has 3 methods: getUserHome(), getDirName() and createAppDirectory().
 * User's home directory is assigned to private variable <i>userHome</i>.
 * A directory that will be created is specified in private variable <i>theDir</i>.
 * @author Anastasia Surikova
 */

public class AppDirectory {

    private String userHome = System.getProperty("user.home");
    private File theDir = new File(userHome + "/" + "NotificationDisplayer");

    /**
     * Gets the path to user's home directory.
     * @return String User's home directory
     */
    public String getUserHome() {
        return userHome;
    }

    /**
     * Gets name of program's directory.
     * @return String Name of program's directory
     */
    public String getDirName() {
        return theDir.getName();
    }

    /**
     * Creates program's directory if it doesn't exist.
     * @return Boolean <code>true</code> if new directory was successfully created;<br>
     *                 <code>false</code> if the directory already exists and nothing was created.
     */
    public Boolean createAppDirectory() {

        Boolean dirCreated = false;

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            }
            catch (SecurityException e){
                e.printStackTrace();
            }
            if(result) {
                System.out.println("New directory was created");
            }
            dirCreated = true;
        }
        return dirCreated;
    }
}
