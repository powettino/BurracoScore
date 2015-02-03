package yeapp.com.burracoscore.core.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Team implements Parcelable {
    private String player1 = null;
    private String player2 = null;
    private String side;
    private ArrayList<Integer> punteggio = new ArrayList<Integer>();
    private ArrayList<Hand> mani = new ArrayList<Hand>();
    private int totale = 0;
    private int gameVinti = 0;

    public Team(String side) {
        setSide(side);
    }

    public Team(String nomePlayer1, String nomePlayer2) {
        setPlayer1(nomePlayer1);
        setPlayer2(nomePlayer2);
    }

    public Team(String side, String nomePlayer1, String nomePlayer2) {
        setSide(side);
        setPlayer1(nomePlayer1);
        setPlayer2(nomePlayer2);
    }

    public Team(Parcel in) {
        side = in.readString();
        this.player1 = in.readString();
        this.player2 = in.readString();
        in.readList(punteggio, ArrayList.class.getClassLoader());
        in.readList(mani, ArrayList.class.getClassLoader());
        totale = in.readInt();
        gameVinti = in.readInt();
    }

    public void addGame() {
        gameVinti++;
    }

    public int getTotGames() {
        return gameVinti;
    }

    public int getNumberHands() {
        return mani.size();
    }

    public int getTotale() {
        return totale;
    }

    public ArrayList<Integer> getPunti() {
        return punteggio;
    }

    public void addMano(Hand mano) {
        totale += mano.getTotaleMano();
        punteggio.add(mano.getTotaleMano());
        mani.add(mano);
    }

    public void cleanTeam() {
        player1 = null;
        player2 = null;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(side);
        dest.writeString(this.player1);
        dest.writeString(this.player2);
        dest.writeList(punteggio);
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
        punteggio.clear();
        mani.clear();
        this.totale = 0;
    }

    public void cleanGames() {
        this.gameVinti = 0;
    }
}
