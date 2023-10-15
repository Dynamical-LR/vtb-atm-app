package ru.dynamical_lr.vtb_atm_app.network.api

import retrofit2.Call
import retrofit2.http.GET
import ru.dynamical_lr.vtb_atm_app.models.AtmModel
import ru.dynamical_lr.vtb_atm_app.models.OfficeModel

interface AtmAPI {
    @GET("api/v1/atm?page=1&size=20")
    fun getAtms(): Call<List<AtmModel>>

    @GET("api/v1/office?page=1&size=20")
    fun getOffices(): Call<List<OfficeModel>>
}