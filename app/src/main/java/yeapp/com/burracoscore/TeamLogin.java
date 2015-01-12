package yeapp.com.burracoscore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import static android.view.View.OnClickListener;
import static android.view.View.OnFocusChangeListener;


public class TeamLogin extends ActionBarActivity implements OnClickListener {
    Button single;
    Button couple;
    GridLayout gridNames;
    Button start;
    EditText gioc11;
    EditText gioc12;
    EditText gioc21;
    EditText gioc22;

    int numberOfPlayerForTeam = 0;

    //    final String varGridState = "gridEnable";
//    final String varGioc12State = "gioc12";
//    final String varGioc22State = "gioc22";
    final String varNumberPlayer = "numberOfPlayer";
//    final String varStartState = "startEnable";
//    final String varGioc11Text = "gioc11Text";
//    final String varGioc12Text = "gioc12Text";
//    final String varGioc21Text = "gioc21Text";
//    final String varGioc22Text = "gioc22Text";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single: {
                numberOfPlayerForTeam = 1;
                setGridPanel(numberOfPlayerForTeam);
//                name11.requestFocus();
//                InputMethodManager ims = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                ims.showSoftInput(name11, InputMethodManager.SHOW_IMPLICIT);
                break;
            }
            case R.id.couple: {
                numberOfPlayerForTeam = 2;
                setGridPanel(numberOfPlayerForTeam);
//                name11.requestFocus();
//                InputMethodManager ims = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                ims.showSoftInput(name11, InputMethodManager.SHOW_IMPLICIT);
                break;
            }
            case R.id.start: {
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
        single = (Button) findViewById(R.id.single);
        single.setOnClickListener(this);
        couple = (Button) findViewById(R.id.couple);
        couple.setOnClickListener(this);
        gridNames = (GridLayout) findViewById(R.id.gridNames);
//        gridNames.setEnabled(false);
        start = (Button) findViewById(R.id.start);
        gioc11 = (EditText) findViewById(R.id.giocatore11);
        gioc12 = (EditText) findViewById(R.id.giocatore12);
        gioc21 = (EditText) findViewById(R.id.giocatore21);
        gioc22 = (EditText) findViewById(R.id.giocatore22);

        OnFocusChangeListener textChecker = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if ((gioc11.isEnabled() && (gioc11.getText().toString().trim().length() != 0)) &&
                        (gioc12.isEnabled() && (gioc12.getText().toString().trim().length() != 0)) &&
                        (gioc21.isEnabled() && (gioc21.getText().toString().trim().length() != 0)) &&
                        (gioc22.isEnabled() && (gioc22.getText().toString().trim().length() != 0))) {
                    start.setEnabled(true);
                } else {
                    if (start.isEnabled()) {
                        start.setEnabled(false);
                    }
                }
            }
        };
        gioc11.setOnFocusChangeListener(textChecker);
        gioc12.setOnFocusChangeListener(textChecker);
        gioc21.setOnFocusChangeListener(textChecker);
        gioc22.setOnFocusChangeListener(textChecker);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(varNumberPlayer, numberOfPlayerForTeam);
/*
outState.putBoolean(varGridState, gridNames.isEnabled());
outState.putBoolean(varGioc12State, gioc12.isEnabled());
outState.putBoolean(varGioc22State, gioc22.isEnabled());
*/
//        outState.putBoolean(varStartState, start.isEnabled());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(varNumberPlayer);
        setGridPanel(numberOfPlayerForTeam);
//        gioc12.setEnabled(savedInstanceState.getBoolean(varGioc12State));
//        gioc22.setEnabled(savedInstanceState.getBoolean(varGioc22State));
//        gridNames.setEnabled(savedInstanceState.getBoolean(varGridState));
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.resetTeam) {
            gioc11.setText("");
            gioc12.setText("");
            gioc21.setText("");
            gioc22.setText("");
            numberOfPlayerForTeam = 0;
            setGridPanel(numberOfPlayerForTeam);
            if (start.isEnabled())
                start.setEnabled(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setGridPanel(int numberPlayer) {
        if (numberPlayer == 0) {
            changeSecondPlayers(true);
            if (gridNames.isEnabled()) {
                gridNames.setEnabled(false);
            }
        } else if (numberPlayer == 1) {
            changeSecondPlayers(false);
            if (!gridNames.isEnabled()) {
                gridNames.setEnabled(true);
            }
        } else if (numberPlayer == 2) {
            changeSecondPlayers(true);
            if (!gridNames.isEnabled()) {
                gridNames.setEnabled(true);
            }
        }
    }

    private void changeSecondPlayers(boolean finalState) {
        if (gioc12.isEnabled() != finalState) {
            gioc12.setEnabled(finalState);
        }
        if (gioc22.isEnabled() != finalState) {
            gioc22.setEnabled(finalState);
        }
    }

}
