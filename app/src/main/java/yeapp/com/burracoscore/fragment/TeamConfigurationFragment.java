package yeapp.com.burracoscore.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import yeapp.com.burracoscore.R;

/**
 * Created by powered on 20/02/15.
 */
public class TeamConfigurationFragment extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_team_configuration);

        //list = (PageList) getFragmentManager().findFragmentById(R.id.listFragment);

   /*     FragmentManager fm = getFragmentManager();
            if (fm.findFragmentById(R.id.listFragment) == null) {
                list = new PageList();
                fm.beginTransaction().add(R.id.listFragment, list).commit();
            }
*/


    }
}
