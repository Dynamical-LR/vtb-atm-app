use serde::{Deserialize, Serialize};

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Office {
    pub sale_point_name: String,
    pub address: String,
    pub status: String,
    pub open_hours: Vec<OpenHour>,
    pub rko: Option<String>,
    pub open_hours_individual: Vec<OpenHoursIndividual>,
    pub office_type: String,
    pub sale_point_format: String,
    pub suo_availability: Option<String>,
    pub has_ramp: Option<String>,
    pub latitude: f64,
    pub longitude: f64,
    pub metro_station: Option<String>,
    pub distance: i64,
    pub kep: Option<bool>,
    pub my_branch: bool,
    #[serde(skip_serializing_if = "Option::is_none")]
    pub network: Option<String>,
    pub sale_point_code: Option<String>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct OpenHour {
    pub days: String,
    pub hours: Option<String>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct OpenHoursIndividual {
    pub days: String,
    pub hours: Option<String>,
}
