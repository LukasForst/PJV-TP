package cz.cvut.fel.testing;

import cz.cvut.fel.coursework.Controller;
import cz.cvut.fel.coursework.Globals;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class QRGeneratorTest {

    private Controller c = new Controller();

    @Test
    public void QRImage() {
        c.setAppDirectory();
        c.setPathToImage();
        c.setIP();
        c.saveQR();

        File f = new File(Globals.getIMGPATH());

        Assert.assertEquals(
                true,
                f.exists() && f.isFile()
        );
    }
}
