package com.example.bhopalmetroapp;

public class helplinesHelp {
    private int logoImage;
    private String logoName;
    private String mobileNumber;

    public helplinesHelp(int logoImage, String logoName, String mobileNumber) {
        this.logoImage = logoImage;
        this.logoName = logoName;
        this.mobileNumber = mobileNumber;
    }

    public int getLogoImage() {
        return logoImage;
    }

    public String getLogoName() {
        return logoName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

}
