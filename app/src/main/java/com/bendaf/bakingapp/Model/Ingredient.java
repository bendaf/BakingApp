package com.bendaf.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bendaf on 2018. 08. 20. BakingApp.
 */

class Ingredient implements Parcelable {
    private int quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    private Ingredient(Parcel in) {
        this.quantity = in.readInt();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
