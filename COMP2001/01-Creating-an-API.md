# Creating an API with Flask, Connexion and Swagger

Starting with a basic Flask application, you'll learn to develop API endpoints. You'll also implement Swagger UI documentation to help you test and visualize your API's functionality.

- [people-dictionary](#people-dictionary)
- [project-setup](#project-setup)
    - [create-a-virtual-environment](#create-a-virtual-environment)
    - [initiate-the-flask-project](#initiate-the-flask-project)
- [adding-a-rest-api-endpoint](#adding-a-rest-api-endpoint)
    - [create-the-api-configuration-file](#create-the-api-configuration-file)
    - [add-connexion-to-the-app](#add-connexion-to-the-app)
    - [return-data-from-your-people-endpoint](#return-data-from-your-people-endpoint)
    - [explore-your-api-documentation](#explore-your-api-documentation)
- [building-the-complete-api](#building-the-complete-api)
    - [work-with-components](#work-with-components)
    - [create-a-new-person](#create-a-new-person)
    - [handle-a-person](#handle-a-person)
    - [explore-complete-api-documentation](#explore-complete-api-documentation)
    
## People Dictionary

Your REST API will manage a simple database of people where each person is keyed by their last name. 

The system will automatically record the time whenever any information is updated.

```py
PEOPLE = {
    "Hopper": {
        "fname": "Grace",
        "lname": "Hopper",
        "timestamp": "2024-11-12 16:15:10",
    },
    "BernersLee": {
        "fname": "Tim",
        "lname": "Berners-Lee",
        "timestamp": "2024-11-12 16:15:13",
    },
    "Lovelace": {
        "fname": "Ada",
        "lname": "Lovelace",
        "timestamp": "2024-11-12 16:15:27",
    }
}
```

## Project Setup

### Create a Virtual Environment

It's good practice to create and activate a virtual environment. That way, you’re installing any project dependencies not system-wide but only in your project’s virtual environment.

```bash
python -m venv <name>
cd <name>
.\Scripts\activate.bat
```
```bash
python -m venv 2024-11-12
cd 2024-11-12
.\Scripts\activate.bat
```

With your virtual environment active, now install Flask using pip. Flask will serve as your main web framework. You'll also need Connexion to manage HTTP requests in your API.

```bash
pip install Flask==2.2.2
pip install "connexion[swagger-ui]==2.14.1"
```

### Initiate the Flask Project

Start by making a project folder - you can name it `comp2001_flask_tutorial` or choose another name.

```bash
mkdir comp2001_flask_tutorial
cd comp2001_flask_tutorial
```

Create a new file called app.py in your project folder. This will be the main file of your Flask application. 

```bash
code app.py
```

```py
# app.py

from flask import Flask, render_template

app = Flask(__name__)

@app.route("/")
def home():
    return render_template("home.html")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
```

The code first imports Flask to access its features. Then it creates an instance of Flask called `app`. The decorator `@app.route("/")` connects the root URL (`"/"`) to the `home()` function, which uses Flask's `render_template()` to display `home.html` when someone visits your website.

Make a new directory called templates and inside it create a file named `home.html`. This is where Flask will look for your HTML template.

```bash
mkdir templates
cd templates
code home.html
```

```html
<!-- templates\home.html -->

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>RP Flask REST API</title>
    </head>
    <body>
        <h1>Hello world!</h1>
    </body>
</html>
```

> Your `home.html` is currently a simple HTML file without using any of Flask's Jinja Templating features . 

With your virtual environment activated, run this command from the directory that has your `app.py` file.

```bash
cd ..
python app.py
```

After running `app.py`, your web server will start on port 8000. Test if it's working by opening your web browser and going to `http://localhost:8000/` - you should see the message "Hello, World!" displayed on the page.

## Adding a REST API Endpoint

The Connexion module lets you use OpenAPI specification with Swagger in your Python program. OpenAPI is a format for describing REST APIs that provides key features.

Swagger works with OpenAPI to create a user interface for testing your API. All this functionality comes from a configuration file that you'll connect to your Flask application.

### Create the API Configuration File

The Swagger configuration file is either a YAML or JSON file that contains your OpenAPI definitions.

Create a new file called `swagger.yml` and start by adding your API's metadata.

```bash
code swagger.yml
```

```yaml
openapi: 3.0.0
info:
  title: "RP Flask REST API"
  description:  "An API about people and notes"
  version: "1.0.0"
```

Then define your API's root path using the `servers` and `url` fields.

```yaml
openapi: 3.0.0
info:
  title: "RP Flask REST API"
  description:  "An API about people and notes"
  version: "1.0.0"

servers:
  - url: "/api"
```

When you set url to `"/api"` , all your API endpoints will be accessible starting with `http://localhost:8000/api`.

Next, you'll define your API endpoints inside the `paths` block.

```yaml
paths:
  /people:
    get:
      operationId: "people.read_all"
      tags:
      - "People"
      summary: "Read the list of people"
      responses:
        "200":
          description: "Successfully read people list"
```

The paths block configures your API endpoint URLs. When you specify `/people` as the relative URL and get as the HTTP method, you're creating an endpoint that responds to `GET` requests. Combined with the `"/api"` server URL you defined earlier, this creates the complete endpoint `http://localhost:8000/api/people`.

The `get` block sets up your `/api/people` endpoint configuration. It needs an operationId that points to the Python function handling the request - in this case, `"people.read_all"` tells Connexion to look for a `read_all()` function in your people module.

The `tags` help organize your endpoints in the UI, while the `summary` provides a description of what this endpoint does. Under `responses`, you define possible status codes the endpoint might return - here you're specifying what happens with a successful `"200"` response.

### Add Connexion to the App

Your `swagger.yml` file acts like a blueprint, defining how your API handles requests and responses. It specifies what data your web server should expect and how it should respond. 

However, you still need to connect this blueprint to your Flask application - right now, your Flask project doesn't know that `swagger.yml` exists.

To set up your REST API endpoint in Flask using Connexion:
- Create an API configuration file (which you've done with `swagger.yml`)
- Link this configuration to your Flask application

```py
# Modify app.py

from flask import render_template
import connexion

app = connexion.App(__name__, specification_dir="./")
app.add_api("swagger.yml")

@app.route("/")
def home():
    return render_template("home.html")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
```

When you `import connexion`, you're adding its features to your program. Instead of creating a Flask app directly, you'll now create a Connexionapp that wraps around Flask and adds more functionality

### Return Data From Your People Endpoint

Your `swagger.yml` has `operationId: "people.read_all"`, which tells Connexion to look for a `read_all()` function in a people module. When someone makes a `GET` request to `/api/people`, this function will handle it.

Create the `people.py` file and implement the `read_all()` function.

```py
# people.py

from datetime import datetime

def get_timestamp():
    return datetime.now().strftime(("%Y-%m-%d %H:%M:%S"))

PEOPLE = {
    "Hopper": {
        "fname": "Grace",
        "lname": "Hopper",
        "timestamp": get_timestamp(),
    },
    "BernersLee": {
        "fname": "Tim",
        "lname": "Berners-Lee",
        "timestamp": get_timestamp(),
    },
    "Lovelace": {
        "fname": "Ada",
        "lname": "Lovelace",
        "timestamp": get_timestamp(),
    }
}


def read_all():
    return list(PEOPLE.values())
```

First, you create a `get_timestamp()` function that provides the current time as a string. You'll use this to track when records are modified.

Finally, you define `read_all()` which responds to `GET` requests to `/api/people`. When someone visits this endpoint, the function returns a list containing all people in your dictionary, with each person's information stored as a dictionary.

The list of people from `read_all()` will automatically be converted to JSON when sent back to the client.

### Explore Your API Documentation

Your REST API is now running with one endpoint, and Flask uses your `swagger.yml` specifications to handle requests. As a bonus, Connexion automatically creates API documentation from your swagger file.

To view this interactive documentation, open your browser and visit `http://localhost:8000/api/ui`.

It displays all available endpoints at `http://localhost:8000/api` - currently showing your single endpoint for managing people data.

The Swagger UI lets you test your API directly in the browser. Just click the Try it out button to experiment with your endpoint. This testing feature becomes especially useful as you add more endpoints to your API - you can test different API calls without writing any code.

## Building the Complete API

So far, you've created one endpoint for your Flask REST API. Now you'll expand it to support complete CRUD operations for managing your people data.

You'll need to modify two files to implement full CRUD functionality: `swagger.yml` to define the new endpoints, and `people.py` to add the corresponding Python functions that handle these operations.

### Work with Components

Before adding new API endpoints in `swagger.yml`, you'll create a components section. Components act as reusable building blocks in your OpenAPI specification that can be referenced throughout your configuration.

Add a `components` block that defines the structure of a person record using `schemas`.

```yaml
openapi: 3.0.0
info:
  title: "RP Flask REST API"
  description:  "An API about people and notes"
  version: "1.0.0"

servers:
  - url: "/api"

components:
  schemas:
    Person:
      type: "object"
      required:
        - lname
      properties:
        fname:
          type: "string"
        lname:
          type: "string"

# ...
```

The `components` block helps organize reusable elements of your API.

In the `schemas` section, we'll define the basic structure of a `Person`.

When you see a dash (`-`) before `lname`, it means required is a list that can have multiple items. Remember, any field listed as `required` needs to be defined in `properties`.

Each of these properties is defined as a string type, meaning they'll hold text values.

### Create a New Person

In the `/people` section, let's add a new `post` block to handle requests for creating new people.

```yaml
paths:
  /people:
    get:
      # ...

    post:
      operationId: "people.create"
      tags:
        - People
      summary: "Create a person"
      requestBody:
        description: "Person to create"
        required: true
        content:
          application/json:
            schema:
              x-body-name: "person"
              $ref: "#/components/schemas/Person"
      responses:
        "201":
          description: "Successfully created person"
```

Add a `create()` function to your `people.py` file. Connexion will call this function when it receives a `POST` request since we specified `people.create` as the `operationId` in our swagger file:

```py
from datetime import datetime
from flask import abort, make_response # Added 

# ...

def create(person):
    lname = person.get("lname")
    fname = person.get("fname", "")

    if lname and lname not in PEOPLE:
        PEOPLE[lname] = {
            "lname": lname,
            "fname": fname,
            "timestamp": get_timestamp(),
        }
        return PEOPLE[lname], 201
    else:
        abort(
            406,
            f"Person with last name {lname} already exists",
        )
```

The code first imports Flask's `abort()` function. 

### Handle a Person

Expand your API to handle operations on individual people. You'll modify both `swagger.yml` to define the new path, and `people.py` to add the functionality for working with a single person record.

Open your `swagger.yml` file and add this new path definition.

```yaml
paths:
  /people:
    # ...

  /people/{lname}:
    get:
      tags:
        - People
      operationId: "people.read_one"
      summary: "Read one person"
      parameters:
        - $ref: "#/components/parameters/lname"
      responses:
        "200":
          description: "Successfully read person"
      requestBody:
        content:
          application/json:
            schema:
              x-body-name: "person"
              $ref: "#/components/schemas/Person"
```

Like the `/people` endpoint, you start by defining a get operation, but this time for `/people/{lname}`. The `{lname}` in the path is a variable - when someone makes a request, they'll replace it with an actual last name. 

Since you'll need the `lname` parameter in multiple operations, you should define it once as a reusable component and reference it when needed.

The `operationId` here points to `read_one()` in `people.py`, so let's create that function.

```py
def read_one(lname):
    if lname in PEOPLE:
        return PEOPLE[lname]
    else:
        abort(
            404, f"Person with last name {lname} not found"
        )
```

If your server finds the requested last name in the `PEOPLE` dictionary, it returns that person's information. If not, it responds with a `404` error indicating the person wasn't found.

Add the ability to update existing records by adding this code to `swagger.yml`:

```yaml
paths:
  /people:
    get:
      # ...
    post:
      # ...

  /people/{lname}:
    get:
      # ...

    put:
      tags:
        - People
      operationId: "people.update"
      summary: "Update a person"
      parameters:
        - $ref: "#/components/parameters/lname"
      responses:
        "200":
          description: "Successfully updated person"
      requestBody:
        content:
          application/json:
            schema:
              x-body-name: "person"
              $ref: "#/components/schemas/Person"
```

Based on this `put` operation specification, your server will look for an `update()` function in `people.py`. 

```py
def update(lname, person):
    if lname in PEOPLE:
        PEOPLE[lname]["fname"] = person.get("fname", PEOPLE[lname]["fname"])
        PEOPLE[lname]["timestamp"] = get_timestamp()
        return PEOPLE[lname]
    else:
        abort(
            404,
            f"Person with last name {lname} not found"
        )
```

Add the ability to remove people from your dataset by adding a `delete` operation.

```yaml
paths:
  /people:
    get:
      # ...
    post:
      # ...

  /people/{lname}:
    get:
      # ...
    put:
      # ...

    delete:
      tags:
        - People
      operationId: "people.delete"
      summary: "Delete a person"
      parameters:
        - $ref: "#/components/parameters/lname"
      responses:
        "204":
          description: "Successfully deleted person"
```

Add the `delete()` function to your `people.py` file to handle removing people from your data.

```py
def delete(lname):
    if lname in PEOPLE:
        del PEOPLE[lname]
        return make_response(
            f"{lname} successfully deleted", 200
        )
    else:
        abort(
            404,
            f"Person with last name {lname} not found"
        )
```

### Explore Complete API Documentation

After adding all these endpoints to `swagger.yml` and their corresponding functions to `people.py`, your Swagger UI at `localhost:8000/api/ui` will update to show all available operations. 

You'll see the complete interface displaying all your API endpoints for managing people: creating, reading, updating, and deleting records.
