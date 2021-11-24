package br.com.frontendproject.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertieUtils {

    private static final String FILE_PATH = "src/main/resources/config.properties";

    public static String getValue(final String propertie) {
        final Properties props = new Properties();
        final FileInputStream file;
        try {
            file = new FileInputStream(FILE_PATH);
            props.load(file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return props.getProperty(propertie);
    }
}
