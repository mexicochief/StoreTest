package org.store.test.property;

import org.store.test.TestApp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SimplePropertiesLoader implements PropertyLoader {
    @Override
    public Properties load(String path) {
        Properties properties = new Properties();
        try (InputStream inputStream =
                     TestApp.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException();
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return properties;
    }
}
