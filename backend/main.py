# filename:     main.py
# author:       Robert Scanlon
# description:  backend code for CS496 final project
# last edit:    16 November 2017

import webapp2
from google.appengine.ext import ndb
import json
import collections

RUN_LOCAL = False 

BASE_URL = "http://localhost:8080/"
if not RUN_LOCAL:
    BASE_URL = "https://cs496final-186222.appspot.com/"

##############################################################################
##############################################################################
# class for People
##############################################################################
##############################################################################
class Person(ndb.Model):
    id = ndb.StringProperty()
    fname = ndb.StringProperty()
    lname = ndb.StringProperty()
    age = ndb.IntegerProperty()
    address = ndb.StringProperty()
    pets = ndb.StringProperty(repeated=True)

##############################################################################
##############################################################################
# class for Pets
##############################################################################
##############################################################################
class Pet(ndb.Model):
    id = ndb.StringProperty()
    name = ndb.StringProperty()
    species = ndb.StringProperty()
    age = ndb.IntegerProperty()
    weight = ndb.IntegerProperty()
    caretaker = ndb.StringProperty()


##############################################################################
# creates a new Person
##############################################################################
def create_new_Person(self):
    Person_data = json.loads(self.request.body)
    print(Person_data)
    if 'fname'   not in Person_data or \
       'lname'   not in Person_data or \
       'age'     not in Person_data or \
       'address' not in Person_data:
        self.response.status_int = 400
        self.response.write("post request must contain data fields: " + \
                            "fname, lname, age, and address")
        return

    new_Person = Person(fname=Person_data['fname'],
                        lname=Person_data['lname'],
                        age=Person_data['age'],
                        address=Person_data['address'],
                        pets=[])

    new_Person.put()
    new_Person.id = new_Person.key.urlsafe()
    new_Person.put()
    get_single_Person(self, new_Person.id)


##############################################################################
# creates a new Pet
##############################################################################
def create_new_Pet(self):
    Pet_data = json.loads(self.request.body)
    print(Pet_data)
    if 'name'    not in Pet_data or \
       'species' not in Pet_data or \
       'age'     not in Pet_data or \
       'weight'  not in Pet_data:
        self.response.status_int = 400
        self.response.write("post request must contain data fields: " + \
                            "name, species, age, and weight")
        return

    new_Pet = Pet(name=Pet_data['name'],
                  species=Pet_data['species'],
                  age=Pet_data['age'],
                  weight=Pet_data['weight'],
                  caretaker=None)

    new_Pet.put()
    new_Pet.id = new_Pet.key.urlsafe()
    new_Pet.put()
    get_single_Pet(self, new_Pet.id)


##############################################################################
# returns a single Person where id = id
##############################################################################
def get_single_Person(self, Person_id):
    person = ndb.Key(urlsafe=Person_id).get()
    if not person:
        self.response.status_int = 400
        return
    odict = collections.OrderedDict()
    odict['id']      = str(person.id)
    odict['fname']   = str(person.fname)
    odict['lname']   = str(person.lname)
    odict['age']     = str(person.age)
    odict['address'] = str(person.address)
    odict['pets']    = person.pets
    odict['self']    = str(BASE_URL) + \
                       str("person/") + \
                       str(person.id)
    self.response.write(json.dumps(odict))


##############################################################################
# returns a single Pet where id = id
##############################################################################
def get_single_Pet(self, Pet_id):
    pet = ndb.Key(urlsafe=Pet_id).get()
    if not pet:
        self.response.status_int = 400
        return
    odict = collections.OrderedDict()
    odict['id']         = str(pet.id)
    odict['name']       = str(pet.name)
    odict['species']    = str(pet.species)
    odict['age']        = str(pet.age)
    odict['weight']     = str(pet.weight)
    odict['caretaker']  = str(pet.caretaker)
    odict['self']       = str(BASE_URL) + \
                          str("pet/") + \
                          str(pet.id)
    self.response.write(json.dumps(odict))


##############################################################################
# returns all People
##############################################################################
def get_all_People(self):
    all_People = []
    People_Q = ndb.gql("SELECT * FROM Person")
    for p in People_Q.fetch():
        odict = collections.OrderedDict()
        odict['id']      = str(p.id)
        odict['fname']   = str(p.fname)
        odict['lname']   = str(p.lname)
        odict['age']     = str(p.age)
        odict['address'] = str(p.address)
        odict['pets']    = p.pets
        odict['self']    = str(BASE_URL) + \
                           str("person/") + \
                           str(p.id)
        all_People.append(odict)
    self.response.write(json.dumps(all_People))


