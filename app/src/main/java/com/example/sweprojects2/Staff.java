package com.example.sweprojects2;

public class Staff {
    private int staffId;
    private String staffName;
    private float rating;
    private String specialty;

    public Staff(int staffId, String staffName, float rating, String specialty) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.rating = rating;
        this.specialty = specialty;
    }

    // Getters and setters

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}