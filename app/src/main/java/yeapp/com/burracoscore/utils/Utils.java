package yeapp.com.burracoscore.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

import yeapp.com.burracoscore.core.database.columns.GameColumns;
import yeapp.com.burracoscore.core.database.columns.HandColumns;
import yeapp.com.burracoscore.core.database.columns.SessionColumns;
import yeapp.com.burracoscore.core.database.columns.TeamColumns;
import yeapp.com.burracoscore.core.model.BurracoSession;
import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.core.model.Team;

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

    @SuppressWarnings("unused")
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

    public static ContentValues getTeamContentValues(Team t){
        ContentValues cv = new ContentValues();
        cv.put(TeamColumns.SIDE, String.valueOf(t.getSide()));
        cv.put(TeamColumns.ALIAS, t.getAlias());
        cv.put(TeamColumns.NUMERO_PLAYER, t.getNumberPlayer());
        cv.put(TeamColumns.PLAYER1, t.getPlayer1());
        cv.put(TeamColumns.PLAYER2, t.getPlayer2());
        cv.put(TeamColumns.FOTO, t.getImagePath());
        return cv;
    }

    public static ContentValues getGameContentValues(Game g, int idSessione){
        ContentValues cv = new ContentValues();
        cv.put(GameColumns.NUMERO_MANI, g.getNumeroMani());
        cv.put(GameColumns.NUMERO_PARTITA, g.getGameNumber());
        cv.put(GameColumns.TOTALE_A, g.getTotalePartita(Utils.ASide));
        cv.put(GameColumns.TOTALE_B, g.getTotalePartita(Utils.BSide));
        cv.put(GameColumns.VINCITORE, String.valueOf(g.getWinner()));
        cv.put(GameColumns.SESSION, idSessione);
        return cv;
    }

    public static ContentValues getSessionContentValues(BurracoSession bs, int idTeamA, int idTeamB){
        ContentValues cv = new ContentValues();
        cv.put(SessionColumns.NUMERO_GAME_A, bs.getNumeroVintiA());
        cv.put(SessionColumns.NUMERO_GAME_B, bs.getNumeroVintiB());
        cv.put(SessionColumns.TIMESTAMP, System.currentTimeMillis());
        cv.put(SessionColumns.TEAM_A, idTeamA);
        cv.put(SessionColumns.TEAM_B, idTeamB);
        return cv;
    }

    public static ContentValues getHandContentValues(Hand h, char side, int idGame){
        ContentValues cv = new ContentValues();
        cv.put(HandColumns.BASE, h.getBase());
        cv.put(HandColumns.CARTE, h.getCarte());
        cv.put(HandColumns.CHIUSURA, h.getChiusura());
        cv.put(HandColumns.GAME, idGame);
        cv.put(HandColumns.MAZZETTO, h.getMazzetto());
        cv.put(HandColumns.NUMERO_MANO, h.getNumeroMano());
        cv.put(HandColumns.TOTALE_MANO, h.getTotaleMano());
        cv.put(HandColumns.SIDE, String.valueOf(side));
        cv.put(HandColumns.WON, h.getWon());
        return cv;
    }
}
