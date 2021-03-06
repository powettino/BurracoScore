package yeapp.com.burracoscore.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import yeapp.com.burracoscore.core.database.columns.GameColumns;
import yeapp.com.burracoscore.core.database.columns.HandColumns;
import yeapp.com.burracoscore.core.database.columns.SessionColumns;
import yeapp.com.burracoscore.core.database.columns.TeamColumns;

public class BurracoDBHelper extends SQLiteOpenHelper {

    // codice SQL per creare le tabelle
    private final static String CREATE_SESSION_TABLE =
            "CREATE TABLE " + SessionColumns.TABLE_NAME + " (" +
                    SessionColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SessionColumns.TEAM_A_ID + " INTEGER REFERENCES "+TeamColumns.TABLE_NAME+"("+TeamColumns.TEAM_ID+"), " +
                    SessionColumns.TEAM_B_ID + " INTEGER REFERENCES "+TeamColumns.TABLE_NAME+"("+TeamColumns.TEAM_ID+"), " +
                    SessionColumns.NUMERO_GAME_A + " INTEGER, " +
                    SessionColumns.NUMERO_GAME_B + " INTEGER, " +
                    SessionColumns.SESSION_ID + " NUMERIC UNIQUE, "+
                    SessionColumns.TIMESTAMP + " NUMERIC"+
                    ")";

    private final static String CREATE_TEAM_TABLE =
            "CREATE TABLE " + TeamColumns.TABLE_NAME + " (" +
                    TeamColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TeamColumns.ALIAS + " TEXT, "+
                    TeamColumns.NUMERO_PLAYER + " INTEGER CHECK ("+TeamColumns.NUMERO_PLAYER+"<=2 AND "+TeamColumns.NUMERO_PLAYER+">=1), "+
                    TeamColumns.PLAYER1 + " TEXT NOT NULL, " +
                    TeamColumns.PLAYER2 + " TEXT NOT NULL, " +
                    TeamColumns.FOTO + " TEXT, "+
                    TeamColumns.TEAM_ID + " NUMERIC, " +
                    TeamColumns.SIDE + " TEXT"+
                    ")";

    private final static String CREATE_GAME_TABLE =
            "CREATE TABLE " + GameColumns.TABLE_NAME + " (" +
                    GameColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    GameColumns.SESSION_ID + " INTEGER REFERENCES "+SessionColumns.TABLE_NAME+"("+SessionColumns.SESSION_ID+"), " +
                    GameColumns.NUMERO_MANI + " INTEGER CHECK ("+GameColumns.NUMERO_MANI +">=1), "+
                    GameColumns.NUMERO_PARTITA + " INTEGER,  "+
                    GameColumns.TOTALE_A + " INTEGER, " +
                    GameColumns.TOTALE_B + " INTEGER, " +
                    GameColumns.GAME_ID + " NUMERIC UNIQUE, " +
                    GameColumns.VINCITORE+ " TEXT" +
                    ")";

    private final static String CREATE_HAND_TABLE =
            "CREATE TABLE " + HandColumns.TABLE_NAME + " (" +
                    HandColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    HandColumns.GAME_ID + " INTEGER REFERENCES "+GameColumns.TABLE_NAME+"("+GameColumns.GAME_ID+"), "+
                    HandColumns.BASE + " NUMERIC CHECK ("+ HandColumns.BASE +">=0),  "+
                    HandColumns.CARTE + " NUMERIC, "+
                    HandColumns.CHIUSURA + " NUMERIC CHECK ("+ HandColumns.CHIUSURA +">=0), "+
                    HandColumns.MAZZETTO + " NUMERIC, "+
                    HandColumns.NUMERO_MANO + " NUMERIC CHECK ("+ HandColumns.NUMERO_MANO +">=0), "+
                    HandColumns.SIDE + " TEXT, "+
                    HandColumns.TOTALE_MANO + " NUMERIC, "+
                    HandColumns.WON + " NUMERIC"+
                    ")";

    // codice SQL per eliminare le tabelle
    private final static String DROP_SESSION_TABLE =
            "DROP TABLE IF EXISTS " + SessionColumns.TABLE_NAME;
    private final static String DROP_GAME_TABLE =
            "DROP TABLE IF EXISTS " + GameColumns.TABLE_NAME;
    private final static String DROP_HAND_TABLE =
            "DROP TABLE IF EXISTS " + HandColumns.TABLE_NAME;
    private final static String DROP_TEAM_TABLE =
            "DROP TABLE IF EXISTS " + TeamColumns.TABLE_NAME;

    public BurracoDBHelper(Context context) {
        super(context, HelperConstants.DBName, null, HelperConstants.DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEAM_TABLE);
        db.execSQL(CREATE_SESSION_TABLE);
        db.execSQL(CREATE_GAME_TABLE);
        db.execSQL(CREATE_HAND_TABLE);
        Log.d(HelperConstants.DBTag, "Creato");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_HAND_TABLE);
        db.execSQL(DROP_GAME_TABLE);
        db.execSQL(DROP_SESSION_TABLE);
        db.execSQL(DROP_TEAM_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // idem come per onUpgrade
        onUpgrade(db, oldVersion, newVersion);
    }
}
