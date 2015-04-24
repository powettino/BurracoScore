package yeapp.com.burracoscore.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import yeapp.com.burracoscore.core.model.BurracoSession;
import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.fragment.SummaryFragment;
import yeapp.com.burracoscore.utils.Constants;

public class TabSummaryAdapter extends FragmentStatePagerAdapter {

    Bundle bun = new Bundle();
    private int number=0;
    private FragmentManager fm;

    public TabSummaryAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fm = fragmentManager;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position < number)
            return "Game  " + (position+1);
        else{
            return null;
        }
    }

    @Override
    public Fragment getItem(int index) {
        if(index < number){
            SummaryFragment sum = new SummaryFragment();
            if(number-1==index) {
                sum.setArguments(bun);
            }
            return sum;
        }else {
            return null;
        }
    }

    public void restore(int numberGame){
        number=numberGame;
        notifyDataSetChanged();
    }

    public void restoreOld(BurracoSession ses){
        for(Game g : ses.getGames()) {
            addGame(g, true);
        }
    }

    public void addGame(Game game, boolean restore){
        bun.putParcelable(Constants.currentGame, game);
        bun.putBoolean(Constants.restoreFragment, restore);
        number++;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return number;
    }

    public void renewLastGame(Game nuovo){
        bun.clear();
        bun.putParcelable(Constants.currentGame, nuovo);
        notifyDataSetChanged();
    }

    public void clearAll(){
        number=0;
        bun.clear();
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

}