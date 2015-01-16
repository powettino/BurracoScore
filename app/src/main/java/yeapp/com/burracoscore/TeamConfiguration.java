package yeapp.com.burracoscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.view.View.OnClickListener;

public class TeamConfiguration extends Activity implements OnClickListener, TextWatcher {
    private Button salvaButton;
    private Button resetButton;
    private EditText gioc11Name;
    private EditText gioc12Name;
    private EditText gioc21Name;
    private EditText gioc22Name;
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
        gioc11Name.setText(R.string.nomeGiocatore11);
        gioc12Name.setText(R.string.nomeGiocatore12);
        gioc21Name.setText(R.string.nomeGiocatore21);
        gioc22Name.setText(R.string.nomeGiocatore22);
        if (!gioc12Name.isEnabled() && !gioc22Name.isEnabled()) {
            gioc12Name.setEnabled(true);
            gioc12Name.setEnabled(false);
            gioc22Name.setEnabled(true);
            gioc22Name.setEnabled(false);
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
            changeVisualConfigurationByPlayers();
        } else {
            resetNames();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.numberPlayerActivity), numberOfPlayerForTeam);
        outState.putString(getString(R.string.nomeGiocatore11), gioc11Name.getText().toString());
        outState.putString(getString(R.string.nomeGiocatore12), gioc12Name.getText().toString());
        outState.putString(getString(R.string.nomeGiocatore21), gioc21Name.getText().toString());
        outState.putString(getString(R.string.nomeGiocatore22), gioc22Name.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(getString(R.string.numberPlayerActivity));
        if (numberOfPlayerForTeam != 0) {
            gioc11Name.setText(savedInstanceState.getString(getString(R.string.nomeGiocatore11)));
            gioc21Name.setText(savedInstanceState.getString(getString(R.string.nomeGiocatore21)));
            if (numberOfPlayerForTeam == 2) {
                gioc12Name.setText(savedInstanceState.getString(getString(R.string.nomeGiocatore12)));
                gioc22Name.setText(savedInstanceState.getString(getString(R.string.nomeGiocatore22)));
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
