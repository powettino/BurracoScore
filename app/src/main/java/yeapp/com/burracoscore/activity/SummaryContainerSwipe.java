package yeapp.com.burracoscore.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
    private int drawerPosition = -1;

    ProgressDialog pDiag;

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
                                tabAdapter.clearAll();
                                new AsyncTask<String, Void, BurracoSession>() {

                                    @Override
                                    protected BurracoSession doInBackground(String... params) {
                                        Log.d(HelperConstants.DBTag, "ricarico tutto");
                                        String idSession = params[0];
                                        BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                                        SQLiteDatabase sloh = bdh.getReadableDatabase();
                                        Log.d(HelperConstants.DBTag, idSession + " tag");
                                        Cursor bs = sloh.query((SessionColumns.TABLE_NAME + " bs"),
                                                new String[]{"bs.*"},
                                                "bs." + SessionColumns.SESSION_ID + "=?",
                                                new String[]{idSession},
                                                null,
                                                null,
                                                null);
                                        Cursor gh = sloh.query((GameColumns.TABLE_NAME + " g, " + HandColumns.TABLE_NAME + " h"),
                                                new String[]{"g.*", "h.*"},
                                                "g." + GameColumns.SESSION_ID + "=? and h." + HandColumns.GAME_ID + "=g." + GameColumns.GAME_ID,
                                                new String[]{idSession},
                                                null,
                                                null,
                                                "g." + GameColumns.GAME_ID + " ASC, h." + HandColumns.NUMERO_MANO + " ASC, h." + HandColumns.SIDE);
                                        Cursor teams = sloh.query((TeamColumns.TABLE_NAME + " t, " + SessionColumns.TABLE_NAME + " bs"),
                                                new String[]{"t.*", "bs.*"},
                                                "bs." + SessionColumns.SESSION_ID + "=? and (t." + TeamColumns.TEAM_ID + "=bs." + SessionColumns.TEAM_A_ID + " or t." + TeamColumns.TEAM_ID + "=bs." + SessionColumns.TEAM_B_ID + ")",
                                                new String[]{idSession},
                                                null,
                                                null,
                                                "t." + TeamColumns.SIDE + " DESC"
                                        );
                                        BurracoSession tempSession = new BurracoSession(bs, gh, teams);
                                        bs.close();
                                        gh.close();
                                        teams.close();
                                        sloh.close();
                                        bdh.close();
                                        return tempSession;
                                    }

                                    @Override
                                    protected void onPostExecute(BurracoSession result) {
                                        sessione = result;
                                        tabAdapter.restoreOld(sessione);
                                        teamSaved = true;
                                        add.setVisible(sessione.getCurrentGame().getWinner() != 0);
                                        setPunteggioTeam();
                                        cancGame.setVisible(false);
                                        updateTeamAlias();
                                        viewPager.setCurrentItem(sessione.getGameTotali() - 1);
                                        stopSpinner();
                                    }

                                    @Override
                                    protected void onPreExecute() {
                                        startSpinner("Caricamento sessione...");
                                    }
                                }.execute(String.valueOf(drawerItem.getTag()));
                            }
                            drawerPosition = position;
                        }
                    })
                    .withTranslucentNavigationBar(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .build();

            new AsyncTask<Void, Void, ArrayList<PrimaryDrawerItem>>() {

                @Override
                protected ArrayList<PrimaryDrawerItem> doInBackground(Void... params) {
                    BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                    SQLiteDatabase sloh = bdh.getReadableDatabase();

                    final Cursor cur = sloh.query(SessionColumns.TABLE_NAME + " bs, " + TeamColumns.TABLE_NAME + " ta, " + TeamColumns.TABLE_NAME + " tb",
                            new String[]{"ta." + TeamColumns.ALIAS, "tb." + TeamColumns.ALIAS, "bs." + SessionColumns.SESSION_ID, "bs." + SessionColumns.NUMERO_GAME_A, "bs." + SessionColumns.NUMERO_GAME_B, "bs." + SessionColumns.TIMESTAMP},
                            "bs." + SessionColumns.TEAM_A_ID + "=ta." + TeamColumns.TEAM_ID + " and bs." + SessionColumns.TEAM_B_ID + "=tb." + TeamColumns.TEAM_ID + " and ta." + TeamColumns.SIDE + "='" + Utils.ASide + "' and tb." + TeamColumns.SIDE + "='" + Utils.BSide + "'",
                            null,
                            null,
                            null,
                            "bs." + SessionColumns.TIMESTAMP + " DESC");
                    Log.d(HelperConstants.DBTag, "Trovate nel caricamento " + cur.getCount() + " sessioni " + cur.getColumnIndex(TeamColumns.ALIAS));
                    ArrayList<PrimaryDrawerItem> p = new ArrayList<PrimaryDrawerItem>();
                    while (cur.moveToNext()) {
                        Log.d("BBB", cur.getCount() + " - 0:" + cur.getString(2));
                        p.add(new PrimaryDrawerItem()
                                .withName(cur.getString(0) + " - " + cur.getString(1))
                                .withTag(cur.getString(2))
                                .withDescription("Parziale: " + cur.getString(3) + " - " + cur.getString(4))
                                .withBadge("Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(cur.getLong(5)))));
                    }
                    cur.close();
                    sloh.close();
                    bdh.close();
                    return p;
                }

                @Override
                protected void onPreExecute() {
                    startSpinner("Caricamento storico partite...");
                }

                @Override
                protected void onPostExecute(ArrayList<PrimaryDrawerItem> primaryDrawerItems) {
                    for (PrimaryDrawerItem primary : primaryDrawerItems) {
                        drawer.addItem(primary);
                        drawer.addItem(new DividerDrawerItem());
                    }
                    stopSpinner();
                }
            }.execute();
        }

        if (savedInstanceState == null) {
            sessione = new BurracoSession();
            setNewGame();
            updateTeamAlias();
            setPunteggioTeam();
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

    private void stopSpinner(){
        if (pDiag.isShowing()) {
            pDiag.dismiss();
        }
    }

    private void startSpinner(String msg) {
        if (pDiag == null || !pDiag.isShowing()) {
            pDiag = new ProgressDialog(SummaryContainerSwipe.this);
            pDiag.setMessage(msg);
            pDiag.setIndeterminate(false);
            pDiag.setCancelable(true);
            pDiag.show();
        }
    }

    private void updateTeamAlias() {
        teamAText.setText(Utils.formattedString(sessione.getTeamA().getAlias()));
        teamBText.setText(Utils.formattedString(sessione.getTeamB().getAlias()));
    }

    private void setNewGame() {
        tabAdapter.addGame(sessione.getCurrentGame(), false);
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
        drawerPosition = savedInstanceState.getInt(Constants.drawerPosition);
        if(drawerPosition!=-1) {
            drawer.setSelection(drawerPosition, true);
        }
        if (savedInstanceState.getBoolean(Constants.dialogStatus)) {
            dialogActive = true;
            winnerDialog.show();
        }
        add.setVisible(savedInstanceState.getBoolean(Constants.addButtonStatus));
        cancGame.setVisible(savedInstanceState.getBoolean(Constants.resetGameButtonStatus));
        setPunteggioTeam();
        updateTeamAlias();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.gameSession, sessione);
        outState.putBoolean(Constants.addButtonStatus, add.isVisible());
        outState.putBoolean(Constants.resetGameButtonStatus, cancGame.isVisible());
        outState.putBoolean(Constants.dialogStatus, dialogActive);
        outState.putInt(Constants.drawerPosition, drawerPosition);
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
                    updateTeamAlias();
                    teamSaved = true;
                    if (drawerPosition != -1) {
                        Log.d(HelperConstants.DBTag, "posizione trovata per la squadra: " + drawerPosition);
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
                                cv = Utils.getInsertTeamCV(team_A);
                                sloh.updateWithOnConflict(TeamColumns.TABLE_NAME,
                                        cv,
                                        TeamColumns.TEAM_ID + "=? and " + TeamColumns.SIDE + "=?",
                                        new String[]{String.valueOf(team_A.getId()), String.valueOf(team_A.getSide())},
                                        SQLiteDatabase.CONFLICT_FAIL);
                                cv = Utils.getInsertTeamCV(team_B);
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
                            Toast.makeText(getApplicationContext(), "Salvataggio completato", Toast.LENGTH_SHORT).show();
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
        Log.d("Grafica", "Gioco aggiornato con id " + gameUpdate.getId());
        PrimaryDrawerItem p1;
        if (drawerPosition == -1) {
            p1 = new PrimaryDrawerItem()
                    .withName(sessione.getTeamA().getAlias() + " - " + sessione.getTeamB().getAlias())
                    .withTag(sessione.getId())
                    .withDescription("Parziale: " + sessione.getNumeroVintiA() + " - " + sessione.getNumeroVintiB())
                    .withBadge("Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
        }else {
            //si rimuove anche il divider sottostante
            drawer.getDrawerItems().remove(drawerPosition+1);
            //altrimenti si aggiorna il primo elemento del drawer
            p1= (PrimaryDrawerItem) drawer.getDrawerItems().remove(drawerPosition);
            p1.setBadge("Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            p1.setDescription("Parziale: " + sessione.getNumeroVintiA() + " - " + sessione.getNumeroVintiB());
//            drawer.updateItem(p1, drawerPosition);
        }
        drawer.addItem(p1, 1);
        drawer.addItem(new DividerDrawerItem(), 2);
        drawerPosition = 1;
        drawer.setSelection(drawerPosition, true);
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
                if (gameTemp.getNumeroMani() == 1 && bSes.getGameTotali()==1 ) {
                    cv = Utils.getInsertSessionCV(bSes);
                    Log.d("RITORNO", String.valueOf(sloh.insertWithOnConflict(SessionColumns.TABLE_NAME,
                            null,
                            cv,
                            SQLiteDatabase.CONFLICT_REPLACE)));
                    Log.d(HelperConstants.DBTag, "inserita la sessione con id " + bSes.getId() + " e " + gameTemp.getNumeroMani() + " mani e " + gameTemp.getWinner() + " vincitori con drawwe " + drawerPosition);
                }else{
                 cv = Utils.getUpdateSessionCV(bSes);
                    sloh.updateWithOnConflict(SessionColumns.TABLE_NAME,
                            cv,
                            SessionColumns.SESSION_ID + " = ?",
                            new String[]{String.valueOf(bSes.getId())},
                            SQLiteDatabase.CONFLICT_FAIL);
                    Log.d(HelperConstants.DBTag, "aggiornata la sessione con id " + bSes.getId() + " e " + gameTemp.getNumeroMani() + " mani e " + bSes.getGameTotali() + " game totali con drawwe " + drawerPosition);
                }
                //Se le squadre non sono presenti si inseriscono
                Cursor cur = sloh.query(TeamColumns.TABLE_NAME,
                        new String[]{TeamColumns.TEAM_ID, TeamColumns.SIDE},
                        String.format("%s = ?", TeamColumns.TEAM_ID),
                        new String[]{String.valueOf(bSes.getTeamA().getId())},
                        null,
                        null,
                        null);
                if (cur.getCount() == 0) {
                    Log.d(HelperConstants.DBTag, "Prima sessione");
                    cv = Utils.getInsertTeamCV(bSes.getTeamA());
                    sloh.insert(TeamColumns.TABLE_NAME,
                            null,
                            cv);
                    cv = Utils.getInsertTeamCV(bSes.getTeamB());
                    sloh.insert(TeamColumns.TABLE_NAME,
                            null,
                            cv);
                    Log.d(HelperConstants.DBTag, "aggiunte le squadre");
                }
                //Si aggirona tutte le altre strutture collegate con un insertonconflict
                cur.close();
                cv = Utils.getInsertGameCV(gameTemp, bSes.getId());
                sloh.insertWithOnConflict(GameColumns.TABLE_NAME,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_REPLACE);
                Log.d(HelperConstants.DBTag, "Inserita la partita");
                cv = Utils.getInsertHandCV(gameTemp.getLastMano(Utils.ASide), Utils.ASide, gameTemp.getId());
                sloh.insertWithOnConflict(HandColumns.TABLE_NAME,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_FAIL);
                cv = Utils.getInsertHandCV(gameTemp.getLastMano(Utils.BSide), Utils.BSide, gameTemp.getId());
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
            setPunteggioTeam();
            dialogActive = true;
            winnerDialog.show();
        }
    }

    private void setPunteggioTeam() {
        punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
        punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
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
                                        setPunteggioTeam();
                                        dialog.cancel();
                                        dialogActive = false;
                                        drawer.setSelection(-1, true);
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