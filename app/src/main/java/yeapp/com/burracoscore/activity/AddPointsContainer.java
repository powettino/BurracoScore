package yeapp.com.burracoscore.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import yeapp.com.burracoscore.R;
import yeapp.com.burracoscore.core.model.Hand;
import yeapp.com.burracoscore.fragment.AddPointsFragment;
import yeapp.com.burracoscore.utils.Constants;


public class AddPointsContainer extends Activity implements AddPointsFragment.OnSavingResult{
    AddPointsFragment pointFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_point_container);
        if(savedInstanceState==null) {
            pointFragment = new AddPointsFragment();
            pointFragment.setArguments(getIntent().getExtras());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(android.R.id.content, pointFragment, "AddPoint");
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
                .putExtra(Constants.manoA, manoA)
                .putExtra(Constants.manoB, manoB));
        finish();
    }
}
