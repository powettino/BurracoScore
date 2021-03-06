package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

import yeapp.com.burracoscore.utils.Utils;

public class Game implements Parcelable {

    private int totalePartitaA;
    private int totalePartitaB;
    private int numeroMani;
    private ArrayList<Hand> maniA = new ArrayList<Hand>();
    private ArrayList<Hand> maniB = new ArrayList<Hand>();
    private char winner = 0;
    private int numberOfGame=0;
    private long id;

    public Game(int num) {
        totalePartitaA = 0;
        totalePartitaB = 0;
        numeroMani = 0;
        winner = 0;
        numberOfGame=num;
        id = System.currentTimeMillis();
        Log.d("Game", "creata il gioco con id: " + id);
    }

    public Game(long id, int numeroMani, int numeroPartita, int totaleA, int totaleB, char vincitore) {
        this.id = id;
        this.numeroMani = numeroMani;
        this.numberOfGame = numeroPartita;
        this.totalePartitaA = totaleA;
        this.totalePartitaB = totaleB;
        this.winner = vincitore;
        Log.d("Game", "ricreato con id: " + id);
    }

    public int getGameNumber(){
        return numberOfGame;
    }

    public void clear(){
        id = System.currentTimeMillis();
        totalePartitaA =0;
        totalePartitaB = 0;
        numeroMani = 0;
        winner = 0;
        maniA.clear();
        maniB.clear();
        Log.d("Game" , "pulita: "+ id);
    }

    public long getId(){
        return id;
    }

    public char getWinner() {
        return winner;
    }

    public void setWinner(char win) {
        winner = win;
    }

    public int getTotalePartita(char side) {
        if (side == Utils.ASide) {
            return totalePartitaA;
        } else {
            return totalePartitaB;
        }
    }

    public Hand getMano(int position, char side){
        if(side==Utils.ASide){
            return maniA.get(position);
        }else{
            return maniB.get(position);
        }
    }

    public Hand getLastMano(char side){
        return getMano(getNumeroMani()-1, side);
    }

    public int getNumeroMani() {
        return numeroMani;
    }

    @SuppressWarnings("unused")
    public void setNumeroMani(int numeroMani) {
        this.numeroMani = numeroMani;
    }

    public ArrayList<Integer> getPuntiMani(char side) {
        ArrayList<Integer> punti = new ArrayList<Integer>();
        if(side== Utils.ASide){
            for(int i = 0; i<maniA.size();i++){
                punti.add(maniA.get(i).getTotaleMano());
            }
        }else{
            for(int i = 0; i<maniB.size();i++){
                punti.add(maniB.get(i).getTotaleMano());
            }
        }
        return punti;
    }

    public ArrayList<Integer> getStatusMani(char side) {
        ArrayList<Integer> status = new ArrayList<Integer>();
        if (side == Utils.ASide) {
            for (int i = 0; i < maniA.size(); i++) {
                status.add(maniA.get(i).getWon());
            }
        }else{
            for (int i = 0; i < maniB.size(); i++) {
                status.add(maniB.get(i).getWon());
            }
        }
        return status;
    }

    public void addMano(Hand manoA, Hand manoB) {
        totalePartitaA += manoA.getTotaleMano();
        maniA.add(manoA);
        totalePartitaB += manoB.getTotaleMano();
        maniB.add(manoB);
        numeroMani++;
    }

    public void addMano(Hand mano, char side){
        if(side==Utils.ASide){
            maniA.add(mano);
        }else{
            maniB.add(mano);
        }
    }

    public Game(Parcel in) {
        totalePartitaA = in.readInt();
        totalePartitaB = in.readInt();
        in.readList(maniA, getClass().getClassLoader());
        in.readList(maniB, getClass().getClassLoader());
        numeroMani = in.readInt();
        numberOfGame = in.readInt();
        id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalePartitaA);
        dest.writeInt(totalePartitaB);
        dest.writeList(maniA);
        dest.writeList(maniB);
        dest.writeInt(numeroMani);
        dest.writeInt(numberOfGame);
        dest.writeLong(id);
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {

        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
