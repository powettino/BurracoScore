package yeapp.com.burracoscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.custom.adapter.DoubleTextAdapter;

public class SummaryActivity extends Activity implements View.OnClickListener {

    public static String teamAKey = "teamA";
    public static String teamBKey = "teamB";
    public static String numerOfPlayer = "numberOfPlayer";
    public static String hand = "hand";


    private TextView teamAText;
    private TextView teamBText;

    private final int CODE_FOR_CONF = 0;
    private final int CODE_FOR_SET_A = 1;
    private final int CODE_FOR_SET_B = 2;
    private int numberOfPlayerForTeam = 0;
    private Team teamA;
    private Team teamB;
    DoubleTextAdapter dtaLVA = new DoubleTextAdapter(this);
    DoubleTextAdapter dtaLVB = new DoubleTextAdapter(this);
    private TextView resultA;
    private TextView resultB;
    private TextView punteggioTotA;
    private TextView punteggioTotB;
    private boolean dialogActive;
    AlertDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        teamAText = (TextView) findViewById(R.id.teamASummary);

        teamBText = (TextView) findViewById(R.id.teamBSummary);
        teamA = new Team("A");
        teamB = new Team("B");

        ((ListView) findViewById(R.id.listPointTeamA)).setAdapter(dtaLVA);
        ((ListView) findViewById(R.id.listPointTeamB)).setAdapter(dtaLVB);

        findViewById(R.id.setTeamA).setOnClickListener(this);
        findViewById(R.id.setTeamB).setOnClickListener(this);

        resultA = (TextView) findViewById(R.id.resultA);
        resultB = (TextView) findViewById(R.id.resultB);

