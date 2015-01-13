package yeapp.com.burracoscore;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.OnClickListener;

public class TeamConfiguration extends ActionBarActivity implements OnClickListener, TextWatcher {
    private Button salvaButton;
    private EditText gioc11;
    private EditText gioc12;
    private EditText gioc21;
    private EditText gioc22;
    private TextView team1Label;
    private TextView team2Label;
    int numberOfPlayerForTeam = 0;
    final String varNumberPlayer = "numberOfPlayer";

//    final String varGioc12State = "gioc12";
//    final String varGioc22State = "gioc22";
    //    final String varStartState = "startEnable";
//    final String varGioc11Text = "gioc11Text";
//    final String varGioc12Text = "gioc12Text";
//    final String varGioc21Text = "gioc21Text";
//    final String varGioc22Text = "gioc22Text";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singleConf: {
                numberOfPlayerForTeam = 1;
                changePlayers(numberOfPlayerForTeam);
                break;
            }
            case R.id.coupleConf: {
                numberOfPlayerForTeam = 2;
                changePlayers(numberOfPlayerForTeam);
                break;
            }
            case R.id.salvaConf: {
                salvaNomi();
                break;
            }
            case R.id.chiudiMenuConf: {
                setResult(RESULT_CANCELED);
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void salvaNomi() {
        String teamA = getString(R.string.teamAName);
        String teamB = getString(R.string.teamBName);
        String gioc11Name = gioc11.getText().toString();
        String gioc21Name = gioc21.getText().toString();
        String gioc12Name = gioc12.getText().toString();
        String gioc22Name = gioc22.getText().toString();
        if (numberOfPlayerForTeam == 1) {
            teamA = gioc11Name.substring(gioc11Name.length() - 3);
            teamB = gioc21Name.substring(gioc21Name.length() - 3);
        } else {

            teamA = (gioc11Name.substring(0, 3)) + "&" + gioc12Name.subSequence(0, 3);
            teamB = (gioc21Name.substring(0, 3)) + "&" + gioc22Name.subSequence(0, 3);
        }
        setResult(RESULT_OK, this.getIntent().putExtra(getString(R.string.teamA), teamA).putExtra(getString(R.string.teamB), teamB));
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
        findViewById(R.id.chiudiConf).setOnClickListener(this);
        gioc11 = (EditText) findViewById(R.id.giocatore11);
        gioc12 = (EditText) findViewById(R.id.giocatore12);
        gioc21 = (EditText) findViewById(R.id.giocatore21);
        gioc22 = (EditText) findViewById(R.id.giocatore22);
        team1Label = (TextView) findViewById(R.id.teamAConf);
        team2Label = (TextView) findViewById(R.id.teamBConf);

        gioc11.addTextChangedListener(this);
        gioc12.addTextChangedListener(this);
        gioc21.addTextChangedListener(this);
        gioc22.addTextChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(varNumberPlayer, numberOfPlayerForTeam);

//        outState.putBoolean(varGioc12State, gioc12.isEnabled());
//        outState.putBoolean(varGioc22State, gioc22.isEnabled());

//        outState.putBoolean(varStartState, start.isEnabled());
//        outState.putBoolean(varGioc12State, gioc12.isEnabled());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(varNumberPlayer);
//        changePlayers(numberOfPlayerForTeam);
//        gioc12.setEnabled(true);
//        gioc22.setEnabled(savedInstanceState.getBoolean(varGioc22State));

//        start.setEnabled(savedInstanceState.getBoolean(varStartState));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.resetMenuConf): {
                numberOfPlayerForTeam = 0;
                changePlayers(numberOfPlayerForTeam);
                return true;
            }
            case (R.id.chiudiMenuConf): {
                setResult(RESULT_CANCELED);
                finish();
                return true;
            }
            case R.id.salvaConf: {
                salvaNomi();
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
            if (!team1Label.isEnabled() && !team2Label.isEnabled()) {
                team2Label.setEnabled(true);
                team1Label.setEnabled(true);
            }
            if (!salvaButton.isEnabled()) {
                salvaButton.setEnabled(true);
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
            salvaButton.setEnabled(false);
            gioc11.setEnabled(false);
            gioc21.setEnabled(false);
            team1Label.setEnabled(false);
            team2Label.setEnabled(false);
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
            if (salvaButton.isEnabled())
                salvaButton.setEnabled(false);
        } else {
            if (!salvaButton.isEnabled()) {
                salvaButton.setEnabled(true);
            }
        }
    }
}
