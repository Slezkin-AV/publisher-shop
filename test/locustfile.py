import json
import logging
from locust import FastHttpUser, task, between, run_single_user, HttpUser
import faker
from requests.exceptions import HTTPError

import random

API_URL='http://{SERVICE_HOST}:{SERVICE_PORT}'.format(
    SERVICE_HOST="publisher.localdev.me",
    SERVICE_PORT=8000
)



PUB_HOST='publisher.localdev.me'
USER_HOST='publisher.localdev.me'
ORDER_HOST='order.localdev.me'
BILLING_HOST='billing.localdev.me'
WARE_HOST='ware.localdev.me'
NOTE_HOST='note.localdev.me'
DELIVERY_HOST='delivery.localdev.me'
SERVICE_PORT=8000
API_URL_PUB=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_USER=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_ORDER=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_BILLING=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_WARE=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_NOTE=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_DELIVERY=f"http://{PUB_HOST}:{SERVICE_PORT}"


#
# from flask import Flask, request, abort, redirect
# app = Flask(__name__)

ware_start = 30
account_sum =100000

# ======================================== #

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
logger.setLevel(logging.INFO)

# create console handler with a higher log level
ch = logging.StreamHandler()
ch.setLevel(logging.INFO)
ch.setFormatter(CustomFormatter())
logger.addHandler(ch)

# ======================================== #

