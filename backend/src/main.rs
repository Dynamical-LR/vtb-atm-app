mod domain;
mod model;
mod service;

use actix_web::{get, post, web, App, HttpServer, Responder, Result};
use actix_web_validator::Query;
use dotenv::dotenv;

#[get("/atm")]
async fn get_atms(
    req: Query<model::page::PageRequest>,
    offices: web::Data<Vec<model::atm::Atm>>,
) -> impl Responder {
    web::Json(req.paginate(offices.as_ref().clone()))
}

#[get("/atm/coordinate")]
async fn get_atm_coordinates(
    req: Query<model::page::PageRequest>,
    atms: web::Data<Vec<model::atm::Atm>>,
) -> Result<impl Responder> {
    let resp = req.paginate(
        atms.as_ref()
            .clone()
            .into_iter()
            .map(|a| domain::Coordinate {
                latitude: a.latitude,
                longitude: a.longitude,
            }),
    );
    Ok(web::Json(resp))
}

#[get("/office")]
async fn get_offices(
    req: Query<model::page::PageRequest>,
    atms: web::Data<Vec<model::office::Office>>,
) -> impl Responder {
    web::Json(req.paginate(atms.as_ref().clone()))
}

#[get("/office/address")]
async fn get_office_addresses(
    req: Query<model::page::PageRequest>,
    offices: web::Data<Vec<model::office::Office>>,
) -> Result<impl Responder> {
    let resp = req.paginate(offices.as_ref().clone().into_iter().map(|o| o.address));
    Ok(web::Json(resp))
}

#[get("/office/coordinate")]
async fn get_office_coordinates(
    req: Query<model::page::PageRequest>,
    offices: web::Data<Vec<model::office::Office>>,
) -> Result<impl Responder> {
    let resp = req.paginate(
        offices
            .as_ref()
            .clone()
            .into_iter()
            .map(|o| domain::Coordinate {
                latitude: o.latitude,
                longitude: o.longitude,
            }),
    );
    Ok(web::Json(resp))
}

#[post("/route")]
async fn route(
    req: web::Json<model::route::RouteRequest>,
    pool: web::Data<sqlx::PgPool>,
) -> Result<impl Responder> {
    let route = service::find_route(pool.as_ref(), req.0).await?;
    Ok(web::Json(route))
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let pool_factory = || async { sqlx::PgPool::connect(std::env!("DATABASE_URL")).await };

    dotenv().ok();
    HttpServer::new(move || {
        App::new()
            .data_factory(pool_factory)
            .data_factory(service::read_atms)
            .data_factory(service::read_offices)
            .service(
                web::scope("/api/v1")
                    .service(get_atms)
                    .service(get_atm_coordinates)
                    .service(get_offices)
                    .service(route)
                    .service(get_office_addresses)
                    .service(get_office_coordinates),
            )
    })
    .bind(("127.0.0.1", 8080))?
    .run()
    .await
}
