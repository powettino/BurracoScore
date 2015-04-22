package yeapp.com.burracoscore.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.database.BurracoDBHelper;
import yeapp.com.burracoscore.core.database.HelperConstants;
import yeapp.com.burracoscore.core.database.columns.GameColumns;
import yeapp.com.burracoscore.core.database.columns.HandColumns;
import yeapp.com.burracoscore.core.database.columns.SessionColumns;
import yeapp.com.burracoscore.core.database.columns.TeamColumns;
import yeapp.com.burracoscore.core.model.BurracoSession;
import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.fragment.SummaryFragment;
import yeapp.com.burracoscore.adapter.TabSummaryAdapter;
import yeapp.com.burracoscore.utils.Constants;
import yeapp.com.burracoscore.utils.Utils;

public class SummaryContainerSwipe extends FragmentActivity implements ViewPager.OnPageChangeListener, Toolbar.OnMenuItemClickListener, SummaryFragment.OnScoreChanging, View.OnClickListener {

    public static final int CODE_FOR_CONF = 0;

    private boolean dialogActive;
    private AlertDialog winnerDialog = null;

    private TextView punteggioTotA;
    private TextView punteggioTotB;

    private boolean teamSaved = false;

    private BurracoSession sessione;

    private ViewPager viewPager;
    private TabSummaryAdapter tabAdapter;

    private MenuItem add;
    private MenuItem cancGame;

    private TextView teamAText;
    private TextView teamBText;

    Drawer.Result drawer = null;
    private int drawerPosition=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_container);
        //FIXME: ogni volta si cancella il DB utile solo per prove temporanee
