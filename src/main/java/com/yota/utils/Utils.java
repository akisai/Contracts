package com.yota.utils;

import com.yota.ui.ExMessage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class Utils {

    public static void writeFile(File path, String data) throws FileNotFoundException {
        try ( PrintWriter writer = new PrintWriter(path) ) {
            writer.write(data);
        }
    }

    public static void showExDialog(String message) {
        JDialog exMessage = new ExMessage(message);
        exMessage.setTitle("Error");
        exMessage.setVisible(true);
    }

    public static void validateInput(String iccid) throws NumberFormatException {
        if (iccid.length() != 10) {
            throw new NumberFormatException("Wrong lenght");
        }
        if (!iccid.startsWith("01") && !iccid.startsWith("02")) {
            throw new NumberFormatException("Wrong start symbol");
        }
        try {
            Integer.parseInt(iccid);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not number");
        }
    }
}
