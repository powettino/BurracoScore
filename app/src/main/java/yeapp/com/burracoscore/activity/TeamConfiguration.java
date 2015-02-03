package yeapp.com.burracoscore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Team;

import static android.view.View.OnClickListener;

public class TeamConfiguration extends Activity implements OnClickListener, TextWatcher {
    private Button salvaButton;
    private Button resetButton;
    private EditText gioc11Text;
    private EditText gioc12Text;
    private EditText gioc21Text;
    private EditText gioc22Text;
    private int numberOfPlayerForTeam = 0;

    Team tA = null;
    Team tB = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.resetConf): {
                resetNames();
                break;
            }
            case R.id.singleConf: {
                numberOfPlayerForTeam = 1;
                changeVisualConfigurationByPlayers();
                break;
            }
            case R.id.coupleConf: {
                numberOfPlayerForTeam = 2;
                changeVisualConfigurationByPlayers();
                break;
            }
            case R.id.salvaConf: {
                salvaNomi();
                break;
            }
            case R.id.chiudiConf: {
                setResult(RESULT_CANCELED);
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void resetNames() {
        gioc11Text.setText(R.string.nomeGiocatore11);
        gioc12Text.setText(R.string.nomeGiocatore12);
        gioc21Text.setText(R.string.nomeGiocatore21);
        gioc22Text.setText(R.string.nomeGiocatore22);
        if (!gioc12Text.isEnabled() && !gioc22Text.isEnabled()) {
            gioc12Text.setEnabled(true);
            gioc12Text.setEnabled(false);
            gioc22Text.setEnabled(true);
            gioc22Text.setEnabled(false);
        }
    }

    private void salvaNomi() {
        tA.setPlayer1(gioc11Text.getText().toString());
        tA.setPlayer2(numberOfPlayerForTeam == 2 ? gioc12Text.getText().toString() : null);
        tB.setPlayer1(gioc21Text.getText().toString());
        tB.setPlayer2(numberOfPlayerForTeam == 2 ? gioc22Text.getText().toString() : null);
        setResult(RESULT_OK, this.getIntent()
                        .putExtra(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam)
                        .putExtra(SummaryActivity.teamAKey, tA)
                        .putExtra(SummaryActivity.teamBKey, tB)
        );
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_configuration);
        findViewById(R.id.singleConf).setOnClickListener(this);
        findViewById(R.id.coupleConf).setOnClickListener(this);
        salvaButton = (Button) findViewById(R.id.salvaConf);
        salvaButton.setOnClickListener(this);
        resetButton = (Button) findViewById(R.id.resetConf);
        resetButton.setOnClickListener(this);
        findViewById(R.id.chiudiConf).setOnClickListener(this);
        gioc11Text = (EditText) findViewById(R.id.giocatore11);
        gioc12Text = (EditText) findViewById(R.id.giocatore12);
        gioc21Text = (EditText) findViewById(R.id.giocatore21);
        gioc22Text = (EditText) findViewById(R.id.giocatore22);

        gioc11Text.addTextChangedListener(this);
        gioc12Text.addTextChangedListener(this);
        gioc21Text.addTextChangedListener(this);
        gioc22Text.addTextChangedListener(this);
        restoreFromMain();
    }

    private void restoreFromMain() {
        Intent startingIntent = getIntent();
        numberOfPlayerForTeam = startingIntent.getIntExtra(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
        tA = startingIntent.getParcelableExtra(SummaryActivity.teamAKey);
        tB = startingIntent.getParcelableExtra(SummaryActivity.teamBKey);

        if (numberOfPlayerForTeam != 0) {
            gioc11Text.setText(tA.getPlayer1());
            gioc21Text.setText(tB.getPlayer1());
            if (numberOfPlayerForTeam == 2) {
                gioc12Text.setText(tA.getPlayer2());
                gioc22Text.setText(tA.getPlayer2());
            }
            changeVisualConfigurationByPlayers();
        } else {
            resetNames();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(SummaryActivity.teamAKey, tA);
        outState.putParcelable(SummaryActivity.teamBKey, tB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(SummaryActivity.numberOfPlayer);
        if (numberOfPlayerForTeam != 0) {
            gioc11Text.setText(tA.getPlayer1());
            gioc21Text.setText(tB.getPlayer1());
            if (numberOfPlayerForTeam == 2) {
                gioc12Text.setText(tA.getPlayer1());
                gioc22Text.setText(tB.getPlayer1());
            }
            changeVisualConfigurationByPlayers();
        } else {
            resetNames();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void changeVisualConfigurationByPlayers() {
        if (gioc11Text.getText().length() == 0) {
            gioc11Text.setText(R.string.nomeGiocatore11);
        }
        if (gioc12Text.getText().length() == 0) {
            gioc12Text.setText(R.string.nomeGiocatore12);
        }
        if (gioc21Text.getText().length() == 0) {
            gioc21Text.setText(R.string.nomeGiocatore21);
        }
        if (gioc22Text.getText().length() == 0) {
            gioc22Text.setText(R.string.nomeGiocatore22);
        }
        if (!salvaButton.isEnabled()) {
            salvaButton.setEnabled(true);
        }
        if (!resetButton.isEnabled()) {
            resetButton.setEnabled(true);
        }
        if (!gioc11Text.isEnabled() && !gioc21Text.isEnabled()) {
            gioc11Text.setEnabled(true);
            gioc21Text.setEnabled(true);
        }
        if (numberOfPlayerForTeam == 1 && gioc12Text.isEnabled() && gioc22Text.isEnabled()) {
            gioc12Text.setEnabled(false);
            gioc22Text.setEnabled(false);
        }
        if (numberOfPlayerForTeam == 2 && !gioc12Text.isEnabled() && !gioc22Text.isEnabled()) {
            gioc12Text.setEnabled(true);
            gioc22Text.setEnabled(true);
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
            if (salvaButton.isEnabled())
                salvaButton.setEnabled(false);
        } else {
            if (!salvaButton.isEnabled() && numberOfPlayerForTeam != 0) {
                salvaButton.setEnabled(true);
            }
        }
    }
}
