package yeapp.com.burracoscore.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.fragment.TeamNameFragment;

import static android.view.View.OnClickListener;

public class TeamConfiguration extends ActionBarActivity implements OnClickListener, TeamNameFragment.OnChangeToolbarVisibility {

    Toolbar toolbar;
    private MenuItem salvaButton;
    private MenuItem resetButton;

    private int numberOfPlayerForTeam = 0;

//    Team tA = null;
//    Team tB = null;

    TeamNameFragment actualTeam = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singleConf: {
                String oldTeam = Integer.toString(numberOfPlayerForTeam);
                numberOfPlayerForTeam = 1;
                changeDisplayName();
                break;
            }
            case R.id.coupleConf: {
                String oldTeam = Integer.toString(numberOfPlayerForTeam);
                numberOfPlayerForTeam = 2;
                changeDisplayName();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void completeAndSend() {
        TeamNameFragment t = (TeamNameFragment) getFragmentManager().findFragmentByTag(Integer.toString(numberOfPlayerForTeam));
        t.saveDisplayedName();
        setResult(RESULT_OK, this.getIntent()
                        .putExtra(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam)
                        .putExtra(SummaryActivity.teamAKey, actualTeam.getTeamA())
                        .putExtra(SummaryActivity.teamBKey, actualTeam.getTeamB())
        );
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_team_configuration);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conf_name);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(8);

        findViewById(R.id.singleConf).setOnClickListener(this);
        findViewById(R.id.coupleConf).setOnClickListener(this);

        if (savedInstanceState == null) {
            Intent startingIntent = getIntent();
            numberOfPlayerForTeam = startingIntent.getIntExtra(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
//            tA = startingIntent.getParcelableExtra(SummaryActivity.teamAKey);
//            tB = startingIntent.getParcelableExtra(SummaryActivity.teamBKey);

            actualTeam = new TeamNameFragment();
            actualTeam.setArguments(startingIntent.getExtras());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentCont, actualTeam, Integer.toString(numberOfPlayerForTeam));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
//        outState.putParcelable(SummaryActivity.teamAKey, tA);
//        outState.putParcelable(SummaryActivity.teamBKey, tB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(SummaryActivity.numberOfPlayer);
//        tA = savedInstanceState.getParcelable(SummaryActivity.teamAKey);
//        tB = savedInstanceState.getParcelable(SummaryActivity.teamBKey);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_configuration, menu);
        salvaButton = menu.findItem(R.id.salvaConfMenu);
        resetButton = menu.findItem(R.id.resetConfMenu);
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
                actualTeam.resetNames();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeDisplayName() {
//        actualTeam.saveDisplayedName();
        actualTeam = new TeamNameFragment();
//        Bundle b = new Bundle();
//        b.putInt(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
//        b.putParcelable(SummaryActivity.teamAKey, tA);
//        b.putParcelable(SummaryActivity.teamBKey, tB);
//        actualTeam.setArguments(b);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (2 == numberOfPlayerForTeam) {
            ft.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        } else {
            ft.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out);
        }
        ft.replace(R.id.fragmentCont, actualTeam, Integer.toString(numberOfPlayerForTeam));
        ft.commit();
    }


//    @Override
//    public void onDataPass(Bundle data) {
//        numberOfPlayerForTeam = data.getInt(SummaryActivity.numberOfPlayer);
//        tA = data.getParcelable(SummaryActivity.teamAKey);
//        tB = data.getParcelable(SummaryActivity.teamBKey);
//    }

    @Override
    public void OnChangeToolbarVisibility(boolean visible) {
        if (visible && !salvaButton.isVisible()) {
            salvaButton.setVisible(true);
        }
        if (!visible && salvaButton.isVisible()) {
            salvaButton.setVisible(false);
        }
    }
}
