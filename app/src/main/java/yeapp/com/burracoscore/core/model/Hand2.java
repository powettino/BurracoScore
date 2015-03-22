package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hand2 implements Parcelable {

    public static final int WON = 1;
    public static final int LOST=-1;
    public static final int PAIR = 0;

    private int carte;
    private int chiusura;
    private int mazzetto;
    private int base;
    private int totale;
    private int won = PAIR;
    private int numeroMano;

    @SuppressWarnings("unused")
    public int getCarte() {
        return carte;
    }

    @SuppressWarnings("unused")
    public int getChiusura() {
        return chiusura;
    }

    @SuppressWarnings("unused")
    public int getMazzetto() {
        return mazzetto;
    }

    @SuppressWarnings("unused")
    public int getBase() {
        return base;
    }

    public int getNumeroMano(){
        return numeroMano;
    }

    public int getTotaleMano() {
        return totale;
    }

    public int getWon(){return won;}

    public Hand2(int base, int carte, boolean chiusura, boolean mazzetto, int numMano) {
        this.carte = carte;
        this.base = base;
        numeroMano = numMano;
        this.chiusura = chiusura ? 100 : 0;
        this.mazzetto = mazzetto ? 0 : -100;
        this.totale = this.carte + this.chiusura + this.mazzetto + this.base;
    }

    public void setWon(int vinto){
        this.won = vinto;
    }

    public Hand2(Parcel in) {
        this.base = in.readInt();
        this.carte = in.readInt();
        this.chiusura = in.readInt();
        this.mazzetto = in.readInt();
        this.totale = in.readInt();
        this.won = in.readInt();
        numeroMano = in.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.base);
        dest.writeInt(this.carte);
        dest.writeInt(this.chiusura);
        dest.writeInt(this.mazzetto);
        dest.writeInt(this.totale);
        dest.writeInt(won);
        dest.writeInt(numeroMano);
//        dest.writeInt(won ? 1 : 0);
    }

    public static final Creator<Hand2> CREATOR = new Creator<Hand2>() {

        public Hand2 createFromParcel(Parcel in) {
            return new Hand2(in);
        }

        public Hand2[] newArray(int size) {
            return new Hand2[size];
        }
    };
}
