package yeapp.com.burracoscore.core.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

import yeapp.com.burracoscore.utils.Utils;

public class Team implements Parcelable {
    private String player1 = null;
    private String player2 = null;
    private String imagePath = null;
    private char side;

    private String alias;
    private ArrayList<Hand> mani = new ArrayList<Hand>();
    private int totale = 0;
    private int gameVinti = 0;
    private int numberPlayer = 1;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public int getNumberPlayer() {
        return numberPlayer;
    }

    public void setNumberPlayer(int numberPlayer) {
        this.numberPlayer = numberPlayer;
    }

    public Hand getMano(int numberHand){
        return mani.get(numberHand);
    }

    public Team(char side) {
        setSide(side);
        alias = Utils.getDefaultTeamName(side);
        player1 = Utils.giocatore1;
        player2 = Utils.giocatore2;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Team(Parcel in) {
        side = (char)in.readInt();
        alias = in.readString();
        imagePath = in.readString();
        numberPlayer = in.readInt();
        player1 = in.readString();
        player2 = in.readString();
        in.readList(mani, getClass().getClassLoader());
        totale = in.readInt();
        gameVinti = in.readInt();
    }

    public void addGame() {
        gameVinti++;
    }

    public int getTotGames() {
        return gameVinti;
    }

    public int getTotale() {
        return totale;
    }

    public ArrayList<Integer> getPunti() {
        ArrayList<Integer> punti = new ArrayList<Integer>();
        for(int i = 0; i<mani.size();i++){
            punti.add(mani.get(i).getTotaleMano());
        }
        return punti;
    }

    public ArrayList<Integer> getStatus() {
        ArrayList<Integer> status = new ArrayList<Integer>();
        for(int i = 0; i<mani.size();i++){
            status.add(mani.get(i).getWon());
        }
        return status;
    }

    public void addMano(Hand mano) {
        totale += mano.getTotaleMano();
        mani.add(mano);
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(side);
        dest.writeString(alias);
        dest.writeString(imagePath);
        dest.writeInt(numberPlayer);
        dest.writeString(player1);
        dest.writeString(player2);
        dest.writeList(mani);
        dest.writeInt(totale);
        dest.writeInt(gameVinti);

    }

    public static final Parcelable.Creator<Team> CREATOR
            = new Parcelable.Creator<Team>() {

        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public void cleanPunteggio() {
        mani.clear();
        this.totale = 0;
    }

    public void clean() {
        this.gameVinti = 0;
        numberPlayer = 1;
        player1 = Utils.giocatore1;
        player2 = Utils.giocatore2;
        alias = Utils.getDefaultTeamName(side);
    }

}
