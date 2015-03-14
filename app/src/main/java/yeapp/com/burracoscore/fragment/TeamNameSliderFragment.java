package yeapp.com.burracoscore.fragment;

import android.app.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.SummaryContainer;
import yeapp.com.burracoscore.activity.TeamConfigurationContainerSlider;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.core.model.Utils;

public class TeamNameSliderFragment extends Fragment implements TextWatcher , ImageView.OnClickListener{

    private int numberOfPlayerForTeam = 0;

    private EditText gioc1Text;
    private EditText gioc2Text;
    private EditText teamAliasText;
    private Team team = null;
    private ImageView teamPicture =null;

    private OnTeamFragmentChanger changer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null) {
            numberOfPlayerForTeam = getArguments().getInt(SummaryContainer.numberOfPlayer);
            team = getArguments().getParcelable(TeamConfigurationContainerSlider.teamKey);

        }else{
            numberOfPlayerForTeam = savedInstanceState.getInt(SummaryContainer.numberOfPlayer);
            team = savedInstanceState.getParcelable(TeamConfigurationContainerSlider.teamKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = ((numberOfPlayerForTeam == 1) ? inflater.inflate(R.layout.team_slider_fragment, container, false) :
                inflater.inflate(R.layout.two_player_fragment, container, false));
        teamAliasText = (EditText) view.findViewById(R.id.teamAlias);
        if(team.getSide() == Utils.ASide){
            teamAliasText.setTextColor(getResources().getColor(R.color.Arancio));
        }else{
            teamAliasText.setTextColor(getResources().getColor(R.color.Blu));
        }
        teamPicture = (ImageView) view.findViewById(R.id.foto);

        //FIXME: va fatto in un async
        if(team.getImagePath() != null){
            try{
                Bitmap bm = BitmapFactory.decodeStream(
                        getActivity().getContentResolver().openInputStream(team.getImagePath()));
                teamPicture.setImageBitmap(bm);
            }catch (FileNotFoundException fnfe){

            }
        }

        teamPicture.setOnClickListener(this);
        teamAliasText.setText(team.getAlias());
        teamAliasText.addTextChangedListener(this);
        if (numberOfPlayerForTeam >= 1 ) {
            gioc1Text = (EditText) view.findViewById(R.id.giocatore1);
            gioc1Text.setText((team.getPlayer1() == null || team.getPlayer1().length() == 0) ? Utils.giocatore1 : team.getPlayer1());
            gioc1Text.addTextChangedListener(this);
        }
        if (numberOfPlayerForTeam == 2) {
            gioc2Text = (EditText) view.findViewById(R.id.giocatore2);
            gioc2Text.setText((team.getPlayer1() == null || team.getPlayer1().length() == 0) ? Utils.giocatore2 : team.getPlayer1());
            gioc2Text.addTextChangedListener(this);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveDisplayedName();
        outState.putInt(SummaryContainer.numberOfPlayer, numberOfPlayerForTeam);
        outState.putParcelable(TeamConfigurationContainerSlider.teamKey, team);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        saveDisplayedName();
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changer = (OnTeamFragmentChanger) activity;
    }

    public void saveDisplayedName() {
        if(teamAliasText!=null) {
            team.setAlias(teamAliasText.getText().toString());
            team.setPlayer1(gioc1Text.getText().toString());
            team.setPlayer2(numberOfPlayerForTeam >= 2 ? gioc2Text.getText().toString() : null);
            changer.savedTeam(team);
        }
    }

    public void resetNames() {
        if(teamAliasText!=null){
            teamAliasText.setText("Team " + team.getSide());
            changer.changedToolbarVisibility(true, teamAliasText.getId()+team.getSide());
            gioc1Text.setText(Utils.giocatore1);
            changer.changedToolbarVisibility(true, gioc1Text.getId()+team.getSide());
        }
        if(numberOfPlayerForTeam == 2) {
            gioc2Text.setText(Utils.giocatore2);
            changer.changedToolbarVisibility(true, gioc2Text.getId()+team.getSide());
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case (R.id.foto):{
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
            }
            default:{
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && data != null && data.getData() != null) {
            Uri uri = data.getData();
//            Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
//            cursor.moveToFirst();
//            final String path = cursor.getString(0);
//            cursor.close();
            team.setImagePath(uri);
            try{
                teamPicture.setImageBitmap(BitmapFactory.decodeStream(
                        getActivity().getContentResolver().openInputStream(uri)));
            }catch (FileNotFoundException fnfe){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(getActivity().getCurrentFocus()!=null) {
            if (s.length() == 0) {
                changer.changedToolbarVisibility(false, getActivity().getCurrentFocus().getId()+team.getSide());
            } else if (s.length() >= 1) {
                changer.changedToolbarVisibility(true, getActivity().getCurrentFocus().getId()+team.getSide());
            }
        }
    }

    public interface OnTeamFragmentChanger {
        public void changedToolbarVisibility(boolean visible, int idRes);
        public void savedTeam(Team teamSaved);
    }
}
