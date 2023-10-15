use serde::{Deserialize, Serialize};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Atm {
    pub address: String,
    pub latitude: f64,
    pub longitude: f64,
    pub all_day: bool,
    pub services: Services,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Services {
    pub wheelchair: Wheelchair,
    pub blind: Blind,
    pub nfc_for_bank_cards: NfcForBankCards,
    pub qr_read: QrRead,
    pub supports_usd: SupportsUsd,
    pub supports_charge_rub: SupportsChargeRub,
    pub supports_eur: SupportsEur,
    pub supports_rub: SupportsRub,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Wheelchair {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Blind {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct NfcForBankCards {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct QrRead {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SupportsUsd {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SupportsChargeRub {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SupportsEur {
    pub service_capability: String,
    pub service_activity: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SupportsRub {
    pub service_capability: String,
    pub service_activity: String,
}
