package pidev.esprit.API;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final String PROPERTY_FILE = "/config.properties"; // Change the path based on your project structure
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = PropertyReader.class.getResourceAsStream(PROPERTY_FILE)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + PROPERTY_FILE);
            }

            // load a properties file from class path, inside static method
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
