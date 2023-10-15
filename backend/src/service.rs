use crate::{domain, model};
use sqlx::PgPool;

pub async fn find_route(
    pool: &PgPool,
    req: model::route::RouteRequest,
) -> Result<Vec<model::route::Route>, Box<dyn std::error::Error>> {
    // let start_node = sqlx::query!(
    //     "SELECT id from ways_vertices_pgr WHERE lat = $1 AND lon = $2",
    //     req.latitude,
    //     req.longitude,
    // )
    // .fetch_one(pool)
    // .await?
    // .id;

    let end_node = 831;
    let path = sqlx::query_as!(
        domain::Coordinate,
        "
        SELECT p.lon AS longitude, p.lat AS latitude
         FROM pgr_aStar(
             'SELECT id, source, target, cost, reverse_cost, x1, y1, x2, y2 FROM ways',
             1022, 831,
             directed => true, heuristic => 2
         ) r
         INNER JOIN ways_vertices_pgr p ON r.node = p.id;
        "
    )
    .fetch_all(pool)
    .await?;

    let mut routes = Vec::with_capacity(path.len() / 2 + 1);
    for i in 1..path.len() {
        routes.push(model::route::Route {
            from_latitude: path[i - 1].latitude,
            from_longitude: path[i - 1].longitude,
            to_latitude: path[i].latitude,
            to_longitude: path[i].longitude,
        });
    }

    Ok(routes)
}

pub async fn read_atms() -> Result<Vec<model::atm::Atm>, Box<dyn std::error::Error>> {
    let atms = std::fs::read_to_string("static/atms.json")?;
    Ok(serde_json::from_str(&atms)?)
}

pub async fn read_offices() -> Result<Vec<model::office::Office>, Box<dyn std::error::Error>> {
    let office = std::fs::read_to_string("static/offices.json")?;
    Ok(serde_json::from_str(&office)?)
}
