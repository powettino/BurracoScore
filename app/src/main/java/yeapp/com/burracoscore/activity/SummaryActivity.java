package yeapp.com.burracoscore.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.adapter.ListPointAdapter;
import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.core.model.Team;

public class SummaryActivity extends ActionBarActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, View.OnTouchListener {

    Toolbar toolbar;
    private boolean requestedFocus = false;
    private static final int maxPoint = 500;
    public static final String teamAKey = "teamA";
    public static final String teamBKey = "teamB";
    public static final String numberOfPlayer = "numberOfPlayer";
    public static final String handA = "handA";
    public static final String handB = "handB";

    public static final String addHand = "addHand";
    public static final String setDialog = "dialog";

    private TextView teamAText;
    private TextView teamBText;

    private final int CODE_FOR_CONF = 0;
    private final int CODE_FOR_SET = 1;
    private int numberOfPlayerForTeam = 0;
    private Team teamA;
    private Team teamB;

    ListPointAdapter dtaLVA = new ListPointAdapter(this, true);
    ListPointAdapter dtaLVB = new ListPointAdapter(this, false);

    ListView listA = null;
    ListView listB = null;

    private TextView resultA;
    private TextView resultB;
    private TextView punteggioTotA;
    private TextView punteggioTotB;
    private boolean dialogActive;
    AlertDialog winnerDialog = null;
    AlertDialog resetDialog = null;
    AlertDialog gameDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_summary);
        toolbar.setOnMenuItemClickListener(this);

        teamAText = (TextView) findViewById(R.id.teamASummary);
        teamBText = (TextView) findViewById(R.id.teamBSummary);
        if (teamA == null) {
            teamA = new Team("A");
        }
        if (teamB == null) {
            teamB = new Team("B");
        }
        listA = (ListView) findViewById(R.id.listPointTeamA);
        listB = (ListView) findViewById(R.id.listPointTeamB);
        listA.setAdapter(dtaLVA);
        listB.setAdapter(dtaLVB);
        listA.setEmptyView(findViewById(R.id.empty));
        listB.setEmptyView(findViewById(R.id.empty2));
        listA.setOnTouchListener(this);
        listB.setOnTouchListener(this);
        listA.setFocusableInTouchMode(true);
        listB.setFocusableInTouchMode(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.cancellaGame).setOnClickListener(this);
            findViewById(R.id.cancellaTutto).setOnClickListener(this);
        }
        findViewById(R.id.addHand).setOnClickListener(this);

        resultA = (TextView) findViewById(R.id.resultA);
        resultB = (TextView) findViewById(R.id.resultB);

        punteggioTotA = (TextView) findViewById(R.id.punteggioASummary);
        punteggioTotB = (TextView) findViewById(R.id.punteggioBSummary);
        if (winnerDialog == null) {
            winnerDialog = new AlertDialog.Builder(this).setTitle("VITTORIA!!!")
                    .setMessage("Complimenti, la partita è conclusa.\n\nVuoi cominciare una nuova partita?")
                    .setCancelable(false)
                    .setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    resetGames();
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    findViewById(R.id.addHand).setEnabled(false);
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .create();
        }
        if (resetDialog == null) {
            resetDialog = new AlertDialog.Builder(this)
                    .setMessage("Sei sicuro di voler cancellare la partite non salvate?")
                    .setCancelable(false)
                    .setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    resetAll();
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
                    .create();
        }
        if (gameDialog == null) {
            gameDialog = new AlertDialog.Builder(this)
                    .setMessage("Vuoi cominciare una nuova partita?")
                    .setCancelable(false)
                    .setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    resetGames();
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
                    .create();

        }
    }

    private void createTeamName() {
        String p11 = teamA.getPlayer1();
        String p12 = teamA.getPlayer2();
        String p21 = teamB.getPlayer1();
        String p22 = teamB.getPlayer2();
        if (p11 == null && p21 == null) {
            teamAText.setText(R.string.teamAName);
            teamBText.setText(R.string.teamBName);
        } else {
            assert p11 != null;
            teamAText.setText(p11.substring(0, ((p11.length() >= 3) ? 3 : p11.length())) + (numberOfPlayerForTeam == 2 ? "∞" + p12.substring(0, (p12.length() >= 3 ? 3 : p12.length())) : ""));
            teamBText.setText(p21.substring(0, (p21.length() >= 3 ? 3 : p21.length())) + (numberOfPlayerForTeam == 2 ? "∞" + p22.substring(0, (p22.length() >= 3 ? 3 : p22.length())) : ""));
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(numberOfPlayer);
        teamA = savedInstanceState.getParcelable(teamAKey);
        teamB = savedInstanceState.getParcelable(teamBKey);
        createTeamName();
        dtaLVA.restore(teamA.getPunti());
        dtaLVB.restore(teamB.getPunti());
        dtaLVA.notifyDataSetChanged();
        dtaLVB.notifyDataSetChanged();
        resultA.setText(teamA.getTotale() == 0 ? "" : String.valueOf(teamA.getTotale()));
        resultB.setText(teamB.getTotale() == 0 ? "" : String.valueOf(teamB.getTotale()));
        punteggioTotA.setText(String.valueOf(teamA.getTotGames()));
        punteggioTotB.setText(String.valueOf(teamB.getTotGames()));
        findViewById(R.id.addHand).setEnabled(savedInstanceState.getBoolean(addHand));
        if (savedInstanceState.getBoolean(setDialog)) {
            dialogActive=true;
            winnerDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(teamAKey, teamA);
        outState.putParcelable(teamBKey, teamB);
        outState.putBoolean(addHand, findViewById(R.id.addHand).isEnabled());
        outState.putBoolean(setDialog, dialogActive);

//        if (dialog != null && dialog.isShowing()) {
//            dialog.cancel();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                    numberOfPlayerForTeam = data.getIntExtra(numberOfPlayer, 0);
                    teamA = data.getParcelableExtra(teamAKey);
                    teamB = data.getParcelableExtra(teamBKey);
                    createTeamName();
                }
                break;
            }
            case CODE_FOR_SET: {
                if (resultCode == RESULT_OK) {
                    Hand mano = data.getParcelableExtra(handA);
                    int lastGame = teamA.getNumberHands();
                    teamA.addMano(mano);
                    resultA.setText(String.valueOf(teamA.getTotale()));
                    if (lastGame == 0) {
                        dtaLVA.addLastDoubleText(String.valueOf(mano.getTotaleMano()), "G1");
                    } else {
                        dtaLVA.addLastDoubleText(String.valueOf(mano.getTotaleMano()), "G" + (lastGame + 1));
                    }
                    dtaLVA.notifyDataSetChanged();
                    mano = data.getParcelableExtra(handB);
                    teamB.addMano(mano);
                    resultB.setText(String.valueOf(teamB.getTotale()));
                    if (lastGame == 0) {
                        dtaLVB.addLastDoubleText(String.valueOf(mano.getTotaleMano()), "G1");
                    } else {
                        dtaLVB.addLastDoubleText(String.valueOf(mano.getTotaleMano()), "G" + (lastGame + 1));
                    }
                    dtaLVB.notifyDataSetChanged();
                    checkWinner();
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }

    }

    private void checkWinner() {
        int totA = teamA.getTotale();
        int totB = teamB.getTotale();
//            int winA = resultA.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultA.getText().toString());
//            int winB = resultB.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultB.getText().toString());
        if (totA >= maxPoint || totB >= maxPoint) {
            if (totA != totB) {
                if (totA > totB) {
                    teamA.addGame();
                    punteggioTotA.setText(String.valueOf(teamA.getTotGames()));
                } else if (totB > totA) {
                    teamB.addGame();
                    punteggioTotB.setText(String.valueOf(teamB.getTotGames()));
                }
                dialogActive=true;
                winnerDialog.show();
            }
        }
    }

    private void resetGames() {
        dtaLVA.clearData();
        dtaLVA.notifyDataSetChanged();
        dtaLVB.clearData();
        dtaLVB.notifyDataSetChanged();
        resultA.setText("");
        resultB.setText("");
        teamA.cleanPunteggio();
        teamB.cleanPunteggio();
        findViewById(R.id.addHand).setEnabled(true);
    }

    private void resetAll() {
        resetGames();
        teamB.cleanTeam();
        teamA.cleanTeam();
        teamA.cleanGames();
        teamB.cleanGames();
        teamAText.setText(R.string.teamAName);
        teamBText.setText(R.string.teamBName);
        punteggioTotA.setText("0");
        punteggioTotB.setText("0");
        numberOfPlayerForTeam = 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancellaGame: {
                gameDialog.show();
                break;
            }
            case R.id.cancellaTutto: {
                resetDialog.show();
                break;
            }
            case (R.id.addHand): {
                Intent add = new Intent(this, AddPoint.class);
                startActivityForResult(add, CODE_FOR_SET);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configuraMenuSum: {
                Intent configurazione = new Intent(this, TeamConfiguration.class);
                configurazione.putExtra(numberOfPlayer, numberOfPlayerForTeam)
                        .putExtra(teamAKey, teamA)
                        .putExtra(teamBKey, teamB);
                startActivityForResult(configurazione, CODE_FOR_CONF);
                return true;
            }
            case R.id.cancellaGame: {
                gameDialog.show();
                return true;
            }
            case R.id.cancellaTutto: {
                resetDialog.show();
                return true;
            }
            default: {
                return true;
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !requestedFocus) {
            view.requestFocus();
            requestedFocus = true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            requestedFocus = false;
        }

        if (view.getId() == R.id.listPointTeamA && view.isFocused()) {
            listB.dispatchTouchEvent(motionEvent);
        } else if (view.getId() == R.id.listPointTeamB && view.isFocused()) {
            listA.dispatchTouchEvent(motionEvent);
        }

        return super.onTouchEvent(motionEvent);
    }
}