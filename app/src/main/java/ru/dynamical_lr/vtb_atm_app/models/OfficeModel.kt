package ru.dynamical_lr.vtb_atm_app.models

import com.google.gson.annotations.SerializedName

data class OfficeModel(
    @SerializedName("salePointName") val salePointName: String,
    @SerializedName("address") val address: String,
    @SerializedName("status") val status: String,
    @SerializedName("openHours") val openHours: List<OpenHour>,
    @SerializedName("rko") val rko: String,
    @SerializedName("openHoursIndividual") val openHoursIndividual: List<OpenHoursIndividual>,
    @SerializedName("officeType") val officeType: String,
    @SerializedName("salePointFormat") val salePointFormat: String,
    @SerializedName("suoAvailability") val suoAvailability: String,
    @SerializedName("hasRamp") val hasRamp: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("metroStation") val metroStation: Any?,
    @SerializedName("distance") val distance: Long,
    @SerializedName("kep") val kep: Boolean,
    @SerializedName("myBranch") val myBranch: Boolean,
    @SerializedName("salePointCode") val salePointCode: Any?
)

data class OpenHour(
    @SerializedName("days") val days: String, @SerializedName("hours") val hours: String
)

data class OpenHoursIndividual(
    @SerializedName("days") val days: String, @SerializedName("hours") val hours: String
)