package ru.dynamical_lr.vtb_atm_app.models

data class AtmModel(
    val latitude: Float,
    val longitude: Float,
    val allDay: Boolean,
    val address: String,
    val services: ServicesModel
)