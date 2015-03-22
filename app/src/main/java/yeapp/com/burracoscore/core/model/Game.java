package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import yeapp.com.burracoscore.utils.Utils;

public class Game implements Parcelable {

    private int totalePartitaA;
    private int totalePartitaB;
    private int numeroMani;

    private ArrayList<Hand2> maniA = new ArrayList<Hand2>();
    private ArrayList<Hand2> maniB = new ArrayList<Hand2>();

    private char winner;

    public Game() {
        totalePartitaA = 0;
        totalePartitaB = 0;
        numeroMani = 0;
        winner = 0;
    }

    public void clear(){
        totalePartitaA =0;
        totalePartitaB = 0;
        numeroMani = 0;
        winner = 0;
        maniA.clear();
        maniB.clear();
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

    public Hand2 getMano(int position, char side){
        if(side==Utils.ASide){
            return maniA.get(position);
        }else{
            return maniB.get(position);
        }
    }

    public int getNumeroMani() {
        return numeroMani;
    }

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

    public void addMano(Hand2 manoA, Hand2 manoB) {
        totalePartitaA += manoA.getTotaleMano();
        maniA.add(manoA);
        totalePartitaB += manoB.getTotaleMano();
        maniB.add(manoB);
        numeroMani++;
    }

    public Game(Parcel in) {
        totalePartitaA = in.readInt();
        totalePartitaB = in.readInt();
        in.readList(maniA, getClass().getClassLoader());
        in.readList(maniB, getClass().getClassLoader());
        numeroMani = in.readInt();
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
