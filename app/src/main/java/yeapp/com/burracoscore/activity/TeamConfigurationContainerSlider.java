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

import java.util.ArrayList;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.yeapp.com.burracoscore.utils.Utils;
import yeapp.com.burracoscore.fragment.TeamNameSliderFragment;

import static android.view.View.OnClickListener;

public class TeamConfigurationContainerSlider extends ActionBarActivity implements OnClickListener, TeamNameSliderFragment.OnTeamFragmentChanger {

    private MenuItem salvaButton;
    private int tempNumberPlayer = 0;
    private char currentTeam = Utils.ASide;
    public static final String teamKey = "teamName";
    public static final String currentTeamKey = "currentTeamFragment";

    private TeamNameSliderFragment fragmentA = null;
    private TeamNameSliderFragment fragmentB = null;

    private Team tA;
    private Team tB;

    private View goLeft;
    private View goRight;

    private ArrayList<Integer> idResEmpty = new ArrayList<Integer>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goRight: {
                changeFragment();
                break;
            }
            case R.id.goLeft: {
                changeFragment();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void completeAndSend() {
        if(currentTeam == Utils.ASide) {
            fragmentA.saveTeamConfiguration();
        }else {
            fragmentB.saveTeamConfiguration();
        }
        tA.setNumberPlayer(tempNumberPlayer);
        tB.setNumberPlayer(tempNumberPlayer);
        setResult(RESULT_OK, this.getIntent()
//                        .putExtra(SummaryContainer.numberOfPlayer, tempNumberPlayer)
                        .putExtra(SummaryContainer.teamAKey, tA)
                        .putExtra(SummaryContainer.teamBKey, tB)
        );
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_configuration_container_slider);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.conf_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        goLeft = findViewById(R.id.goLeft);
        goLeft.setOnClickListener(this);
        goRight = findViewById(R.id.goRight);
        goRight.setOnClickListener(this);
        goLeft.setEnabled(false);

        if (savedInstanceState == null) {
            Intent startingIntent = getIntent();
            tempNumberPlayer = startingIntent.getIntExtra(SummaryContainer.numberOfPlayer, tempNumberPlayer);
            tA = startingIntent.getParcelableExtra(SummaryContainer.teamAKey);
            tB = startingIntent.getParcelableExtra(SummaryContainer.teamBKey);
            Bundle b = new Bundle();
            b.putInt(SummaryContainer.numberOfPlayer, tempNumberPlayer);
            b.putParcelable(teamKey, tA);
            fragmentA = new TeamNameSliderFragment();
            fragmentA.setArguments(b);
            fragmentB = new TeamNameSliderFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentCont, fragmentA, String.valueOf(currentTeam));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putChar(currentTeamKey, currentTeam);
        outState.putInt(SummaryContainer.numberOfPlayer, tempNumberPlayer);
        outState.putParcelable(SummaryContainer.teamAKey, tA);
        outState.putParcelable(SummaryContainer.teamBKey, tB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTeam = savedInstanceState.getChar(currentTeamKey);
        tempNumberPlayer = savedInstanceState.getInt(SummaryContainer.numberOfPlayer);
        tA = savedInstanceState.getParcelable(SummaryContainer.teamAKey);
        tB = savedInstanceState.getParcelable(SummaryContainer.teamBKey);
//        fragmentA = (TeamNameSliderFragment) getFragmentManager().findFragmentByTag(Integer.toString(tempNumberPlayer));
//        fragmentB = (TeamNameSliderFragment) getFragmentManager().findFragmentByTag(Integer.toString(tempNumberPlayer));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_configuration, menu);
        salvaButton = menu.findItem(R.id.salvaConfMenu);
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
                if(currentTeam == Utils.ASide){
                    fragmentA.resetNames();
                }else{
                    fragmentB.resetNames();
                }
                return true;
            }
//            case R.id.clearConfMenu: {
//                fragmentA.resetNames();
//                fragmentB.resetNames();
//                idResEmpty.clear();
//                salvaButton.setVisible(true);
//            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(currentTeam == Utils.ASide){
            fragmentA.saveTeamConfiguration();
            goLeft.setEnabled(true);
            goRight.setEnabled(false);
            currentTeam = Utils.BSide;
            Bundle b = new Bundle();
            b.putInt(SummaryContainer.numberOfPlayer, tempNumberPlayer);
            b.putParcelable(teamKey, tB);
            fragmentB.setArguments(b);
            ft.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
            ft.replace(R.id.fragmentCont, fragmentB, Integer.toString(Utils.BSide));
        }else{
            fragmentB.saveTeamConfiguration();
            currentTeam = Utils.ASide;
            goLeft.setEnabled(false);
            goRight.setEnabled(true);
            Bundle b = new Bundle();
            b.putInt(SummaryContainer.numberOfPlayer, tempNumberPlayer);
            b.putParcelable(teamKey, tA);
            fragmentA.setArguments(b);
            ft.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out);
            ft.replace(R.id.fragmentCont, fragmentA, Integer.toString(Utils.ASide));
        }
        ft.commit();
    }

    @Override
    public void changedToolbarVisibility(boolean visible, int idRes) {
        if (visible) {
            if(idResEmpty.contains(idRes)) {
                idResEmpty.remove(Integer.valueOf(idRes));
            }
            if (idResEmpty.isEmpty() && !salvaButton.isVisible()) {
                salvaButton.setVisible(true);
            }
        } else {
            if(!idResEmpty.contains(idRes)) {
                idResEmpty.add(idRes);
            }
            if(salvaButton.isVisible()){
                salvaButton.setVisible(false);
            }
        }
    }

    @Override
    public void savedTeam(Team teamSaved) {
        if(teamSaved.getSide() == Utils.ASide){
            tA = teamSaved;
        }else{
            tB = teamSaved;
        }
    }
}
