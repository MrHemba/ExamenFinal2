package com.example.examenfinal;

public class User {
    private String name, country, city, email, imageUrl, address, phone, cell, nationality;
    private double latitude, longitude;
    private int age;

    public User(String name, String country, String city, String email, String imageUrl, String address, double latitude, double longitude, int age, String phone, String cell, String nationality) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.email = email;
        this.imageUrl = imageUrl;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.age = age;
        this.phone = phone;
        this.cell = cell;
        this.nationality = nationality;
    }

    // Getters para cada campo
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public String getEmail() { return email; }
    public String getImageUrl() { return imageUrl; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }
    public String getCell() { return cell; }
    public String getNationality() { return nationality; }
}
