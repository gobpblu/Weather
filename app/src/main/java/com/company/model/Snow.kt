package com.company.model

import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName("1h")
    val height1: Double,
    @SerializedName("3h")
    val height3: Double
)
