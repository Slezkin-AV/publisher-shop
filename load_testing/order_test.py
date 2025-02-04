#!/usr/bin/python3


import json
import logging
# from locust import FastHttpUser, task, between, run_single_user, HttpUser
import faker
# from faker import Faker
import requests
from requests.exceptions import HTTPError

USER_HOST='publisher.localdev.me'
ORDER_HOST='order.localdev.me'
ORDER_BILLING='billing.localdev.me'
SERVICE_PORT=8000
API_URL_USER=f"http://{USER_HOST}:{SERVICE_PORT}"
API_URL_ORDER=f"http://{ORDER_HOST}:{SERVICE_PORT}"
API_URL_BILLING=f"http://{ORDER_BILLING}:{SERVICE_PORT}"

class OrderTest:
    users = {}
    logins = {}
    tokens = {}

    def test_request(self, operation, url, header, json_data, param):
        logging.info(f"test_request: on {url}")
        try:
            match operation:
                case "get":
                    resp = requests.get(url, headers=header, params=param)
                    pass
                case "put":
                    resp = requests.post(url, headers=header, json=json_data, params=param)
                    pass
                case "post":
                    resp = requests.post(url, headers=header, json=json_data, params=param)
                    pass
                case _:
                    logging.info(f"test_request: no operation {operation}")
                    return 
            pass    
        except HTTPError as err:
            logging.error(f"Error: {err}")
        except Exception as ex:
            logging.error(f"Exception: {ex}")
        else:
            logging.info(f"responce: {resp}")
            # logging.info(f"data: {resp.text}")
            if resp.ok:
                logging.info(f"response on on {url}: {resp.text}")
                return resp
            else:
                logging.info(f"Error response on on {url}: {resp.status_code}") 


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
            resp = requests.post(API_URL_USER + "/register",
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
            resp = requests.post(API_URL_USER + "/login",
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
            resp = requests.post(API_URL_ORDER + "/order",
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


    def increase_account(self, id, sum):
        logging.info(f"increase_account: for user {id} on {sum}")
        token = self.tokens[id]
        account = {}
        # header = {"Authorization": f"Bearer {token}"}
        header = {"Contetn-Type": "application/json"}
        resp = self.test_request("get", API_URL_BILLING + "/account/" + str(id), header, {}, {})
        if resp:
            resp = self.test_request("post", API_URL_BILLING + "/account/" + str(id) +"?amount=" + str(sum), header, {}, {})





if __name__ == "__main__":
    
    logging.basicConfig(level=logging.INFO)
    logging.info("Test STARTING");
    test1 = OrderTest() 
    id = test1.create_new_user()
    if id:
        test1.login(id)
        test1.increase_account(id, 1000)
        # test1.send_order(id)
    logging.info("Test FINISHED");


