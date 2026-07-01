package com.apptech.benefit.data.remote

import com.apptech.benefit.data.model.AppDetail
import com.apptech.benefit.data.model.AppSummary
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/apps")
    suspend fun getApps(): List<AppSummary>

    @GET("api/apps/{id}")
    suspend fun getAppDetail(@Path("id") id: String): AppDetail
}
