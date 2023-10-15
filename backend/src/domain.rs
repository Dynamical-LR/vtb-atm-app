use serde::Serialize;

#[derive(sqlx::FromRow, Serialize)]
pub struct Coordinate {
    pub latitude: f64,
    pub longitude: f64,
}
