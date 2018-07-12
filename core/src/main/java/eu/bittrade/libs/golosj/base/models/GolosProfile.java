package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 "profile": {
 "profile_image": "http://savepic.ru/14500814.jpg",
 "name": "Настя",
 "about": "ЗОЖ. Программирование. Оптимизация. Блокчейн. И немножечко сарказма.",
 "location": "Севастополь",
 "website": "http://vk.com/lokkie12",
 "cover_image": "https://pp.userapi.com/c840025/v840025118/1f99/NgqlA1cA6Y0.jpg"
 }
 }
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GolosProfile {
    @JsonProperty("profile_image")
    private String profileImage;
    @JsonProperty("name")
    private String shownName;
    @JsonProperty("about")
    private String about;
    @JsonProperty("location")
    private String location;
    @JsonProperty("website")
    private String website;
    @JsonProperty("cover_image")
    private String coverImage;
    @JsonProperty("gender")
    private String gender;

    public GolosProfile(String profileImage, String shownName, String about, String location, String website, String coverImage, String gender) {
        this.profileImage = profileImage;
        this.shownName = shownName;
        this.about = about;
        this.location = location;
        this.website = website;
        this.coverImage = coverImage;
        this.gender = gender;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public GolosProfile() {
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getShownName() {
        return shownName;
    }

    public void setShownName(String shownName) {
        this.shownName = shownName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "GolosProfile{" +
                "profileImage='" + profileImage + '\'' +
                ", shownName='" + shownName + '\'' +
                ", about='" + about + '\'' +
                ", location='" + location + '\'' +
                ", website='" + website + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
