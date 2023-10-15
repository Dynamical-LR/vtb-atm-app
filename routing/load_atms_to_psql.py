import psycopg2
import requests
from psycopg2.extensions import connection as PgConnection

conn: PgConnection = psycopg2.connect("postgres://user:pass@localhost:5432/routingv2")
atms_coords = requests.get("http://localhost:8080/api/v1/atm/coordinate?page=1&size=5000")

assert atms_coords.status_code == 200, f"Got {atms_coords.status_code}"

atms_coords = atms_coords.json()

with conn.cursor() as cur:
    cur.executemany(
        """
        INSERT INTO atm (latitude, longitude)
        VALUES (%s, %s)
        """,
        [(atm["latitude"], atm["longitude"]) for atm in atms_coords]
    )
conn.commit()
