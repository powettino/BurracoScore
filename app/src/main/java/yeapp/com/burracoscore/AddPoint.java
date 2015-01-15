package yeapp.com.burracoscore;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


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
                setResult(RESULT_OK, getIntent()
                        .putExtra(getString(R.string.puntiBase), !base.getText().toString().isEmpty() ? Integer.valueOf(base.getText().toString()) : 0)
                        .putExtra(getString(R.string.puntiCarte), !carte.getText().toString().isEmpty() ? Integer.valueOf(base.getText().toString()) : 0)
                        .putExtra(getString(R.string.puntiChiusura), chiusura.isChecked() ? 100 : 0)
                        .putExtra(getString(R.string.puntiMazzetto), mazzetto.isChecked() ? 0 : -100));
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }
}
