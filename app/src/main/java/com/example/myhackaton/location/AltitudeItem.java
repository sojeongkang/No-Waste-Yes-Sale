package com.example.myhackaton.location;

/**
 * Created by Choong on 16. 6. 20..
 */
public class AltitudeItem {

    private String location_key;
    private float elevation = 0.0f;
    private float atmosphericPressure = 0.0f;
    private String address;
    private String localizedName;
    private float temperature = 0.0f;
    private String mobileLink = "http://www.accuweather.com/";

    public AltitudeItem() {

    }

    public String getLocation_key() {
        return location_key;
    }

    public void setLocation_key(String location_key) {
        this.location_key = location_key;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public float getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(float atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    @Override
    public String toString() {
        return "AltitudeItem{" +
                "location_key='" + location_key + '\'' +
                ", elevation=" + elevation +
                ", atmosphericPressure=" + atmosphericPressure +
                ", address='" + address + '\'' +
                ", localizedName='" + localizedName + '\'' +
                ", temperature=" + temperature +
                ", mobileLink='" + mobileLink + '\'' +
                '}';
    }
}
