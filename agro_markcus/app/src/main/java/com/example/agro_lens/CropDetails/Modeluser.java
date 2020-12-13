package com.example.agro_lens.CropDetails;

public class Modeluser {
    String basic,cropname,imageurl,temperature,soiltype,places,season,cropimageurl;

    public Modeluser() {
    }


    public Modeluser(String basic, String cropname, String imageurl, String temperature, String soiltype, String places, String season, String cropimageurl) {
        this.basic = basic;
        this.cropname = cropname;
        this.imageurl = imageurl;
        this.temperature = temperature;
        this.soiltype = soiltype;
        this.places = places;
        this.season = season;
        this.cropimageurl = cropimageurl;
    }


    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCropimageurl() {
        return cropimageurl;
    }

    public void setCropimageurl(String cropimageurl) {
        this.cropimageurl = cropimageurl;
    }
}