        punteggioTotA = (TextView) findViewById(R.id.punteggioASummary);
        punteggioTotB = (TextView) findViewById(R.id.punteggioBSummary);
    }

    private void createTeamName() {
        if (teamA.getPlayer1() == null && teamB.getPlayer2() == null) {
            teamAText.setText(R.string.teamAName);
            teamBText.setText(R.string.teamBName);
        } else {
            String p11 = teamA.getPlayer1();
            String p12 = teamA.getPlayer2();
            String p21 = teamB.getPlayer1();
            String p22 = teamB.getPlayer2();
            teamAText.setText(p11.substring(0, (p11.length() >= 3 ? 3 : p11.length())) + (numberOfPlayerForTeam == 2 ? "-" + p12.substring(0, (p12.length() >= 3 ? 3 : p12.length())) : ""));
            teamBText.setText(p21.substring(0, (p21.length() >= 3 ? 3 : p21.length())) + (numberOfPlayerForTeam == 2 ? "-" + p22.substring(0, (p22.length() >= 3 ? 3 : p22.length())) : ""));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(numerOfPlayer);
        teamA = (Team) savedInstanceState.getParcelable(teamAKey);
        teamB = (Team) savedInstanceState.getParcelable(teamBKey);
        createTeamName();
        dtaLVA.restore(teamA.getPunti());
        dtaLVB.restore(teamB.getPunti());
        dtaLVA.notifyDataSetChanged();
        dtaLVB.notifyDataSetChanged();
        resultA.setText(teamA.getTotale());
        resultB.setText(teamB.getTotale());
        punteggioTotA.setText(teamA.getGame());
        punteggioTotB.setText(teamB.getGame());
        findViewById(R.id.setTeamA).setEnabled(savedInstanceState.getBoolean(getString(R.string.setPointA)));
        findViewById(R.id.setTeamB).setEnabled(savedInstanceState.getBoolean(getString(R.string.setPointB)));
        if (savedInstanceState.getBoolean(getString(R.string.dialogWinner))) {
            createDialogWinner();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(numerOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(teamAKey, teamA);
        outState.putParcelable(teamBKey, teamB);
        outState.putBoolean(getString(R.string.setPointA), findViewById(R.id.setTeamA).isEnabled());
        outState.putBoolean(getString(R.string.setPointB), findViewById(R.id.setTeamB).isEnabled());
        outState.putBoolean(getString(R.string.dialogWinner), dialogActive);

        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chiudiAppMenuSum: {
                finish();
                return true;
            }
            case R.id.configuraMenuSum: {
                Intent configurazione = new Intent(this, TeamConfiguration.class);
                configurazione.putExtra(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam)
                        .putExtra(teamAKey, teamA)
                        .putExtra(teamBKey, teamB);
                startActivityForResult(configurazione, CODE_FOR_CONF);
                return true;
            }
            case R.id.cancellaGame: {
                resetGames();
                return true;
            }
            case R.id.cancellaTutto: {
                resetAll();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                    numberOfPlayerForTeam = data.getIntExtra(SummaryActivity.numerOfPlayer, 0);
                    teamA = (Team) data.getParcelableExtra(SummaryActivity.teamAKey);
                    teamB = (Team) data.getParcelableExtra(SummaryActivity.teamBKey);
                    createTeamName();
                }
                break;
            }
            case CODE_FOR_SET_A: {
                if (resultCode == RESULT_OK) {
                    Hand h = (Hand) data.getParcelableExtra(hand);
                    String lastGame = dtaLVA.getLastDoubleText().second;
                    int currentResult = h.getBase() + h.getCarte() + h.getChiusura() + h.getMazzetto();
                    teamA.addPunti(currentResult);
                    resultA.setText(teamA.getTotale());
                    if (lastGame == null) {
                        dtaLVA.addLastDoubleText(String.valueOf(currentResult), "G1");
                    } else {
                        dtaLVA.addLastDoubleText(String.valueOf(currentResult), "G" + (Integer.valueOf(lastGame.substring(1, 2)) + 1));
                    }
                    dtaLVA.notifyDataSetChanged();
                    checkWinner();
                }
                break;
            }
            case CODE_FOR_SET_B: {
                if (resultCode == RESULT_OK) {
                    Hand h = (Hand) data.getParcelableExtra(hand);
                    String lastGame = dtaLVB.getLastDoubleText().second;
                    int currentResult = h.getBase() + h.getCarte() + h.getChiusura() + h.getMazzetto();
                    resultB.setText(teamB.getTotale());
                    if (lastGame == null) {
                        dtaLVB.addLastDoubleText(String.valueOf(currentResult), "G1");
                    } else {
                        dtaLVB.addLastDoubleText(String.valueOf(currentResult), "G" + (Integer.valueOf(lastGame.substring(1, 2)) + 1));
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
        if (dtaLVA.getCount() == dtaLVB.getCount()) {
            int winA = resultA.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultA.getText().toString());
            int winB = resultB.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultB.getText().toString());
            if (winA >= 200 || winB >= 200) {
                if (winA != winB) {
                    if (winA > winB) {
                        teamA.addGame();
                        punteggioTotA.setText(String.valueOf(Integer.valueOf(punteggioTotA.getText().toString()) + 1));
                    } else if (winB > winA) {
                        teamB.addGame();
                        punteggioTotB.setText(String.valueOf(Integer.valueOf(punteggioTotB.getText().toString()) + 1));
                    }
                    createDialogWinner();
                }
            }
        }
    }

    private void createDialogWinner() {
        dialogActive = true;
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).setTitle("VITTORIA!!!")
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
                                    findViewById(R.id.setTeamA).setEnabled(false);
                                    findViewById(R.id.setTeamB).setEnabled(false);
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .create();
        }
        dialog.show();
    }

    private void resetGames() {
        dtaLVA.clearData();
        dtaLVB.clearData();
        dtaLVB.notifyDataSetChanged();
        dtaLVA.notifyDataSetChanged();
        resultA.setText("");
        resultB.setText("");
        teamA.cleanPunteggio();
        teamB.cleanPunteggio();
        findViewById(R.id.setTeamA).setEnabled(true);
        findViewById(R.id.setTeamB).setEnabled(true);
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
            case (R.id.setTeamA): {
                Intent add = new Intent(this, AddPoint.class);
                startActivityForResult(add, CODE_FOR_SET_A);
                break;
            }
            case (R.id.setTeamB): {
                Intent add = new Intent(this, AddPoint.class);
                startActivityForResult(add, CODE_FOR_SET_B);
                break;
            }
            default: {
                break;
            }
        }
    }
}