##############################################################################
# returns all Pets
##############################################################################
def get_all_Pets(self):
    all_Pets = []
    Pets_Q = ndb.gql("SELECT * FROM Pet")
    for p in Pets_Q.fetch():
        odict = collections.OrderedDict()
        odict['id']         = str(p.id)
        odict['name']       = str(p.name)
        odict['species']    = str(p.species)
        odict['age']        = str(p.age)
        odict['weight']     = str(p.weight)
        odict['caretaker']  = str(p.caretaker)
        odict['self']       = str(BASE_URL) + \
                              str("pet/") + \
                              str(p.id)
        all_Pets.append(odict)
    self.response.write(json.dumps(all_Pets))


##############################################################################
# returns all People and Pets
##############################################################################
def get_all_pets_people(self):

    all_People = []
    People_Q = ndb.gql("SELECT * FROM Person")
    for p in People_Q.fetch():
        odict = collections.OrderedDict()
        odict['id']      = str(p.id)
        odict['fname']   = str(p.fname)
        odict['lname']   = str(p.lname)
        odict['age']     = str(p.age)
        odict['address'] = str(p.address)
        odict['pets']    = p.pets
        odict['self']    = str(BASE_URL) + \
                           str("person/") + \
                           str(p.id)
        all_People.append(odict)

    all_Pets = []
    Pets_Q = ndb.gql("SELECT * FROM Pet")
    for p in Pets_Q.fetch():
        odict = collections.OrderedDict()
        odict['id']         = str(p.id)
        odict['name']       = str(p.name)
        odict['species']    = str(p.species)
        odict['age']        = str(p.age)
        odict['weight']     = str(p.weight)
        odict['caretaker']  = str(p.caretaker)
        odict['self']       = str(BASE_URL) + \
                              str("pet/") + \
                              str(p.id)
        all_Pets.append(odict)

    stuff = {}
    stuff['People'] = all_People
    stuff['Pets'] = all_Pets
    self.response.write(json.dumps(stuff))

##############################################################################
# allows update of a Persons age and address
##############################################################################
def modify_Person(self, id):
    person = ndb.Key(urlsafe=id).get()
    if not person:
        self.response.status_int = 404
        return
    modify_data = json.loads(self.request.body)
    if 'age' in modify_data:
        person.age = int(modify_data['age'])
    if 'address' in modify_data:
        person.address = str(modify_data['address'])
    person.put()
    get_single_Person(self, id)


##############################################################################
# allows update of a Pets age and weight
##############################################################################
def modify_Pet(self, id):
    pet = ndb.Key(urlsafe=id).get()
    if not pet:
        self.response.status_int = 404
        return
    modify_data = json.loads(self.request.body)
    if 'age' in modify_data:
        pet.age = int(modify_data['age'])
    if 'weight' in modify_data:
        pet.weight = int(modify_data['weight'])
    pet.put()
    get_single_Pet(self, id)


##############################################################################
# deletes a Person
##############################################################################
def delete_Person(self, id):
    person_to_delete = ndb.Key(urlsafe=id).get()
    if not person_to_delete:
        self.response.status_int = 404
        return

    # remove care relation for all pets this person
    # was cartaker for
    pet_q = ndb.gql("SELECT * FROM Pet WHERE caretaker = '" + \
                    id + "'")
    for p in pet_q:
        p.caretaker = None
        p.put()

    person_to_delete.key.delete()
    self.response.status_int = 204

##############################################################################
# deletes a Pet
##############################################################################
def delete_Pet(self, id):
    pet_to_delete = ndb.Key(urlsafe=id).get()
    if not pet_to_delete:
        self.response.status_int = 404
        return

    # remove the pet.id from any persons pet list
    person_q = ndb.gql("SELECT * FROM Person WHERE pets = '" + \
                       id + "'")
    for p in person_q:
        p.pets.remove(id)
        p.put()

    pet_to_delete.key.delete()
    self.response.status_int = 204


