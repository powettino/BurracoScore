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
import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.core.model.Hand2;
import yeapp.com.burracoscore.utils.Constants;
import yeapp.com.burracoscore.utils.Utils;

public class SummaryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView teamAText;
    private TextView teamBText;

    private Game currentGame;

    private ListPointAdapter dtaLVA = null;
    private ListPointAdapter dtaLVB = null;

    private TextView resultA;
    private TextView resultB;

    private FloatingActionButton addButton;

    private OnScoreChanging changingCB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            currentGame = getArguments().getParcelable(Constants.currentGame);
        }else{
            currentGame = savedInstanceState.getParcelable(Constants.currentGame);
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
        teamAText.setText(Utils.formattedString(getArguments().getString(Constants.teamAAlias)));
        teamBText.setText(Utils.formattedString(getArguments().getString(Constants.teamBAlias)));
        if(savedInstanceState!=null) {
            addButton.setEnabled(savedInstanceState.getBoolean(Constants.addManoButtonStatus));
            setUIStatus();
            addButton.setEnabled(savedInstanceState.getBoolean(Constants.addManoButtonStatus));
        }
        if( currentGame.getNumeroMani()!=0){
            resultB.setBackgroundResource(R.color.SfondoMedio);
            resultA.setBackgroundResource(R.color.SfondoMedio);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.currentGame, currentGame);
        outState.putBoolean(Constants.addManoButtonStatus, addButton.isEnabled());
    }

    private void checkWinner() {
        int totA = currentGame.getTotalePartita(Utils.ASide);
        int totB = currentGame.getTotalePartita(Utils.BSide);
        if (totA >= Constants.maxPoint || totB >= Constants.maxPoint) {
            if (totA != totB) {
                if (totA > totB) {
                    currentGame.setWinner(Utils.ASide);
                } else if (totB > totA) {
                    currentGame.setWinner(Utils.BSide);
                }
                changingCB.gameEnded(currentGame);
            }
        }
    }

    private void setUIStatus(){
        dtaLVA.restore(currentGame.getPuntiMani(Utils.ASide), currentGame.getStatusMani(Utils.ASide));
        dtaLVB.restore(currentGame.getPuntiMani(Utils.BSide), currentGame.getStatusMani(Utils.BSide));
        dtaLVA.notifyDataSetChanged();
        dtaLVB.notifyDataSetChanged();
        int tempTot = currentGame.getTotalePartita(Utils.ASide);
        resultA.setText(tempTot == 0 ? "" : String.valueOf(tempTot));
        tempTot = currentGame.getTotalePartita(Utils.BSide);
        resultB.setText(tempTot == 0 ? "" : String.valueOf(tempTot));
    }

    public void resetGames(Game cleaned) {
        currentGame = cleaned;
        setUIStatus();
        resultB.setBackgroundResource(R.color.SfondoOmbre);
        resultA.setBackgroundResource(R.color.SfondoOmbre);
        addButton.setEnabled(true);
    }

    public void resetAll(Game cleaned, String aliasA, String aliasB) {
        resetGames(cleaned);
        updateTeamAlias(Utils.formattedString(aliasA),Utils.formattedString(aliasB));
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
                        add.putExtra(Constants.numeroMano, currentGame.getNumeroMani()+1);
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

    public void updateTeamAlias(String aliasA, String aliasB){
        teamAText.setText(aliasA);
        teamBText.setText(aliasB);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SummaryContainer.CODE_FOR_SET: {
                if (resultCode == FragmentActivity.RESULT_OK) {
                    Hand2 manoA = data.getParcelableExtra(Constants.manoA);
                    Hand2 manoB = data.getParcelableExtra(Constants.manoB);
                    currentGame.addMano(manoA, manoB);
                    resultA.setText(String.valueOf(currentGame.getTotalePartita(Utils.ASide)));
                    resultB.setText(String.valueOf(currentGame.getTotalePartita(Utils.BSide)));
                    dtaLVA.addLastDoubleText(String.valueOf(manoA.getTotaleMano()), manoA.getWon());
                    dtaLVB.addLastDoubleText(String.valueOf(manoB.getTotaleMano()), manoB.getWon());
                    if(currentGame.getNumeroMani()==1) {
                        resultB.setBackgroundResource(R.color.SfondoMedio);
                        resultA.setBackgroundResource(R.color.SfondoMedio);
                    }
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
        add.putExtra(Constants.numeroMano, position+1);
        add.putExtra(Constants.manoA, currentGame.getMano(position, Utils.ASide) )
                .putExtra(Constants.manoB, currentGame.getMano(position, Utils.BSide));
        startActivity(add);
    }

    public interface OnScoreChanging {
        public void gameEnded(Game current);
    }
}
