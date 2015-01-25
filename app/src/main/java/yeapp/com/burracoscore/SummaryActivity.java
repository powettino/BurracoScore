package yeapp.com.burracoscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.custom.adapter.DoubleTextAdapter;

public class SummaryActivity extends Activity implements View.OnClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        teamAText = (TextView) findViewById(R.id.teamASummary);
        // namesTeamA = teamAText.getText().toString();
        teamBText = (TextView) findViewById(R.id.teamBSummary);
       // teamA = new Team("A");
       // teamB = new Team("B");
        // namesTeamB = teamBText.getText().toString();
        ((ListView) findViewById(R.id.listPointTeamA)).setAdapter(dtaLVA);
        ((ListView) findViewById(R.id.listPointTeamB)).setAdapter(dtaLVB);

        findViewById(R.id.setTeamA).setOnClickListener(this);
        findViewById(R.id.setTeamB).setOnClickListener(this);

        resultA = (TextView) findViewById(R.id.resultA);
        resultB = (TextView) findViewById(R.id.resultB);

        punteggioTotA = (TextView) findViewById(R.id.punteggioASummary);
        punteggioTotB = (TextView) findViewById(R.id.punteggioBSummary);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (teamA == null) {
            teamA = new Team("A");
        }
        if (teamB == null) {
            teamB = new Team("B");
        }
        numberOfPlayerForTeam = savedInstanceState.getInt(getString(R.string.numberPlayerActivity));
        teamA.setPlayer1(savedInstanceState.getString(getString(R.string.nomeGiocatore11)));
        teamA.setPlayer2(savedInstanceState.getString(getString(R.string.nomeGiocatore12)));
        teamB.setPlayer1(savedInstanceState.getString(getString(R.string.nomeGiocatore21)));
        teamB.setPlayer2(savedInstanceState.getString(getString(R.string.nomeGiocatore22)));
        createTeamName();
        dtaLVA.setArrayLists(savedInstanceState.getStringArrayList(getString(R.string.listaPuntiA)), savedInstanceState.getStringArrayList(getString(R.string.listaGameA)));
        dtaLVB.setArrayLists(savedInstanceState.getStringArrayList(getString(R.string.listaPuntiB)), savedInstanceState.getStringArrayList(getString(R.string.listaGameB)));
        dtaLVA.notifyDataSetChanged();
        dtaLVB.notifyDataSetChanged();
        resultA.setText(savedInstanceState.getString(getString(R.string.risultatoA)));
        resultB.setText(savedInstanceState.getString(getString(R.string.risultatoB)));
        punteggioTotA.setText(savedInstanceState.getString(getString(R.string.risComplessivoA)));
        punteggioTotB.setText(savedInstanceState.getString(getString(R.string.risComplessivoB)));
        findViewById(R.id.setTeamA).setEnabled(savedInstanceState.getBoolean(getString(R.string.setPointA)));
        findViewById(R.id.setTeamB).setEnabled(savedInstanceState.getBoolean(getString(R.string.setPointB)));
        if (savedInstanceState.getBoolean(getString(R.string.dialogWinner))) {
            createDialogWinner();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam);
        outState.putString(getString(R.string.nomeGiocatore11), teamA.getPlayer1());
        outState.putString(getString(R.string.nomeGiocatore12), teamA.getPlayer2());
        outState.putString(getString(R.string.nomeGiocatore21), teamB.getPlayer1());
        outState.putString(getString(R.string.nomeGiocatore22), teamB.getPlayer2());
        outState.putStringArrayList(getString(R.string.listaPuntiA), dtaLVA.getTextLeft());
        outState.putStringArrayList(getString(R.string.listaGameA), dtaLVA.getTextRight());
        outState.putStringArrayList(getString(R.string.listaPuntiB), dtaLVB.getTextLeft());
        outState.putStringArrayList(getString(R.string.listaGameB), dtaLVB.getTextRight());
        outState.putString(getString(R.string.risultatoA), resultA.getText().toString());
        outState.putString(getString(R.string.risultatoB), resultB.getText().toString());
        outState.putString(getString(R.string.risComplessivoA), punteggioTotA.getText().toString());
        outState.putString(getString(R.string.risComplessivoB), punteggioTotB.getText().toString());
        outState.putBoolean(getString(R.string.setPointA), findViewById(R.id.setTeamA).isEnabled());
        outState.putBoolean(getString(R.string.setPointB), findViewById(R.id.setTeamB).isEnabled());
        outState.putBoolean(getString(R.string.dialogWinner), dialogActive);
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                        .putExtra(getString(R.string.nomeGiocatore11), teamA.getPlayer1())
                        .putExtra(getString(R.string.nomeGiocatore12), teamA.getPlayer2())
                        .putExtra(getString(R.string.nomeGiocatore21), teamB.getPlayer1())
                        .putExtra(getString(R.string.nomeGiocatore22), teamB.getPlayer2());
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
                    numberOfPlayerForTeam = data.getIntExtra(getString(R.string.numberPlayerActivity), 0);
                    teamA.setPlayer1(data.getStringExtra(getString(R.string.nomeGiocatore11)));
                    teamB.setPlayer1(data.getStringExtra(getString(R.string.nomeGiocatore21)));
                    if (numberOfPlayerForTeam == 2) {
                        teamA.setPlayer2(data.getStringExtra(getString(R.string.nomeGiocatore12)));
                        teamB.setPlayer2(data.getStringExtra(getString(R.string.nomeGiocatore22)));
                    }
                    createTeamName();
                }
                break;
            }
            case CODE_FOR_SET_A: {
                if (resultCode == RESULT_OK) {
                    int base = data.getIntExtra(getString(R.string.puntiBase), 0);
                    int carte = data.getIntExtra(getString(R.string.puntiCarte), 0);
                    int chiusura = data.getIntExtra(getString(R.string.puntiChiusura), 0);
                    int mazzetto = data.getIntExtra(getString(R.string.puntiMazzetto), 0);
                    String lastGame = dtaLVA.getLastDoubleText().second;
                    int currentResult = base + carte + chiusura + mazzetto;
                    resultA.setText(resultA.getText().toString().isEmpty() ? String.valueOf(currentResult) : String.valueOf((currentResult + Integer.valueOf(resultA.getText().toString()))));

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
                    int base = data.getIntExtra(getString(R.string.puntiBase), 0);
                    int carte = data.getIntExtra(getString(R.string.puntiCarte), 0);
                    int chiusura = data.getIntExtra(getString(R.string.puntiChiusura), 0);
                    int mazzetto = data.getIntExtra(getString(R.string.puntiMazzetto), 0);
                    String lastGame = dtaLVB.getLastDoubleText().second;
                    int currentResult = base + carte + chiusura + mazzetto;
                    resultB.setText(resultB.getText().toString().isEmpty() ? String.valueOf(currentResult) : String.valueOf((currentResult + Integer.valueOf(resultB.getText().toString()))));

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
                        punteggioTotA.setText(String.valueOf(Integer.valueOf(punteggioTotA.getText().toString()) + 1));
                    } else if (winB > winA) {
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
        findViewById(R.id.setTeamA).setEnabled(true);
        findViewById(R.id.setTeamB).setEnabled(true);
    }

    private void resetAll() {
        resetGames();
        teamB.cleanTeam();
        teamA.cleanTeam();
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
