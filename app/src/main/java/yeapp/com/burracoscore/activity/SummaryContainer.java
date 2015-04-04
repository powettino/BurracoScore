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
import yeapp.com.burracoscore.core.model.BurracoSession;
import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.core.model.Team;
import yeapp.com.burracoscore.fragment.SummaryFragment;
import yeapp.com.burracoscore.utils.Constants;

public class SummaryContainer extends FragmentActivity implements Toolbar.OnMenuItemClickListener, SummaryFragment.OnScoreChanging {

    public static final int CODE_FOR_CONF = 0;
    public static final int CODE_FOR_SET = 1;

    private SummaryFragment sum;
    private boolean dialogActive;
    private AlertDialog winnerDialog = null;

    private TextView punteggioTotA;
    private TextView punteggioTotB;

    private boolean teamSaved = false;

    private BurracoSession sessione;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_container);
        if (savedInstanceState == null) {
            sessione = new BurracoSession();
            sum = new SummaryFragment();
            Bundle b = new Bundle();
            b.putParcelable(Constants.currentGame, sessione.getCurrentGame());
            b.putString(Constants.teamAAlias, sessione.getTeamA().getAlias());
            b.putString(Constants.teamBAlias, sessione.getTeamB().getAlias());
            sum.setArguments(b);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
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
                                    sessione.addNewGame(sessione.getGameTotali());
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
                    Intent configurazione = new Intent(this, TeamSliderContainer.class);
                    configurazione.putExtra(Constants.numberOfPlayer, sessione.getTeamA().getNumberPlayer())
                            .putExtra(Constants.teamAKey, sessione.getTeamA())
                            .putExtra(Constants.teamBKey, sessione.getTeamB());
                    startActivityForResult(configurazione, CODE_FOR_CONF);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Tipo di partita")
                            .setCancelable(true)
                            .setItems(new CharSequence[]
                                            {"1 vs 1", "2 vs 2"},
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent configurazione = new Intent(getBaseContext(), TeamSliderContainer.class);
                                            switch (id) {
                                                case 0:
                                                    configurazione.putExtra(Constants.numberOfPlayer, 1);
                                                    break;
                                                case 1:
                                                    configurazione.putExtra(Constants.numberOfPlayer, 2);
                                                    break;
                                            }
                                            configurazione.putExtra(Constants.teamAKey, sessione.getTeamA())
                                                    .putExtra(Constants.teamBKey, sessione.getTeamB());
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
                                        sessione.clearCurrentGame();
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
//            case R.id.cancellaTutto: {
//                new AlertDialog.Builder(this)
//                        .setMessage("Sei sicuro di voler cancellare le partite non salvate?")
//                        .setCancelable(true)
//                        .setPositiveButton("Si",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        sessione.clear();
//                                        sum.resetGames();
//                                        teamSaved = false;
//                                        punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
//                                        punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
//                                        dialog.cancel();
//                                        dialogActive = false;
//                                    }
//                                })
//                        .setNegativeButton("No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        dialogActive = false;
//                                    }
//                                })
//                        .create().show();
//                return true;
//            }
            default: {
                return true;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sessione = savedInstanceState.getParcelable(Constants.gameSession);
        if (savedInstanceState.getBoolean(Constants.dialogStatus)) {
            dialogActive = true;
            winnerDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.gameSession, sessione);
        outState.putBoolean(Constants.dialogStatus, dialogActive);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_FOR_CONF: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                    sessione.setTeamA((Team) data.getParcelableExtra(Constants.teamAKey));
                    sessione.setTeamB((Team) data.getParcelableExtra(Constants.teamBKey));
//                    sum.updateTeamAlias(sessione.getTeamA().getAlias(), sessione.getTeamB().getAlias());
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
    public void gameEnded(Game current) {
        sessione.updateLastGame(current);
        sessione.addNumeroVinti(current.getWinner());
        punteggioTotA.setText(String.valueOf(sessione.getNumeroVintiA()));
        punteggioTotB.setText(String.valueOf(sessione.getNumeroVintiB()));
        dialogActive = true;
        winnerDialog.show();
    }

    @Override
    public void gameUpdate(Game current) {

    }
}