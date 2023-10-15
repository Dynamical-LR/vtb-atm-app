package ru.dynamical_lr.vtb_atm_app.network.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.dynamical_lr.vtb_atm_app.models.AtmModel
import ru.dynamical_lr.vtb_atm_app.models.OfficeModel
import ru.dynamical_lr.vtb_atm_app.models.RouteNode
import ru.dynamical_lr.vtb_atm_app.requests.RouteRequest

interface AtmAPI {
    @GET("api/v1/atm?page=1&size=10")
    fun getAtms(): Call<List<AtmModel>>

    @GET("api/v1/office?page=1&size=10")
    fun getOffices(): Call<List<OfficeModel>>

    @POST("api/v1/route")
    fun getRoute(@Body body: RouteRequest): Call<List<RouteNode>>
}