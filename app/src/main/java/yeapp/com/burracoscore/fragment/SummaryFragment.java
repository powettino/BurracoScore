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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.melnykov.fab.FloatingActionButton;
import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.AddPointsContainer;
import yeapp.com.burracoscore.activity.SummaryContainer;
import yeapp.com.burracoscore.adapter.ListPointAdapter;
import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.yeapp.com.burracoscore.utils.Utils;

public class SummaryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView teamAText;
    private TextView teamBText;

    private Team teamA;
    private Team teamB;

    private ListPointAdapter dtaLVA = null;
    private ListPointAdapter dtaLVB = null;

    private TextView resultA;
    private TextView resultB;

    private FloatingActionButton addButton;

    private OnScoreChanging changingCB;

    public static final String gameNumber = "gameNumber";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            if (teamA == null) {
                teamA = new Team('A');
            }
            if (teamB == null) {
                teamB = new Team('B');
            }
        }else{
            teamA = savedInstanceState.getParcelable(SummaryContainer.teamAKey);
            teamB = savedInstanceState.getParcelable(SummaryContainer.teamBKey);
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
        ListView listA = (ListView) view.findViewById(R.id.listPointTeamA);
        ListView listB = (ListView) view.findViewById(R.id.listPointTeamB);
        listA.setAdapter(dtaLVA);
        listB.setAdapter(dtaLVB);
        listA.setEmptyView(view.findViewById(R.id.empty));
        listB.setEmptyView(view.findViewById(R.id.empty2));
        listA.setOnItemClickListener(this);
        listB.setOnItemClickListener(this);

        addButton = (FloatingActionButton) view.findViewById(R.id.addHand);
        addButton.setOnClickListener(this);

        resultA = (TextView) view.findViewById(R.id.resultA);
        resultB = (TextView) view.findViewById(R.id.resultB);
        teamAText.setText(Utils.formattedString(teamA.getAlias()));
        teamBText.setText(Utils.formattedString(teamB.getAlias()));
        if(savedInstanceState!=null) {
            addButton.setEnabled(savedInstanceState.getBoolean(SummaryContainer.addHand));
            dtaLVA.restore(teamA.getPunti(), teamA.getStatus());
            dtaLVB.restore(teamB.getPunti(), teamB.getStatus());
            dtaLVA.notifyDataSetChanged();
            dtaLVB.notifyDataSetChanged();
            resultA.setText(teamA.getTotale() == 0 ? "" : String.valueOf(teamA.getTotale()));
            resultB.setText(teamB.getTotale() == 0 ? "" : String.valueOf(teamB.getTotale()));
            if(teamA.getTotale() != 0) {
                resultB.setBackgroundResource(R.color.SfondoMedio);
                resultA.setBackgroundResource(R.color.SfondoMedio);
            }
            addButton.setEnabled(savedInstanceState.getBoolean(SummaryContainer.addHand));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SummaryContainer.teamAKey, teamA);
        outState.putParcelable(SummaryContainer.teamBKey, teamB);
        outState.putBoolean(SummaryContainer.addHand, addButton.isEnabled());
    }

    private void checkWinner() {
        int totA = teamA.getTotale();
        int totB = teamB.getTotale();
//            int winA = resultA.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultA.getText().toString());
//            int winB = resultB.getText().toString().isEmpty() ? 0 : Integer.valueOf(resultB.getText().toString());
        if (totA >= SummaryContainer.maxPoint || totB >= SummaryContainer.maxPoint) {
            if (totA != totB) {
                if (totA > totB) {
                    teamA.addGame();
                } else if (totB > totA) {
                    teamB.addGame();
                }
                changingCB.scoreChanged(teamA.getTotGames(), teamB.getTotGames(), true);
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
        changingCB.scoreChanged(teamA.getTotGames(), teamB.getTotGames(), false);
    }

    public void resetAll() {
        resetGames();
        teamA.clean();
        teamB.clean();
        teamAText.setText(Utils.formattedString(teamA.getAlias()));
        teamBText.setText(Utils.formattedString(teamB.getAlias()));
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
                        Intent add = new Intent(getActivity(), AddPointsContainer.class);
                        add.putExtra(gameNumber, dtaLVA.getCount()+1);
                        startActivityForResult(add, SummaryContainer.CODE_FOR_SET);
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

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
        teamAText.setText(Utils.formattedString(teamA.getAlias()));
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
        teamBText.setText(Utils.formattedString(teamB.getAlias()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SummaryContainer.CODE_FOR_SET: {
                if (resultCode == FragmentActivity.RESULT_OK) {
                    Hand manoA = data.getParcelableExtra(SummaryContainer.handA);
                    Hand manoB = data.getParcelableExtra(SummaryContainer.handB);
                    teamA.addMano(manoA);
                    teamB.addMano(manoB);
                    resultA.setText(String.valueOf(teamA.getTotale()));
                    resultB.setText(String.valueOf(teamB.getTotale()));
                    dtaLVA.addLastDoubleText(String.valueOf(manoA.getTotaleMano()), manoA.getWon());
                    dtaLVB.addLastDoubleText(String.valueOf(manoB.getTotaleMano()), manoB.getWon());
                    resultB.setBackgroundResource(R.color.SfondoMedio);
                    resultA.setBackgroundResource(R.color.SfondoMedio);
                    dtaLVA.notifyDataSetChanged();
                    dtaLVA.notifyDataSetChanged();
                    dtaLVB.notifyDataSetChanged();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent add = new Intent(getActivity(), AddPointsContainer.class);
        add.putExtra(gameNumber, position+1);
        add.putExtra(SummaryContainer.handA, teamA.getMano(position))
                .putExtra(SummaryContainer.handB, teamB.getMano(position));
        startActivity(add);
    }

    public interface OnScoreChanging {
        public void scoreChanged(int puntiA, int puntiB, boolean won);
    }
}
