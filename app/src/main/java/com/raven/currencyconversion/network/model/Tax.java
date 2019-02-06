package com.raven.currencyconversion.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Tax {
    @SerializedName("effective_from")
    String effectiveFrom;
    HashMap<String, Double> rates;

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }
}
