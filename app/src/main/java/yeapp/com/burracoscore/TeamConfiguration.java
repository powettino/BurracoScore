package yeapp.com.burracoscore;

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
import android.widget.TextView;

import static android.view.View.OnClickListener;

public class TeamConfiguration extends Activity implements OnClickListener, TextWatcher {
    private Button salvaButton;
    private Button resetButton;
    private EditText gioc11Name;
    private EditText gioc12Name;
    private EditText gioc21Name;
    private EditText gioc22Name;
    private TextView team1Label;
    private TextView team2Label;
    int numberOfPlayerForTeam = 0;

//    final String varGioc12State = "gioc12Name";
//    final String varGioc22State = "gioc22";
    //    final String varStartState = "startEnable";
//    final String varGioc11Text = "gioc11Text";
//    final String varGioc12Text = "gioc12Text";
//    final String varGioc21Text = "gioc21Text";
//    final String varGioc22Text = "gioc22Text";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.resetConf): {
                numberOfPlayerForTeam = 0;
                changePlayers();
                break;
            }
            case R.id.singleConf: {
                numberOfPlayerForTeam = 1;
                changePlayers();
                break;
            }
            case R.id.coupleConf: {
                numberOfPlayerForTeam = 2;
                changePlayers();
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

    private void salvaNomi() {
        setResult(RESULT_OK, getIntent()
                .putExtra(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam)
                .putExtra(getString(R.string.nomeGiocatore11), gioc11Name.getText().toString())
                .putExtra(getString(R.string.nomeGiocatore12), numberOfPlayerForTeam == 2 ? gioc12Name.getText().toString() : null)
                .putExtra(getString(R.string.nomeGiocatore21), gioc21Name.getText().toString())
                .putExtra(getString(R.string.nomeGiocatore22), numberOfPlayerForTeam == 2 ? gioc22Name.getText().toString() : null));
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
        gioc11Name = (EditText) findViewById(R.id.giocatore11);
        gioc12Name = (EditText) findViewById(R.id.giocatore12);
        gioc21Name = (EditText) findViewById(R.id.giocatore21);
        gioc22Name = (EditText) findViewById(R.id.giocatore22);
        team1Label = (TextView) findViewById(R.id.teamAConf);
        team2Label = (TextView) findViewById(R.id.teamBConf);

        gioc11Name.addTextChangedListener(this);
        gioc12Name.addTextChangedListener(this);
        gioc21Name.addTextChangedListener(this);
        gioc22Name.addTextChangedListener(this);
        restoreFromMain();
    }

    private void restoreFromMain() {
        Intent startingIntent = getIntent();
        numberOfPlayerForTeam = startingIntent.getIntExtra(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam);
        if (numberOfPlayerForTeam != 0) {
            gioc11Name.setText(startingIntent.getStringExtra(getString(R.string.nomeGiocatore11)));
            gioc21Name.setText(startingIntent.getStringExtra(getString(R.string.nomeGiocatore21)));
            if (numberOfPlayerForTeam == 2) {
                gioc12Name.setText(startingIntent.getStringExtra(getString(R.string.nomeGiocatore12)));
                gioc22Name.setText(startingIntent.getStringExtra(getString(R.string.nomeGiocatore22)));
            }
            changePlayers();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam);

//        outState.putBoolean(varGioc12State, gioc12Name.isEnabled());
//        outState.putBoolean(varGioc22State, gioc22.isEnabled());

//        outState.putBoolean(varStartState, start.isEnabled());
//        outState.putBoolean(varGioc12State, gioc12Name.isEnabled());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(getString(R.string.numberPlayerActivity));
//        changePlayers(numberOfPlayerForTeam);
//        gioc12Name.setEnabled(true);
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
//        switch (item.getItemId()) {
//
//            case (R.id.chiudiMenuConf): {
//                setResult(RESULT_CANCELED);
//                finish();
//                return true;
//            }
//            case R.id.salvaConf: {
//                salvaNomi();
//            }
//            default: {
        return super.onOptionsItemSelected(item);
//            }
//        }
    }

    private void changePlayers() {
        if (numberOfPlayerForTeam > 0) {
            if (gioc11Name.getText().length() == 0) {
                gioc11Name.setText(R.string.nomeGiocatore11);
            }
            if (gioc12Name.getText().length() == 0) {
                gioc12Name.setText(R.string.nomeGiocatore12);
            }
            if (gioc21Name.getText().length() == 0) {
                gioc21Name.setText(R.string.nomeGiocatore21);
            }
            if (gioc22Name.getText().length() == 0) {
                gioc22Name.setText(R.string.nomeGiocatore22);
            }
            if (!team1Label.isEnabled() && !team2Label.isEnabled()) {
                team2Label.setEnabled(true);
                team1Label.setEnabled(true);
            }
            if (!salvaButton.isEnabled()) {
                salvaButton.setEnabled(true);
            }
            if (!resetButton.isEnabled()) {
                resetButton.setEnabled(true);
            }
            if (!gioc11Name.isEnabled() && !gioc21Name.isEnabled()) {
                gioc11Name.setEnabled(true);
                gioc21Name.setEnabled(true);
            }
            if (numberOfPlayerForTeam == 1 && gioc12Name.isEnabled() && gioc22Name.isEnabled()) {
                gioc12Name.setEnabled(false);
                gioc22Name.setEnabled(false);
            }
            if (numberOfPlayerForTeam == 2 && !gioc12Name.isEnabled() && !gioc22Name.isEnabled()) {
                gioc12Name.setEnabled(true);
                gioc22Name.setEnabled(true);
            }
        } else {
            gioc11Name.setText(R.string.nomeGiocatore11);
            gioc12Name.setText(R.string.nomeGiocatore12);
            gioc21Name.setText(R.string.nomeGiocatore21);
            gioc22Name.setText(R.string.nomeGiocatore22);
//            salvaButton.setEnabled(false);
//            resetButton.setEnabled(false);
//            gioc11Name.setEnabled(false);
//            gioc21Name.setEnabled(false);
//            team1Label.setEnabled(false);
//            team2Label.setEnabled(false);
//            if (gioc12Name.isEnabled() && gioc22.isEnabled()) {
//                gioc12Name.setEnabled(false);
//                gioc22.setEnabled(false);

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
