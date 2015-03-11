package yeapp.com.burracoscore.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.fragment.TeamNameFragment;
import static android.view.View.OnClickListener;

public class TeamConfiguration extends ActionBarActivity implements OnClickListener, TeamNameFragment.OnChangeToolbarVisibility {

    Toolbar toolbar;
    private MenuItem salvaButton;
    private MenuItem resetButton;
    private int numberOfPlayerForTeam = 1;

    TeamNameFragment fragment1 = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singleConf: {
                if (numberOfPlayerForTeam != 1) {
                    numberOfPlayerForTeam = 1;
                    changeFragment();
                }
                break;
            }
            case R.id.coupleConf: {
                if (numberOfPlayerForTeam != 2) {
                    numberOfPlayerForTeam = 2;
                    changeFragment();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void completeAndSend() {
        fragment1.saveDisplayedName();
        setResult(RESULT_OK, this.getIntent()
                        .putExtra(SummaryContainer.numberOfPlayer, numberOfPlayerForTeam)
                        .putExtra(SummaryContainer.teamAKey, fragment1.getTeamA())
                        .putExtra(SummaryContainer.teamBKey, fragment1.getTeamB())
        );
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_configuration_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conf_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.singleConf).setOnClickListener(this);
        findViewById(R.id.coupleConf).setOnClickListener(this);

        if (savedInstanceState == null) {
            Intent startingIntent = getIntent();
            numberOfPlayerForTeam = startingIntent.getIntExtra(SummaryContainer.numberOfPlayer, numberOfPlayerForTeam);

            fragment1 = new TeamNameFragment();
            fragment1.setArguments(startingIntent.getExtras());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentCont, fragment1, Integer.toString(numberOfPlayerForTeam));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SummaryContainer.numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(SummaryContainer.teamAKey, fragment1.getTeamA());
        outState.putParcelable(SummaryContainer.teamBKey, fragment1.getTeamB());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfPlayerForTeam = savedInstanceState.getInt(SummaryContainer.numberOfPlayer);
        fragment1 = (TeamNameFragment) getFragmentManager().findFragmentByTag(Integer.toString(numberOfPlayerForTeam));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_configuration, menu);
        salvaButton = menu.findItem(R.id.salvaConfMenu);
        resetButton = menu.findItem(R.id.resetConfMenu);
        if (numberOfPlayerForTeam != 0) {
            salvaButton.setVisible(true);
            resetButton.setVisible(true);
        }
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
                fragment1.resetNames();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeFragment() {
        Bundle b = new Bundle();
        b.putInt(SummaryContainer.numberOfPlayer, numberOfPlayerForTeam);
        b.putParcelable(SummaryContainer.teamAKey, fragment1.getTeamA());
        b.putParcelable(SummaryContainer.teamBKey, fragment1.getTeamB());
        fragment1 = new TeamNameFragment();
        fragment1.setArguments(b);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (2 == numberOfPlayerForTeam) {
            ft.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        } else {
            ft.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out);
        }
        ft.replace(R.id.fragmentCont, fragment1, Integer.toString(numberOfPlayerForTeam));
        ft.commit();
        salvaButton.setVisible(true);
        resetButton.setVisible(true);
    }

    @Override
    public void changedToolbarVisibility(boolean visible) {
        if (salvaButton != null) {
            if (visible && !salvaButton.isVisible()) {
                salvaButton.setVisible(true);
            }
            if (!visible && salvaButton.isVisible()) {
                salvaButton.setVisible(false);
            }
        }
    }
}
