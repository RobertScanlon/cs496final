# filename:     main.py
# author:       Robert Scanlon
# description:  backend code for CS496 final project
# last edit:    16 November 2017

import webapp2
from google.appengine.ext import ndb
import json

RUN_LOCAL = True

BASE_URL = "http://localhost:8080"
if not RUN_LOCAL:
    BASE_URL = "https://cs496final-186222.appspot.com"

class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write('Hello, World!')

# allow PATCH
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods

app = webapp2.WSGIApplication([
    ('/', MainPage),
], debug=True)
