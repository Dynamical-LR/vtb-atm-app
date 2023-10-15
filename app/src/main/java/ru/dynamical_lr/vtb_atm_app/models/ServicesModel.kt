package ru.dynamical_lr.vtb_atm_app.models

import com.google.gson.annotations.SerializedName

data class ServicesModel(
    @SerializedName("wheelchair")
    val wheelChair: Capability?,
    @SerializedName("blind")
    val blind: Capability?,
    @SerializedName("nfcForBankCards")
    val nfcForBankCards: Capability?,
    @SerializedName("qrRead")
    val qrRead: Capability?,
    @SerializedName("supportsUsd")
    val supportsUsd: Capability?,
    @SerializedName("supportsChargeRub")
    val supportsChargeRub: Capability?,
    @SerializedName("supportsEur")
    val supportsEur: Capability?,
    @SerializedName("supportsRub")
    val supportsRub: Capability?
)