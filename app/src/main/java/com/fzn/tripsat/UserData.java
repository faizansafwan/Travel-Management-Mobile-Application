package com.fzn.tripsat;

public class UserData {

    String firstName, lastName, profileUrl,  date, description, destination, image1, image2, createdDate;

    public UserData(String firstName, String lastName, String date, String description,
                    String destination, String image1, String image2, String profileUrl, String createdDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.description = description;
        this.destination = destination;
        this.image1 = image1;
        this.image2 = image2;
        this.profileUrl = profileUrl;
        this.createdDate = createdDate;
    }

    public UserData() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getDestination() {
        return destination;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
