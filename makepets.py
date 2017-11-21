import requests
import json
import sys
if sys.argv[len(sys.argv) - 1] == "local":
    new_person_url = "http://localhost:8080/person"
else:
    new_person_url = "https://cs496final-186222.appspot.com/person"

class pet:
    def __init__(self, n, s, a, w):
        self.name = n
        self.species = s
        self.age = a
        self.weight = w
    name = ""
    species = ""
    age = 0
    weight = 0

pt1 = pet("Hazel", "Cat", 5, 10) 
pt2 = pet("Fallon", "Cat", 5, 9) 
pt3 = pet("Freddy", "Cat", 6, 12) 
pt4 = pet("Rocky", "Dog", 4, 20) 
pt5 = pet("Duchess", "Dog", 6, 22) 
pets = [pt1, pt2, pt3, pt4, pt5]

for p in pets:
    j = json.dumps(p.__dict__)
    r = requests.post(new_pet_url, j)
    print(r.text)
