package com.example.and103_ph44245lab1.cities;

public class Regions {
    private String province, city;

    public Regions() {
    }

    public Regions(String province, String city) {
        this.province = province;
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    @Override
//    public String toString() {
//        return "Regions{" +
//                "province='" + province + '\'' +
//                ", city='" + city + '\'' +
//                '}';
//    }
}
