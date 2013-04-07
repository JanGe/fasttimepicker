package org.fasttimepicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public final class ParcelableTime implements Parcelable {

    // ////////////////////////////
    // Parcelable apis
    // ////////////////////////////
    public static final Parcelable.Creator<ParcelableTime> CREATOR =
            new Parcelable.Creator<ParcelableTime>() {
                public ParcelableTime createFromParcel(Parcel p) {
                    Time time = new Time();
                    time.set(p.readLong());
                    return new ParcelableTime(time);
                }

                public ParcelableTime[] newArray(int size) {
                    return new ParcelableTime[size];
                }
            };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeLong(time.toMillis(false));
    }
    // ////////////////////////////
    // end Parcelable apis
    // ////////////////////////////

    private Time time;

    public ParcelableTime() {
        this(new Time());
    }

    public ParcelableTime(Time time) {
        this.time = time;
    }

    public final Time asTime() {
        return time;
    }

}
