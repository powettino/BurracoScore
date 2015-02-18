package yeapp.com.burracoscore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Team;

import static android.view.View.OnClickListener;

public class TeamConfiguration extends ActionBarActivity implements OnClickListener, TextWatcher {

    Toolbar toolbar;
    private MenuItem salvaButton;
    private MenuItem resetButton;
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
                changeDisplayName();
                break;
            }
            case R.id.coupleConf: {
                numberOfPlayerForTeam = 2;
                changeDisplayName();
                break;
            }
            case R.id.salvaConf: {
                completeAndSend();
                break;
            }
//            case R.id.chiudiConf: {
//                setResult(RESULT_CANCELED);
//                finish();
//                break;
//            }
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

    private void saveDisplayedName() {
        tA.setPlayer1(gioc11Text.getText().toString());
        tA.setPlayer2(numberOfPlayerForTeam == 2 ? gioc12Text.getText().toString() : null);
        tB.setPlayer1(gioc21Text.getText().toString());
        tB.setPlayer2(numberOfPlayerForTeam == 2 ? gioc22Text.getText().toString() : null);
    }

    private void completeAndSend() {
        saveDisplayedName();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conf_name);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(8);

        findViewById(R.id.singleConf).setOnClickListener(this);
        findViewById(R.id.coupleConf).setOnClickListener(this);
//        salvaButton = (Button) findViewById(R.id.salvaConf);
//        salvaButton.setOnClickListener(this);
//        resetButton = (Button) findViewById(R.id.resetConf);
//        resetButton.setOnClickListener(this);
//        findViewById(R.id.chiudiConf).setOnClickListener(this);
        gioc11Text = (EditText) findViewById(R.id.giocatore11);
        gioc12Text = (EditText) findViewById(R.id.giocatore12);
        gioc21Text = (EditText) findViewById(R.id.giocatore21);
        gioc22Text = (EditText) findViewById(R.id.giocatore22);

        if (savedInstanceState == null) {
            Intent startingIntent = getIntent();
            numberOfPlayerForTeam = startingIntent.getIntExtra(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
            tA = startingIntent.getParcelableExtra(SummaryActivity.teamAKey);
            tB = startingIntent.getParcelableExtra(SummaryActivity.teamBKey);
        }
    }

    private void setDiplayTeamName() {
        if (numberOfPlayerForTeam != 0) {
            gioc11Text.setText(tA.getPlayer1());
            gioc21Text.setText(tB.getPlayer1());
            if (numberOfPlayerForTeam == 2) {
                gioc12Text.setText(tA.getPlayer2());
                gioc22Text.setText(tA.getPlayer2());
            }
            changeDisplayName();
        } else {
            resetNames();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveDisplayedName();
        outState.putInt(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(SummaryActivity.teamAKey, tA);
        outState.putParcelable(SummaryActivity.teamBKey, tB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(SummaryActivity.numberOfPlayer);
        tA = savedInstanceState.getParcelable(SummaryActivity.teamAKey);
        tB = savedInstanceState.getParcelable(SummaryActivity.teamBKey);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_configuration, menu);
        salvaButton = menu.findItem(R.id.salvaConfMenu);
        resetButton = menu.findItem(R.id.resetConfMenu);

        gioc11Text.addTextChangedListener(this);
        gioc12Text.addTextChangedListener(this);
        gioc21Text.addTextChangedListener(this);
        gioc22Text.addTextChangedListener(this);
        setDiplayTeamName();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                setResult(RESULT_CANCELED);
                finish();
                return true;
            }
            case R.id.salvaConfMenu: {
                completeAndSend();
                return true;
            }
            case R.id.resetConfMenu: {
                resetNames();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeDisplayName() {
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
        if (!salvaButton.isVisible()) {
            salvaButton.setVisible(true);
        }
        if (!resetButton.isVisible()) {
            resetButton.setVisible(true);
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
            if (salvaButton.isVisible()) {
                salvaButton.setVisible(false);
            }
        } else {
            if (!salvaButton.isVisible() && numberOfPlayerForTeam != 0) {
                salvaButton.setVisible(true);
            }
        }
    }
}
