package com.example.videostreamingapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class ItemModel implements Parcelable {
    String name;
    String email;
    int image;
    String description;

    public ItemModel(String name, String email, int image, String description) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.description = description;
    }

    protected ItemModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        image = in.readInt();
        description = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeInt(image);
        parcel.writeString(description);
    }
}