//        if(savedInstanceState==null) {
//            getApplicationContext().deleteDatabase(HelperConstants.DBName);
//        }
        tabAdapter = new TabSummaryAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOnPageChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_summary);
        toolbar.setOnMenuItemClickListener(this);

        add = toolbar.getMenu().findItem(R.id.addGame);
        cancGame = toolbar.getMenu().findItem(R.id.cancellaGame);

        findViewById(R.id.restartSession).setOnClickListener(this);
        findViewById(R.id.confTeam).setOnClickListener(this);

        punteggioTotA = (TextView) findViewById(R.id.punteggioASummary);
        punteggioTotB = (TextView) findViewById(R.id.punteggioBSummary);

        teamAText = (TextView) findViewById(R.id.teamASummary);
        teamBText = (TextView) findViewById(R.id.teamBSummary);

        if (drawer == null) {
            drawer = new Drawer()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withDisplayBelowToolbar(true)
                    .withSelectedItem(-1)
                    .addDrawerItems(new SectionDrawerItem()
                            .withName(R.string.storico)
                            .setDivider(false)
                            .withTextColorRes(R.color.Bianco))
                    .withSliderBackgroundColorRes(R.color.SfondoOmbre)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            if (view != null) {
                                Log.d(HelperConstants.DBTag, "ricarico tutto");
                                BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                                SQLiteDatabase sloh = bdh.getReadableDatabase();
                                String idSession = String.valueOf(drawerItem.getTag());
                                Log.d(HelperConstants.DBTag, idSession + " tag");

                                Cursor cur = sloh.query((SessionColumns.TABLE_NAME + " bs, " + GameColumns.TABLE_NAME + " g, " + HandColumns.TABLE_NAME + " h"),
                                        new String[]{"bs.*", "g.*", "h.*"},
                                        "bs." + SessionColumns.SESSION_ID + "=? and bs." + SessionColumns.SESSION_ID + "=g." + GameColumns.SESSION_ID + " and h." + HandColumns.GAME_ID + "=g." + GameColumns.GAME_ID,
                                        new String[]{idSession},
                                        null,
                                        null,
                                        "g." + GameColumns.GAME_ID + " DESC, h." + HandColumns.NUMERO_MANO + " DESC, h." + HandColumns.SIDE);
                                Cursor cur2 = sloh.query((TeamColumns.TABLE_NAME + " t, " + SessionColumns.TABLE_NAME + " bs"),
                                        new String[]{"t.*", "bs.*"},
                                        "bs." + SessionColumns.SESSION_ID + "=? and (t." + TeamColumns.TEAM_ID + "=bs." + SessionColumns.TEAM_A_ID + " or t." + TeamColumns.TEAM_ID + "=bs." + SessionColumns.TEAM_B_ID + ")",
                                        new String[]{idSession},
                                        null,
                                        null,
                                        "t." + TeamColumns.SIDE + " DESC"
                                );

//                                if(cSes.moveToNext()){
//                                    Cursor cTeam = sloh.query(TeamColumns.TABLE_NAME,
//                                            new String[]{"*"},
//                                            TeamColumns.TEAM_ID + "=? or "+TeamColumns.TEAM_ID+ "=?",
//                                            new String[]{cSes.getString(cSes.getColumnIndex(SessionColumns.TEAM_A_ID)), cSes.getString(cSes.getColumnIndex(SessionColumns.TEAM_B_ID))},
//                                            null,
//                                            null,
//                                            TeamColumns.SIDE + " DESC");
//                                    Cursor cGame = sloh.query(GameColumns.TABLE_NAME,
//                                            new String[]{"*"},
//                                            GameColumns.SESSION_ID+"=?",
//                                            new String[]{idSession},
//                                            null,
//                                            null,
//                                            GameColumns.GAME_ID+" DESC");
//                                    Cursor cHand = sloh.query(HandColumns.TABLE_NAME),
//                                            new String[]{"*"},
//                                    HandColumns.
                                BurracoSession tempSession = new BurracoSession(cur, cur2);
                            }
                            drawerPosition = position;
                        }
                    })
                    .withTranslucentNavigationBar(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .build();

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                    SQLiteDatabase sloh = bdh.getReadableDatabase();

                    Cursor cur = sloh.query(SessionColumns.TABLE_NAME+ " bs, "+TeamColumns.TABLE_NAME+" ta, "+TeamColumns.TABLE_NAME+" tb",
                            new String[]{"ta."+TeamColumns.ALIAS, "tb."+TeamColumns.ALIAS, "bs."+SessionColumns.SESSION_ID, "bs."+SessionColumns.NUMERO_GAME_A,"bs."+SessionColumns.NUMERO_GAME_B,"bs."+SessionColumns.TIMESTAMP },
                            "bs."+SessionColumns.TEAM_A_ID+"=ta."+TeamColumns.TEAM_ID+" and bs."+SessionColumns.TEAM_B_ID+"=tb."+TeamColumns.TEAM_ID+" and ta."+TeamColumns.SIDE+"='"+Utils.ASide+"' and tb."+TeamColumns.SIDE+"='"+Utils.BSide+"'",
                            null,
                            null,
                            null,
                            "bs."+SessionColumns.TIMESTAMP + " DESC");
                    Log.d(HelperConstants.DBTag, "Trovate nel caricamento " + cur.getCount() + " sessioni " + cur.getColumnIndex(TeamColumns.ALIAS));
                    while (cur.moveToNext()) {
                        drawer.addItem(new PrimaryDrawerItem()
                                        .withName(cur.getString(0) + " - " + cur.getString(1))
                                        .withTag(cur.getString(2))
                                        .withDescription("Parziale: " + cur.getString(3) + " - " + cur.getString(4))
                                        .withBadge("Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.valueOf(cur.getString(5)))).toString())
                        );
                        drawer.addItem(new DividerDrawerItem());
                    }
                    cur.close();
                    sloh.close();
                    bdh.close();
                    return null;
                }
            }.execute();
        }

        updateTeamAlias(Utils.getDefaultTeamName(Utils.ASide), Utils.getDefaultTeamName(Utils.BSide));
        if (savedInstanceState == null) {
            sessione = new BurracoSession();
            setNewGame();
        }

        if (winnerDialog == null) {
            winnerDialog = new AlertDialog.Builder(this).setTitle("VITTORIA!!!")
                    .setMessage("Complimenti, la partita è conclusa.\n\nVuoi cominciare una nuova partita?")
                    .setCancelable(false)
                    .setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sessione.addNewGame(sessione.getGameTotali() + 1);
                                    setNewGame();
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    add.setVisible(true);
                                    cancGame.setVisible(false);
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .create();
        }
    }

    public void updateTeamAlias(String aliasA, String aliasB) {
        teamAText.setText(Utils.formattedString(aliasA));
        teamBText.setText(Utils.formattedString(aliasB));
    }

    private void setNewGame() {
        tabAdapter.addGame(sessione.getCurrentGame());
        viewPager.setCurrentItem(sessione.getGameTotali() - 1);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addGame: {
                sessione.addNewGame(sessione.getGameTotali() + 1);
                setNewGame();
                add.setVisible(false);
                return true;
            }
            case R.id.cancellaGame: {
                new AlertDialog.Builder(this)
                        .setMessage("Vuoi cancellare la partita attuale?")
                        .setCancelable(true)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new AsyncTask<Long, Void, Void>() {

                                            @Override
                                            protected Void doInBackground(Long... params) {
                                                long id = params[0];
                                                BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                                                SQLiteDatabase sloh = bdh.getWritableDatabase();
                                                sloh.beginTransaction();
//                                                Cursor cur = sloh.query(GameColumns.TABLE_NAME,
//                                                        new String[]{GameColumns.GAME_ID},
//                                                        String.format("%s = ?", GameColumns.GAME_ID),
//                                                        new String[]{String.valueOf(id)},
//                                                        null,
//                                                        null,
//                                                        null);
//                                                Log.d(HelperConstants.DBTag, "trovato "+cur.getCount()+" righe per id "+id);
                                                int res = sloh.delete(HandColumns.TABLE_NAME,
                                                        HandColumns.GAME_ID + "=?",
                                                        new String[]{String.valueOf(id)});
                                                Log.d(HelperConstants.DBTag, "Cancellate " + res + " mani dal db");
                                                res = sloh.delete(GameColumns.TABLE_NAME,
                                                        GameColumns.GAME_ID + "=?",
                                                        new String[]{String.valueOf(id)});
                                                Log.d(HelperConstants.DBTag, "Cancellati " + res + " games dal db");
                                                sloh.setTransactionSuccessful();
                                                sloh.endTransaction();
                                                sloh.close();
                                                bdh.close();
                                                return null;
                                            }
                                        }.execute(sessione.getCurrentGame().getId());
                                        sessione.clearCurrentGame();
                                        tabAdapter.renewLastGame(sessione.getCurrentGame());
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .create()
                        .show();
                return true;
            }
            default: {
                return true;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sessione = savedInstanceState.getParcelable(Constants.gameSession);
        tabAdapter.restore(sessione.getGameTotali());
        viewPager.setCurrentItem(sessione.getGameTotali() - 1);

        if (savedInstanceState.getBoolean(Constants.dialogStatus)) {
            dialogActive = true;
            winnerDialog.show();
        }
        add.setVisible(savedInstanceState.getBoolean(Constants.addButtonStatus));
        cancGame.setVisible(savedInstanceState.getBoolean(Constants.resetGameButtonStatus));
        punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
        punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.gameSession, sessione);
        outState.putBoolean(Constants.addButtonStatus, add.isVisible());
        outState.putBoolean(Constants.resetGameButtonStatus, cancGame.isVisible());
        outState.putBoolean(Constants.dialogStatus, dialogActive);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Team a_temp = data.getParcelableExtra(Constants.teamAKey);
                    Team b_temp = data.getParcelableExtra(Constants.teamBKey);
                    sessione.setTeamA(a_temp);
                    sessione.setTeamB(b_temp);
                    updateTeamAlias(a_temp.getAlias(), b_temp.getAlias());
                    teamSaved = true;
                    if(drawerPosition!=-1) {
                        Log.d(HelperConstants.DBTag, "posizione trovata per la squadra: "+drawerPosition);
//                                ((PrimaryDrawerItem)drawer.getDrawerItems().get(drawerPosition)).setName(team_A.getAlias() + " - " + team_B.getAlias());
                        drawer.updateName(a_temp.getAlias() + " - " + b_temp.getAlias(), drawerPosition);
                    }
                    new AsyncTask<Team, Void, String>() {

                        @Override
                        protected String doInBackground(Team... params) {
                            BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                            SQLiteDatabase sloh = bdh.getWritableDatabase();
                            Team team_A = params[0];
                            Team team_B = params[1];
                            Cursor cur = sloh.query(TeamColumns.TABLE_NAME,
                                    new String[]{TeamColumns.TEAM_ID},
                                    String.format("%s = ?", TeamColumns.TEAM_ID),
                                    new String[]{String.valueOf(team_A.getId())},
                                    null,
                                    null,
                                    null);
                            if (cur.getCount() != 0) {
                                sloh.beginTransaction();
                                ContentValues cv;
                                cv = Utils.getTeamContentValues(team_A);
                                sloh.updateWithOnConflict(TeamColumns.TABLE_NAME,
                                        cv,
                                        TeamColumns.TEAM_ID + "=? and " + TeamColumns.SIDE + "=?",
                                        new String[]{String.valueOf(team_A.getId()), String.valueOf(team_A.getSide())},
                                        SQLiteDatabase.CONFLICT_FAIL);
                                cv = Utils.getTeamContentValues(team_B);
                                sloh.updateWithOnConflict(TeamColumns.TABLE_NAME,
                                        cv,
                                        TeamColumns.TEAM_ID + "=? and " + TeamColumns.SIDE + "=?",
                                        new String[]{String.valueOf(team_B.getId()), String.valueOf(team_B.getSide())},
                                        SQLiteDatabase.CONFLICT_FAIL);
                                Log.d(HelperConstants.DBTag, "aggiunte le squadre");
                                sloh.setTransactionSuccessful();
                                sloh.endTransaction();
                            }
                            cur.close();
                            sloh.close();
                            bdh.close();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                        }
                    }.execute(a_temp, b_temp);
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    public void gameUpdate(Game gameUpdate, boolean ended) {
        sessione.updateLastGame(gameUpdate);
        Log.d("Grafica", "Sessione aggiornata con id " + gameUpdate.getId());
        PrimaryDrawerItem p1 = new PrimaryDrawerItem()
                .withName(sessione.getTeamA().getAlias() + " - " + sessione.getTeamB().getAlias())
                .withTag(sessione.getId())
                .withDescription("Parziale: " + sessione.getNumeroVintiA() + " - " + sessione.getNumeroVintiB())
                .withBadge("Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.valueOf(System.currentTimeMillis()))).toString());
        if(drawerPosition==-1){
            drawer.addItem(p1, 1);
            drawer.addItem(new DividerDrawerItem(), 2);
            drawerPosition=1;
            drawer.setSelection(drawerPosition, true);
        }else{
            //altrimenti si aggiorna il primo elemento del drawer
            drawer.updateItem(p1, drawerPosition);
        }
        Log.d("RunOnUI", "Eseguito il tread, ora dovrebbe essere aggiornato");
        new AsyncTask<BurracoSession, Void, Void>() {

            @Override
            protected Void doInBackground(BurracoSession... params) {
                final BurracoSession bSes = params[0];
                BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                SQLiteDatabase sloh = bdh.getWritableDatabase();
                sloh.beginTransaction();
                ContentValues cv;
                Game gameTemp = bSes.getCurrentGame();
                //Si aggiorna la sessione quando c'e' un nuovo game con una mano oppure e' segnato un vincitore
                if (gameTemp.getNumeroMani() == 1 || gameTemp.getWinner()!= 0) {
                    cv = Utils.getSessionContentValues(bSes);
                    sloh.insertWithOnConflict(SessionColumns.TABLE_NAME,
                            null,
                            cv,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    Log.d(HelperConstants.DBTag, "aggiornata la sessione con "+gameTemp.getNumeroMani()+" mani e "+gameTemp.getWinner()+" vincitori");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                    //Se c'e' il primo game allora e' una nuova partita e si aggiunge al drawer
//                            PrimaryDrawerItem p1 = new PrimaryDrawerItem()
//                                    .withName(bSes.getTeamA().getAlias() + " - " + bSes.getTeamB().getAlias())
//                                    .withTag(bSes.getId())
//                                    .withDescription("Parziale: " + bSes.getNumeroVintiA() + " - " + bSes.getNumeroVintiB())
//                                    .withBadge("Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.valueOf(System.currentTimeMillis()))).toString());
//                            if(drawerPosition==-1){
//                                drawer.addItem(p1, 1);
//                                drawer.addItem(new DividerDrawerItem(), 2);
//                                drawerPosition=1;
//                                drawer.setSelection(drawerPosition, true);
//                            }else{
//                                //altrimenti si aggiorna il primo elemento del drawer
//                                drawer.updateItem(p1, drawerPosition);
//                            }
//                            Log.d("RunOnUI", "Eseguito il tread, ora dovrebbe essere aggiornato");
//                        }
//                    });
                }
                //Se c'e' un solo game e una sola mano e' la prima mano in generale e si salva le squadre
                if (bSes.getGameTotali() == 1 && gameTemp.getNumeroMani() == 1) {
                    Log.d(HelperConstants.DBTag, "Prima sessione");
                    cv = Utils.getTeamContentValues(bSes.getTeamA());
                    sloh.insert(TeamColumns.TABLE_NAME,
                            null,
                            cv);
                    cv = Utils.getTeamContentValues(bSes.getTeamB());
                    sloh.insert(TeamColumns.TABLE_NAME,
                            null,
                            cv);
                    Log.d(HelperConstants.DBTag, "aggiunte le squadre");
                }
                Cursor cur = sloh.query(GameColumns.TABLE_NAME,
                        new String[]{GameColumns.GAME_ID},
                        String.format("%s = ?", GameColumns.GAME_ID),
                        new String[]{String.valueOf(gameTemp.getId())},
                        null,
                        null,
                        null);
                cv = Utils.getGameContentValues(gameTemp, bSes.getId());
                if (cur.getCount() == 0) {
                    sloh.insertWithOnConflict(GameColumns.TABLE_NAME,
                            null,
                            cv,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    Log.d(HelperConstants.DBTag, "Inserita la partita");
                } else {
                    sloh.updateWithOnConflict(GameColumns.TABLE_NAME,
                            cv,
                            String.format("%s = ?", GameColumns.GAME_ID),
                            new String[]{String.valueOf(gameTemp.getId())},
                            SQLiteDatabase.CONFLICT_FAIL);
                    Log.d(HelperConstants.DBTag, "Aggiornata la partita");
                }
                cur.close();
                cv = Utils.getHandContentValues(gameTemp.getLastMano(Utils.ASide), Utils.ASide, gameTemp.getId());
                sloh.insertWithOnConflict(HandColumns.TABLE_NAME,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_FAIL);
                cv = Utils.getHandContentValues(gameTemp.getLastMano(Utils.BSide), Utils.BSide, gameTemp.getId());
                sloh.insertWithOnConflict(HandColumns.TABLE_NAME,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_FAIL);
                sloh.setTransactionSuccessful();
                sloh.endTransaction();
                sloh.close();
                bdh.close();
                Log.d(HelperConstants.DBTag, "Salvataggio concluso");
                return null;
            }
        }.execute(sessione);

        if (ended) {
            punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
            punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
            dialogActive = true;
            winnerDialog.show();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position != sessione.getGameTotali() - 1) {
            if (cancGame.isVisible()) {
                cancGame.setVisible(false);
            }
        } else {
            if (!cancGame.isVisible() && sessione.getCurrentGame().getWinner() == 0) {
                cancGame.setVisible(true);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confTeam: {
                if (teamSaved) {
                    Intent configurazione = new Intent(this, TeamSliderContainer.class);
                    configurazione.putExtra(Constants.numberOfPlayer, sessione.getTeamA().getNumberPlayer())
                            .putExtra(Constants.teamAKey, sessione.getTeamA())
                            .putExtra(Constants.teamBKey, sessione.getTeamB());
                    startActivityForResult(configurazione, CODE_FOR_CONF);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Numero di giocatori")
                            .setCancelable(true)
                            .setItems(R.array.choosePlayer,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent configurazione = new Intent(getBaseContext(), TeamSliderContainer.class);
                                            switch (id) {
                                                case 0:
                                                    configurazione.putExtra(Constants.numberOfPlayer, 1);
                                                    break;
                                                case 1:
                                                    configurazione.putExtra(Constants.numberOfPlayer, 2);
                                                    break;
                                            }
                                            configurazione.putExtra(Constants.teamAKey, sessione.getTeamA())
                                                    .putExtra(Constants.teamBKey, sessione.getTeamB());
                                            startActivityForResult(configurazione, CODE_FOR_CONF);
                                        }
                                    })
                            .create()
                            .show();
                }
                break;
            }
            case R.id.restartSession: {
                new AlertDialog.Builder(this)
                        .setMessage("Sei sicuro di voler cominciare una nuova sessione?")
                        .setCancelable(true)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        tabAdapter.clearAll();
                                        sessione.clear();
                                        setNewGame();
                                        teamSaved = false;
                                        add.setVisible(false);
                                        punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
                                        punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .create()
                        .show();
            }
            default:
                break;
        }
    }
}