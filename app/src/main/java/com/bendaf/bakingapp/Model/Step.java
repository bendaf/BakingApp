package com.bendaf.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bendaf on 2018. 08. 20. BakingApp.
 */

public class Step implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;

    public Step() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
    }

    private Step(Parcel in) {
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
    }

    static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
