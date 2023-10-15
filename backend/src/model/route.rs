use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize)]
pub struct RouteRequest {
    pub latitude: f64,
    pub longitude: f64,
}

#[derive(Debug, Serialize)]
pub struct Route {
    pub from_latitude: f64,
    pub from_longitude: f64,
    pub to_latitude: f64,
    pub to_longitude: f64,
}
