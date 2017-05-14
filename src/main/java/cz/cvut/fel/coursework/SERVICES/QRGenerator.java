package cz.cvut.fel.coursework.SERVICES;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import cz.cvut.fel.coursework.Globals;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Generates and saves QR image that helps computer and smartphone connect with each other.
 * The class has only one method <b>saveQR()</b>.
 * @author Crunchify.com
 */

public class QRGenerator {

    /**
     * Creates QR image with information about IP address and port number.
     * Method saves created image as 'qr.png' to program's directory.
     * This image will be scanned with smartphone that will lead to connection between smartphone and computer.
     * @see cz.cvut.fel.coursework.GUI.MainWindow MainWindow class where created image is used
     */
    public void saveQR() {

        String myCodeText = "IP " + Globals.getIP() + " PORT " + Globals.getPORT();
        System.out.println("Text to code in qr: " + myCodeText);
        int size = 250;
        String fileType = "png";
        File myFile = new File(Globals.getIMG_PATH());

        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            hintMap.put(EncodeHintType.MARGIN, 1);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
                    size, hintMap);
            int Width = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(Width, Width, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, Width, Width);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < Width; i++) {
                for (int j = 0; j < Width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, myFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException we) {
            we.printStackTrace();
        }
        System.out.println("You have successfully created QR Code.");
    }
}