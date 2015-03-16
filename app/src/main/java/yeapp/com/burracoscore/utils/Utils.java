package yeapp.com.burracoscore.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static final char ASide ='A';
    public static final char BSide = 'B';
    public static final String giocatore1 = "Giocatore 1";
    public static final String giocatore2 = "Giocatore 2";

    public static String getDefaultTeamName(char side){
        return "Team " + side;
    }

    public static String formattedString(String originalName)
    {
        StringBuilder result = new StringBuilder();
        for(int i=0;i<originalName.length();i++) {
            result.append(originalName.charAt(i));
            if (i+1 < originalName.length()) {
                result.append('\n');
            }
        }
        return result.toString();
    }

    public static Bitmap getThumbnail(Context context, String uri, int preferredSize) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(Uri.parse(uri));
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > preferredSize) ? (originalSize / preferredSize) : 1.0;
        BitmapFactory.Options newOpt = new BitmapFactory.Options();
        newOpt.inSampleSize = (int)(ratio * 2);
        input = context.getContentResolver().openInputStream(Uri.parse(uri));
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, newOpt);
        input.close();
        return bitmap;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
