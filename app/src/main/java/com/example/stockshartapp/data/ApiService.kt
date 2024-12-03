package com.example.stockshartapp.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("aggs/ticker/AAPL/range/{timeFrame}/2021-01-09/2023-01-09?adjusted=true&sort=desc&limit=50000&apiKey=IWHqUp21oNuJXZHWW61wuR8Q8lB7mVze")
    suspend fun loadBars(
        @Path("timeFrame") timeFrame: String
    ): Result
}