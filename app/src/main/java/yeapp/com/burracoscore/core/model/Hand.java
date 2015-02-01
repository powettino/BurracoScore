package yeapp.com.burracoscore.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hand implements Parcelable {

    public int getCarte() {
        return carte;
    }

    public int getChiusura() {
        return chiusura;
    }

    public int getMazzetto() {
        return mazzetto;
    }

    public int getBase() {
        return base;
    }

    private int carte;
    private int chiusura;
    private int mazzetto;
    private int base;

    public Hand(int base, int carte, boolean chiusura, boolean mazzetto) {
        this.carte = carte;
        this.base = base;
        this.chiusura = chiusura ? 100 : 0;
        this.mazzetto = mazzetto ? 0 : -100;
    }

    public Hand(Parcel in) {
        this.base = in.readInt();
        this.carte = in.readInt();
        this.chiusura = in.readInt();
        this.mazzetto = in.readInt();
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
