package com.raven.currencyconversion.network;

import com.raven.currencyconversion.network.model.RateObject;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {
    // Fetch all rate
    @GET("/")
    Single<RateObject> fetchAllRate();
}
