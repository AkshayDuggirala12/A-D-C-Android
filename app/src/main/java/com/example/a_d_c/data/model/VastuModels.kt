package com.example.a_d_c.data.model

import com.google.gson.annotations.SerializedName

data class VastuRequest(
    @SerializedName("plot_width") val plotWidth: Int,
    @SerializedName("plot_height") val plotHeight: Int,
    @SerializedName("facing") val facing: String,
    @SerializedName("rooms") val rooms: RoomCounts,
    @SerializedName("entrance_preferences") val entrancePreferences: Map<String, String> = emptyMap()
)

data class RoomCounts(
    @SerializedName("bedroom") val bedroom: Int,
    @SerializedName("bathroom") val bathroom: Int,
    @SerializedName("kitchen") val kitchen: Int,
    @SerializedName("hall") val hall: Int,
    @SerializedName("pooja") val pooja: Int
)

data class VastuResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("svg") val svg: String,
    @SerializedName("vastu_score") val vastuScore: Int,
    @SerializedName("score_color") val scoreColor: String,
    @SerializedName("warnings") val warnings: List<String>,
    @SerializedName("recommendations") val recommendations: List<String>,
    @SerializedName("rooms") val rooms: List<RoomDetail>,
    @SerializedName("metadata") val metadata: ResponseMetadata
)

data class RoomDetail(
    @SerializedName("type") val type: String,
    @SerializedName("zone") val zone: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)

data class ResponseMetadata(
    @SerializedName("plot_width") val plotWidth: Int,
    @SerializedName("plot_height") val plotHeight: Int,
    @SerializedName("facing") val facing: String,
    @SerializedName("rooms_count") val roomsCount: Int
)
