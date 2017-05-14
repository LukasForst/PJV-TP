package cz.cvut.fel.testing;

import cz.cvut.fel.coursework.Controller;
import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVICES.AppDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Tests the creation of new directory
 * @author Anastasia Surikova
 */
public class AppDirectoryTest {

    private AppDirectory dm = new AppDirectory();
    private Controller c = new Controller();
    private File directory = new File(System.getProperty("user.home") + "/" + "NotificationDisplayer");

    @Test
    public void createAppDirectory() {

        dm.createAppDirectory();

        Assert.assertEquals(
                true,
                directory.exists()
        );
    }

    @Test
    public void setAppDirectory() {

        c.setAppDirectory();

        Assert.assertEquals(
                directory.getPath(),
                Globals.getAPP_DIRECTORY()
        );
    }
}
