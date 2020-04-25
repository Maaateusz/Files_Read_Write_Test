package com.example.files_test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    /**
     *  The Gallery type applications must be notified about the Freshly Captured Pic.
     *  Using the broadcast we are notifying the Media Scanner that a new pic has been
     *  saved inside the External Storage.
     * */

    public static void notifyGalleryAboutPic(Context context, String imagePath) {

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imagePath);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        context.sendBroadcast(intent);
    }

    /**
     * @return File having a unique name as it's suffix has Time Stamp. Eg: IMG_20180923_104519
     * */
    public static File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File imageFile = new File(storageDir, imageFileName + ".jpg");

        return imageFile;
    }

    /**
     * @param originalBitmap : The very large sized bitmap that needs to be scaled down.
     * @return The resized Bitmap.
     * */
    public static Bitmap getResizedBitmap(Bitmap originalBitmap) {

        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        int maxSize = 960;

        if(width > maxSize || height > maxSize) {

            if (width > height) {			// If the image is in Landscape mode
                float ratio = (float) width / maxSize;
                width = maxSize;
                height = (int) (height / ratio);
            } else if (height > width) {	// If the image if in Portrait mode
                float ratio = (float) height / maxSize;
                height = maxSize;
                width = (int) (width / ratio);
            } else {						// Square Shaped
                height = maxSize;
                width = maxSize;
            }
        } // Else: The image is too small, lets not resize it.

        // Lets create a scaled bitmap.
        Bitmap bitmapScaled = Bitmap.createScaledBitmap(originalBitmap, width, height, false);

        Log.i("BitmapOriginal Size", originalBitmap.getHeight() + " " + originalBitmap.getWidth());
        Log.i("BitmapScaled Size", bitmapScaled.getHeight() + " " + bitmapScaled.getWidth());

        return bitmapScaled;
    }
}
