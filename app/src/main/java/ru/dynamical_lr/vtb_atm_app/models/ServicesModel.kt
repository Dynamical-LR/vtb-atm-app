package ru.dynamical_lr.vtb_atm_app.models

data class ServicesModel(
    val wheelChair: Capability?,
    val blind: Capability?,
    val nfcForBankCards: Capability?,
    val qrRead: Capability?,
    val supportsUsd: Capability?,
    val supportsChargeRub: Capability?,
    val supportsEur: Capability?,
    val supportsRub: Capability?
)