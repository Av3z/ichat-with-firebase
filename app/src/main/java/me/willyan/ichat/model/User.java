package me.willyan.ichat.model;

public class User {

    private String photoUrl;
    private String uuid;
    private String name;

    public User(){}

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
