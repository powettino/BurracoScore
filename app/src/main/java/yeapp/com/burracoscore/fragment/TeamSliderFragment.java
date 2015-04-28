package yeapp.com.burracoscore.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import yeapp.com.burracoscore.activity.TeamSliderContainer;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.utils.Constants;
import yeapp.com.burracoscore.utils.Utils;


public class TeamSliderFragment extends Fragment implements TextWatcher, ImageView.OnClickListener, View.OnLongClickListener {

    private EditText gioc1Text;
    private EditText gioc2Text;
    private EditText teamAliasText;

    private Team team = null;
    private ImageView teamPicture = null;

    private OnTeamFragmentChanger changer;

    ProgressDialog pDiag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            team = getArguments().getParcelable(Constants.genericTeamKey);
        } else {
            team = savedInstanceState.getParcelable(Constants.genericTeamKey);
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
        String url = team.getImagePath();
        if (url != null) {
            Bitmap b = Utils.getThumbnail(getActivity(), url, getResources().getDimensionPixelSize(R.dimen.maxPicture));
            if(b!=null) {
                startSpinner("Caricamento immagine...");
                teamPicture.setTag(url);
                teamPicture.setImageBitmap(b);
                stopSpinner();
            }

//            new AsyncTask<String, Void, Bitmap>() {
//
//                @Override
//                protected Bitmap doInBackground(String... params) {
//                    String url = params[0];
//                    int w = Integer.parseInt(params[1]);
//                    Bitmap b = null;
//                    try {
//                        Log.d("FRAGMENT", "carico l'immagine di url: " + url);
//                        b = Utils.getThumbnail(getActivity(), url, w);
//                        Log.d("FRAGMENT", "L'immagine e': "+ (b!=null));
//                    } catch (Exception ioe) {
//                        Log.d("FRAGMENT", ioe.getMessage());
//                        ioe.printStackTrace();
//                    }finally {
//                        Log.d("FRAGMENT", "L'immagine2 e': " + (b != null));
//                    }
//                    return b;
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap bitmap) {
//                    teamPicture.setImageBitmap(bitmap);
//                    Log.d("FRAGMENT", "Ho settato l'immagine: "+ (bitmap!=null));
//                    stopSpinner();
//                }
//
//                @Override
//                protected void onPreExecute() {
//                    startSpinner("Caricamento immagine...");
//                }
//            }.execute(url, String.valueOf(teamPicture.getWidth()));
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
            animatePlayerName();
        }
    }

    private void stopSpinner(){
        if (pDiag.isShowing()) {
            pDiag.dismiss();
        }
    }

    private void startSpinner(String msg) {
        if (pDiag == null || !pDiag.isShowing()) {
            pDiag = new ProgressDialog(getActivity());
            pDiag.setMessage(msg);
            pDiag.setIndeterminate(false);
            pDiag.setCancelable(true);
            pDiag.show();
        }
    }

    public void animatePlayerName() {
        if(team.getNumberPlayer()==1) {
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
//            gioc2Text.startAnimation(anim);
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(gioc2Text, "alpha", 1f, 0f);
            fadeOut.setDuration(getResources().getInteger(R.integer.player1));
            fadeOut.start();
            ObjectAnimator translation = ObjectAnimator.ofFloat(gioc1Text, "translationY", 50);
            translation.setDuration(getResources().getInteger(R.integer.player1));
            translation.setInterpolator(new AccelerateDecelerateInterpolator());
            translation.start();
        }else{
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fading_in);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
//                    gioc2Text.setAlpha(1);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
//            gioc2Text.startAnimation(anim);
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(gioc2Text, "alpha", 0f, 1f);
            fadeIn.setDuration(getResources().getInteger(R.integer.player1));
            fadeIn.start();
            ObjectAnimator translation = ObjectAnimator.ofFloat(gioc1Text, "translationY", 0);
            translation.setDuration(getResources().getInteger(R.integer.player1));
            translation.setInterpolator(new AccelerateDecelerateInterpolator());
            translation.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveTeamConfiguration();
        outState.putInt(Constants.numberOfPlayer, team.getNumberPlayer());
        outState.putParcelable(Constants.genericTeamKey, team);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        if(pDiag != null && pDiag.isShowing()) {
            pDiag.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDetach() {
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
                startSpinner("Caricamento immagini...");
                Intent intent;
                if (Build.VERSION.SDK_INT < 19){
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                }
                stopSpinner();
                startActivityForResult(intent, 1);

//                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
                    startSpinner("Caricamento immagine...");
                    String uri = data.getData().toString();
                    teamPicture.setTag(uri);
                    Log.d("FRAGMENT", "uri: "+ uri);
                    teamPicture.setImageBitmap(Utils.getThumbnail(getActivity(), uri, teamPicture.getWidth()));
                    stopSpinner();
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
