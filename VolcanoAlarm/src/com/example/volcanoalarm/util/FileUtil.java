package com.example.volcanoalarm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

public class FileUtil {

    public static void saveToInternalStorage(String fileName, Bitmap outputImage) {

        File file = new File(fileName);

        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            outputImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }
}
