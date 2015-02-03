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


public class AddPoint extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    EditText base;
    EditText carte;
    CheckBox mazzetto;
    CheckBox chiusura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        base = (EditText) findViewById(R.id.puntiBaseText);
        carte = (EditText) findViewById(R.id.puntiCarteText);
        mazzetto = (CheckBox) findViewById(R.id.checkMazzetto);
        chiusura = (CheckBox) findViewById(R.id.checkChiusura);
        findViewById(R.id.salvaPunti).setOnClickListener(this);
        mazzetto.setOnCheckedChangeListener(this);
        chiusura.setOnCheckedChangeListener(this);
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
            case R.id.checkChiusura: {
                if (isChecked) {
                    mazzetto.setChecked(true);
                    mazzetto.setEnabled(false);
                } else {
                    mazzetto.setEnabled(true);
                    mazzetto.setChecked(false);
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
                Hand mano = new Hand(
                        !base.getText().toString().isEmpty() ? Integer.valueOf(base.getText().toString()) : 0,
                        !carte.getText().toString().isEmpty() ? Integer.valueOf(carte.getText().toString()) : 0,
                        chiusura.isChecked(),
                        mazzetto.isChecked());
                setResult(RESULT_OK, getIntent()
                        .putExtra(SummaryActivity.hand, mano));
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }
}
