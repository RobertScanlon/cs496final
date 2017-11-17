# filename:    main.py
# author:      Robert Scanlon (structure based on google app engine
#                             sample documentation)
# description: source code for cs 496 marina api assignment
# last edit:   15 October 2017

from google.appengine.ext import ndb
import webapp2
import json
import collections

BASE_URL = "https://stable-splicer-182920.appspot.com/"
#BASE_URL = "http://localhost:8080/"

# class for Boats
class Boat(ndb.Model):
    id = ndb.StringProperty()
    name = ndb.StringProperty(required=True)
    type = ndb.StringProperty(required=True)
    length = ndb.IntegerProperty(required=True)
    at_sea = ndb.BooleanProperty(required=True)

# class for Boat Slips
class Slip(ndb.Model):
    id = ndb.StringProperty()
    number = ndb.IntegerProperty(required=True)
    current_boat = ndb.StringProperty()
    arrival_date = ndb.StringProperty()
    departure_history = ndb.StringProperty(repeated=True)

# return one boat by ID
def get_single_boat(self, boat_id):
    boat = ndb.Key(urlsafe=boat_id).get()
    if not boat:
        self.response.status_int = 404
        return
    boat_ordered_dict = collections.OrderedDict()
    boat_ordered_dict['id'] = str(boat.id)
    boat_ordered_dict['self'] = str(BASE_URL) + "boat/" + \
                                str(boat.id)
    boat_ordered_dict['name'] = str(boat.name)
    boat_ordered_dict['type'] = str(boat.type)
    boat_ordered_dict['length'] = str(boat.length)
    boat_ordered_dict['at_sea'] = str(boat.at_sea)
    self.response.write(json.dumps(boat_ordered_dict))

# return all boats in datastore
def get_all_boats(self):
    all_boats = []
    boats_query = ndb.gql("SELECT * FROM Boat")
    for b in boats_query.fetch():
        boat_ordered_dict = collections.OrderedDict()
        boat_ordered_dict['id'] = b.id
        boat_ordered_dict['self'] = str(BASE_URL) + "boat/" + \
                                    str(b.id)
        boat_ordered_dict['name'] = b.name
        boat_ordered_dict['type'] = b.type
        boat_ordered_dict['length'] = b.length
        boat_ordered_dict['at_sea'] = b.at_sea
        all_boats.append(boat_ordered_dict)
    self.response.write(json.dumps(all_boats))

# return one slip by ID
def get_single_slip(self, slip_id):
    slip = ndb.Key(urlsafe=slip_id).get()
    if not slip:
        self.response.status_int = 404
        return
    slip_ordered_dict = collections.OrderedDict()
    slip_ordered_dict['id'] = str(slip.id)
    slip_ordered_dict['self'] = str(BASE_URL) + "slip/" + \
                                str(slip.id)
    slip_ordered_dict['number'] = str(slip.number)
    slip_ordered_dict['current_boat'] = str(slip.current_boat)
    slip_ordered_dict['arrival_date'] = str(slip.arrival_date)
    slip_ordered_dict['departure_history'] = str(slip.departure_history)
    self.response.write(json.dumps(slip_ordered_dict))

# return all slips in datastore
def get_all_slips(self):
    all_slips = []
    slips_query = ndb.gql("SELECT * FROM Slip")
    for s in slips_query.fetch():
        slip_ordered_dict = collections.OrderedDict()
        slip_ordered_dict['id'] = s.id
        slip_ordered_dict['self'] = str(BASE_URL) + "slip/" + \
                                    str(s.id)
        slip_ordered_dict['number'] = s.number
        slip_ordered_dict['current_boat'] = s.current_boat
        slip_ordered_dict['arrival_date'] = s.arrival_date
        slip_ordered_dict['departure_history'] = s.departure_history
        all_slips.append(slip_ordered_dict)
    self.response.write(json.dumps(all_slips))

def create_new_boat(self):
    # a new boat must have a name, type, and length
    # verify request data for new boat
    boat_data = json.loads(self.request.body)
    if 'name' not in boat_data or \
       'type' not in boat_data or \
       'length' not in boat_data:
           self.response.status_int = 400
           self.response.write("request must include a boat \'name\', " +
                               "\'type\', and \'length\'")
           return
    # all new boat start out 'at sea'
    new_boat = Boat(name=boat_data['name'],
                    type=boat_data['type'],
                    length=boat_data['length'],
                    at_sea=True)
    
    # add to database
    new_boat.put()

    # set the new boats id as the url_safe version of
    # its database key
    new_boat.id = new_boat.key.urlsafe()
    new_boat.put()

    # write back to client
    # add self link to returned json
    self.response.status_int = 201
    get_single_boat(self, new_boat.id)

def update_boat(self, id):
    boat = ndb.Key(urlsafe=id).get()
    if not boat:
        self.response.status_int = 404
        return
    update_data = json.loads(self.request.body)
    
    new_name = str(boat.name)
    new_type = str(boat.type)
    new_length = int(boat.length)

    if 'name' in update_data:
        new_name = update_data['name']
    if 'type' in update_data:
        new_type = update_data['type']
    if 'length' in update_data:
        new_length = update_data['length']

    boat.name = new_name
    boat.type = new_type
    boat.length = new_length
    boat.put()

    get_single_boat(self, id)

