from typing import cast
from xml.etree.ElementTree import Element, iterparse

import psycopg2

conn = psycopg2.connect("postgres://user:pass@localhost:5432/routing")
osm = open("moscow.osm")
buf = []
counter = 1
batch_size = 10

for evt, elem in iterparse(osm):
    elem = cast(Element, elem)
    if elem.tag != "node":
        continue

    for tag in list(elem.iter())[1:]:
        k = tag.attrib.get("k")
        if k == "addr:housenumber":
            buf.append((tag.attrib.get("v"), elem.attrib.get("id")))

    if len(buf) == batch_size:
        print(f"Processed {counter * batch_size} buildings")
        print(buf)
        with conn.cursor() as cur:
            cur.executemany("UPDATE ways SET house_number = %s where osm_id = %s", buf)
        conn.commit()
        counter += 1
        buf = []

osm.close()
