# Connecting to SQL with pyodbc and SQLAlchemy

- [setup-sql-server-connection-from-python-on-personal-machine](#setup-sql-server-connection-from-python-on-personal-machine)
    - [install-necessary-libraries-and-drivers](#install-necessary-libraries-and-drivers)
        - [install-pyodbc](#install-pyodbc)
        - [install-microsoft-odbc-driver-for-sql-server](#install-microsoft-odbc-driver-for-sql-server)
    - [add-the-pyodbc-library-to-your-python-code](#add-the-pyodbc-library-to-your-python-code)
    - [set-up-the-connection-string](#set-up-the-connection-string)
    - [establish-a-connection](#establish-a-connection)
    - [create-a-cursor-and-execute-sql-commands](#create-a-cursor-and-execute-sql-commands)
    - [fetch-data-from-the-database](#fetch-data-from-the-database)
    - [close-the-connection](#close-the-connection)
- [preparation](#preparation)
- [initialising-the-database](#initialising-the-database)
    - [inspect-the-current-data-structure](#inspect-the-current-data-structure)
    - [build-the-database](#build-the-database)
    - [interact-with-the-database](#interact-with-the-database)
- [adding-sql-database-support-to-your-flask-application](#adding-sql-database-support-to-your-flask-application)
    - [configure-the-database](#configure-the-database)
    - [model-data-with-sqlalchemy](#model-data-with-sqlalchemy)
    - [serialize-the-modeled-data-with-marshmallow](#serialize-the-modeled-data-with-marshmallow)
    - [clean-up](#clean-up)
- [connecting-the-database-to-the-api](#connecting-the-database-to-the-api)
    - [read-from-the-database](#read-from-the-database)
    - [write-to-the-database](#write-to-the-database)
- [display-data-on-the-front-end](#display-data-on-the-front-end)
- [explore-your-api-documentation](#explore-your-api-documentation)

## Setup SQL Server Connection from Python on Personal Machine

This guide walks you through setting up SQL Server connectivity in your Python environment.

By following these steps, you'll be able to connect any Python application to your SQL Server database

### Install Necessary Libraries and Drivers

Before you can connect to SQL Server, you need to install specific libraries and drivers. These components enable Python to communicate with SQL Server databases

#### Install `pyodbc`

You'll need the `pyodbc` library to connect to SQL Server databases. 

After activating your virtual environment for API development, execute:

```bash
pip install pyodbc
```

#### Install Microsoft ODBC Driver for SQL Server

[Download ODBC Driver for SQL Server](https://learn.microsoft.com/en-us/sql/connect/odbc/download-odbc-driver-for-sql-server?view=sql-server-ver16)

Run `msodbcsql.msi`.

### Add the `pyodbc` library to your Python code

```py
import pyodbc
```

### Set Up the Connection String

Gather the following information from your Azure SQL Database

```py
server = 'dist-6-505.uopnet.plymouth.ac.uk'
database = 'your_database_name'
username = 'your_username'
password = 'your_password'
driver = '{ODBC Driver 17 for SQL Server}'
```

### Establish a Connection

```py
conn_str = (
    f'DRIVER={driver};'
    f'SERVER={server};'
    f'DATABASE={database};'
    f'UID={username};'
    f'PWD={password};'
    'Encrypt=Yes;'
    'TrustServerCertificate=Yes;'
    'Connection Timeout=30;'
    'Trusted_Connection=No'
)

conn = pyodbc.connect(conn_str)
```

### Create a Cursor and Execute SQL Commands

```py
cursor = conn.cursor()

# Create a table
cursor.execute('''
CREATE TABLE users (
    id INT PRIMARY KEY,
    name NVARCHAR(50),
    age INT
)
''')

# Insert data
cursor.execute('INSERT INTO users (id, name, age) VALUES (?, ?, ?)', (1, 'Alice', 30))
cursor.execute('INSERT INTO users (id, name, age) VALUES (?, ?, ?)', (2, 'Bob', 25))

# Commit the transaction
conn.commit()
```

### Fetch Data from the Database

```py
cursor.execute('SELECT * FROM users')
rows = cursor.fetchall()

for row in rows:
    print(row)
```

### Close the Connection

```py
cursor.execute('DROP TABLE users')
conn.commit()

conn.close()
```

## Preparation

Create a file named `requirements.txt`

```text
attrs==23.1.0
blinker==1.6.3
certifi==2023.7.22
charset-normalizer==3.3.0
click==8.1.7
clickclick==20.10.2
connexion==2.14.1
Flask==2.2.2
flask-marshmallow==0.14.0
Flask-SQLAlchemy==3.0.3
greenlet==3.0.0
idna==3.4
inflection==0.5.1
itsdangerous==2.1.2
Jinja2==3.1.2
jsonschema==4.19.1
jsonschema-specifications==2023.7.1
MarkupSafe==2.1.3
marshmallow==3.20.1
marshmallow-sqlalchemy==0.29.0
packaging==23.2
PyYAML==6.0.1
referencing==0.30.2
requests==2.31.0
rpds-py==0.10.3
six==1.16.0
SQLAlchemy==2.0.22
swagger-ui-bundle==0.0.9
typing_extensions==4.8.0
urllib3==2.0.6
Werkzeug==2.2.2
pytz
```

The file contains all the dependencies you need. The two most important are `flask-marshmallow` and `Flask-SQLAlchemy`.

When you install `flask-marshmallow`, you get the `marshmallow` library too. This helps your REST API handle data conversion: it can turn Python objects into JSON format when sending data out, and convert JSON back into Python objects when receiving data.

Installing the `sqlalchemy` option gives you additional packages that connect your Flask application with `SQLAlchemy`'s powerful database management features

SQLAlchemy works as an ORM (Object-Relational Mapper), meaning it can store Python objects directly into database format. This lets you write normal Python code without worrying about the underlying database structure or SQL commands.

Within your virtual environment, execute:

```bash
pip install -r requirments.txt
```

```
comp2001_flask_tutorial/
│
├── templates/
│   └── home.html
│
├── app.py
├── people.py
└── swagger.yml
```

Run this command from the directory that contains your `app.py` file:

```bash
python app.py
```

## Initialising the Database

### Inspect the Current Data Structure

Right now, the Flask application stores data in a dictionary, which is only temporary storage. This means whenever you restart your application, all of the changes and new data are lost. We need a more permanent solution to store the data.

Add a database to your Flask project to solve the data persistence problem.

Remember, your data is currently stored in the PEOPLE dictionary inside `people.py`.

```py
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
```

The changes you'll make will move all your data from the `PEOPLE` dictionary to a database table.

- Saved permanently on your disk
- Available even after restarting your application
- Accessible between different runs of `app.py`

### Build the Database

You'll use the package `pyodbc` to access your SQL server to store the `PEOPLE` data

For now, instead of editing your existing Python script, let's start a new interactive Python session in a command line tool like PowerShell or Terminal to test your connection and create the people.db database. 

First, open your command line tool, activate your virtual environment, and type `python` to start an interactive session. 

```bash
python
```

```py
>>> import pyodbc

>>> server = 'dist-6-505.uopnet.plymouth.ac.uk'
>>> database = 'your_database_name'
>>> username = 'your_username'
>>> password = 'your_password'
>>> driver = '{ODBC Driver 17 for SQL Server}'

>>> conn_str = (
... f'DRIVER={driver};'
... f'SERVER={server};'
... f'DATABASE={database};'
... f'UID={username};'
... f'PWD={password};'
... 'Encrypt=Yes;'
... 'TrustServerCertificate=Yes;'
... 'Connection Timeout=30;'
... 'Trusted_Connection=No'
)
>>> conn = pyodbc.connect(conn_str)
>>> cursor = conn.cursor()

>>> columns = [
... 'id INT IDENTITY(1,1) PRIMARY KEY',
... 'lname VARCHAR(25) UNIQUE',
... 'fname VARCHAR(25)',
... 'timestamp DATETIME',
... ]
>>> create_table_cmd = f"CREATE TABLE person ({','.join(columns)})"
>>> cursor.execute(create_table_cmd)
>>> cursor.commit()
```

This code sets up a SQL Server database connection using pyodbc and creates a new table named 'person'. First, in lines 1-7, it imports the `pyodbc` module and sets up the connection parameters including the server address (`dist-6-505.uopnet.plymouth.ac.uk`), database name, username, password, and SQL Server driver version.

If you refresh your database in Azure Data Studio, you should see the person table under Tables

Now that your database exists, you can add data to it.

```py
>>> people = [
... "'Grace', 'Hopper', '2024-11-19 16:15:10'",
... "'Tim', 'Berners-Lee', '2024-11-19 16:15:13'",
... "'Ada', 'Lovelace', '2024-11-19 16:15:27'",
... ]

>>> for person_data in people:
...     insert_cmd = f"INSERT INTO person VALUES ({person_data})"
```

### Interact With the Database

```py
>>> cursor.execute("SELECT * FROM person")

>>> people = cursor.fetchall()
>>> for person in people:
...     print(person)
```

```
(1, 'Grace', 'Hopper', datetime.datetime(2024, 11, 19, 16, 15, 10))
(2, 'Tim', 'Berners-Lee', datetime.datetime(2024, 11, 19, 16, 15, 13))
(3, 'Ada', 'Lovelace', datetime.datetime(2024, 11, 19, 16, 15, 27))
```

In our current code, we're directly writing SQL statements as strings that get executed by the database. While this is relatively safe for our learning purpose since we control these fixed SQL statements, it becomes risky when building a REST API. 

This is because REST APIs typically accept user input from web applications to construct SQL queries, and if not properly secured, malicious users could inject harmful SQL code into these queries, potentially compromising your database security.

Converting database records into Python objects offers better security and usability. When you work with Python objects, each field becomes an object attribute with proper type checking, rather than raw SQL strings.

This approach helps prevent malicious SQL commands from being executed and ensures your data matches expected types and formats.

## Adding SQL Database Support to Your Flask Application

In this section, you'll use `SQLAlchemy` to manage communication between your `Flask` app and the database.

`SQLAlchemy` offers two major benefits:
- Handles different database types automatically, letting you focus on your data models
- Protects against SQL injection by sanitizing user input before creating database queries

### Configure the Database

Create two new Python files:
1. `config.py` : Sets up and configures your required modules ( `Flask` , `Connexion` , `SQLAlchemy` , and `Marshmallow` )
2. `models.py `: Contains your `SQLAlchemy` and `Marshmallow` class definitions for data handling

```py
# config.py

import pathlib
import connexion
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow

basedir = pathlib.Path(__file__).parent.resolve()
connex_app = connexion.App(__name__, specification_dir=basedir)

app = connex_app.app
app.config["SQLALCHEMY_DATABASE_URI"] = (
    "mssql+pyodbc:///?odbc_connect="
    "DRIVER={ODBC Driver 17 for SQL Server};"
    "SERVER=dist-6-505.uopnet.plymouth.ac.uk;"
    "DATABASE=your_database_name;"
    "UID=your_username;"
    "PWD=your_password;"
    "TrustServerCertificate=yes;"
    "Encrypt=yes;"
)
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

db = SQLAlchemy(app)
ma = Marshmallow(app)
```

For application setup i, the code creates `basedir` to store the current directory path, then uses this to create a Connexion app instance with the correct path to your specification files.

The Flask and database configuration happens next. Here, the code gets the Flask app instance from Connexion, configures the SQL database path, and turns off the SQLAlchemy event system to improve performance since we're not using event-driven features.

Finally, the code initialises the extensions. It creates the SQLAlchemy database instance by passing the app configuration, and then sets up Marshmallow to work with your SQLAlchemy components.

### Model Data With SQLAlchemy

SQLAlchemy's key feature is its *object-relational mapper* (ORM), which converts database rows into Python objects. 

This lets you work with your database in a more natural Python way - instead of writing SQL queries, you'll interact with Python objects that represent your data.

Create a `models.py` file to define how the person table should map to Python objects.

```py
from datetime import datetime
import pytz

from config import db

class Person(db.Model):
    __tablename__ = "person"
    id = db.Column(db.Integer, primary_key=True)
    lname = db.Column(db.String(32), unique=True)
    fname = db.Column(db.String(32))
    timestamp = db.Column(
        db.DateTime, default=lambda: datetime.now(pytz.timezone('Europe/London')),
        onupdate=lambda: datetime.now(pytz.timezone('Europe/London'))
    )
```

Working with SQLAlchemy lets you handle your data as Python objects with methods and behaviors, rather than writing raw SQL queries. 

This *object-oriented approach* becomes particularly valuable as your database grows and you need to manage more complex relationships and interactions between tables.

### Serialize the Modeled Data With Marshmallow

- JSON Conversion Challenge: Your REST API needs JSON format, but `SQLAlchemy` works with Python objects. We need to convert between these formats.
- Data Validation Challenge: You must verify all data before storing it in the database to ensure it's valid and safe.

Marshmallow handles both problems by converting data formats and providing validation tools, making it a perfect bridge between your REST API and database.

Marshmallow lets you create a `PersonSchema` class that works alongside your SQLAlchemy `Person` class. This schema class defines how to convert your Python object attributes into JSON format that your REST API can use. It also  verifies that all required attributes are present and contain the correct type of data.

```py
# models.py

# ...

from config import db, ma

# ...

class PersonSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Person
        load_instance = True
        sql_session = db.session

person_schema = PersonSchema()
people_schema = PersonSchema(many=True)
```

### Clean Up


Update `people.py` with new imports for database handling and remove outdated code.

```py
# people.py

# Remove: from datetime import datetime
from flask import make_response, abort

from config import db
from models import Person, people_schema, person_schema

# Remove: get_timestamp():
# Remove: PEOPLE

# ...
```

```py
# people.py

from flask import abort, make_response

def read_all():
    # ...

def create(person):
    # ...

def read_one(lname):
    # ...

def update(lname, person):
    # ...

def delete(lname):
    # ...
```

## Connecting the Database to the API

Your Flask project is now connected to the database, but your REST API isn't using it yet. While you could add data to your database using the Python interactive shell, it would be more practical to update your REST API endpoints to work with the database. This way, you can use your existing API endpoints to manage the data.

### Read From the Database

```py
def read_all():
    people = Person.query.all()
    return people_schema.dump(people)
```

```py
def read_one(lname):
    person = Person.query.filter(Person.lname == lname).one_or_none()

    if person is not None:
        return person_schema.dump(person)
    else:
        abort(404, f"Person with last name {lname} not found")
```

### Write to the Database

```py
def create(person):
    lname = person.get("lname")
    existing_person = Person.query.filter(Person.lname == lname).one_or_none()

    if existing_person is None:
        new_person = person_schema.load(person, session=db.session)
        db.session.add(new_person)
        db.session.commit()
        return person_schema.dump(new_person), 201
    else:
        abort(406, f"Person with last name {lname} already exists")
```

```py
def update(lname, person):
    existing_person = Person.query.filter(Person.lname == lname).one_or_none()

    if existing_person:
        update_person = person_schema.load(person, session=db.session)
        existing_person.fname = update_person.fname
        db.session.merge(existing_person)
        db.session.commit()
        return person_schema.dump(existing_person), 201
    else:
        abort(404, f"Person with last name {lname} not found")
```

```py
def delete(lname):
    existing_person = Person.query.filter(Person.lname == lname).one_or_none()

    if existing_person:
        db.session.delete(existing_person)
        db.session.commit()
        return make_response(f"{lname} successfully deleted", 200)
    else:
        abort(404, f"Person with last name {lname} not found")
```

## Display Data on the Front End

Update the `app.py` file to work with the new database configuration. 

The changes will connect the Flask application with your database setup and prepare it to display data in the frontend.

Instead of importing connexion directly, we'll now import from our new configuration files. 

Update `app.py` to use:
- `config` for your application setup
- `Person` model from models for database operations

```py
# app.py

from flask import render_template
# Remove: import connexion
import config
from models import Person

app = config.connex_app
app.add_api(config.basedir / "swagger.yml")

@app.route("/")
def home():
    people = Person.query.all()
    return render_template("home.html", people=people)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
```

> We now use the Flask app that's already configured in `config.py` (using `config.connex_app`) instead of creating a new one.
>
> We use the `Person` model to fetch all records from the `person` table, and then pass this data to our template using `render_template()`.

Update `home.html` to display this database data.

```html
<!-- templates/home.html -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>RP Flask REST API</title>
</head>
<body>
    <h1>
        Hello, People!
    </h1>
    <ul>
        {% for person in people %}
            <li>{{ person.fname }} {{ person.lname }}</li>
        {% endfor %}
    </ul>
</body>
</html>
```

```bash
python app.py
```

Open your browser and go to `http://localhost:8000/` to see your database data displayed.

## Explore Your API Documentation

Your application now has proper data persistence. Thanks to the database integration, all your data remains safely stored even when you stop and restart your application.

To view the interactive documentation, open your browser and visit `http://localhost:8000/api/ui`.

Your REST API now provides full database functionality - you can add new people, update existing records, and remove entries as needed. 

The frontend displays everyone currently in your database.
