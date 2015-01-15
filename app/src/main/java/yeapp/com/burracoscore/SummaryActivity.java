package yeapp.com.burracoscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.custom.adapter.DoubleTextAdapter;

public class SummaryActivity extends Activity implements View.OnClickListener {

    private TextView teamAText;
    private TextView teamBText;
    private final int CODE_FOR_CONF = 0;
    private final int CODE_FOR_SET_A = 1;
    private final int CODE_FOR_SET_B = 1;
    private ListView listPointTeamA;
    private ListView listPointTeamB;
    private int numberOfPlayerForTeam = 0;
    private Team teamA;
    private Team teamB;


    private void createTeamName() {
        if (teamA.getPlayer1() == null && teamB.getPlayer2() == null) {
            teamAText.setText(R.string.teamAName);
            teamBText.setText(R.string.teamBName);
        } else {
            teamAText.setText(teamA.getPlayer1().substring(0, 3) + (numberOfPlayerForTeam == 2 ? "-" + teamA.getPlayer2().substring(0, 3) : ""));
            teamBText.setText(teamB.getPlayer1().substring(0, 3) + (numberOfPlayerForTeam == 2 ? "-" + teamB.getPlayer2().substring(0, 3) : ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        teamAText = (TextView) findViewById(R.id.teamASummary);
        // namesTeamA = teamAText.getText().toString();
        teamBText = (TextView) findViewById(R.id.teamBSummary);
        teamA = new Team("A");
        teamB = new Team("B");
        // namesTeamB = teamBText.getText().toString();
        listPointTeamA = (ListView) findViewById(R.id.listPointTeamA);
        listPointTeamB = (ListView) findViewById(R.id.listPointTeamB);

        findViewById(R.id.setTeamA).setOnClickListener(this);
        findViewById(R.id.setTeamB).setOnClickListener(this);

        ArrayList<String> your_array_list = new ArrayList<String>();
        your_array_list.add(" s");
//        your_array_list.add("bar");
        ArrayList<String> a = new ArrayList<String>();
        a.add("d ");
//        a.add("ss");
        DoubleTextAdapter dta = new DoubleTextAdapter(this, your_array_list, a);
        listPointTeamA.setAdapter(dta);
        listPointTeamB.setAdapter(dta);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam);
        outState.putString(getString(R.string.nomeGiocatore11), teamA.getPlayer1());
        outState.putString(getString(R.string.nomeGiocatore12), teamA.getPlayer2());
        outState.putString(getString(R.string.nomeGiocatore21), teamB.getPlayer1());
        outState.putString(getString(R.string.nomeGiocatore22), teamB.getPlayer2());
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
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //FIXME: usare una risorse e non un intero
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                    numberOfPlayerForTeam = data.getIntExtra(getString(R.string.numberPlayerActivity), 0);
                    teamA.setPlayer1(data.getStringExtra(getString(R.string.nomeGiocatore11)));
                    teamB.setPlayer1(data.getStringExtra(getString(R.string.nomeGiocatore21)));
                    if (numberOfPlayerForTeam == 2) {
                        teamA.setPlayer2(data.getStringExtra(getString(R.string.nomeGiocatore21)));
                        teamB.setPlayer2(data.getStringExtra(getString(R.string.nomeGiocatore22)));
                    }
                    createTeamName();
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
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
