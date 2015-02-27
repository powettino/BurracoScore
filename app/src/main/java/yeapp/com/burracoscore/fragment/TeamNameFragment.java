package yeapp.com.burracoscore.fragment;

import android.app.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.SummaryActivity;
import yeapp.com.burracoscore.core.model.Team;

public class TeamNameFragment extends Fragment implements TextWatcher {

    private int numberOfPlayerForTeam = 0;

    private EditText gioc11Text;
    private EditText gioc12Text;
    private EditText gioc21Text;
    private EditText gioc22Text;
    private Team a = null;
    private Team b = null;

    //    private OnDataPass passer;
    private OnChangeToolbarVisibility changer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberOfPlayerForTeam = getArguments().getInt(SummaryActivity.numberOfPlayer);
        a = getArguments().getParcelable(SummaryActivity.teamAKey);
        b = getArguments().getParcelable(SummaryActivity.teamBKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = (numberOfPlayerForTeam == 0 ? inflater.inflate(R.layout.empty_team_configuration, container, false) :
                ((numberOfPlayerForTeam == 1) ? inflater.inflate(R.layout.one_player_name_fragment, container, false) :
                        inflater.inflate(R.layout.two_player_name_fragment, container, false)));
        if (numberOfPlayerForTeam > 0) {
            gioc11Text = (EditText) view.findViewById(R.id.giocatore11);
            gioc21Text = (EditText) view.findViewById(R.id.giocatore21);
            gioc11Text.setText((a.getPlayer1() == null || a.getPlayer1().length() == 0) ? getString(R.string.nomeGiocatore11) : a.getPlayer1());
            gioc21Text.setText((b.getPlayer1() == null || b.getPlayer1().length() == 0) ? getString(R.string.nomeGiocatore21) : b.getPlayer1());
            gioc11Text.addTextChangedListener(this);
            gioc21Text.addTextChangedListener(this);
        }
        if (numberOfPlayerForTeam > 1) {
            gioc12Text = (EditText) view.findViewById(R.id.giocatore12);
            gioc22Text = (EditText) view.findViewById(R.id.giocatore22);
            gioc12Text.setText((a.getPlayer2() == null || a.getPlayer2().length() == 0) ? getString(R.string.nomeGiocatore12) : a.getPlayer2());
            gioc22Text.setText((b.getPlayer2() == null || b.getPlayer2().length() == 0) ? getString(R.string.nomeGiocatore22) : b.getPlayer2());
            gioc12Text.addTextChangedListener(this);
            gioc22Text.addTextChangedListener(this);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveDisplayedName();
        outState.putInt(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(SummaryActivity.teamAKey, a);
        outState.putParcelable(SummaryActivity.teamBKey, b);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changer = (OnChangeToolbarVisibility) activity;
    }

    public Team getTeamA() {
        return a;
    }

    public Team getTeamB() {
        return b;
    }

    public void saveDisplayedName() {
        a.setPlayer1(numberOfPlayerForTeam > 0 ? gioc11Text.getText().toString() : null);
        a.setPlayer2(numberOfPlayerForTeam > 1 ? gioc12Text.getText().toString() : null);
        b.setPlayer1(numberOfPlayerForTeam > 0 ? gioc21Text.getText().toString() : null);
        b.setPlayer2(numberOfPlayerForTeam > 1 ? gioc22Text.getText().toString() : null);
    }

    public void resetNames() {
        gioc11Text.setText(R.string.nomeGiocatore11);
        gioc21Text.setText(R.string.nomeGiocatore21);
        if(numberOfPlayerForTeam==2) {
            gioc12Text.setText(R.string.nomeGiocatore12);
            gioc22Text.setText(R.string.nomeGiocatore22);
        }
    }

    public interface OnChangeToolbarVisibility {
        public void changedToolbarVisibility(boolean visible);

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
            changer.changedToolbarVisibility(false);
        } else if (s.length() > 0) {
            changer.changedToolbarVisibility(true);
        }
    }

}
