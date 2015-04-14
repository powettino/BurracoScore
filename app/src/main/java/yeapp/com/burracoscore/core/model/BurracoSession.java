package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

import yeapp.com.burracoscore.utils.Utils;

public class BurracoSession implements Parcelable {

    private ArrayList<Game> games;
    private long id;
    private Team team_a;
    private Team team_b;
    private int numero_vinti_a;
    private int numero_vinti_b;

    public BurracoSession(){
        id = System.currentTimeMillis();
        games = new ArrayList<Game>();
        team_a = new Team(Utils.ASide);
        team_b = new Team(Utils.BSide);
        numero_vinti_a = 0;
        numero_vinti_b = 0;
        addNewGame(games.size()+1);
        Log.d("Sessione" , "creata la sessione con id: "+ id);
    }

    public Team getTeamA() {
        return team_a;
    }

    public void clear(){
        id = System.currentTimeMillis();
        games.clear();
        addNewGame(games.size()+1);
        team_a.clean();
        team_b.clean();
        numero_vinti_a = 0;
        numero_vinti_b = 0;
        Log.d("Sessione" , "rigenerata con id: "+ id);
    }

    public long getId() {
        return id;
    }

    public void clearCurrentGame(){
        games.get(games.size()-1).clear();
    }

    public int getGameTotali(){
        return games.size();
    }

    public void updateLastGame(Game g){
        games.set(games.size()-1, g);
        if(g.getWinner() == Utils.ASide){
            numero_vinti_a++;
        }else if (g.getWinner() == Utils.BSide){
            numero_vinti_b++;
        }
    }

    public void setTeamA(Team team_a) {
        this.team_a = team_a;
    }

    public Team getTeamB() {
        return team_b;
    }

    public void setTeamB(Team team_b) {
        this.team_b = team_b;
    }

    public String getNumeroVintiA() {
        return String.valueOf(numero_vinti_a);
    }

    @SuppressWarnings("unused")
    public void setNumeroVintiA(int numero_vinti_a) {
        this.numero_vinti_a = numero_vinti_a;
    }

    public int getNumeroVintiB() {
        return numero_vinti_b;
    }

    @SuppressWarnings("unused")
    public void setNumeroVintiB(int numero_vinti_b) {
        this.numero_vinti_b = numero_vinti_b;
    }

    @SuppressWarnings("unused")
    public ArrayList<Game> getGames() {
        return games;
    }

    public Game getCurrentGame (){
        return games.get(games.size()-1);
    }

    public void addNewGame (int numberGame){
        games.add(new Game(numberGame));
    }

    public BurracoSession(Parcel in) {
        numero_vinti_a = in.readInt();
        numero_vinti_b = in.readInt();
        in.readList(games, getClass().getClassLoader());
        team_a = in.readParcelable(BurracoSession.class.getClassLoader());
        team_b = in.readParcelable(BurracoSession.class.getClassLoader());
        id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numero_vinti_a);
        dest.writeInt(numero_vinti_b);
        dest.writeList(games);
        dest.writeParcelable(team_a, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(team_b, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeLong(id);

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