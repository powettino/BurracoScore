package yeapp.com.burracoscore.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.fragment.AddResultFragment;


public class AddPointcontainer extends Activity implements AddResultFragment.OnSavingResult{
    AddResultFragment poi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_point_container);
        poi = new AddResultFragment();
        if(savedInstanceState==null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentPointcont, poi, "AddPoint");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void OnSaving(Hand manoA, Hand manoB) {
        setResult(RESULT_OK, getIntent()
                .putExtra(SummaryContainer.handA, manoA)
                .putExtra(SummaryContainer.handB, manoB));
        finish();
    }
}
