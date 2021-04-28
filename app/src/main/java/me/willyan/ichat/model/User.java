package me.willyan.ichat.model;

public class User {

    private final String photoUrl;
    private final String uuid;
    private final String name;

    public User(String uuid, String photoUrl, String name) {
        this.uuid = uuid;
        this.photoUrl = photoUrl;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUuid() {
        return uuid;
    }

}
