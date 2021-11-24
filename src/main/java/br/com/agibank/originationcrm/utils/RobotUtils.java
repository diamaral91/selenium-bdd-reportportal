package br.com.frontendproject.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class RobotUtils {

    public static void atalhoDadosSimulacao(){
        final Robot robot;
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_I);
            robot.keyRelease(KeyEvent.VK_I);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveClipBoard() {
        final Clipboard systemClipboard = getSystemClipboard();
        final DataFlavor dataFlavor = DataFlavor.stringFlavor;

        if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
            try {
                return (String) systemClipboard.getData(dataFlavor);
            } catch (final IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Clipboard getSystemClipboard() {
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        return defaultToolkit.getSystemClipboard();
    }

    public static void pageDown() {
        final Robot robot;
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void pageUp() {
        final Robot robot;
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_PAGE_UP);
            robot.keyRelease(KeyEvent.VK_PAGE_UP);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
