package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import yeapp.com.burracoscore.utils.Utils;

public class Team implements Parcelable {
    private String player1 = null;
    private String player2 = null;
    private String imagePath = null;
    private char side;
    private String alias;
    private int numberPlayer = 1;
    private long id;

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

    public long getId(){
        return id;
    }

    public Team(char side) {
        setSide(side);
        alias = Utils.getDefaultTeamName(side);
        player1 = Utils.giocatore1;
        player2 = Utils.giocatore2;
        id = System.currentTimeMillis();
    }

    public Team(char sideDb, String aliasDb, int numeroPlayerDb, String player1Db, String player2Db, long idDb){
        setSide(sideDb);
        alias = aliasDb;
        player1 = player1Db;
        player2 = player2Db;
        numberPlayer = numeroPlayerDb;
        id = idDb;
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
        id = in.readLong();
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
        dest.writeLong(id);
    }

    public static final Creator<Team> CREATOR
            = new Creator<Team>() {

        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public void clean() {
        numberPlayer = 1;
        player1 = Utils.giocatore1;
        player2 = Utils.giocatore2;
        alias = Utils.getDefaultTeamName(side);
        id = System.currentTimeMillis();
    }

}
