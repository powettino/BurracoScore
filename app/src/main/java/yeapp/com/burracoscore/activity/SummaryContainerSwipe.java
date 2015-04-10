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
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_container);
        //FIXME: ogni volta si cancella il DB utile solo per prove temporanee
        getApplicationContext().deleteDatabase(HelperConstants.DBName);
        tabAdapter = new TabSummaryAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOnPageChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_summary);
        toolbar.setOnMenuItemClickListener(this);

        add = toolbar.getMenu().findItem(R.id.addGame);
        cancGame = toolbar.getMenu().findItem(R.id.cancellaGame);

        findViewById(R.id.cancSession).setOnClickListener(this);
        findViewById(R.id.confTeam).setOnClickListener(this);

        punteggioTotA = (TextView) findViewById(R.id.punteggioASummary);
        punteggioTotB = (TextView) findViewById(R.id.punteggioBSummary);

        teamAText = (TextView) findViewById(R.id.teamASummary);
        teamBText = (TextView) findViewById(R.id.teamBSummary);

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
                                    sessione.addNewGame(sessione.getGameTotali()+1);
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

    public void updateTeamAlias(String aliasA, String aliasB){
        teamAText.setText(Utils.formattedString(aliasA));
        teamBText.setText(Utils.formattedString(aliasB));
    }

    private void setNewGame(){
        tabAdapter.addGame(sessione.getCurrentGame());
        viewPager.setCurrentItem(sessione.getGameTotali() - 1);



    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addGame:{
                sessione.addNewGame(sessione.getGameTotali()+1);
                setNewGame();
                add.setVisible(false);
                return true;
            }
//            case R.id.configuraMenuSum: {
//                if (teamSaved) {
//                    Intent configurazione = new Intent(this, TeamSliderContainer.class);
//                    configurazione.putExtra(Constants.numberOfPlayer, sessione.getTeamA().getNumberPlayer())
//                            .putExtra(Constants.teamAKey, sessione.getTeamA())
//                            .putExtra(Constants.teamBKey, sessione.getTeamB());
//                    startActivityForResult(configurazione, CODE_FOR_CONF);
//                } else {
//                    new AlertDialog.Builder(this)
//                            .setTitle("Tipo di partita")
//                            .setCancelable(true)
//                            .setItems(new CharSequence[]
//                                            {"1 vs 1", "2 vs 2"},
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            Intent configurazione = new Intent(getBaseContext(), TeamSliderContainer.class);
//                                            switch (id) {
//                                                case 0:
//                                                    configurazione.putExtra(Constants.numberOfPlayer, 1);
//                                                    break;
//                                                case 1:
//                                                    configurazione.putExtra(Constants.numberOfPlayer, 2);
//                                                    break;
//                                            }
//                                            configurazione.putExtra(Constants.teamAKey, sessione.getTeamA())
//                                                    .putExtra(Constants.teamBKey, sessione.getTeamB());
//                                            startActivityForResult(configurazione, CODE_FOR_CONF);
//                                        }
//                                    })
//                            .create().show();
//                }
//                return true;
//            }
            case R.id.cancellaGame: {
                new AlertDialog.Builder(this)
                        .setMessage("Vuoi cancellare la partita attuale?")
                        .setCancelable(true)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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
                        .create().show();
                return true;
            }
//            case R.id.cancellaTutto: {
//                new AlertDialog.Builder(this)
//                        .setMessage("Sei sicuro di voler cancellare tutte le partite?")
//                        .setCancelable(true)
//                        .setPositiveButton("Si",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        tabAdapter.clearAll();
//                                        sessione.clear();
//                                        setNewGame();
//                                        teamSaved = false;
//                                        add.setVisible(false);
//                                        punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
//                                        punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
//                                        dialog.cancel();
//                                        dialogActive = false;
//                                    }
//                                })
//                        .setNegativeButton("No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        dialogActive = false;
//                                    }
//                                })
//                        .create().show();
//                return true;
//            }
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
        viewPager.setCurrentItem(sessione.getGameTotali()-1);

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
                    Team b_temp =  data.getParcelableExtra(Constants.teamBKey);
                    sessione.setTeamA(a_temp);
                    sessione.setTeamB(b_temp);
                    updateTeamAlias(a_temp.getAlias(), b_temp.getAlias());
                    teamSaved = true;
                    new AsyncTask<Team, Void, String>(){

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
                            if(cur.getCount()!=0) {
                                sloh.beginTransaction();
                                ContentValues cv;
                                cv = Utils.getTeamContentValues(team_A);
                                sloh.updateWithOnConflict(TeamColumns.TABLE_NAME,
                                        cv,
                                        TeamColumns.TEAM_ID+"=? and "+TeamColumns.SIDE+"=?",
                                        new String[]{String.valueOf(team_A.getId()), String.valueOf(team_A.getSide())},
                                        SQLiteDatabase.CONFLICT_FAIL);
                                cv = Utils.getTeamContentValues(team_B);
                                sloh.updateWithOnConflict(TeamColumns.TABLE_NAME,
                                        cv,
                                        TeamColumns.TEAM_ID+"=? and "+TeamColumns.SIDE+"=?",
                                        new String[]{String.valueOf(team_B.getId()), String.valueOf(team_B.getSide())},
                                        SQLiteDatabase.CONFLICT_FAIL);
                                Log.d(HelperConstants.DBTag, "aggiunte le squadre");
                                sloh.setTransactionSuccessful();
                                sloh.endTransaction();
                            }
                            cur.close();
                            sloh.close();
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

    public void gameUpdate(Game gameUpdate, boolean ended){
        sessione.updateLastGame(gameUpdate);
        if(ended){
            sessione.addNumeroVinti(gameUpdate.getWinner());
            punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
            punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
            dialogActive = true;
            winnerDialog.show();
        }
        Log.d("Grafica", "Sessione aggiornata con id " + gameUpdate.getId());

        new AsyncTask<BurracoSession, Void, Void>() {

            @Override
            protected Void doInBackground(BurracoSession... params) {

                BurracoDBHelper bdh = new BurracoDBHelper(getApplicationContext());
                SQLiteDatabase sloh = bdh.getWritableDatabase();
                sloh.beginTransaction();
                ContentValues cv;
                BurracoSession ses = params[0];
                Game gameTemp = ses.getCurrentGame();
                if(gameTemp.getNumeroMani()==1) {
                    cv = Utils.getSessionContentValues(sessione);
                    sloh.insertWithOnConflict(SessionColumns.TABLE_NAME,
                            null,
                            cv,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (ses.getGameTotali() == 1) {
                        Log.d(HelperConstants.DBTag, "Prima sessione");
                        cv = Utils.getTeamContentValues(sessione.getTeamA());
                        sloh.insert(TeamColumns.TABLE_NAME,
                                null,
                                cv);
                        cv = Utils.getTeamContentValues(sessione.getTeamB());
                        sloh.insert(TeamColumns.TABLE_NAME,
                                null,
                                cv);
                        Log.d(HelperConstants.DBTag, "aggiunte le squadre");
                    }
                }
                Cursor cur = sloh.query(GameColumns.TABLE_NAME,
                        new String[]{GameColumns.GAME_ID},
                        String.format("%s = ?", GameColumns.GAME_ID),
                        new String[]{String.valueOf(gameTemp.getId())},
                        null,
                        null,
                        null);
                cv = Utils.getGameContentValues(gameTemp, sessione.getId());
                if (cur.getCount()==0){
                    sloh.insertWithOnConflict(GameColumns.TABLE_NAME,
                            null,
                            cv,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    Log.d(HelperConstants.DBTag, "Inserita la partita");
                }else{
                    sloh.updateWithOnConflict(GameColumns.TABLE_NAME,
                            cv,
                            String.format("%s = ?", GameColumns.GAME_ID),
                            new String[]{String.valueOf(gameTemp.getId())},
                            SQLiteDatabase.CONFLICT_FAIL);
                    Log.d(HelperConstants.DBTag, "Aggiornata la partita");
                }
                cur.close();
                cv = Utils.getHandContentValues(gameTemp.getLastMano(Utils.ASide),Utils.ASide, gameTemp.getId());
                sloh.insertWithOnConflict(HandColumns.TABLE_NAME,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_FAIL);
                cv = Utils.getHandContentValues(gameTemp.getLastMano(Utils.BSide),Utils.BSide, gameTemp.getId());
                sloh.insertWithOnConflict(HandColumns.TABLE_NAME,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_FAIL);
                sloh.setTransactionSuccessful();
                sloh.endTransaction();
                sloh.close();
                Log.d(HelperConstants.DBTag, "Salvataggio concluso");
                return null;
            }
        }.execute(sessione);
    }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position != sessione.getGameTotali()-1){
            if(cancGame.isVisible()){
                cancGame.setVisible(false);
            }
        }else{
            if(!cancGame.isVisible() && sessione.getCurrentGame().getWinner() == 0){
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
            case R.id.confTeam:{
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
                            .create().show();
                }
                break;
            }
            case R.id.cancSession:{
                new AlertDialog.Builder(this)
                        .setMessage("Sei sicuro di voler cancellare tutte le partite?")
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
                        .create().show();
            }
            default:
                break;
        }
    }
}