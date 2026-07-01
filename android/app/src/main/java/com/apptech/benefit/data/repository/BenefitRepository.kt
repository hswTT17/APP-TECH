package com.apptech.benefit.data.repository

import com.apptech.benefit.data.model.AppDetail
import com.apptech.benefit.data.model.AppSummary
import com.apptech.benefit.data.remote.ApiService
import com.apptech.benefit.data.remote.RetrofitClient

class BenefitRepository(
    private val apiService: ApiService = RetrofitClient.apiService,
) {
    suspend fun getApps(): List<AppSummary> = apiService.getApps()

    suspend fun getAppDetail(id: String): AppDetail = apiService.getAppDetail(id)
}
