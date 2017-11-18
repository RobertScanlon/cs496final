# filename:     main.py
# author:       Robert Scanlon
# description:  backend code for CS496 final project
# last edit:    16 November 2017

import webapp2
from google.appengine.ext import ndb
import json
import collections

RUN_LOCAL = True

BASE_URL = "http://localhost:8080/"
if not RUN_LOCAL:
    BASE_URL = "https://cs496final-186222.appspot.com/"

# class for item 1
class Person(ndb.Model):
    id = ndb.StringProperty()
    fname = ndb.StringProperty()
    lname = ndb.StringProperty()
    age = ndb.IntegerPropery()
    address = ndb.StringProperty()
    pets = ndb.StringProperty(repeated=True)

class Pet(ndb.Model):
    id = ndb.StringProperty()
    name = ndb.StringProperty()
    species = ndb.StringProperty()
    age = ndb.IntegerProperty()
    weight = ndb.IntegerProperty()
    owner = ndb.StringProperty()

def create_new_Item1(self):
    Item1_data = json.loads(self.request.body)
    print(Item1_data)
    if 'attribute1' not in Item1_data or \
       'attribute2' not in Item1_data:
        self.response.status_int = 400
        self.response.write('\'attribute\' and \'attribute2\' not found in POST request')
        return

    new_Item1 = Item1(attribute1=Item1_data['attribute1'],
                      attribute2=Item1_data['attribute2'])

    new_Item1.put()
    new_Item1.id = new_Item1.key.urlsafe()
    new_Item1.put()
    new_Item1 = ndb.Key(urlsafe=new_Item1.id).get()

    odict = collections.OrderedDict()
    odict['id'] = str(new_Item1.id)
    odict['attribute1'] = str(new_Item1.attribute1)
    odict['attribute2'] = str(new_Item1.attribute2)
    odict['self'] = str(BASE_URL) + \
                    str("item1/") + \
                    str(new_Item1.id)
    self.response.write(json.dumps(odict))


def get_single_Item1(self, Item1_id):
    item = ndb.Key(urlsafe=Item1_id).get()
    if not item:
        self.response.status_int = 400
        return

    odict = collections.OrderedDict()
    odict['id'] = str(item.id)
    odict['attribute1'] = str(item.attribute1)
    odict['attribute2'] = str(item.attribute2)
    odict['self'] = str(BASE_URL) + \
                    str("item1/") + \
                    str(item.id)
    self.response.write(json.dumps(odict))

class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write('Hello, World!')

class Item1Handler(webapp2.RequestHandler):
    def post(self):
        create_new_Item1(self)

    def get(self, id=None):
        if id:
            get_single_Item1(self, id)


# allow PATCH
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods

app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/item1', Item1Handler),
    ('/item1/([\w-]+)', Item1Handler),
], debug=True)
