package yeapp.com.burracoscore.core.model;

import yeapp.com.burracoscore.R;

/**
 * Created by iacopo on 14/01/15.
 */
public class Team {
    private String player1 = null;
    private String player2 = null;
    private String side;

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
}
