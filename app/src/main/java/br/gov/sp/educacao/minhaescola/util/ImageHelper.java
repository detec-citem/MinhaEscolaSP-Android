package br.gov.sp.educacao.minhaescola.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by T. Rocha on 12/07/2018.
 */

public class ImageHelper {

    public static Bitmap bitmapFromBase64(String imgString){

        final String[] base = imgString.split(",");
        String input = base[1];
        byte[] imageBytes;
        imageBytes = Base64.decode(input, Base64.DEFAULT);
        final Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return decodedImage;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }
}
