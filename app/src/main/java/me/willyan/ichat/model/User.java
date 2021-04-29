package me.willyan.ichat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String photoUrl;
    private String uuid;
    private String name;

    public User(){}

    public User(String uuid, String photoUrl, String name) {
        this.uuid = uuid;
        this.photoUrl = photoUrl;
        this.name = name;
    }

    protected User(Parcel in) {
        photoUrl = in.readString();
        uuid = in.readString();
        name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoUrl);
        dest.writeString(uuid);
        dest.writeString(name);
    }
}
