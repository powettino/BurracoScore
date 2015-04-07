package yeapp.com.burracoscore.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.fragment.SummaryFragment;
import yeapp.com.burracoscore.utils.Constants;

public class TabSummaryAdapter extends FragmentStatePagerAdapter {

    Bundle bun = new Bundle();
    private int number=0;
    private FragmentManager fm;
//    private  SummaryFragment last;

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
//                last=sum;
            }
            return sum;
        }else {
            return null;
        }
    }

    public void restore(int numberGame){
//        List<Fragment> list = fm.getFragments();
//        last = (SummaryFragment)list.get(list.size()-1);
        number=numberGame;
        notifyDataSetChanged();
    }

    public void addGame(Game game){
        bun.putParcelable(Constants.currentGame, game);
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
//        last.resetGames();
    }

    public void clearAll(){
//        for ( Fragment f : fm.getFragments()){
//            if(f instanceof SummaryFragment){
//                ((SummaryFragment) f).setNotRestore();
//            }
//        }
        number=0;
        bun.clear();
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

}