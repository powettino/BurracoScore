package yeapp.com.burracoscore.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import yeapp.com.burracoscore.core.database.columns.HelperConstants;

public class BurracoDBHelper extends SQLiteOpenHelper {

 // codice SQL per create la tabella
//    private final static String CREATE_TABLE =
//            "CREATE TABLE " + WiFiNetEntry.TABLE_NAME + " (" +
//                    WiFiNetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    WiFiNetEntry.COLUMN_NAME_TIMESTAMP + " INTEGER, " +
//                    WiFiNetEntry.COLUMN_NAME_BSSID + " TEXT, " +
//                    WiFiNetEntry.COLUMN_NAME_SSID + " TEXT, " +
//                    WiFiNetEntry.COLUMN_NAME_SIGNAL + " INTEGER " +
//                    ")";

    // codice SQL per eliminare la tabella
//    private final static String DROP_TABLE =
//            "DROP TABLE IF EXISTS " + WiFiNetEntry.TABLE_NAME;


//
//    private final static String CREATE_TABLE = "CREATE TABLE "+ DBColumns.TABLE_NAME+"("
//            + DBColumns._ID + " INTEGER primary key autoincrement, "
//            + DBColumns.SSID + " text, "
//            + DBColumns.BSSID +" text, "
//            + DBColumns.TIMESTAMP + " numeric, "
//            + DBColumns.SIGNAL_POWER + " integer)";
//
//    private final static String DROP_TABLE = "DROP TABLE IF EXIST"+ DBColumns.TABLE_NAME;

    public BurracoDBHelper(Context context) {
        super(context, HelperConstants.DBName, null, HelperConstants.DBVersion);
//        super(context, DBHelperConstant.DBName, null, DBHelperConstant.DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d(HelperConstants.DBTag, "Creato");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // idem come per onUpgrade
        onUpgrade(db, oldVersion, newVersion);
    }
}
