package com.webbora.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/5/4
 */
class ApachePoiXLSBReaderTest {

    @Test
    void testTeadXLSBFile() {
        String xlsbFileName = getClass().getClassLoader().getResource("test.xlsb").getPath();
        String[][] data = ApachePoiXLSBReader.readXLSBFile(xlsbFileName, "report1", "A1", "D5");
        for (String[] row : data) {
            System.out.println(Arrays.toString(row));
        }
        Assertions.assertEquals(5, data.length);
    }


}