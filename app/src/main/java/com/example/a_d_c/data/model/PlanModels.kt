package com.example.a_d_c.data.model

import com.google.gson.annotations.SerializedName

// REQUEST MODELS
data class PlanRequest(
    @SerializedName("plot_width")
    val plotWidth: Int,
    @SerializedName("plot_height")
    val plotHeight: Int,
    val facing: String,  // "north", "south", "east", "west"
    val rooms: RoomsRequest,
    @SerializedName("entrance_preferences")
    val entrancePreferences: Map<String, String> = mapOf()
)

data class RoomsRequest(
    val bedroom: Int = 1,
    val bathroom: Int = 1,
    val kitchen: Int = 1,
    val hall: Int = 1,
    val pooja: Int = 0
)

// RESPONSE MODELS
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
    val facing: String,
    @SerializedName("rooms_count")
    val roomsCount: Int
)
