import json
import time

from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.by import By
from tqdm import tqdm

opts = webdriver.ChromeOptions()
opts.add_experimental_option(
    "prefs", {"profile.managed_default_content_settings.images": 2}
)
opts.add_argument("--no-sandbox")
opts.add_argument("--remote-debugging-port=9222")  # this
browser = webdriver.Chrome(options=opts)

browser.get(
    "https://yandex.ru/maps/213/moscow/chain/bank_vtb_otdelenya/6002211/filter/chain_id/6002211/?ll=37.624842%2C55.753916&sll=37.624842%2C55.753916&sspn=0.189158%2C0.074688&z=12.4"
)
time.sleep(3)

get_builidngs = lambda: browser.find_elements(
    By.CLASS_NAME, "search-business-snippet-view__content"
)

input("Zoom out the map")

buildings = list(get_builidngs())
while True:
    browser.execute_script("arguments[0].scrollIntoView();", buildings[-1])
    buf = [b for b in get_builidngs() if b not in buildings]
    buildings.extend(buf)
    time.sleep(1)

    if not buf:
        print("New buildings not found")
    else:
        print(f"Found {len(buf)} builidngs")

    for item in tqdm(buf, leave=False, position=0):
        address = item.find_element(
            By.CLASS_NAME, "search-business-snippet-view__address"
        ).text
        print(f"Saving reviews for {address=}")
        item.click()
        time.sleep(1)

        browser.find_element(By.CLASS_NAME, "_name_reviews").click()
        time.sleep(3)

        get_reviews = lambda: browser.find_elements(
            By.CLASS_NAME, "business-reviews-card-view__review"
        )
        reviews = get_reviews()
        reviews_count = len(reviews)

        while True:
            if not reviews:
                break
            browser.execute_script("arguments[0].scrollIntoView();", reviews[-1])
            time.sleep(1)
            reviews = get_reviews()
            if len(reviews) == reviews_count:
                break
            reviews_count = len(reviews)

        print(f"Found {reviews_count} reviews")

        result = []
        for review in tqdm(
            reviews,
            leave=False,
            position=1,
        ):
            date = review.find_element(By.CLASS_NAME, "business-review-view__date").text
            text = review.find_element(
                By.CLASS_NAME, "business-review-view__body-text"
            ).text
            stars = len(
                review.find_elements(By.CLASS_NAME, "business-rating-badge-view__star")
            )
            result.append((address, stars, date, text))

        with open(f"{address}.json", "w") as f:
            f.write(json.dumps(result))
