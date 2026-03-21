package com.example.a_d_c.data.api

import com.example.a_d_c.data.model.PlanRequest
import com.example.a_d_c.data.model.PlanResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface VastuApiService {
    @POST("plan/generate-full")
    suspend fun generatePlan(@Body request: PlanRequest): PlanResponse
}
