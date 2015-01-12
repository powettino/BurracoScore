package yeapp.com.burracoscore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.OnClickListener;

public class TeamLogin extends ActionBarActivity implements OnClickListener, TextWatcher {
    private Button start;
    private EditText gioc11;
    private EditText gioc12;
    private EditText gioc21;
    private EditText gioc22;
    private TextView squadra1;
    private TextView squadra2;
    int numberOfPlayerForTeam = 0;
    final String varNumberPlayer = "numberOfPlayer";

    final String varGioc12State = "gioc12";
    final String varGioc22State = "gioc22";
    //    final String varStartState = "startEnable";
    final String varGioc11Text = "gioc11Text";
    final String varGioc12Text = "gioc12Text";
    final String varGioc21Text = "gioc21Text";
    final String varGioc22Text = "gioc22Text";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single: {
                numberOfPlayerForTeam = 1;
                changePlayers(numberOfPlayerForTeam);
                break;
            }
            case R.id.couple: {
                numberOfPlayerForTeam = 2;
                changePlayers(numberOfPlayerForTeam);
                break;
            }
            case R.id.start: {
                String teamA = getString(R.string.teamAName);
                String teamB = getString(R.string.teamBName);
                if (numberOfPlayerForTeam == 1) {
                    teamA = gioc11.getText().toString();
                    teamB = gioc21.getText().toString();
                } else {
                    teamA = (gioc11.getText()).append('-').append(gioc12.getText()).toString();
                    teamB = (gioc21.getText()).append('-').append(gioc22.getText()).toString();
                }
                Intent intent = new Intent(this, SummaryActivity.class)
                        .putExtra(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam)
                        .putExtra(getString(R.string.teamA), teamA)
                        .putExtra(getString(R.string.teamB), teamB);
                startActivityForResult(intent, 0);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_login);
        findViewById(R.id.single).setOnClickListener(this);
//        single.setOnClickListener(this);
        findViewById(R.id.couple).setOnClickListener(this);
//        couple.setOnClickListener(this);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        gioc11 = (EditText) findViewById(R.id.giocatore11);
        gioc12 = (EditText) findViewById(R.id.giocatore12);
        gioc21 = (EditText) findViewById(R.id.giocatore21);
        gioc22 = (EditText) findViewById(R.id.giocatore22);
        squadra1 = (TextView) findViewById(R.id.squadra1);
        squadra2 = (TextView) findViewById(R.id.squadra2);

        gioc11.addTextChangedListener(this);
        gioc12.addTextChangedListener(this);
        gioc21.addTextChangedListener(this);
        gioc22.addTextChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(varNumberPlayer, numberOfPlayerForTeam);

        outState.putBoolean(varGioc12State, gioc12.isEnabled());
        outState.putBoolean(varGioc22State, gioc22.isEnabled());

//        outState.putBoolean(varStartState, start.isEnabled());
        outState.putBoolean(varGioc12State, gioc12.isEnabled());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(varNumberPlayer);
//        changePlayers(numberOfPlayerForTeam);
        gioc12.setEnabled(true);
        gioc22.setEnabled(savedInstanceState.getBoolean(varGioc22State));

//        start.setEnabled(savedInstanceState.getBoolean(varStartState));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case (R.id.resetTeam): {
                numberOfPlayerForTeam = 0;
                changePlayers(numberOfPlayerForTeam);
                return true;
            }
            case (R.id.close): {
                finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changePlayers(int numberOfPlayerForTeam) {
        if (numberOfPlayerForTeam > 0) {
            if (gioc11.getText().length() == 0) {
                gioc11.setText(R.string.nomeGiocatore11);
            }
            if (gioc12.getText().length() == 0) {
                gioc12.setText(R.string.nomeGiocatore12);
            }
            if (gioc21.getText().length() == 0) {
                gioc21.setText(R.string.nomeGiocatore21);
            }
            if (gioc22.getText().length() == 0) {
                gioc22.setText(R.string.nomeGiocatore22);
            }
            if (!squadra1.isEnabled() && !squadra2.isEnabled()) {
                squadra2.setEnabled(true);
                squadra1.setEnabled(true);
            }
            if (!start.isEnabled()) {
                start.setEnabled(true);
            }
            if (!gioc11.isEnabled() && !gioc21.isEnabled()) {
                gioc11.setEnabled(true);
                gioc21.setEnabled(true);
            }
            if (numberOfPlayerForTeam == 1 && gioc12.isEnabled() && gioc22.isEnabled()) {
                gioc12.setEnabled(false);
                gioc22.setEnabled(false);
            }
            if (numberOfPlayerForTeam == 2 && !gioc12.isEnabled() && !gioc22.isEnabled()) {
                gioc12.setEnabled(true);
                gioc22.setEnabled(true);
            }
        } else {
            gioc11.setText(R.string.nomeGiocatore11);
            gioc12.setText(R.string.nomeGiocatore12);
            gioc21.setText(R.string.nomeGiocatore21);
            gioc22.setText(R.string.nomeGiocatore22);
            start.setEnabled(false);
            gioc11.setEnabled(false);
            gioc21.setEnabled(false);
            squadra1.setEnabled(false);
            squadra2.setEnabled(false);
            if (gioc12.isEnabled() && gioc22.isEnabled()) {
                gioc12.setEnabled(false);
                gioc22.setEnabled(false);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            if (start.isEnabled())
                start.setEnabled(false);
        } else {
            if (!start.isEnabled()) {
                start.setEnabled(true);
            }
        }
    }
}
