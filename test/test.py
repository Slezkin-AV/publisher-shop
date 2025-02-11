#!/usr/bin/python3


import json
import logging
# from locust import FastHttpUser, task, between, run_single_user, HttpUser
import faker
# from faker import Faker
import requests
from requests.exceptions import HTTPError
import sys
import time

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
API_URL_WARE=f"http://{PUB_HOST}:{SERVICE_PORT}/ware"
API_URL_NOTE=f"http://{PUB_HOST}:{SERVICE_PORT}"
API_URL_DELIVERY=f"http://{PUB_HOST}:{SERVICE_PORT}"

# targets={API_URL_USER, API_URL_ORDER, API_URL_BILLING, API_URL_WARE, NOTE_HOST, API_URL_DELIVERY}
targets={API_URL_USER,API_URL_BILLING + "/account",API_URL_ORDER+"/order",API_URL_DELIVERY+"/delivery",API_URL_NOTE+"/note"}

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
                    resp = requests.put(url, headers=header, json=json_data, params=param)
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
            logger.debug(f"data: {resp.text}")
            if resp.ok:
                logger.info(f"response on {url}: {resp.text}")
                return resp
            else:
                logger.error(f"responce: {resp}: {resp.status_code}")
                logger.error(f"Error response on {url}, headers={header}, json={json_data}, params={param}") 


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
        resp = self.test_request("post", API_URL_PUB + "/register", header, user, {})
        if resp and resp.ok:
            id = resp.json()["id"]
            self.users[id]=user
            login = {
                "login": user['login'],
                "password": user['password']
                }
            self.logins[id]=login
            time.sleep(1)
            return id


    def login(self, id):
        logger.info(f"login for id: {id}")
        lg = self.logins[id]
        header={'Contetn-Type': 'application/json'}
        resp = self.test_request("post", API_URL_PUB + "/login", header, lg, {})
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
        logger.debug(f"send order : {order}")

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

    def cancel_order(self, id, order):
        
        # order = self.new_order(id)
        logger.debug(f"send order : {order}")

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

        resp = self.test_request("post", API_URL_ORDER + "/cancel", header, order, {})
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



    def clean_all(self):
        header = {"Contetn-Type": "application/json"}
        for target in targets:
            resp = self.test_request("post", target + "/clean", header, {}, {} )
            if resp:
                pass


# ======================================== #


if __name__ == "__main__":
    
    # logging.basicConfig(level=logging.INFO)
    logger.info("Test STARTING");


    ware1 = 21       # успешный сценарий + идемпотентность
    ware2 = 22       # не хватает денег, товара, безуспешная доставка
    ware9 = 29       # отмена счета

    test1 = OrderTest() 

    nums = len(sys.argv)
    for i in range(1,nums):
        

        if sys.argv[i] == "ware":
            logger.debug(f"processing 'ware'")
            test1.clean_ware()


        if sys.argv[i] == "clean":
            logger.debug(f"processing 'clean'")
            test1.clean_all()


        if sys.argv[i] == "order1":
            logger.debug(f"processing 'order1'")
            id = test1.create_new_user()
            if id:
                test1.login(id)
                test1.increase_account(id, 1000)

                order = test1.new_order(id,10,50,ware1)
                logger.debug(f"Test USER: {order}");
                test1.send_order(id, order)


        if sys.argv[i] == "order11":
            logger.debug(f"processing 'order11'")
            id = test1.create_new_user()
            if id:
                test1.login(id)
                test1.increase_account(id, 1000)

                order = test1.new_order(id,10,50,ware1)
                logger.debug(f"Test USER: {order}");
                test1.send_order(id, order)
                test1.send_order(id, order)
                time.sleep(5)
                test1.send_order(id, order)


        if sys.argv[i] == "order2":
            logger.debug(f"processing 'order2'")
            id = test1.create_new_user()
            if id:
                test1.login(id)
                test1.increase_account(id, 1000)

                order = test1.new_order(id,10,1100,ware2) # превышение денег
                logger.debug(f"Test USER: {order}");
                test1.send_order(id, order)

                order = test1.new_order(id,200,450,ware2) # превышение товара
                logger.debug(f"Test USER: {order}");
                test1.send_order(id, order)

                order = test1.new_order(id,10,450,ware2) # безуспешная доставка
                logger.debug(f"Test USER: {order}");
                test1.send_order(id, order)


        if sys.argv[i] == "order9":
            logger.debug(f"processing 'order9'")
            id = test1.create_new_user()

            if id:
                test1.login(id)
                test1.increase_account(id, 1000)
                order = test1.new_order(id,10,50,ware9) # отмена счета
                logger.debug(f"Test USER: {order}");
                test1.send_order(id, order)
                sleep(1)
                test1.cancel_order(id, order)


    logger.info("Test FINISHED");
