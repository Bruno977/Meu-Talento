package com.example.meutalento.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserAccountSettings {

    private String description;
    private String display_name;
    private long followers;
    private long following;
    private long posts;
    private String profile_photo;
    private String username;
    private String website;

    public UserAccountSettings(String description, String display_name, long followers, long following,
                               long posts, String profile_photo, String username, String website) {
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.username = username;
        this.website = website;
    }

}
