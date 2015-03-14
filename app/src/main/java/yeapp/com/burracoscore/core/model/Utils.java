package yeapp.com.burracoscore.core.model;

/**
 * Created by powered on 14/03/15.
 */
public class Utils {
    public static final char ASide ='A';
    public static final char BSide = 'B';
    public static final String giocatore1 = "giocatore 1";
    public static final String giocatore2 = "giocatore 2";

    public static String getDefaultTeamName(char side){
        return "Team " + side;
    }
}