class FirstSuccess(FastHttpUser):
    wait_time = between(1, 2)
    token = ""
    users = []
    logins = []
    tokens = []


    def test_request(self, operation, url, path, head_name, header, json_data, param):
        logger.debug(f"test_request: on {url} {path}")
        API_URL = url
        try:
            match operation:
                case "get":
                    resp = self.client.get(path, name = head_name, headers=header, params=param)
                    pass
                case "put":
                    resp = self.client.put(path, name = head_name, headers=header, json=json_data, params=param)
                    pass
                case "post":
                    resp = self.client.post(path, name = head_name, headers=header, json=json_data, params=param)
                    pass
                case _:
                    logger.warning(f"test_request: no operation {operation}")
                    return 
            pass    
        except HTTPError as err:
            logger.error(f"HTTPError: {err}")
        except Exception as ex:
            logger.error(f"Exception: {ex}")
        else:
            logger.debug(f"data: {resp.text}")
            logger.debug(f"responce: {resp}: {resp.status_code}")
            if 200 <= resp.status_code < 300:
                # logger.debug(f"response on {url}: {resp.text}")
                # resp.success()
                pass
            else:
                logger.error(f"responce: status_code: {resp.status_code}")
                logger.error(f"Error response on {url}, path={path},  headers={header}, json={json_data}, params={param}") 
            return resp




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
        header={'Contetn-Type': 'application/json'}
        resp = self.test_request("post", API_URL_USER, "/register", "/register", header, user, {} )
        # resp = self.client.post("/register", name="/register", json=user)
        if 200 <= resp.status_code < 300:
            self.users.append(user)
            login = {
                "login": user['login'],
                "password": user['password']
            }
            return login


    @task(2)
    def create_users(self, num = 3):
        logger.info("create_users")
        tokenList = {}
        header={'Contetn-Type': 'application/json'}
        for i in range(num):
            lg = self.create_new_user()
            logger.debug("create_users: login: {lg}")
            with self.test_request("post", API_URL_USER, "/login", "/login", header, lg, {} ) as resp:
            # with self.client.post("/login", name="/login", json=lg) as resp:
                # logger.info(f"/login got: {resp.text}")
                if resp.text:
                    if not (200 <= resp.status_code < 300):
                        logger.error(f"Error response /login: {resp.status_code}")
                        pass
                    elif "accessToken" not in resp.text:
                        logger.warning(f"'accessToken' missing from response {resp.text}")
                    else:
                        tok = resp.json()["accessToken"]
                        id = resp.json()["userID"]
                        tokenList[id] = (lg['login'], tok)
                        self.logins.append(lg)
                        self.increase_account(id, account_sum)
                        # self.tokens[id] = (lg['login'], tok)
        if tokenList:
            self.tokens.append(tokenList)
        logger.info(f"create_users: {len(self.tokens)}: {len(tokenList)}")


    def on_start(self):
        # locust.stats_logger
        logger.info("on_start")
        # test_data
        self.create_users(1)
        # self.create_users(5)
        # self.create_users(5)
        # user = {"firstName": "f", "lastName": "l", "login": "ll", "email": "em","password": "123" }


    # @task(5)
    def get_user(self):
        # logger.info("get_user")
        tokenListNew = {}
        if self.tokens:
            tokenList = self.tokens.pop(0)
        else:
            logger.warning(f"No tokens /get_user")
            return
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            # logger.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            with self.client.get(f"/user/{id}", name="/get_user", headers=header) as resp:
                if resp.text:
                    if 200 <= resp.status_code < 300:
                        tokenListNew[id] = (lg, tok)
                    else:
                        logger.error(f"Error response /get_user: {resp.status_code}")
                        logger.error(f"id: {id}, login: {lg}, header: {header}")
                else:
                    logger.error(f"Error response /get_user: {resp.text}")
                    logger.error(f"id: {id}, login: {lg}, header: {header}")
        if tokenListNew:
            self.tokens.append(tokenListNew)
        logger.info(f"get_user: {len(self.tokens)}: {len(tokenListNew)}")

    # @task(3)
    def update_user(self):
        # logger.info("update_user")
        tokenListNew = {}
        if self.tokens:
            tokenList = self.tokens.pop(0)
        else:
            logger.warning(f"No tokens /update_user")
            return
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            # logger.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            usr = self.find_user(lg)
            usr["lastName"] = usr["firstName"]
            with self.client.put(f"/user/{id}", name="/update_user",headers=header, json=usr) as resp:
                if resp.text:
                    if 200 <= resp.status_code < 300:
                        tokenListNew[id] = (lg, tok)
                    else:
                        logger.error(f"Error response /update_user: {resp.status_code}")
                        logger.error(f"id: {id}, login: {lg}, user: {usr}, header: {header}")
                else:
                    logger.error(f"Error response /update_user: {resp.text}")
                    logger.error(f"id: {id}, login: {lg}, user: {usr}, header: {header}")
        if tokenListNew:
            self.tokens.append(tokenListNew)
        logger.info(f"update_user: {len(self.tokens)}: {len(tokenListNew)}")


    # @task(1)
    def delete_user(self):
        # logger.info("delete_user")
        if self.tokens:
            tokenList = self.tokens.pop(0)
        else:
            logger.warning(f"No tokens /delete_user")
            return
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            # logger.info(f"id: {id}, login: {lg}, token: {tok}")
            header = {"Authorization": f"Bearer {tok}"}
            usr = self.find_user(lg)
            # del self.users
            with self.client.delete(f"/user/{id}", name="/delete_user", headers=header, json=usr) as resp:
                if resp.text:
                    if 200 <= resp.status_code < 300:
                        pass
                    else:
                        logger.error(f"Error response /delete_user: {resp.status_code}")
                        logger.error(f"id: {id}, login: {lg}, user: {usr}, header: {header}")
                else:
                    logger.error(f"Error response /delete_user: {resp.text}")
                    logger.error(f"id: {id}, login: {lg}, user: {usr}, header: {header}")
        logger.info(f"delete_user: {len(self.tokens)}")

    # @task(5)
    # def get_account(self):
    #     # logger.info("get_user")
    #     for id in self.tokens:
    #         lg = self.tokens[id][0]
    #         tok = self.tokens[id][1]
    #         # logger.info(f"id: {id}, login: {lg}, token: {tok}")
    #         header = {"Authorization": f"Bearer {tok}"}
    #         with self.client.get(f"/accountByUserId/{id}", headers=header) as resp:
    #             pass

    # @task(5)
    def clean_ware(self):
        logger.info(f"clean_ware")
        header={'Contetn-Type': 'application/json'}
        resp = self.test_request("post", API_URL_WARE, "/ware/clean", "/clean_ware", header, {}, {} )


    def new_order(self, id, am, sum=450, ware=5):
        order = {
            "userId": id,
            "amount": am,
            "sum": sum,
            "wareId": ware + ware_start
        }
        return order


    @task(40)
    def order1(self):
        # logger.info("get_user")
        tokenListNew = {}
        if self.tokens:
            tokenList = self.tokens[0]
        else:
            logger.warning(f"No tokens /order1")
            return
        for id in tokenList:
            lg = tokenList[id][0]
            tok = tokenList[id][1]
            header={'Contetn-Type': 'application/json'}
            order = self.new_order(id, random.randint(1,5), random.randint(1,10), random.randint(1,10))
            logger.info(f"order1: order={order}")
            with self.test_request("post", API_URL_ORDER, "/order", "/order", header, order, {} ) as resp:
                if resp.text:
                    if not (200 <= resp.status_code < 300):
                        logger.error(f"Error response /order: {resp.status_code}")
                        pass
                    else:
                        # logger.info(f"order1: {resp.status_code} on {order}")
                        pass
        


    def increase_account(self, id, sum):
        logger.info(f"increase_account: for user {id} on {sum}")
        # if self.tokens:
        #     token = self.tokens[id]
        # else:
        #     logger.warning(f"No tokens /increase_account")
        #     return
        account = {}
        # header = {"Authorization": f"Bearer {token}"}
        header = {"Contetn-Type": "application/json"}
        param = "sum=" + str(sum)
        resp = self.test_request("post", API_URL_BILLING, "/account/" + str(id) ,"/account", header, {}, param)
        # resp = self.test_request("get", API_URL_BILLING + "/account/" + str(id), header, {}, {})
        # if resp:
        #     resp = self.test_request("post", API_URL_BILLING + "/account/" + str(id),"/account" ,"/account", header, {}, param)


if __name__ == "__main__":

    # # create console handler with a higher log level
    # ch = logger.StreamHandler()
    # ch.setLevel(logger.DEBUG)
    # ch.setFormatter(CustomFormatter())
    # logger.addHandler(ch)

    run_single_user(FirstSuccess)