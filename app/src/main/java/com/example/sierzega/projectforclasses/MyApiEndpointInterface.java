package com.example.sierzega.projectforclasses;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by Jakub Sierżęga on 20.05.2018.
 */

public interface MyApiEndpointInterface {

    @GET("/api/random")
    Call<List<Integer>> getRandomNumbers(@Header("List_lenght") int list_lenght);
    @POST("/api/generated-numbers")
    Call<List<Integer>> sendNumbersValues(@Body List<Integer> list);
    @GET("/api/avg-value")
    Call<Integer> getAvgValue();
}
