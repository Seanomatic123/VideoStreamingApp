package com.example.videostreamingapp;

import java.io.Serializable;

public class ItemModel {
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
//    @Override
//    public boolean equals(@Nullable Object obj) {
//        if(obj == this)
//            return true;
//        if(!(obj instanceof Item))
//            return false;
//        Item item = (Item) obj;
//        return Objects.equals(item.email, this.email);
//    }
}
