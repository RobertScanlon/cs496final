import requests
import json

#new_person_url = "https://cs496final-186222.appspot.com/person"
new_person_url = "http://localhost:8080/person"

class people:
    def __init__(self, fn, ln, age, addr):
        self.fname = fn
        self.lname = ln
        self.age = age
        self.address = addr
    fname = ""
    lname = ""
    age = 0
    address = ""

p1 = people("Robert", "Scanlon", 25, "Huntington, NY")
p2 = people("Cathleen", "Scanlon", 61, "Huntington, NY")
p3 = people("Jim", "Scanlon", 10, "Westbury, NY")
people = [p1, p2, p3]

for p in people:
    j = json.dumps(p.__dict__)
    r = requests.post(new_person_url, j)
    print(r.text)
