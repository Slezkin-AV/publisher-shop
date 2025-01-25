import json
import logging
from locust import FastHttpUser, task, between, run_single_user, HttpUser
import faker
# from faker import Faker

API_URL='http://{SERVICE_HOST}:{SERVICE_PORT}'.format(
    SERVICE_HOST="publisher.localdev.me",
    SERVICE_PORT=8000
)
#
# from flask import Flask, request, abort, redirect
# app = Flask(__name__)


class FirstSuccess(FastHttpUser):
    wait_time = between(1, 2)
    token = ""
    users = []
    logins = []
    tokens = {}

    def create_user(self, count):
        fake = faker.Faker()
        for i in range(count):
            user = {
                "firstName": fake.first_name(),
                "lastName": fake.last_name(),
                "login": fake.simple_profile()['username'],
                "email": fake.simple_profile()['username'] + "@bk.ru",
                "password": "123"
            }
            login = {
                "login": user['login'],
                "password": user['password']
            }
            self.users.append(user)
            self.logins.append(login)

    def on_start(self):
        # locust.stats_logger
        logging.info("on_start")
        # test_data
        self.create_user(5)
        # user = {"firstName": "f", "lastName": "l", "login": "ll", "email": "em","password": "123" }

        for usr in self.users:
            resp = self.client.post("/register", json=usr)

        for lg in self.logins:
            with self.client.post("/login", json=lg) as resp:
                # logging.info(f"/login got: {resp.text}")
                if not (resp.ok):
                    logging.info(f"Error response: {resp.text}")
                    pass
                elif "accessToken" not in resp.text:
                    logging.info(f"'accessToken' missing from response {resp.text}")
                else:
                    tok = resp.json()["accessToken"]
                    id = resp.json()["userID"]
                    self.tokens[id] = (lg['login'], tok)

    @task(3)
    def get_user(self):
        # logging.info("get_user")
        for id in self.tokens:
            lg = self.tokens[id][0]
            tok = self.tokens[id][1]
            # logging.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            with self.client.get(f"/user/{id}", headers=header) as resp:
                pass

    # @task(5)
    # def get_account(self):
    #     # logging.info("get_user")
    #     for id in self.tokens:
    #         lg = self.tokens[id][0]
    #         tok = self.tokens[id][1]
    #         # logging.info(f"id: {id}, login: {lg}, token: {tok}")
    #         header = {"Authorization": f"Bearer {tok}"}
    #         with self.client.get(f"/accountByUserId/{id}", headers=header) as resp:
    #             pass



if __name__ == "__main__":
    run_single_user(FirstSuccess)