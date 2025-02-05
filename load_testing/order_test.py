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
BILLING_HOST='billing.localdev.me'
WARE_HOST='ware.localdev.me'
SERVICE_PORT=8000
API_URL_USER=f"http://{USER_HOST}:{SERVICE_PORT}"
API_URL_ORDER=f"http://{ORDER_HOST}:{SERVICE_PORT}"
API_URL_BILLING=f"http://{BILLING_HOST}:{SERVICE_PORT}"
API_URL_WARE=f"http://{WARE_HOST}:{SERVICE_PORT}"

targets={API_URL_USER, API_URL_ORDER, API_URL_BILLING, API_URL_WARE}


class CustomFormatter(logging.Formatter):

    grey = "\x1b[38;20m"
    yellow = "\x1b[33;20m"
    red = "\x1b[31;20m"
    bold_red = "\x1b[31;1m"
    reset = "\x1b[0m"
    bold_yellow = "\x1b[33;1m"
    format = "%(asctime)s: %(name)s: %(levelname)s: %(message)s (%(filename)s:%(lineno)d)"

    FORMATS = {
        logging.DEBUG: bold_yellow + format + reset,
        logging.INFO: grey + format + reset,
        logging.WARNING: yellow + format + reset,
        logging.ERROR: red + format + reset,
        logging.CRITICAL: bold_red + format + reset
    }

    def format(self, record):
        log_fmt = self.FORMATS.get(record.levelno)
        formatter = logging.Formatter(log_fmt)
        return formatter.format(record)

# create logger with 'spam_application'
logger = logging.getLogger("order_test")
logger.setLevel(logging.DEBUG)

# create console handler with a higher log level
ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
ch.setFormatter(CustomFormatter())
logger.addHandler(ch)

# ======================================== #



class OrderTest:
    users = {}
    logins = {}
    tokens = {}

    def test_request(self, operation, url, header, json_data, param):
        logger.info(f"test_request: on {url}")
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
                    logger.info(f"test_request: no operation {operation}")
                    return 
            pass    
        except HTTPError as err:
            logger.error(f"Error: {err}")
        except Exception as ex:
            logger.error(f"Exception: {ex}")
        else:
            # logger.debug(f"data: {resp.text}")
            if resp.ok:
                logger.info(f"response on {url}: {resp.text}")
                return resp
            else:
                logger.error(f"responce: {resp}")
                logger.error(f"Error response on {url}: {resp.status_code}") 


    def create_new_user(self):
        fake = faker.Faker()
        user = {
            "firstName": fake.first_name(),
            "lastName": fake.last_name(),
            "login": fake.simple_profile()['username'],
            "email": fake.simple_profile()['username'] + "@bk.ru",
            "password": "123"
        }
        logger.info(f"user : {user}")
        header={'Contetn-Type': 'application/json'}
        resp = self.test_request("post", API_URL_USER + "/register", header, user, {})
        if resp and resp.ok:
            id = resp.json()["id"]
            self.users[id]=user
            login = {
                "login": user['login'],
                "password": user['password']
                }
            self.logins[id]=login
            return id


    def login(self, id):
        logger.info(f"login for id: {id}")
        lg = self.logins[id]
        header={'Contetn-Type': 'application/json'}
        resp = self.test_request("post", API_URL_USER + "/login", header, lg, {})
        if resp and resp.ok:
            token = resp.json()["accessToken"]
            self.tokens[id] = token
            return token


    def new_order(self, id, am, sum=450, ware=5):
        order = {
            "userId": id,
            "amount": am,
            "sum": sum,
            "wareId": ware
        }
        return order


    def send_order(self, id, order):
        
        # order = self.new_order(id)
        logger.info(f"send order : {order}")

        header={'Contetn-Type': 'application/json'}
        # header = {"Authorization": f"Bearer {token}"}

        try: 
            lg = self.logins[id]
            token = self.tokens[id]
        except Exception as ex:
            logger.error(f"send order Exception: {ex}, user not logged")
            return
        else:
            logger.info(f"order for login: {lg}")
            # with token: {token}")

        resp = self.test_request("post", API_URL_ORDER + "/order", header, order, {})
        if resp and resp.ok:
            pass



    def increase_account(self, id, sum):
        logger.info(f"increase_account: for user {id} on {sum}")
        token = self.tokens[id]
        account = {}
        # header = {"Authorization": f"Bearer {token}"}
        header = {"Contetn-Type": "application/json"}
        resp = self.test_request("get", API_URL_BILLING + "/account/" + str(id), header, {}, {})
        if resp:
            resp = self.test_request("post", API_URL_BILLING + "/account/" + str(id) +"?sum=" + str(sum), header, {}, {})

    def clean_ware(self):
        logger.info(f"clean_ware")
        header = {"Contetn-Type": "application/json"}
        resp = self.test_request("post", API_URL_WARE + "/clean", header, {}, {})
        if resp:
            pass



    def cleanAll(self):
        header = {"Contetn-Type": "application/json"}
        for target in targets:
            resp = self.test_request("post", target + "/clean", header, {}, {} )
            if resp:
                pass





if __name__ == "__main__":
    
    # logging.basicConfig(level=logging.INFO)
    logger.info("Test STARTING");
    test1 = OrderTest() 
    # test1.cleanAll()
    # test1.clean_ware()

    id = test1.create_new_user()
    order = test1.new_order(id,30,450,14)
    logger.debug(f"Test ORDER: {order}");
    if id:
        test1.login(id)
        test1.increase_account(id, 1000)
        test1.send_order(id, order)
        test1.send_order(id, order)
        test1.send_order(id, order)
    logger.info("Test FINISHED");


