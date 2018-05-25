package com.qqzeng.bloom.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author qqzeng
 * @desc Property file reader.
 */
public class PropertyReader {

    private String fileName;

    public PropertyReader(String fileName) {
        this.fileName = fileName;
    }

    public String getProperty(String key) {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + this.fileName);
            }
            prop.load(input);
            return prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
