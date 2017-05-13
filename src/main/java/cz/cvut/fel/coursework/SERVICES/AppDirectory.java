package cz.cvut.fel.coursework.SERVICES;

import java.io.File;

public class AppDirectory {

    private String userHome = System.getProperty("user.home");
    private File theDir = new File(userHome + "/" + "NotificationDisplayer");

    public String getUserHome() {
        return userHome;
    }

    public String getDirName() {
        return theDir.getName();
    }

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
                System.out.println("DIR created");
            }
            dirCreated = true;
        }
        return dirCreated;
    }
}
