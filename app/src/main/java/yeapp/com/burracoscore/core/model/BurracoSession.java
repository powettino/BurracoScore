package yeapp.com.burracoscore.core.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

import yeapp.com.burracoscore.core.database.columns.GameColumns;
import yeapp.com.burracoscore.core.database.columns.HandColumns;
import yeapp.com.burracoscore.core.database.columns.SessionColumns;
import yeapp.com.burracoscore.core.database.columns.TeamColumns;
import yeapp.com.burracoscore.utils.Utils;

public class BurracoSession implements Parcelable {

    private ArrayList<Game> games;
    private long id = -1;
    private Team team_a;
    private Team team_b;
    private int numero_vinti_a;
    private int numero_vinti_b;

    public BurracoSession() {
        id = System.currentTimeMillis();
        games = new ArrayList<Game>();
        team_a = new Team(Utils.ASide);
        team_b = new Team(Utils.BSide);
        numero_vinti_a = 0;
        numero_vinti_b = 0;
        addNewGame(games.size() + 1);
        Log.d("Sessione", "creata la sessione con id: " + id);
    }

    public BurracoSession(Cursor cSession, Cursor cGame, Cursor cTeam) {
        while (cTeam.moveToNext()) {
            char tempSide = cTeam.getString(cTeam.getColumnIndex(TeamColumns.SIDE)).charAt(0);
            if (tempSide == Utils.ASide) {
                team_a = new Team(tempSide,
                        cTeam.getString(cTeam.getColumnIndex(TeamColumns.ALIAS)),
                        cTeam.getInt(cTeam.getColumnIndex(TeamColumns.NUMERO_PLAYER)),
                        cTeam.getString(cTeam.getColumnIndex(TeamColumns.PLAYER1)),
                        cTeam.getString(cTeam.getColumnIndex(TeamColumns.PLAYER2)),
                        cTeam.getLong(cTeam.getColumnIndex(TeamColumns.TEAM_ID)));
            } else {
                team_b = new Team(tempSide,
                        cTeam.getString(cTeam.getColumnIndex(TeamColumns.ALIAS)),
                        cTeam.getInt(cTeam.getColumnIndex(TeamColumns.NUMERO_PLAYER)),
                        cTeam.getString(cTeam.getColumnIndex(TeamColumns.PLAYER1)),
                        cTeam.getString(cTeam.getColumnIndex(TeamColumns.PLAYER2)),
                        cTeam.getLong(cTeam.getColumnIndex(TeamColumns.TEAM_ID)));
            }
        }
        while (cSession.moveToNext()) {
            Log.d("Sessione", "cursore: " + cSession.getCount());
            id = cSession.getLong(cSession.getColumnIndex(SessionColumns.SESSION_ID));
            numero_vinti_a = cSession.getInt(cSession.getColumnIndex(SessionColumns.NUMERO_GAME_A));
            numero_vinti_b = cSession.getInt(cSession.getColumnIndex(SessionColumns.NUMERO_GAME_B));
        }
        games = new ArrayList<Game>();
        long idGame = -1;
        Game curGame = null;
        while (cGame.moveToNext()) {
            long tempIdGame = cGame.getLong(cGame.getColumnIndex(GameColumns.GAME_ID));
            if (idGame != tempIdGame) {
                curGame = new Game(
                        tempIdGame,
                        cGame.getInt(cGame.getColumnIndex(GameColumns.NUMERO_MANI)),
                        cGame.getInt(cGame.getColumnIndex(GameColumns.NUMERO_PARTITA)),
                        cGame.getInt(cGame.getColumnIndex(GameColumns.TOTALE_A)),
                        cGame.getInt(cGame.getColumnIndex(GameColumns.TOTALE_B)),
                        cGame.getString(cGame.getColumnIndex(GameColumns.VINCITORE)).charAt(0));
                games.add(curGame);
                idGame = tempIdGame;
            }
            Hand h = new Hand(
                    cGame.getInt(cGame.getColumnIndex(HandColumns.BASE)),
                    cGame.getInt(cGame.getColumnIndex(HandColumns.CARTE)),
                    cGame.getInt(cGame.getColumnIndex(HandColumns.CHIUSURA)),
                    cGame.getInt(cGame.getColumnIndex(HandColumns.MAZZETTO)),
                    cGame.getInt(cGame.getColumnIndex(HandColumns.NUMERO_MANO)),
                    cGame.getInt(cGame.getColumnIndex(HandColumns.TOTALE_MANO)),
                    cGame.getInt(cGame.getColumnIndex(HandColumns.WON)));

            curGame.addMano(h, cGame.getString(cGame.getColumnIndex(HandColumns.SIDE)).charAt(0));
        }

        if (idGame == -1) {
            addNewGame(games.size() + 1);
            Log.d("Sessione", "cursore game: " + idGame);
        }

        Log.d("Sessione", "creata la sessione con id: " + id);

    }

    public Team getTeamA() {
        return team_a;
    }

    public void clear() {
        id = System.currentTimeMillis();
        games.clear();
        addNewGame(games.size() + 1);
        team_a.clean();
        team_b.clean();
        numero_vinti_a = 0;
        numero_vinti_b = 0;
        Log.d("Sessione", "rigenerata con id: " + id);
    }

    public long getId() {
        return id;
    }

    public void clearCurrentGame() {
        games.get(games.size() - 1).clear();
    }

    public int getGameTotali() {
        return games.size();
    }

    public void updateLastGame(Game g) {
        games.set(games.size() - 1, g);
        if (g.getWinner() == Utils.ASide) {
            numero_vinti_a++;
        } else if (g.getWinner() == Utils.BSide) {
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

    public Game getCurrentGame() {
        return games.get(games.size() - 1);
    }

    public void addNewGame(int numberGame) {
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