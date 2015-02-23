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
        View view = (numberOfPlayerForTeam == 0 ? inflater.inflate(R.layout.empty_result, container, false) :
                ((numberOfPlayerForTeam == 1) ? inflater.inflate(R.layout.one_player_name_fragment, container, false) :
                        inflater.inflate(R.layout.two_player_name_fragment, container, false)));
        if (numberOfPlayerForTeam == 1) {
            gioc11Text = (EditText) view.findViewById(R.id.giocatore11);
            gioc21Text = (EditText) view.findViewById(R.id.giocatore21);
        } else if (numberOfPlayerForTeam == 2) {
            gioc12Text = (EditText) view.findViewById(R.id.giocatore12);
            gioc22Text = (EditText) view.findViewById(R.id.giocatore22);
        }
        return view;
    }

    @SuppressWarnings("unused")
    private void setDiplayTeamName() {
        if (numberOfPlayerForTeam != 0) {
            gioc11Text.setText(a.getPlayer1());
            gioc21Text.setText(b.getPlayer1());
            if (numberOfPlayerForTeam == 2) {
                gioc12Text.setText(a.getPlayer2());
                gioc22Text.setText(b.getPlayer2());
            }
            changeDisplayName();
        } else {
            resetNames();
        }
    }

    private void changeDisplayName() {

        if (gioc11Text.getText().length() == 0) {
            gioc11Text.setText(R.string.nomeGiocatore11);
        }
        if (gioc12Text.getText().length() == 0) {
            gioc12Text.setText(R.string.nomeGiocatore12);
        }
        if (gioc21Text.getText().length() == 0) {
            gioc21Text.setText(R.string.nomeGiocatore21);
        }
        if (gioc22Text.getText().length() == 0) {
            gioc22Text.setText(R.string.nomeGiocatore22);
        }
        if (!gioc11Text.isEnabled() && !gioc21Text.isEnabled()) {
            gioc11Text.setEnabled(true);
            gioc21Text.setEnabled(true);
        }
        if (numberOfPlayerForTeam == 1 && gioc12Text.isEnabled() && gioc22Text.isEnabled()) {
            gioc12Text.setEnabled(false);
            gioc22Text.setEnabled(false);
        }
        if (numberOfPlayerForTeam == 2 && !gioc12Text.isEnabled() && !gioc22Text.isEnabled()) {
            gioc12Text.setEnabled(true);
            gioc22Text.setEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveDisplayedName();
        outState.putInt(SummaryActivity.numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(SummaryActivity.teamAKey, a);
        outState.putParcelable(SummaryActivity.teamBKey, b);
        // sendDataBack();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        gioc11Text.addTextChangedListener(this);
        gioc12Text.addTextChangedListener(this);
        gioc21Text.addTextChangedListener(this);
        gioc22Text.addTextChangedListener(this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        passer = (OnDataPass) activity;
        changer = (OnChangeToolbarVisibility) activity;
    }

//    private void sendDataBack() {
//        Bundle b = new Bundle();
//        b.putParcelable(SummaryActivity.teamAKey, a);
//        b.putParcelable(SummaryActivity.teamBKey, b);
//        passer.onDataPass(b);
//    }

    public Team getTeamA(){
        return a;
    }

    public Team getTeamB(){
        return b;
    }

    public void saveDisplayedName() {
        a.setPlayer1(gioc11Text.getText().toString());
        a.setPlayer2(numberOfPlayerForTeam == 2 ? gioc12Text.getText().toString() : null);
        b.setPlayer1(gioc21Text.getText().toString());
        b.setPlayer2(numberOfPlayerForTeam == 2 ? gioc22Text.getText().toString() : null);
    }

    public void resetNames() {
        gioc11Text.setText(R.string.nomeGiocatore11);
        gioc12Text.setText(R.string.nomeGiocatore12);
        gioc21Text.setText(R.string.nomeGiocatore21);
        gioc22Text.setText(R.string.nomeGiocatore22);
        if (!gioc12Text.isEnabled() && !gioc22Text.isEnabled()) {
            gioc12Text.setEnabled(true);
            gioc12Text.setEnabled(false);
            gioc22Text.setEnabled(true);
            gioc22Text.setEnabled(false);
        }
    }

//    public interface OnDataPass {
//        public void onDataPass(Bundle b);
//    }

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
        } else {
            changer.changedToolbarVisibility(true);
        }
    }

}