##############################################################################
# makes a Person a Pets caretaker
##############################################################################
def add_care_relation(self, id):
    care_data = json.loads(self.request.body)
    if 'person_id' not in care_data:
        self.response.status_int = 400
        self.response.write("PUT request to add a care relationship " + \
                            "must contain a person_id in the body")
        return

    caretaker = ndb.Key(urlsafe=str(care_data['person_id'])).get()
    if not caretaker:
        self.response.status_int = 400
        self.response.write("the given id does not match any person")
        return

    pet = ndb.Key(urlsafe=id).get()
    if not pet:
        self.response.status_int = 400
        self.response.write("the given id does not match any pet")
        return

    if pet.caretaker:
        self.response.status_int = 400
        self.response.write("pet already has a caretaker")
        return

    pet.caretaker = str(caretaker.id)
    pet.put()

    caretaker.pets.append(str(pet.id))
    caretaker.put()



##############################################################################
# deletes care relationship bewteen person and pet
##############################################################################
def remove_care_relation(self, id):
    pet = ndb.Key(urlsafe=id).get()
    if not pet:
        self.response.status_int = 400
        self.response.write("the given id does not match any pet")
        return

    person = ndb.Key(urlsafe=pet.caretaker).get()
    if not person:
        self.response.status_int = 400
        self.response.write("the caretaker id for this pet does not " + \
                            "identify a valid person")
        return

    # remove the pet.id from the persons pets list
    person.pets.remove(pet.id)
    pet.caretaker = None
    person.put()
    pet.put()


##############################################################################
# Returns all pets with no caretaker
##############################################################################
def get_free_pets(self):
    free_pets = []
    pets_q = ndb.gql("SELECT * FROM Pet WHERE caretaker = NULL")
    for p in pets_q:
        odict = collections.OrderedDict()
        odict['id']         = str(p.id)
        odict['name']       = str(p.name)
        odict['species']    = str(p.species)
        odict['age']        = str(p.age)
        odict['weight']     = str(p.weight)
        odict['caretaker']  = str(p.caretaker)
        odict['self']       = str(BASE_URL) + \
                              str("pet/") + \
                              str(p.id)
        free_pets.append(odict)
    self.response.write(json.dumps(free_pets))


##############################################################################
# Returns a list of pets a person is caretaker for
##############################################################################
def get_persons_pets(self, id):
    pets = []
    person = ndb.Key(urlsafe=id).get()
    if not person:
        self.response.status_int = 404
        self.response.write("No such person with ID id")

    pets_q = ndb.gql("SELECT * FROM Pet WHERE caretaker = '" + \
                     person.id + "'")
    for p in pets_q:
        odict = collections.OrderedDict()
        odict['id']         = str(p.id)
        odict['name']       = str(p.name)
        odict['species']    = str(p.species)
        odict['age']        = str(p.age)
        odict['weight']     = str(p.weight)
        odict['caretaker']  = str(p.caretaker)
        odict['self']       = str(BASE_URL) + \
                              str("pet/") + \
                              str(p.id)
        pets.append(odict)
    self.response.write(json.dumps(pets))


##############################################################################
# Handlers
#############################################################################
class MainPage(webapp2.RequestHandler):
    def get(self):
        get_all_pets_people(self)

class PersonHandler(webapp2.RequestHandler):
    def post(self):
        create_new_Person(self)

    def get(self, id=None):
        if id:
            get_single_Person(self, id)
        else:
            get_all_People(self)

    def patch(self, id=None):
        if not id:
            return
        modify_Person(self, id)

    def delete(self, id=None):
        if not id:
            return
        delete_Person(self, id)

class PetHandler(webapp2.RequestHandler):
    def post(self):
        create_new_Pet(self)

    def get(self, id=None):
        if id:
            get_single_Pet(self, id)
        else:
            get_all_Pets(self)

    def patch(self, id=None):
        if not id:
            return
        modify_Pet(self, id)

    def delete(self, id=None):
        if not id:
            return
        delete_Pet(self, id)

class CaretakerHandler(webapp2.RequestHandler):
    def put(self, id=None):
        if not id:
            return
        add_care_relation(self, id)

    def patch(self, id=None):
        if not id:
            return
        remove_care_relation(self, id)

class FreePetsHandler(webapp2.RequestHandler):
    def get(self):
        get_free_pets(self)

class PersonPetsHandler(webapp2.RequestHandler):
    def get(self, id=None):
        if not id:
            return
        get_persons_pets(self, id)

# allow PATCH
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods

app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/person', PersonHandler),
    ('/person/([\w-]+)', PersonHandler),
    ('/person/([\w-]+)/pets', PersonPetsHandler),
    ('/pet', PetHandler),
    ('/pet/free', FreePetsHandler),
    ('/pet/([\w-]+)', PetHandler),
    ('/pet/([\w-]+)/caretaker', CaretakerHandler),
], debug=True)
