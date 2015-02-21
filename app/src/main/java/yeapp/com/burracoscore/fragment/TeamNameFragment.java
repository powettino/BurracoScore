package yeapp.com.burracoscore.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.SummaryActivity;

/**
 * Created by powered on 20/02/15.
 */
public class TeamNameFragment extends Fragment {

    private int numPlayer=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        numPlayer = getArguments().getInt(SummaryActivity.numberOfPlayer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return numPlayer == 0 ? inflater.inflate(R.layout.empty_result, container, false) : inflater.inflate(R.layout.two_player_name_fragment, container, false);
    }

    public void init(int number) {
        numPlayer = number;
    }

    @Override
    public void onAttach(Activity activity) {
        if(getArguments()!=null){
            numPlayer = getArguments().getInt(SummaryActivity.numberOfPlayer);
        }
        super.onAttach(activity);
    }

}
