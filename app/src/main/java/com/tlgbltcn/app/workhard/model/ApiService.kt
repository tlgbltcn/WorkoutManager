package com.tlgbltcn.app.workhard.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") api_key : String, @Query("page") page : Long) : Call<Popular>


}