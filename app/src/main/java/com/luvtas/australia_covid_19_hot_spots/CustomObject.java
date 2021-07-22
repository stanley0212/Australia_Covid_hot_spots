package com.luvtas.australia_covid_19_hot_spots;

class CustomObject {
    private String MapID;
    private String UserEmail;
    private String Lat;
    private String Lng;
    private String title;
    private String info;
    private String iConPicture;
    private int Mtype;
    private String MapStartTime;
    private String MapEndTime;
    private String SmallPhoto;
    private String Address;

    public CustomObject(String mapID, String lat, String lng, String title, String info, String iConPicture, String smallPhoto, String address, int mytype, String mapStartTime, String mapEndTime, String userEmail) {
        this.MapID = mapID;
        Lat = lat;
        Lng = lng;
        this.title = title;
        this.info = info;
        this.iConPicture = iConPicture;
        Mtype = mytype;
        MapStartTime = mapStartTime;
        MapEndTime = mapEndTime;
        SmallPhoto = smallPhoto;
        Address = address;
        UserEmail = userEmail;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSmallPhoto() {
        return SmallPhoto;
    }

    public void setSmallPhoto(String smallPhoto) {
        SmallPhoto = smallPhoto;
    }

    public String getMapStartTime() {
        return MapStartTime;
    }

    public void setMapStartTime(String mapStartTime) {
        MapStartTime = mapStartTime;
    }

    public String getMapEndTime() {
        return MapEndTime;
    }

    public void setMapEndTime(String mapEndTime) {
        MapEndTime = mapEndTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMapID() {
        return MapID;
    }

    public void setMapID(String mapID) {
        MapID = mapID;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getiConPicture() {
        return iConPicture;
    }

    public void setiConPicture(String iConPicture) {
        this.iConPicture = iConPicture;
    }

    public int getMtype() {
        return Mtype;
    }

    public void setMtype(int mtype) {
        Mtype = mtype;
    }
}
