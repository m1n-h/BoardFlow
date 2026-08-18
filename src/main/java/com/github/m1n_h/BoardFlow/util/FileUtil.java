package com.github.m1n_h.BoardFlow.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) file = new File("src/main/resources/" + filePath);

        return new String(Files.readAllBytes(file.toPath()));
    }
}
