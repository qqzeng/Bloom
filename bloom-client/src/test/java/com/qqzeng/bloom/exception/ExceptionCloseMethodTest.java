package com.qqzeng.bloom.exception;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author qqzeng
 * @desc Method for exception closing
 */
public class ExceptionCloseMethodTest {

    final private Logger LOGGER = LoggerFactory.getLogger(ExceptionCloseMethodTest.class);

    /**
     * using Try-With-Resource feature
     */
    @Test
    public void testAutomaticallyCloseResource() {
        String relativelyPath=System.getProperty("user.dir");
        System.out.println("-------------------");
        try (FileInputStream fis = new FileInputStream(
                new File(relativelyPath +"\\src\\test\\java\\com\\qqzeng\\bloom\\exception\\ExceptionCloseMethodTest.java"))) {
            byte[] bytes = new byte[1024];
            fis.read(bytes);
            System.out.println(new String(bytes, 0, bytes.length));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    @Test
    public void testLog4j() throws Exception {
        LOGGER.debug("Discover service: {} from {}", "TestService", "192.168.0.107");
    }

}
