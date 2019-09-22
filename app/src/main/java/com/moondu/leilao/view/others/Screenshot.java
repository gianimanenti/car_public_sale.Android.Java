package com.moondu.leilao.view.others;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Screenshot {

    private static Bitmap takescreenshot(View v) {

        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        return b;
    }

    public static Bitmap takescreenshotOfRootView(View v) {

        return takescreenshot(v.getRootView());
    }

    public static File storeScreenshot(Bitmap bitmap, String filename, File storageDir) {

        //String path = Environment.getExternalStorageDirectory().toString() + "/" + filename;

        OutputStream out = null;
        //File imageFile = new File(path);

        File imageFile = null;

        try {

            imageFile = File.createTempFile(
                filename, /* prefixo */
                ".jpg", /* suffixo */
                storageDir /* diretório */
            );

            out = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {

            String msg = e.getMessage();
        } catch (IOException e) {

            String msg = e.getMessage();
        } finally {

            try {

                if (out != null) {
                    out.close();
                }

            } catch (Exception exc) {

                String msg = exc.getMessage();
            }
        }

        return imageFile;
    }
}
