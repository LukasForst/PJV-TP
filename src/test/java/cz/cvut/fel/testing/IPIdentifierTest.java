package cz.cvut.fel.testing;

import cz.cvut.fel.coursework.Controller;
import cz.cvut.fel.coursework.Globals;
import cz.cvut.fel.coursework.SERVICES.IPIdentifier;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests correct IP address setting
 * @author Anastasia Surikova
 */
public class IPIdentifierTest {

    private IPIdentifier ipi = new IPIdentifier();
    private Controller c = new Controller();

    @Test
    public void getIP() {
        Assert.assertEquals(
            false,
                ipi.getIP().equals("")
        );
    }

    @Test
    public void setIP() {

        c.setIP();
        Assert.assertEquals(
                false,
                Globals.getIP() == null
        );
    }

    @Test
    public void checkIP() {
        c.setIP();
        Assert.assertEquals(
                false,
                Globals.getIP().startsWith("127.")
        );
    }
}
