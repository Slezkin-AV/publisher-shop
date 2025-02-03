#!/usr/bin/python3


import json
import logging
# from locust import FastHttpUser, task, between, run_single_user, HttpUser
import faker
# from faker import Faker
import requests
from requests.exceptions import HTTPError

SERVICE_HOST='publisher.localdev.me'
SERVICE_HOST1='order.localdev.me'
SERVICE_PORT=8000
API_URL=f"http://{SERVICE_HOST}:{SERVICE_PORT}"
API_URL1=f"http://{SERVICE_HOST1}:{SERVICE_PORT}"

class OrderTest:
    users = {}
    logins = {}
    tokens = {}


    def create_new_user(self):
        fake = faker.Faker()
        user = {
            "firstName": fake.first_name(),
            "lastName": fake.last_name(),
            "login": fake.simple_profile()['username'],
            "email": fake.simple_profile()['username'] + "@bk.ru",
            "password": "123"
        }
        logging.info(f"user : {user}")
        try:
            resp = requests.post(API_URL + "/register",
                headers={'Contetn-Type': 'application/json'},
                json=user)
        except HTTPError as err:
            logging.error(f"Error: {err}")
        except Exception as ex:
            logging.error(f"Exception: {ex}")
        else:
            logging.info(f"responce: {resp}")
            logging.info(f"data: {resp.text}")
            if resp.ok:
                id = resp.json()["id"]
                self.users[id]=user
                login = {
                    "login": user['login'],
                    "password": user['password']
                }
                self.logins[id]=login
                return id
            else:
                logging.info(f"Error response /register: {resp.status_code}")
        # else:
            # logging.info(f"Error response /register: {resp.text}")


    def login(self, id):
        logging.info(f"login for id: {id}")
        lg = self.logins[id]

        try:
            resp = requests.post(API_URL + "/login",
                headers={'Contetn-Type': 'application/json'},
                json=lg)
        except HTTPError as err:
            logging.error(f"Error: {err}")
        except Exception as ex:
            logging.error(f"Exception: {ex}")
        else:
            logging.info(f"responce: {resp}")
            # logging.info(f"data: {resp.text}")
            if resp.ok:
                token = resp.json()["accessToken"]
                self.tokens[id] = token
                return token
            else:
                logging.info(f"Error response /login: {resp.status_code}")


    def new_order(self, id):
        order = {
            "userId": id,
            "amount": "10"
        }
        return order


    def send_order(self, id):

        order = self.new_order(id)
        logging.info(f"send order : {order}")

        try: 
            lg = self.logins[id]
            token = self.tokens[id]
        except Exception as ex:
            logging.error(f"send order Exception: {ex}, user not logged")
            return
        else:
            logging.info(f"order for login: {lg}")
            # with token: {token}")

        # header = {"Authorization": f"Bearer {token}"}
        header = {"Contetn-Type": "application/json"}
        try:
            resp = requests.post(API_URL1 + "/order",
                headers=header,
                json=order)
        except HTTPError as err:
            logging.error(f"Error: {err}")
        except Exception as ex:
            logging.error(f"Exception: {ex}")
        else:
            logging.info(f"responce: {resp}")
            # logging.info(f"data: {resp.text}")
            if resp.ok:
                # token = resp.json()["accessToken"]
                # self.tokens[id] = token
                # return token
                logging.info(f"response /order: {resp.text}")
            else:
                logging.info(f"Error response /order: {resp.status_code}")  




if __name__ == "__main__":
    
    logging.basicConfig(level=logging.INFO)
    logging.info("Test STARTING");
    test1 = OrderTest() 
    id = test1.create_new_user()
    test1.login(id)
    test1.send_order(id)
    logging.info("Test FINISHED");


