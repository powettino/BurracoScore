package yeapp.com.burracoscore.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BurracoDBHelper extends SQLiteOpenHelper {

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
        super(context, "", null, 1);
//        super(context, DBHelperConstant.DBName, null, DBHelperConstant.DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE);
//        Log.d(DBHelperConstant.DBTag, "Creato");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
