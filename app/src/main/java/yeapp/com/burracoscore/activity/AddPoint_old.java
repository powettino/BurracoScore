package yeapp.com.burracoscore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Hand;

@Deprecated
public class AddPoint_old extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    EditText baseA;
    EditText carteA;
    CheckBox mazzettoA;
    CheckBox chiusuraA;
    EditText baseB;
    EditText carteB;
    CheckBox mazzettoB;
    CheckBox chiusuraB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_point);
        baseA = (EditText) findViewById(R.id.puntiBaseTextA);
        baseB = (EditText) findViewById(R.id.puntiBaseTextB);
        carteA = (EditText) findViewById(R.id.puntiCarteTextA);
        mazzettoA = (CheckBox) findViewById(R.id.checkMazzettoA);
        chiusuraA = (CheckBox) findViewById(R.id.checkChiusuraA);
        mazzettoA.setOnCheckedChangeListener(this);
        chiusuraA.setOnCheckedChangeListener(this);
        carteB = (EditText) findViewById(R.id.puntiCarteTextB);
        mazzettoB = (CheckBox) findViewById(R.id.checkMazzettoB);
        chiusuraB = (CheckBox) findViewById(R.id.checkChiusuraB);
        mazzettoB.setOnCheckedChangeListener(this);
        chiusuraB.setOnCheckedChangeListener(this);
        findViewById(R.id.salvaPunti).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkChiusuraA: {
                if (isChecked) {
                    mazzettoA.setChecked(true);
                    mazzettoA.setEnabled(false);
                } else {
                    mazzettoA.setEnabled(true);
                    mazzettoA.setChecked(false);
                }
                break;
            }
            case R.id.checkChiusuraB: {
                if (isChecked) {
                    mazzettoB.setChecked(true);
                    mazzettoB.setEnabled(false);
                } else {
                    mazzettoB.setEnabled(true);
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
                setResult(RESULT_OK, getIntent()
                        .putExtra(SummaryActivity_old.handA, manoA)
                        .putExtra(SummaryActivity_old.handB, manoB));
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }
}
