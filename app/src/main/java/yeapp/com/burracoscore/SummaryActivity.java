package yeapp.com.burracoscore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class SummaryActivity extends ActionBarActivity {
    //    String namesTeamA = "";
    //    String namesTeamB = "";
    private TextView teamAText;
    private TextView teamBText;

    private final int CODE_FOR_CONF = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        teamAText = (TextView) findViewById(R.id.teamASummary);
        // namesTeamA = teamAText.getText().toString();
        teamBText = (TextView) findViewById(R.id.teamBSummary);
        // namesTeamB = teamBText.getText().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chiudiAppMenuSum: {
                finish();
                return true;
            }
            case R.id.configuraMenuSum: {
                Intent configura = new Intent(this, TeamConfiguration.class);
                startActivityForResult(configura, CODE_FOR_CONF);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //FIXME: usare una risorse e non un intero
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                    teamAText.setText(data.getStringExtra(getString(R.string.teamA)));
                    teamBText.setText(data.getStringExtra(getString(R.string.teamB)));
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }
}
