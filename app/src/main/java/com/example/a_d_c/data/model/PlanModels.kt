package com.example.a_d_c.data.model

import com.google.gson.annotations.SerializedName

data class PlanRequest(
    @SerializedName("plot_width")
    val plotWidth: Int,
    @SerializedName("plot_length")
    val plotLength: Int,
    val facing: String,
    val entrance: String? = null,
    val rooms: RoomsRequest
)

data class RoomsRequest(
    val bedroom: Int = 0,
    val bathroom: Int = 0,
    val kitchen: Int = 0,
    val hall: Int = 0,
    val pooja: Int = 0
)

data class PlanResponse(
    val success: Boolean,
    val svg: String,
    @SerializedName("vastu_score")
    val vastuScore: Int,
    @SerializedName("score_color")
    val scoreColor: String,
    val warnings: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val metadata: Metadata? = null,
    val error: String? = null,
    val message: String? = null
)

data class Room(
    val type: String,
    val zone: String,
    val width: Int,
    val height: Int
)

data class Metadata(
    @SerializedName("plot_width")
    val plotWidth: Int,
    @SerializedName("plot_height")
    val plotHeight: Int,
    @SerializedName("plot_length")
    val plotLength: Int,
    val facing: String,
    val entrance: String?,
    @SerializedName("rooms_count")
    val roomsCount: Int
)
