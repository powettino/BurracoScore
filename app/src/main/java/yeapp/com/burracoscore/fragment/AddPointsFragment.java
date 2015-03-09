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
import android.widget.TextView;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.activity.SummaryContainer;
import yeapp.com.burracoscore.core.model.Hand;

public class AddPointsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    EditText baseA;
    EditText carteA;
    CheckBox mazzettoA;
    CheckBox chiusuraA;
    EditText baseB;
    EditText carteB;
    CheckBox mazzettoB;
    CheckBox chiusuraB;
    View salva;

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
        carteB = (EditText) view.findViewById(R.id.puntiCarteTextB);
        mazzettoB = (CheckBox) view.findViewById(R.id.checkMazzettoB);
        chiusuraB = (CheckBox) view.findViewById(R.id.checkChiusuraB);
        ((TextView)view.findViewById(R.id.gameNumber)).setText("GAME "+ getArguments().getInt(SummaryFragment.gameNumber));
        Hand manoA = getArguments().getParcelable(SummaryContainer.handA);
        Hand manoB = getArguments().getParcelable(SummaryContainer.handB);
        mazzettoB.setOnCheckedChangeListener(this);
        chiusuraB.setOnCheckedChangeListener(this);
        mazzettoA.setOnCheckedChangeListener(this);
        chiusuraA.setOnCheckedChangeListener(this);
        salva = view.findViewById(R.id.salvaPunti);
        if(manoA != null && manoB != null){
            baseA.setText(String.valueOf(manoA.getBase()));
            baseB.setText(String.valueOf(manoB.getBase()));
            baseA.setEnabled(false);
            baseB.setEnabled(false);

            carteA.setText(String.valueOf(manoA.getCarte()));
            carteB.setText(String.valueOf(manoB.getCarte()));
            carteA.setEnabled(false);
            carteB.setEnabled(false);

            mazzettoA.setChecked(manoA.getMazzetto()==0);
            mazzettoB.setChecked(manoB.getMazzetto()==0);
            mazzettoB.setEnabled(false);
            mazzettoA.setEnabled(false);

            chiusuraA.setChecked(manoA.getChiusura()>0);
            chiusuraB.setChecked(manoB.getChiusura()>0);
            chiusuraA.setEnabled(false);
            chiusuraB.setEnabled(false);
            salva.setEnabled(false);
        }
        //Questo deve stare dopo l'aggancio con i listener altrimenti si cambia la visiilita'
        salva.setOnClickListener(this);
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
                    mazzettoA.setChecked(isChecked);
                    mazzettoA.setEnabled(!isChecked);
                    chiusuraB.setEnabled(!isChecked);
                } else {
                    mazzettoA.setEnabled(!isChecked);
                    chiusuraB.setEnabled(!isChecked);
                    mazzettoA.setChecked(isChecked);
                }
                salva.setEnabled(isChecked);
                break;
            }
            case R.id.checkChiusuraB: {
                if (isChecked) {
                    mazzettoB.setChecked(isChecked);
                    mazzettoB.setEnabled(!isChecked);
                    chiusuraA.setEnabled(!isChecked);
                } else {
                    mazzettoB.setEnabled(!isChecked);
                    chiusuraA.setEnabled(!isChecked);
                    mazzettoB.setChecked(isChecked);
                }
                salva.setEnabled(isChecked);
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
