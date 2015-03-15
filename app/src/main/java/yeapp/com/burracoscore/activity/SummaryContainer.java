package yeapp.com.burracoscore.activity;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.fragment.SummaryFragment;

public class SummaryContainer extends FragmentActivity implements Toolbar.OnMenuItemClickListener, SummaryFragment.OnScoreChanging {

    public static final int CODE_FOR_CONF = 0;
    public static final int CODE_FOR_SET = 1;

    public static final String handA = "handA";
    public static final String handB = "handB";
    public static final int maxPoint = 500;
    public static final String teamAKey = "teamA";
    public static final String teamBKey = "teamB";

    public static final String addHand = "addHand";

    public static final String numberOfPlayer = "numberOfPlayer";

    public static final String setDialog = "dialog";

    private SummaryFragment sum;
    private boolean dialogActive;
    private AlertDialog winnerDialog = null;

    private TextView punteggioTotA;
    private TextView punteggioTotB;

    //    private int numberOfPlayerForTeam = 1;
    private boolean teamSaved = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_container);
        if (savedInstanceState == null) {
            sum = new SummaryFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentContSum, sum, "Summary");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_summary);
        toolbar.setOnMenuItemClickListener(this);

        punteggioTotA = (TextView) findViewById(R.id.punteggioASummary);
        punteggioTotB = (TextView) findViewById(R.id.punteggioBSummary);

        if (winnerDialog == null) {
            winnerDialog = new AlertDialog.Builder(this).setTitle("VITTORIA!!!")
                    .setMessage("Complimenti, la partita è conclusa.\n\nVuoi cominciare una nuova partita?")
                    .setCancelable(false)
                    .setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sum.resetGames();
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sum.setEnableAdd(false);
                                    dialog.cancel();
                                    dialogActive = false;
                                }
                            })
                    .create();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configuraMenuSum: {
                if (teamSaved) {
                    Intent configurazione = new Intent(this, TeamConfigurationContainerSlider.class);
                    configurazione.putExtra(numberOfPlayer, sum.getTeamA().getNumberPlayer())
                            .putExtra(teamAKey, sum.getTeamA())
                            .putExtra(teamBKey, sum.getTeamB());
                    startActivityForResult(configurazione, CODE_FOR_CONF);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Tipo di partita")
                            .setCancelable(true)
                            .setItems(new CharSequence[]
                                            {"1 vs 1", "2 vs 2"},
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent configurazione = new Intent(getBaseContext(), TeamConfigurationContainerSlider.class);
                                            switch (id) {
                                                case 0:
                                                    configurazione.putExtra(numberOfPlayer, 1);
                                                    break;
                                                case 1:
                                                    configurazione.putExtra(numberOfPlayer, 2);
                                                    break;
                                            }
                                            configurazione.putExtra(teamAKey, sum.getTeamA())
                                                    .putExtra(teamBKey, sum.getTeamB());
                                            startActivityForResult(configurazione, CODE_FOR_CONF);
                                        }
                                    })
                            .create().show();
                }
                return true;
            }
            case R.id.cancellaGame: {
                new AlertDialog.Builder(this)
                        .setMessage("Vuoi cominciare una nuova partita?")
                        .setCancelable(true)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sum.resetGames();
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .create().show();
                return true;
            }
            case R.id.cancellaTutto: {
                new AlertDialog.Builder(this)
                        .setMessage("Sei sicuro di voler cancellare le partite non salvate?")
                        .setCancelable(true)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sum.resetAll();
//                                        numberOfPlayerForTeam = 1;
                                        teamSaved = false;
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        dialogActive = false;
                                    }
                                })
                        .create().show();
                return true;
            }
            default: {
                return true;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sum = (SummaryFragment) getFragmentManager().findFragmentByTag("Summary");
        if (savedInstanceState.getBoolean(setDialog)) {
            dialogActive = true;
            winnerDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(setDialog, dialogActive);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
//                    numberOfPlayerForTeam = data.getIntExtra(numberOfPlayer, 1);
                    sum.setTeamA((Team) data.getParcelableExtra(teamAKey));
                    sum.setTeamB((Team) data.getParcelableExtra(teamBKey));
//                    sum.createTeamName();
                    teamSaved = true;
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
    public void scoreChanged(int puntiA, int puntiB, boolean won) {
        punteggioTotA.setText(String.valueOf(puntiA));
        punteggioTotB.setText(String.valueOf(puntiB));
        if (won) {
            dialogActive = true;
            winnerDialog.show();
        }
    }
}
