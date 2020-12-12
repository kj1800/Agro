package com.example.agro_lens.CropHistory;

public class Modelhistory {
    String cropage,cropname,date,desc,image,latitude,longitude,time,generatetext,username;

    public Modelhistory() {
    }

    public Modelhistory(String cropage, String cropname, String date, String desc, String image, String latitude, String longitude, String time, String generatetext, String username) {
        this.cropage = cropage;
        this.cropname = cropname;
        this.date = date;
        this.desc = desc;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.generatetext = generatetext;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCropage() {
        return cropage;
    }

    public void setCropage(String cropage) {
        this.cropage = cropage;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGeneratetext() {
        return generatetext;
    }

    public void setGeneratetext(String generatetext) {
        this.generatetext = generatetext;
    }
}
