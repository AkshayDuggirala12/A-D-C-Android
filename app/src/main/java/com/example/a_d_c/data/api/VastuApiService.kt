package com.example.a_d_c.data.api

import com.example.a_d_c.data.model.VastuRequest
import com.example.a_d_c.data.model.VastuResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface VastuApiService {
    @POST("plan/generate-full")
    suspend fun generateFullPlan(@Body request: VastuRequest): VastuResponse
}
