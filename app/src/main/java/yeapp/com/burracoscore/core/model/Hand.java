package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hand implements Parcelable {

    private int carte;
    private int chiusura;
    private int mazzetto;
    private int base;
    private int totale;

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

    public int getTotaleMano() {
        return totale;
    }

    public Hand(int base, int carte, boolean chiusura, boolean mazzetto) {
        this.carte = carte;
        this.base = base;
        this.chiusura = chiusura ? 100 : 0;
        this.mazzetto = mazzetto ? 0 : -100;
        this.totale = this.carte + this.chiusura + this.mazzetto + this.base;
    }

    public Hand(Parcel in) {
        this.base = in.readInt();
        this.carte = in.readInt();
        this.chiusura = in.readInt();
        this.mazzetto = in.readInt();
        this.totale = in.readInt();
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
    }

    public static final Parcelable.Creator<Hand> CREATOR = new Parcelable.Creator<Hand>() {

        public Hand createFromParcel(Parcel in) {
            return new Hand(in);
        }

        public Hand[] newArray(int size) {
            return new Hand[size];
        }


    };
}
