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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.utils.Constants;
import yeapp.com.burracoscore.utils.Utils;
import yeapp.com.burracoscore.fragment.TeamSliderFragment;

public class TeamSliderContainer extends ActionBarActivity implements View.OnClickListener, TeamSliderFragment.OnTeamFragmentChanger, AdapterView.OnItemSelectedListener {

    private MenuItem salvaButton;
    private char currentTeam = Utils.ASide;

    private TeamSliderFragment fragment = null;

    private Team tA;
    private Team tB;

    private View goLeft;
    private View goRight;

    private ArrayList<Integer> idResEmpty = new ArrayList<Integer>();
    Spinner spinnerTeam;

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
        fragment.saveTeamConfiguration();
        setResult(RESULT_OK, this.getIntent()
                        .putExtra(Constants.teamAKey, tA)
                        .putExtra(Constants.teamBKey, tB)
        );
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_configuration_container_slider);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.conf_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.choosePlayer, R.layout.support_simple_spinner_dropdown_item);
        spinnerTeam = (Spinner)findViewById(R.id.spinnerTeam);
        spinnerTeam.setAdapter(arrayAdapter);
        spinnerTeam.setOnItemSelectedListener(this);

        goLeft = findViewById(R.id.goLeft);
        goLeft.setOnClickListener(this);
        goRight = findViewById(R.id.goRight);
        goRight.setOnClickListener(this);

        if (savedInstanceState == null) {
            goLeft.setEnabled(false);
            Intent startingIntent = getIntent();
            int tempNumberPlayer = startingIntent.getIntExtra(Constants.numberOfPlayer, 1);
            tA = startingIntent.getParcelableExtra(Constants.teamAKey);
            tB = startingIntent.getParcelableExtra(Constants.teamBKey);
            tA.setNumberPlayer(tempNumberPlayer);
            tB.setNumberPlayer(tempNumberPlayer);
            Bundle ba = new Bundle();
            ba.putParcelable(Constants.genericTeamKey, tA);
            fragment = new TeamSliderFragment();
            fragment.setArguments(ba);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentCont, fragment, Constants.fragmentKey);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
            spinnerTeam.setSelection(tempNumberPlayer-1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putChar(Constants.currentTeamFragment, currentTeam);
        outState.putParcelable(Constants.teamAKey, tA);
        outState.putParcelable(Constants.teamBKey, tB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTeam = savedInstanceState.getChar(Constants.currentTeamFragment);
        tA = savedInstanceState.getParcelable(Constants.teamAKey);
        tB = savedInstanceState.getParcelable(Constants.teamBKey);
        fragment = (TeamSliderFragment) getFragmentManager().findFragmentByTag(Constants.fragmentKey);
        if(currentTeam == Utils.ASide){
            goLeft.setEnabled(false);
            goRight.setEnabled(true);
        }else{
            goRight.setEnabled(false);
            goLeft.setEnabled(true);
        }
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
                fragment.resetNames();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void changeFragment() {
        fragment.saveTeamConfiguration();
        Bundle b = new Bundle();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragment = new TeamSliderFragment();
        if(currentTeam == Utils.ASide){
            currentTeam = Utils.BSide;
            goLeft.setEnabled(true);
            goRight.setEnabled(false);
            b.putParcelable(Constants.genericTeamKey, tB);
            fragment.setArguments(b);
            ft.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out);
            ft.replace(R.id.fragmentCont, fragment, Constants.fragmentKey);
        }else{
            currentTeam = Utils.ASide;
            goLeft.setEnabled(false);
            goRight.setEnabled(true);
            b.putParcelable(Constants.genericTeamKey, tA);
            fragment.setArguments(b);
            ft.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out);
            ft.replace(R.id.fragmentCont, fragment, Constants.fragmentKey);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(tA.getNumberPlayer()!=position+1) {
            tA.setNumberPlayer(position+1);
            tB.setNumberPlayer(position+1);
            fragment.animatePlayerName();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
