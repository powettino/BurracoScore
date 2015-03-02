package yeapp.com.burracoscore.fragment;

import android.app.Activity;
import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.AddPointcontainer;
import yeapp.com.burracoscore.adapter.ListPointAdapter;
import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.core.model.Team;

public class SummaryFragment extends Fragment implements View.OnClickListener {

    public static final int CODE_FOR_SET = 1;
    private static final int maxPoint = 500;
    public static final String teamAKey = "teamA";
    public static final String teamBKey = "teamB";
    public static final String numberOfPlayer = "numberOfPlayer";
    public static final String handA = "handA";
    public static final String handB = "handB";

    public static final String addHand = "addHand";

    private TextView teamAText;
    private TextView teamBText;

    private Team teamA;
    private Team teamB;

    private int numberOfPlayerForTeam = 0;

    ListPointAdapter dtaLVA = null;
    ListPointAdapter dtaLVB = null;

    ListView listA = null;
    ListView listB = null;

    private TextView resultA;
    private TextView resultB;


    private FloatingActionButton addButton;

    OnScoreChanging changingCB;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (teamA == null) {
            teamA = new Team("A");
        }
        if (teamB == null) {
            teamB = new Team("B");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changingCB = (OnScoreChanging)activity;
        dtaLVA = new ListPointAdapter(activity, true);
        dtaLVB = new ListPointAdapter(activity, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary, container, false);
        teamAText = (TextView) view.findViewById(R.id.teamASummary);
        teamBText = (TextView) view.findViewById(R.id.teamBSummary);

        listA = (ListView) view.findViewById(R.id.listPointTeamA);
        listB = (ListView) view.findViewById(R.id.listPointTeamB);
        listA.setAdapter(dtaLVA);
        listB.setAdapter(dtaLVB);
        listA.setEmptyView(view.findViewById(R.id.empty));
        listB.setEmptyView(view.findViewById(R.id.empty2));

        addButton = (FloatingActionButton) view.findViewById(R.id.addHand);
        addButton.setOnClickListener(this);

        resultA = (TextView) view.findViewById(R.id.resultA);
        resultB = (TextView) view.findViewById(R.id.resultB);
        return view;
    }

    public void createTeamName() {
        String p11 = teamA.getPlayer1();
        String p12 = teamA.getPlayer2();
        String p21 = teamB.getPlayer1();
        String p22 = teamB.getPlayer2();
        if (p11 == null && p21 == null) {
            teamAText.setText(R.string.teamAName);
            teamBText.setText(R.string.teamBName);
        } else {
            assert p11 != null;
            teamAText.setText(p11.substring(0, ((p11.length() >= 3) ? 3 : p11.length())) + (numberOfPlayerForTeam == 2 ? "∞" + p12.substring(0, (p12.length() >= 3 ? 3 : p12.length())) : ""));
            teamBText.setText(p21.substring(0, (p21.length() >= 3 ? 3 : p21.length())) + (numberOfPlayerForTeam == 2 ? "∞" + p22.substring(0, (p22.length() >= 3 ? 3 : p22.length())) : ""));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            createTeamName();
            dtaLVA.restore(teamA.getPunti(), teamA.getStatus());
            dtaLVB.restore(teamB.getPunti(), teamB.getStatus());
            dtaLVA.notifyDataSetChanged();
            dtaLVB.notifyDataSetChanged();
            resultA.setText(teamA.getTotale() == 0 ? "" : String.valueOf(teamA.getTotale()));
            resultB.setText(teamB.getTotale() == 0 ? "" : String.valueOf(teamB.getTotale()));
            addButton.setEnabled(savedInstanceState.getBoolean(addHand));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(teamAKey, teamA);
        outState.putParcelable(teamBKey, teamB);
        outState.putBoolean(addHand, addButton.isEnabled());
    }

    private void checkWinner() {
        int totA = teamA.getTotale();
        int totB = teamB.getTotale();
//            int winA = resultA.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultA.getText().toString());
//            int winB = resultB.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultB.getText().toString());
        if (totA >= maxPoint || totB >= maxPoint) {
            if (totA != totB) {
                if (totA > totB) {
                    teamA.addGame();
                } else if (totB > totA) {
                    teamB.addGame();
                }
                changingCB.scoreChanged(teamA.getTotGames(), teamB.getTotGames());
            }
        }
    }

    public void resetGames() {
        teamA.cleanPunteggio();
        teamB.cleanPunteggio();
        dtaLVA.clearData();
        dtaLVA.notifyDataSetChanged();
        dtaLVB.clearData();
        dtaLVB.notifyDataSetChanged();
        resultA.setText("");
        resultB.setText("");
        resultB.setBackgroundResource(R.color.SfondoOmbre);
        resultA.setBackgroundResource(R.color.SfondoOmbre);
        addButton.setEnabled(true);
        changingCB.scoreChanged(teamA.getTotGames(), teamB.getTotGames());
    }

    public void resetAll() {
        resetGames();
        teamB.cleanTeam();
        teamA.cleanTeam();
        teamA.cleanGames();
        teamB.cleanGames();
        numberOfPlayerForTeam = 0;
        teamAText.setText(R.string.teamAName);
        teamBText.setText(R.string.teamBName);
        changingCB.scoreChanged(teamA.getTotGames(), teamB.getTotGames());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.addHand): {

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.bouncing_button);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent add = new Intent(getActivity(), AddPointcontainer.class);
                        startActivityForResult(add, CODE_FOR_SET);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(anim);
                break;
            }
            default: {
                break;
            }
        }
    }

    public int getNumberOfPlayerForTeam() {
        return numberOfPlayerForTeam;
    }

    public void setNumberOfPlayerForTeam(int numberOfPlayerForTeam) {
        this.numberOfPlayerForTeam = numberOfPlayerForTeam;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_FOR_SET: {
                if (resultCode == FragmentActivity.RESULT_OK) {
                    Hand manoA = data.getParcelableExtra(handA);
                    Hand manoB = data.getParcelableExtra(handB);
                    teamA.addMano(manoA);
                    resultA.setText(String.valueOf(teamA.getTotale()));
                    dtaLVA.addLastDoubleText(String.valueOf(manoA.getTotaleMano()), manoA.getWon());
                    dtaLVA.notifyDataSetChanged();
                    teamB.addMano(manoB);
                    resultB.setText(String.valueOf(teamB.getTotale()));
                    dtaLVB.addLastDoubleText(String.valueOf(manoB.getTotaleMano()), manoB.getWon());
                    dtaLVB.notifyDataSetChanged();
                    resultB.setBackgroundResource(R.color.SfondoMedio);
                    resultA.setBackgroundResource(R.color.SfondoMedio);
                    checkWinner();
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    public void setEnableAdd(boolean enable){
        addButton.setEnabled(enable);
    }

    public interface OnScoreChanging {
        public void scoreChanged(int puntiA, int puntiB);
    }
}
