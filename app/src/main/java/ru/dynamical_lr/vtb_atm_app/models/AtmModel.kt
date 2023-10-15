package ru.dynamical_lr.vtb_atm_app.models

import com.google.gson.annotations.SerializedName

data class AtmModel(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("allDay")
    val allDay: Boolean,
    @SerializedName("address")
    val address: String,
    @SerializedName("services")
    val services: ServicesModel
)