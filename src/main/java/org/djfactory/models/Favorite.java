package org.djfactory.models;

import static org.djfactory.Main.API_KEY;

public class Favorite {
    String id;
    String userId;
    String imageId;
    String subId;
    String createdAt;
    ImageCat image;
    String apiKey = API_KEY;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImageCat getImage() {
        return image;
    }

    public void setImage(ImageCat image) {
        this.image = image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}