def replace_boat(self, id):
    boat = ndb.Key(urlsafe=id).get()
    if not boat:
        self.response.status_int = 404
        return

    replace_data = json.loads(self.request.body)
    if 'name' not in replace_data or \
       'type' not in replace_data or \
       'length' not in replace_data:
           self.response.status_int = 400
           self.response.write("Request to replace boat must contain " + \
                               "a new name, type, and length.")
           return

    if not boat.at_sea:
        undock_boat(self, id, True)

    boat.name = replace_data['name']
    boat.type = replace_data['type']
    boat.length = replace_data['length']
    boat.at_sea = True
    boat.put()
    get_single_boat(self, id)

def create_new_slip(self):
    # requests for new slips must have a slip number
    slip_data = json.loads(self.request.body)
    if 'number' not in slip_data:
        self.response.status_int = 400
        self.response.write("request must include a slip 'number'")
        return

    # make sure 'number' is not already used
    used_numbers = []
    num_query = ndb.gql("SELECT number FROM Slip")
    for s in num_query.fetch():
        used_numbers.append(s.number)
    if slip_data['number'] in used_numbers:
        self.response.status = 400
        self.response.write("A slip already exists with the 'number' " + \
                            str(slip_data['number']))
    else:
        # all new slips start out 'empty'
        new_slip = Slip(number=slip_data['number'],
                        current_boat=None,
                        arrival_date=None,
                        departure_history=[])

        # add to database
        new_slip.put()

        # set slip id to urlsafe database key
        new_slip.id = new_slip.key.urlsafe()
        new_slip.put()

        # write back to client
        # and include self link
        self.response.status_int = 201
        get_single_slip(self, new_slip.id)

def update_slip(self, id):
    slip = ndb.Key(urlsafe=id).get()
    if not slip:
        self.response.status_int = 404
        return
    update_data = json.loads(self.request.body)
    
    if 'number' not in update_data:
        self.response.status_int = 400
        self.response.write("Request to modify slip must " + \
                            "include a new slip number")

    # make sure 'number' is not already used
    used_numbers = []
    num_query = ndb.gql("SELECT number FROM Slip")
    for s in num_query.fetch():
        used_numbers.append(s.number)
    if update_data['number'] in used_numbers:
        self.response.status = 400
        self.response.write("A slip already exists with the 'number' " + \
                            str(update_data['number']))
        return

    slip.number = update_data['number']
    slip.put()

    get_single_slip(self, id)

def replace_slip(self, id):
    slip = ndb.Key(urlsafe=id).get()
    if not slip:
        self.response.status_int = 404
        return

    replace_data = json.loads(self.request.body)
    if 'number' not in replace_data:
           self.response.status_int = 400
           self.response.write("Request to replace slip must contain " + \
                               "a new number.")
           return

    if slip.current_boat:
        boat = ndb.Key(urlsafe=slip.current_boat)
        if boat:
            undock_boat(self, slip.current_boat, True)

    slip.number = replace_data['number']
    slip.current_boat = None
    slip.arrival_date = None
    slip.departure_history = []
    slip.put()
    get_single_slip(self, id)

def get_current_boat(self, id):
    slip = ndb.Key(urlsafe=id).get()
    if not slip:
        self.response.status_int = 404
        return
    boat_id = slip.current_boat
    if not boat_id:
        self.response.write("No boat currently at slip")
        return
    boat = ndb.Key(urlsafe=boat_id).get()
    if not boat:
        self.response.status_int = 404
        return
    get_single_boat(self, boat.id)

def delete_boat(self, boat_id):
    
    boat_to_delete = ndb.Key(urlsafe=boat_id).get()
    if not boat_to_delete:
        self.response.status_int = 404
        return

    # if boat is at a slip, update the slip to available
    if boat_to_delete.at_sea == False:
        slip_query = ndb.gql("SELECT * FROM Slip WHERE current_boat = '" + \
                             str(boat_to_delete.id) + "'").fetch(1)
        if slip_query:
            slip = slip_query[0]
            slip.current_boat = None
            slip.arrival_date = None
            slip.put()

    # delete Boat
    boat_to_delete.key.delete()
    self.response.status_int = 204

def delete_slip(self, slip_id):

    slip_to_delete = ndb.Key(urlsafe=slip_id).get()
    if not slip_to_delete:
        self.response.status_int = 404
        return

    # if the slip contains a boat, set boat to at_sea: True
    if slip_to_delete.current_boat is not None:
        boat_query = ndb.gql("SELECT * FROM Boat WHERE id = '" + \
                             str(slip_to_delete.current_boat) + "'").fetch(1)
        if boat_query:
            boat = boat_query[0]
            boat.at_sea = True
            boat.put()

    # delete slip
    slip_to_delete.key.delete()
    self.response.status_int = 204

