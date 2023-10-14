package ru.dynamical_lr.vtb_atm_app.network.api

import retrofit2.Call
import retrofit2.http.GET
import ru.dynamical_lr.vtb_atm_app.models.AtmModel

interface AtmAPI {
    @GET("api/v1/atm?page=1&size=5")
    fun getAtms(): Call<List<AtmModel>>
}