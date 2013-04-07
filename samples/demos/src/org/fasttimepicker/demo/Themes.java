package org.fasttimepicker.demo;

public enum Themes {
    DEFAULT(0, android.R.style.Theme_DeviceDefault, "Device Default"),
    HOLO(1, android.R.style.Theme_Holo, "Holo"),
    HOLO_WHITE(2, android.R.style.Theme_Holo_Light, "Holo White");

    private int mPosition;
    private int mId;
    private String mName;

    Themes(int mPosition, int mId, String mName) {
        this.mPosition = mPosition;
        this.mId = mId;
        this.mName = mName;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getId() {
        return mId;
    }

    @Override
    public String toString() {
        return mName;
    }
}
