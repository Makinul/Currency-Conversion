package com.raven.currencyconversion.network.model;

import java.util.ArrayList;

public class RateObject {
    String details;
    String version;
    ArrayList<Rate> rates;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<Rate> rates) {
        this.rates = rates;
    }
}
