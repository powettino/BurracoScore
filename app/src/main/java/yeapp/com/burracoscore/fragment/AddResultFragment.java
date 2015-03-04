package yeapp.com.burracoscore.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Hand;

public class AddResultFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    EditText baseA;
    EditText carteA;
    CheckBox mazzettoA;
    CheckBox chiusuraA;
    EditText baseB;
    EditText carteB;
    CheckBox mazzettoB;
    CheckBox chiusuraB;

    OnSavingResult cb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_point, container, false);
        baseA = (EditText) view.findViewById(R.id.puntiBaseTextA);
        baseB = (EditText) view.findViewById(R.id.puntiBaseTextB);
        carteA = (EditText) view.findViewById(R.id.puntiCarteTextA);
        mazzettoA = (CheckBox) view.findViewById(R.id.checkMazzettoA);
        chiusuraA = (CheckBox) view.findViewById(R.id.checkChiusuraA);
        mazzettoA.setOnCheckedChangeListener(this);
        chiusuraA.setOnCheckedChangeListener(this);
        carteB = (EditText) view.findViewById(R.id.puntiCarteTextB);
        mazzettoB = (CheckBox) view.findViewById(R.id.checkMazzettoB);
        chiusuraB = (CheckBox) view.findViewById(R.id.checkChiusuraB);
        mazzettoB.setOnCheckedChangeListener(this);
        chiusuraB.setOnCheckedChangeListener(this);
        view.findViewById(R.id.salvaPunti).setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cb = (OnSavingResult) activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkChiusuraA: {
                if (isChecked) {
                    mazzettoA.setChecked(true);
                    mazzettoA.setEnabled(false);
                    chiusuraB.setEnabled(false);
                } else {
                    mazzettoA.setEnabled(true);
                    chiusuraB.setEnabled(true);
                    mazzettoA.setChecked(false);
                }
                break;
            }
            case R.id.checkChiusuraB: {
                if (isChecked) {
                    mazzettoB.setChecked(true);
                    mazzettoB.setEnabled(false);
                    chiusuraA.setEnabled(false);
                } else {
                    mazzettoB.setEnabled(true);
                    chiusuraA.setEnabled(true);
                    mazzettoB.setChecked(false);
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.salvaPunti: {
                Hand manoA = new Hand(
                        !baseA.getText().toString().isEmpty() ? Integer.valueOf(baseA.getText().toString()) : 0,
                        !carteA.getText().toString().isEmpty() ? Integer.valueOf(carteA.getText().toString()) : 0,
                        chiusuraA.isChecked(),
                        mazzettoA.isChecked());
                int resA = manoA.getTotaleMano();
                Hand manoB = new Hand(
                        !baseB.getText().toString().isEmpty() ? Integer.valueOf(baseB.getText().toString()) : 0,
                        !carteB.getText().toString().isEmpty() ? Integer.valueOf(carteB.getText().toString()) : 0,
                        chiusuraB.isChecked(),
                        mazzettoB.isChecked());
                int resB = manoB.getTotaleMano();
                if(resB > resA){
                    manoB.setWon(Hand.WON);
                    manoA.setWon(Hand.LOST);
                }else
                if(resA > resB){
                    manoA.setWon(Hand.WON);
                    manoB.setWon(Hand.LOST);
                }
                cb.OnSaving(manoA, manoB);
                break;
            }
            default: {
                break;
            }
        }
    }

    public interface OnSavingResult{
        public void OnSaving(Hand manoA, Hand manoB);
    }
}
