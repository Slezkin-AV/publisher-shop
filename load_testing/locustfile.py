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
    tokens = []

    def find_user(self, login):
        for usr in self.users:
            if usr["login"] == login:
                return usr
        return null


    def create_new_user(self):
        fake = faker.Faker()
        user = {
            "firstName": fake.first_name(),
            "lastName": fake.last_name(),
            "login": fake.simple_profile()['username'],
            "email": fake.simple_profile()['username'] + "@bk.ru",
            "password": "123"
        }
        resp = self.client.post("/register", name="/register", json=user)
        if resp.ok:
            self.users.append(user)
            login = {
                "login": user['login'],
                "password": user['password']
            }
            return login

    @task(2)
    def create_users(self, num = 3):
        tokenList = {}
        for i in range(num):
            lg = self.create_new_user()
            with self.client.post("/login", name="/login", json=lg) as resp:
                # logging.info(f"/login got: {resp.text}")
                if not (resp.ok):
                    logging.info(f"Error response: {resp.text}")
                    pass
                elif "accessToken" not in resp.text:
                    logging.info(f"'accessToken' missing from response {resp.text}")
                else:
                    tok = resp.json()["accessToken"]
                    id = resp.json()["userID"]
                    tokenList[id] = (lg['login'], tok)
                    self.logins.append(lg)
                    # self.tokens[id] = (lg['login'], tok)
        self.tokens.append(tokenList)


    def on_start(self):
        # locust.stats_logger
        logging.info("on_start")
        # test_data
        self.create_users(5)
        # user = {"firstName": "f", "lastName": "l", "login": "ll", "email": "em","password": "123" }


    @task(5)
    def get_user(self):
        # logging.info("get_user")
        tokenList = self.tokens[0]
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            # logging.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            with self.client.get(f"/user/{id}", name="/get_user", headers=header) as resp:
                pass

    @task(3)
    def update_user(self):
        # logging.info("update_user")
        tokenList = self.tokens[0]
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            # logging.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            usr = self.find_user(lg)
            usr["lastName"] = usr["firstName"]
            with self.client.put(f"/user/{id}", name="/update_user",headers=header, json=usr) as resp:
                pass


    @task(1)
    def delete_user(self):
        # logging.info("delete_user")
        tokenList = self.tokens.pop(0)
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            # logging.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            usr = self.find_user(lg)
            # del self.users
            with self.client.delete(f"/user/{id}", name="/delete_user", headers=header, json=usr) as resp:
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