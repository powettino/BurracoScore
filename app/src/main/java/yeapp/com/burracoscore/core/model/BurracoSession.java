package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import yeapp.com.burracoscore.utils.Utils;

public class BurracoSession implements Parcelable {

    private ArrayList<Game> game;
    private Team2 team_a;
    private Team2 team_b;
    private int numero_vinti_a;
    private int numero_vinti_b;

    public BurracoSession(){
        game = new ArrayList<Game>();
        team_a = new Team2(Utils.ASide);
        team_b = new Team2(Utils.BSide);
        numero_vinti_a = 0;
        numero_vinti_b = 0;
        addNewGame();
    }

    public Team2 getTeamA() {
        return team_a;
    }

    public void clear(){
        game.clear();
        addNewGame();
        team_a = new Team2(Utils.ASide);
        team_b = new Team2(Utils.BSide);
        numero_vinti_a = 0;
        numero_vinti_b = 0;
    }

    public void clearCurrentGame(){
        game.get(game.size()-1).clear();
    }

    public void addNumeroVinti(char side){
        if(side == Utils.ASide){
            numero_vinti_a++;
        }else{
            numero_vinti_b++;
        }
    }

    public void updateLastGame(Game g){
        game.set(game.size()-1, g);
    }

    public void setTeamA(Team2 team_a) {
        this.team_a = team_a;
    }

    public Team2 getTeamB() {
        return team_b;
    }

    public void setTeamB(Team2 team_b) {
        this.team_b = team_b;
    }

    public String getNumeroVintiA() {
        return String.valueOf(numero_vinti_a);
    }

    public void setNumeroVintiA(int numero_vinti_a) {
        this.numero_vinti_a = numero_vinti_a;
    }

    public int getNumeroVintiB() {
        return numero_vinti_b;
    }

    public void setNumeroVintiB(int numero_vinti_b) {
        this.numero_vinti_b = numero_vinti_b;
    }

    public ArrayList<Game> getGames() {
        return game;
    }

    public Game getCurrentGame (){
        return game.get(game.size()-1);
    }

    public void addNewGame (){
        game.add(new Game());
    }

    public BurracoSession(Parcel in) {
        numero_vinti_a = in.readInt();
        numero_vinti_b = in.readInt();
        in.readList(game, getClass().getClassLoader());
        team_a = in.readParcelable(BurracoSession.class.getClassLoader());
        team_b = in.readParcelable(BurracoSession.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numero_vinti_a);
        dest.writeInt(numero_vinti_b);
        dest.writeList(game);
        dest.writeParcelable(team_a, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(team_b, PARCELABLE_WRITE_RETURN_VALUE);

    }
    public static final Creator<BurracoSession> CREATOR = new Creator<BurracoSession>() {

        public BurracoSession createFromParcel(Parcel in) {
            return new BurracoSession(in);
        }

        public BurracoSession[] newArray(int size) {
            return new BurracoSession[size];
        }
    };
}