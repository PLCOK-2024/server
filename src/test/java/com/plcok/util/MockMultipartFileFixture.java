package com.plcok.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MockMultipartFileFixture {
    public static File mockImageFile() throws IOException {
        File tempFile = File.createTempFile("test", ".jpg");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("test file content".getBytes());
        }

        return tempFile;
    }
}
