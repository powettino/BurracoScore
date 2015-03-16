package yeapp.com.burracoscore.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.SummaryContainer;
import yeapp.com.burracoscore.activity.TeamConfigurationContainerSlider;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.utils.Utils;


public class TeamNameSliderFragment extends Fragment implements TextWatcher, ImageView.OnClickListener, View.OnLongClickListener {

//    private int tempNumberPlayer = 0;

    private EditText gioc1Text;
    private EditText gioc2Text;
    private EditText teamAliasText;
    private Team team = null;
    private ImageView teamPicture = null;

    private OnTeamFragmentChanger changer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            team = getArguments().getParcelable(TeamConfigurationContainerSlider.teamKey);
        } else {
            team = savedInstanceState.getParcelable(TeamConfigurationContainerSlider.teamKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.team_slider_fragment, container, false);
        teamAliasText = (EditText) view.findViewById(R.id.teamAlias);
        if (team.getSide() == Utils.ASide) {
            teamAliasText.setTextColor(getResources().getColor(R.color.Arancio));
        } else {
            teamAliasText.setTextColor(getResources().getColor(R.color.Blu));
        }
        teamPicture = (ImageView) view.findViewById(R.id.foto);

        //FIXME: va fatto in un async
        if (team.getImagePath() != null) {
            try {
                teamPicture.setTag(team.getImagePath());
                teamPicture.setImageBitmap(Utils.getThumbnail(getActivity(), team.getImagePath(), 300));
            } catch (IOException ioe) {
            }
        }

        teamPicture.setOnClickListener(this);
        teamPicture.setOnLongClickListener(this);
        teamAliasText.setText(team.getAlias());
        teamAliasText.addTextChangedListener(this);
        gioc1Text = (EditText) view.findViewById(R.id.giocatore1);
        gioc1Text.setText(team.getPlayer1());
        gioc1Text.addTextChangedListener(this);

        gioc2Text = (EditText) view.findViewById(R.id.giocatore2);
        gioc2Text.setText(team.getPlayer2());
        gioc2Text.addTextChangedListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(team.getNumberPlayer()==1){
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fading_out);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    gioc2Text.setAlpha(0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            gioc2Text.startAnimation(anim);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(gioc1Text, "translationY", 50);
            objectAnimator.setDuration(getResources().getInteger(R.integer.player1));
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveTeamConfiguration();
        outState.putInt(SummaryContainer.numberOfPlayer, team.getNumberPlayer());
        outState.putParcelable(TeamConfigurationContainerSlider.teamKey, team);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
//        saveTeamConfiguration();
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changer = (OnTeamFragmentChanger) activity;
    }

    public void saveTeamConfiguration() {
        if (teamAliasText != null) {
            team.setAlias(teamAliasText.getText().toString());
            team.setPlayer1(gioc1Text.getText().toString());
            team.setPlayer2(gioc2Text.getText().toString());
            team.setImagePath(teamPicture.getTag() != null ?  (String) teamPicture.getTag() : null);
        }
        changer.savedTeam(team);
    }

    public void resetNames() {
        if (teamAliasText != null) {
            teamPicture.setTag(null);
            teamPicture.setImageResource(R.drawable.ic_action_picture);
            teamAliasText.setText("Team " + team.getSide());
            changer.changedToolbarVisibility(true, teamAliasText.getId() + team.getSide());
            gioc1Text.setText(Utils.giocatore1);
            changer.changedToolbarVisibility(true, gioc1Text.getId() + team.getSide());
        }
        if (team.getNumberPlayer() == 2) {
            gioc2Text.setText(Utils.giocatore2);
            changer.changedToolbarVisibility(true, gioc2Text.getId() + team.getSide());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.foto): {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (data != null && data.getData() != null) {
                    String uri = data.getData().toString();
                    teamPicture.setTag(uri);
                    try {

                        teamPicture.setImageBitmap(Utils.getThumbnail(getActivity(), uri, teamPicture.getWidth()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (getActivity().getCurrentFocus() != null) {
            if (s.length() == 0) {
                changer.changedToolbarVisibility(false, getActivity().getCurrentFocus().getId() + team.getSide());
            } else if (s.length() >= 1) {
                changer.changedToolbarVisibility(true, getActivity().getCurrentFocus().getId() + team.getSide());
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //FIXME: rimuovere l-immagine
        return false;
    }

    public interface OnTeamFragmentChanger {
        public void changedToolbarVisibility(boolean visible, int idRes);

        public void savedTeam(Team teamSaved);
    }
}
