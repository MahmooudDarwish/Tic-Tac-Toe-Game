/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.jsonutil;

/**
 *
 * @author Mohammed
 */
// 3. Save JSON to a File

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static void saveJsonToFile(String json, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(json);
        }
    }
}
