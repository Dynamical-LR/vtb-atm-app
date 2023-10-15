package ru.dynamical_lr.vtb_atm_app.models

import com.google.gson.annotations.SerializedName


data class RouteNode(
    @SerializedName("from_latitude")
    val fromLatitude: Double,

    @SerializedName("from_longitude")
    val fromLongitude: Double,

    @SerializedName("to_latitude")
    val toLatitude: Double,

    @SerializedName("to_longitude")
    val toLongitude: Double,
)