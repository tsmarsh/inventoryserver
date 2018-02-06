from locust import HttpLocust, TaskSet, task
from inventory_server.models import User, Inventory, Category, Metric
from faker import Faker
import random
import json

fake = Faker()
known_inventories = []
all_users = []

def none_or():
    if random.randint(0,1) and len(known_inventories) > 0:
        random.choice(known_inventories)
    return None

def j(model):
    return json.dumps(model.to_dict())

def gen_new_metric():
    m = Metric(value=fake.text(), type=fake.word())
    return m

def gen_new_inv():
    inv = Inventory(
        category=fake.domain_name(),
        metrics=[gen_new_metric() for _ in range(0, random.randint(0,10))],
        parent=none_or()
    )
    return inv

def gen_new_user():
    u = User(name = fake.name(), inventories= list(filter(None, [none_or() for _ in range(0, random.randint(0,10))])))
    return u

class InventoryTasks(TaskSet):
    @task
    def inventory_register(self):
        response = self.client.post("/inventories", j(gen_new_inv()))

    @task(10)
    def all_inventory(self):
        response = self.client.get("/inventories")
        if(response.status_code < 400):
            try:
                global known_inventories
                known_inventories = response.json()
            except:
                print(known_inventories)
    
    @task(100)
    def read(self):
        if len(known_inventories) > 0:
            inv = random.choice(known_inventories)
            if 'id' in inv:
                self.client.get(inv['id'], name='/inventories/[id]')
        

    @task
    def user_register(self):
        self.client.post("/users", j(gen_new_user()))
    
    @task(10)
    def users_all(self):
        response = self.client.get("/users")
        global all_users
        all_users = response.json()

    @task(100)
    def users_read(self):
        if len(all_users) > 0:
            u = random.choice(all_users)
            if 'id' in u:
                self.client.get(u['id'], name="/users/[id]")
    
class MyLocust(HttpLocust):
    task_set = InventoryTasks
    min_wait = 1000 
    max_wait = 5000