def dock_boat(self, id):
    
    docking_data = json.loads(self.request.body)
    if 'arrival_date' not in docking_data:
        self.response.status_int = 400
        self.response.write("Missing arrival date")
        return

    # check that boat id identifies a valid Boat
    boat_to_dock = ndb.Key(urlsafe=id).get()
    if not boat_to_dock:
        self.response.status_int = 400
        self.response.write("Invalid Boat ID")
        return

    if boat_to_dock.at_sea == False:
        self.response.status_int = 400
        self.response.write("Boat already at slip")
        return

    # get an available slip, if any
    slip_query = ndb.gql("SELECT * FROM Slip WHERE current_boat = NULL").fetch(1)
    if not slip_query:
        self.response.status_int = 403
        self.response.write("No available slips")
        return
    
    slip = slip_query[0]
    slip.current_boat = boat_to_dock.id
    slip.arrival_date = docking_data['arrival_date']
    boat_to_dock.at_sea = False
    slip.put()
    boat_to_dock.put()
    get_single_boat(self, id)

def undock_boat(self, id, replacement):
  
    if not replacement:
        departure_data = json.loads(self.request.body)
        if 'departure_date' not in departure_data:
            self.response.status_int = 400
            self.response.write("Missing departure date")
            return

    # check that boat id identifies a valid Boat
    boat_to_depart = ndb.Key(urlsafe=id).get()
    if not boat_to_depart:
        self.response.status_int = 400
        self.response.write("Invalid Boat ID")
        return

    if boat_to_depart.at_sea == True:
        self.response.status_int = 400
        self.response.write("Boat already at sea")
        return

    # get an slip containing the boat 
    slip_query = ndb.gql("SELECT * FROM Slip WHERE current_boat = '" + \
                         str(boat_to_depart.id) + "'").fetch(1)
    if not slip_query:
        self.response.status_int = 500
        self.response.write("Error getting slip data")
        return
    
    slip = slip_query[0]
    
    if not replacement:
        history_data = {}
        history_data['departure_date'] = str(departure_data['departure_date'])
        history_data['departed_boat'] = str(boat_to_depart.id)
        history_data = str(history_data).encode('utf-8')
        slip.departure_history.append(history_data)
    slip.current_boat = None
    slip.arrival_date = None
    slip.put()

    boat_to_depart.at_sea = True
    boat_to_depart.put()

    if not replacement:
        get_single_boat(self, id)

class MainPage(webapp2.RequestHandler):

    def get(self):

        data = {}
        data['Boats'] = []
        data['Slips'] = []

        # select all created boats
        boat_query = ndb.gql("SELECT * FROM Boat")

        for b in boat_query.fetch():
            boat_dict = b.to_dict()
            data['Boats'].append(boat_dict)

        # select all slips
        slip_query = ndb.gql("SELECT * FROM Slip")
        for s in slip_query.fetch():
            slip_dict = s.to_dict()
            data['Slips'].append(slip_dict)
        
        self.response.write(json.dumps(data))

class BoatHandler(webapp2.RequestHandler):
    def post(self):
        print(self.request.body)
        create_new_boat(self)

    def get(self, id=None):
        if id:
            get_single_boat(self, id)
        else:
            get_all_boats(self)
    
    def delete(self, id=None):
        if id:
            delete_boat(self, id)

    def patch(self, id=None):
        if id:
            update_boat(self, id)

    def put(self, id=None):
        if id:
            replace_boat(self, id)

class SlipHandler(webapp2.RequestHandler):
    def post(self):
        create_new_slip(self)

    def get(self, id=None):
        if id:
            get_single_slip(self, id)
        else:
            get_all_slips(self)

    def delete(self, id=None):
        if id:
            delete_slip(self, id)
    
    def patch(self, id=None):
        if id:
            update_slip(self, id)

    def put(self, id=None):
        if id:
            replace_slip(self, id)

class SlipHandlerCurrentBoat(webapp2.RequestHandler):
    def get(self, id=None):
        if id:
            get_current_boat(self, id)

class DockingHandler(webapp2.RequestHandler):
    def put(self, id=None):
        if id:
            dock_boat(self, id)

    def patch(self, id=None):
        if id:
            undock_boat(self, id, False)

# allow PATCH
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods

# App
app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/boat', BoatHandler),
    ('/boat/', BoatHandler),
    ('/boat/([\w-]+)', BoatHandler),
    ('/boat/([\w-]+)/', BoatHandler),
    ('/slip', SlipHandler),
    ('/slip/', SlipHandler),
    ('/slip/([\w-]+)', SlipHandler),
    ('/slip/([\w-]+)/', SlipHandler),
    ('/slip/([\w-]+)/current', SlipHandlerCurrentBoat),
    ('/slip/([\w-]+)/current/', SlipHandlerCurrentBoat),
    ('/boat/([\w-]+)/at_sea', DockingHandler),
    ('/boat/([\w-]+)/at_sea/', DockingHandler)
], debug=True)
