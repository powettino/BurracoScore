package yeapp.com.burracoscore.core.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import yeapp.com.burracoscore.R;

/**
 * Created by iacopo on 14/01/15.
 */
public class Team implements Parcelable {
    private String player1 = null;
    private String player2 = null;
    private String side;
    private ArrayList<Integer> punteggio = new ArrayList<Integer>();
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
        String[] data = new String[2];
        in.readStringArray(data);
        this.player1 = data[0];
        this.player2 = data[1];
        in.readList(this.punteggio, this.getClass().getClassLoader());
        totale = in.readInt();

    }

    public void addGame() {
        gameVinti++;
    }

    public int getGame() {
        return gameVinti;
    }

    public int getTotale() {
        return totale;
    }

    public ArrayList<Integer> getPunti() {
        return this.punteggio;
    }

    public void addPunti(int punti) {
        totale += punti;
        punteggio.add(punti);
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
        dest.writeArray(new String[]{this.player1, this.player2});
        dest.writeList(punteggio);
        dest.writeInt(totale);
    }


    static final Parcelable.Creator<Team> CREATOR
            = new Parcelable.Creator<Team>() {

        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public void cleanPunteggio() {
        this.punteggio.clear();
        this.totale = 0;
    }

    public void cleanGames() {
        this.gameVinti = 0;
    }
}
