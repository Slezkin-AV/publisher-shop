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
SERVICE_HOST2='billing.localdev.me'
SERVICE_HOST3='note.localdev.me'
SERVICE_HOST4='ware.localdev.me'
SERVICE_PORT='8000'
API_URL=f"http://{SERVICE_HOST}:{SERVICE_PORT}"
API_URL1=f"http://{SERVICE_HOST1}:{SERVICE_PORT}"

targets={SERVICE_HOST, SERVICE_HOST1, SERVICE_HOST2, SERVICE_HOST4}
# "/note", 


class ClearAll:

    def clearAll(self):
        header = {"Contetn-Type": "application/json"}
        for target in targets:
            host = "http://" + target +":" + SERVICE_PORT +"/clear"
            logging.info(f"requesting for: {host}")
            try:
                resp = requests.post(host, headers=header)
            except HTTPError as err:
                logging.error(f"Error: {err}")
            except Exception as ex:
                logging.error(f"Exception: {ex}")
            else:
                logging.info(f"responce: {resp}")
                if resp.ok:
                    logging.info(f"response {target}/clear: {resp.text}")
                else:
                    logging.info(f"Error response {target}/clear: {resp.status_code}")  


if __name__ == "__main__":
    
    logging.basicConfig(level=logging.INFO)
    logging.info("Clear STARTING");
    clear = ClearAll();
    clear.clearAll()
    logging.info("Clear FINISHED");


