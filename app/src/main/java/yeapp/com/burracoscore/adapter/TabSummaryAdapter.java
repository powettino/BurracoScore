package yeapp.com.burracoscore.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import java.util.List;

import yeapp.com.burracoscore.core.model.BurracoSession;
import yeapp.com.burracoscore.core.model.Game;
import yeapp.com.burracoscore.fragment.SummaryFragment;
import yeapp.com.burracoscore.utils.Constants;

public class TabSummaryAdapter extends FragmentStatePagerAdapter {

    private int numberGame=0;
    //    private ArrayList<SummaryFragment> sums = new ArrayList<SummaryFragment>();
    private  SummaryFragment current;
    private FragmentManager fm;
    private boolean cleaning=false;


    public TabSummaryAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fm = fragmentManager;
//        List<Fragment> lf = fragmentManager.getFragments();
//        if(lf != null && lf.size()>0){
//            for(Fragment f : lf){
//                if(f instanceof SummaryFragment){
//                    sums.put(numberGame, (SummaryFragment)f);
//                    numberGame++;
//                }
//            }
//        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if(position < numberGame)
            return "Game  " + (position+1);
        else{
            return null;
        }
    }

    @Override
    public Fragment getItem(int index) {
        if(index < numberGame){
            return current;
//            return sums.get(index);
        }else {
            return null;
        }
    }

    public void restore(List<Fragment> lista, int numGames ){
        for(int i=0;i<lista.size();i++ ) {
            if (i < numGames) {
                Fragment f = lista.get(i);
                if (f instanceof SummaryFragment) {
                    SummaryFragment s = (SummaryFragment) f;
                    numberGame++;
                    current = (SummaryFragment) f;
                }
            }
        }
    }

    public void restore(BurracoSession sessione){

        for(Game g : sessione.getGames()){
            this.addGame(g, sessione.getTeamA().getAlias(), sessione.getTeamB().getAlias());
        }
    }

    public void addGame(Game game, String aliasA, String aliasB){
        SummaryFragment temp = new SummaryFragment();
        temp.setArguments(prepareBundle(game, aliasA, aliasB));
//        sums.put(numberGame, temp);
        current = temp;
        numberGame++;
    }

    private Bundle prepareBundle(Game g, String aA, String aB){
        Bundle b = new Bundle();
        b.putParcelable(Constants.currentGame, g);
        b.putString(Constants.teamAAlias, aA);
        b.putString(Constants.teamBAlias, aB);
        return b;
    }

    @Override
    public int getCount() {
        return numberGame;
    }

//    public void disableLastGame() {
//        sums.get(numberGame-1).setEnableAdd(false);
//    }
//
//    public void enableLastGame() {
//        sums.get(numberGame-1).setEnableAdd(true);
//    }

    public void clearLastGame(){
//        sums.get(numberGame-1).resetGames();
        current.resetGames();
    }

    public void clearAll(){
        List<Fragment> gg = fm.getFragments();
//        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment g : gg){
            if (g instanceof SummaryFragment){
                ((SummaryFragment) g).resetAll();
            }
        }
//        ft.commit();

//        for(int i=sums.size()-1 ; i >= 0 ;i--) {
//            super.destroyItem(null, i, sums.get(i));
//            sums.get(i).resetAll();
//            sums.remove(i);
//        }
//        sums.clear();
//        current.resetAll();
        numberGame=1;
        cleaning= true;
    }

    @Override
    public int getItemPosition(Object object) {
        SummaryFragment f = (SummaryFragment)object;
        if(cleaning) {
            f.resetAll();
            return POSITION_NONE;
        }else{
            return POSITION_UNCHANGED;
        }
    }

    public void retain(){
        cleaning = false;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}