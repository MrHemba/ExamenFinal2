package com.example.examenfinal;

public class User {

    private String name;
    private String country;
    private String city;
    private String email;
    private String imageUrl;
    private String address;
    private int age;
    private String phone;
    private String cell;
    private String nationality;
    private double latitude;
    private double longitude;

    // Constructor
    public User(String name, String country, String city, String email, String imageUrl, String address, int age,
                String phone, String cell, String nationality, double latitude, double longitude) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.email = email;
        this.imageUrl = imageUrl;
        this.address = address;
        this.age = age;
        this.phone = phone;
        this.cell = cell;
        this.nationality = nationality;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getCell() {
        return cell;
    }

    public String getNationality() {
        return nationality;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
