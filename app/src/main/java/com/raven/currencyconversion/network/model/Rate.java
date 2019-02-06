package com.raven.currencyconversion.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Rate {
    String name;
    String code;
    @SerializedName("country_code")
    String countryCode;
    ArrayList<Tax> periods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public ArrayList<Tax> getPeriods() {
        return periods;
    }

    public void setPeriods(ArrayList<Tax> periods) {
        this.periods = periods;
    }
}
