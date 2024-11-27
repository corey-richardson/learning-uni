# Handling Database Relationships with SQLAlchemy and Marshmallow

Over the past lab sessions, you've built a complete system: first creating a REST API, then connecting it to a database. Your Flask application can now perform persistent data operations - any changes you make or new data you create is saved in the database and remains available even after restarting your server.

Your database currently only stores people's information in a single table. Now we'll expand it by adding a new table for notes and creating relationships between people and their notes. This means each note in the note table will be connected to a specific person in the person table.

- [preparation](#preparation)
- [extending-your-database](#extending-your-database)
    - [create-sqlalchemy-models](#create-sqlalchemy-models)
    - [feed-the-database](#feed-the-database)
- [displaying-people-with-their-notes](#displaying-people-with-their-notes)
    - [show-notes-in-the-front-end](#show-notes-in-the-front-end)
    - [respond-with-notes](#respond-with-notes)
    - [create-a-notes-schema](#create-a-notes-schema)
- [handle-notes-with-your-rest-api](#handle-notes-with-your-rest-api)
    - [read-a-single-note](#read-a-single-note)
    - [update-and-delete-a-note](#update-and-delete-a-note)
    - [create-a-note-for-a-person](#create-a-note-for-a-person)
    
## Preparation

```
Flask_Tutorial/S3/
│
├── templates/
│   └── home.html
│
├── app.py
├── config.py
├── models.py
├── people.py
├── requirements.txt
└── swagger.yml
```

Start the `comp2001sqlserver` container via Docker Desktop.

```bash
python app.py
```

## Extending Your Database

Modify the `Person` class in `models.py` to include notes for each person. Each person will be
able to have multiple notes associated with them, creating a one-to-many relationship. After setting up this structure, you'll add some initial data to populate your database.

### Create SQLAlchemy Models

Modify `models.py` to establish the relationship between people and their notes. Update the Person model to include a connection to a collection of notes, creating a link between the two tables.

```py
class Person(db.Model):
    __tablename__ = "person"
    id = db.Column(db.Integer, primary_key=True)
    lname = db.Column(db.String(32), unique=True)
    fname = db.Column(db.String(32))
    timestamp = db.Column(
        db.DateTime, default=lambda: datetime.now(pytz.timezone('Europe/London')),
        onupdate=lambda: datetime.now(pytz.timezone('Europe/London'))
    )
    notes = db.relationship (
        Note, # specifies Note as the related class, although it's not created yet
        backref = "person",
        cascade = "all, delete, delete-orphan",
        single_parent = True,
        order_by = "desc(Note.timestamp)"
    )
```

Create the `Note` model class before our `Person` class in `models.py`. Since `Person` references `Note` in its relationship definition, we need to define `Note` first to avoid any reference issues

```py
class Note(db.Model):
    __tablename__ = "note" # connects this class to its corresponding database table
    
    id = db.Column(db.Integer, primary_key=True, autoincrement=True) 
    person_id = db.Column(db.Integer, db.ForeignKey("person.id")) # creates a .person_id foreign key that connects 
                                                                  # each note to its owner in the Person table
    content = db.Column(db.String, nullable=False) # adds the .content attribute that holds the note's text
                                                   # nullable=False; every note must have content
    timestamp = db.Column(
        db.DateTime, 
        default=lambda: datetime.now(pytz.timezone('Europe/London')),
        onupdate=lambda: datetime.now(pytz.timezone('Europe/London'))
    )
    
class Person(db.Model):
    __tablename__ = "person"
    id = db.Column(db.Integer, primary_key=True)
    lname = db.Column(db.String(32), unique=True)
    fname = db.Column(db.String(32))
    timestamp = db.Column(
        db.DateTime, default=lambda: datetime.now(pytz.timezone('Europe/London')),
        onupdate=lambda: datetime.now(pytz.timezone('Europe/London'))
    )
    notes = db.relationship (
        Note, # specifies Note as the related class, although it's not created yet
        backref = "person",
        cascade = "all, delete, delete-orphan",
        single_parent = True,
        order_by = "desc(Note.timestamp)"
    )
```

The `Note` class that inherits from `db.Model`, giving it all the SQLAlchemy database functionality, just like we did with `Person`.


### Feed the Database

Create a utility script called `build_database.py` that will help us rebuild our database with the new structure that includes both people and their notes.

```py
from datetime import datetime
from config import app, db
from models import Person, Note

PEOPLE_NOTES = [
    {
        "lname": "Hopper",
        "fname": "Grace",
        "notes": [
            ("Great spot to see deer early morning!", "2024-11-26 09:10:24"),
            ("Rocky path near the quarry needs good shoes.", "2024-11-26 11:17:54"),
        ],
    },
    {
        "lname": "Berners-Lee",
        "fname": "Tim",
        "notes": [
            ("Lovely tea room at the old station building.", "2024-11-26 12:15:03"),
            ("Watch out for cyclists on narrow sections.", "2024-11-26 15:09:21"),
        ],
    },
    {
        "lname": "Lovelace",
        "fname": "Ada",
        "notes": [
            ("Old copper mining ruins are fascinating.", "2024-11-26 10:47:54"),
            ("Fast flowing river after the rain yesterday!", "2024-11-26 11:03:17"),
        ],
    },
]

with app.app_context():
    db.drop_all()
    db.create_all()
    for data in PEOPLE_NOTES:
        new_person = Person(lname=data.get("lname"), fname=data.get("fname"))
        for content, timestamp in data.get("notes", []):
            new_person.notes.append(
                Note(
                    content=content,
                    timestamp=datetime.strptime(timestamp, "%Y-%m-%d %H:%M:%S"),
                )
            )
        db.session.add(new_person)
    db.session.commit()
```

> Running `build_database.py` will create a fresh `people.db` database. 
>
> This means if you have any existing data in your current `people.db`, it will be erased and replaced with the new sample data.

```bash
python build_database.py
```

## Displaying People With Their Notes

### Show Notes in the Front End

Modify `home.html` to display the notes associated with each person. Now that we've established the relationship through the `.notes` attribute in the `Person` class, we can access a person's notes from our template.

```html
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
    {% for person in people %}
    <h2>{{ person.fname }} {{ person.lname }}</h2>
    <ul>
        {% for note in person.notes %}
        <li>
            {{ note.content }}
        </li>
        {% endfor %}
    </ul>
    {% endfor %}
</body>
</html>
```

To see if it's working, open your browser and visit `http://localhost:8000/`. You should see all people and their associated notes displayed.

### Respond With Notes

Check the `/api/people` endpoint of your API at `http://localhost:8000/api/people`.

```json
[
  {
    "fname": "Grace",
    "id": 1,
    "lname": "Hopper",
    "timestamp": "2024-11-27T15:29:07.627000"
  },
  {
    "fname": "Tim",
    "id": 2,
    "lname": "Berners-Lee",
    "timestamp": "2024-11-27T15:29:07.627000"
  },
  {
    "fname": "Ada",
    "id": 3,
    "lname": "Lovelace",
    "timestamp": "2024-11-27T15:29:07.627000"
  }
]
```

The data you're getting shows the people correctly, but their notes are missing.

```py
def read_all():
    people = Person.query.all()
    return people_schema.dump(people)
```

Since we're using the same database query as in `app.py`, the query should be retrieving the data correctly.

This suggests that Marshmallow might not be properly handling the serialization of the relationship between people and their notes.

Since the same query successfully shows notes in the frontend, the problem must be with our `PersonSchema`. Marshmallow schemas don't automatically include related objects - we need to explicitly tell the schema to include relationships.

Update the `PersonSchema` in `models.py` to properly handle the notes relationship.

```py
class PersonSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Person
        load_instance = True
        sql_session = db.session
        include_relationships = True # Added
```

Adding `include_relationships` to the Meta class in `PersonSchema` tells Marshmallow to include related objects, but checking the results shows it's still not displaying quite right.

```json
[
  {
    "fname": "Grace",
    "id": 1,
    "lname": "Hopper",
    "notes": [
      2,
      1
    ],
    "timestamp": "2024-11-27T15:29:07.627000"
  },
  {
    "fname": "Tim",
    "id": 2,
    "lname": "Berners-Lee",
    "notes": [
      4,
      3
    ],
    "timestamp": "2024-11-27T15:29:07.627000"
  },
  {
    "fname": "Ada",
    "id": 3,
    "lname": "Lovelace",
    "notes": [
      6,
      5
    ],
    "timestamp": "2024-11-27T15:29:07.627000"
  }
]
```

Instead of showing the full note content, the notes field only contains a list of note IDs (primary keys).

### Create a Notes Schema

Add this schema between your `Note` and `Person` classes.

```py
class Note(db.Model):
    # ...

class NoteSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Note
        load_instance = True
        sqla_session = db.session
        include_fk = True

class Person(db.Model):
    # ...

note_schema = NoteSchema()
```

Now that we have `NoteSchema` set up, we can use it in `PersonSchema` to properly handle the note data.

```py
from marshmallow_sqlalchemy import fields # Added

# ...

class PersonSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Person
        load_instance = True
        sqla_session = db.session
        include_relationships = True

    notes = fields.Nested(NoteSchema, many=True) # Added
```

After importing `fields` from `marshmallow_sqlalchemy` , you can connect notes to the person schema using `NoteSchema`.

Remember, `NoteSchema` must be defined before `PersonSchema` to work correctly.

Even though we're using `SQLAlchemyAutoSchema`, we need to explicitly define the notes field in `PersonSchema`. This tells Marshmallow to expect a list of note objects and how to handle them.

```json
[
  {
    "fname": "Grace",
    "id": 1,
    "lname": "Hopper",
    "notes": [
      {
        "content": "Rocky path near the quarry needs good shoes.",
        "id": 2,
        "person_id": 1,
        "timestamp": "2024-11-26T11:17:54"
      },
      {
        "content": "Great spot to see deer early morning!",
        "id": 1,
        "person_id": 1,
        "timestamp": "2024-11-26T09:10:24"
      }
    ],
    "timestamp": "2024-11-27T15:29:07.627000"
  },
  {
    "fname": "Tim",
    "id": 2,
    "lname": "Berners-Lee",
    "notes": [
      {
        "content": "Watch out for cyclists on narrow sections.",
        "id": 4,
        "person_id": 2,
        "timestamp": "2024-11-26T15:09:21"
      },
      {
        "content": "Lovely tea room at the old station building.",
        "id": 3,
        "person_id": 2,
        "timestamp": "2024-11-26T12:15:03"
      }
    ],
    "timestamp": "2024-11-27T15:29:07.627000"
  },
  {
    "fname": "Ada",
    "id": 3,
    "lname": "Lovelace",
    "notes": [
      {
        "content": "Fast flowing river after the rain yesterday!",
        "id": 6,
        "person_id": 3,
        "timestamp": "2024-11-26T11:03:17"
      },
      {
        "content": "Old copper mining ruins are fascinating.",
        "id": 5,
        "person_id": 3,
        "timestamp": "2024-11-26T10:47:54"
      }
    ],
    "timestamp": "2024-11-27T15:29:07.627000"
  }
]
```

## Handle Notes With Your REST API

`/api/notes`, `/api/notes/<note_id>`, `api/notes/<note_id>`, `api/notes/<note_id>`

### Read a Single Note

Before adding the new endpoint for individual notes, first add a `note_id` parameter component to your `swagger.yml` file. This will help define how to identify specific notes in your API calls.

```yml
components:
  schemas:
    Person:
      # ...

  parameters:
    lname:
      # ...
    note_id:
      name: "note_id"
      description: "ID of the note"
      in: path
      required: true
      schema:
        type: "integer"
```

Add the endpoint for reading a single note in your `swagger.yml`. The `note_id` parameter we just defined will be used to identify which specific note to retrieve.

```yml
paths:
  /people:
    # ...

  /people/{lname}:
    # ...

  /notes/{note_id}:
    get:
      operationId: "notes.read_one"
      tags:
        - Notes
      summary: "Read one note"
      parameters:
        - $ref: "#/components/parameters/note_id"
      responses:
        "200":
          description: "Successfully read one note"
```

Since we specified operationId: `"notes.read_one"`, we need to create a new file `notes.py` with a `read_one()` function to handle these requests.

`http://localhost:8000/api/notes/1`

```json
{
  "content": "Great spot to see deer early morning!",
  "id": 1,
  "person_id": 1,
  "timestamp": "2024-11-26T09:10:24"
}
```

### Update and Delete a Note

Add the `update()` and `delete()` functions to `notes.py` first, and then we'll add their corresponding operations to the Swagger configuration.

```py
from flask import abort, make_response # Added make_response

# ...

def update(note_id, note):
    existing_note = Note.query.get(note_id)

    if existing_note:
        update_note = note_schema.load(note, session=db.session)
        existing_note.content = update_note.content
        db.session.merge(existing_note)
        db.session.commit()
        return note_schema.dump(existing_note), 201
    else:
        abort(404, f"Note with ID {note_id} not found")

def delete(note_id):
    existing_note = Note.query.get(note_id)

    if existing_note:
        db.session.delete(existing_note)
        db.session.commit()
        return make_response(f"{note_id} successfully deleted", 204)
    else:
        abort(404, f"Note with ID {note_id} not found")
```

The `update()` and `delete()` functions share a similar pattern - they both find a note by ID and work with the database session. The main difference is that update() needs new content from a note object to modify the existing note, while `delete()` only needs the note's ID to remove it.

Add the corresponding operations to `swagger.yml` to connect these functions to your API.


```yml
  /notes/{note_id}:

    get:
      # ...

    put:
      tags:
        - Notes
      operationId: "notes.update"
      summary: "Update a note"
      parameters:
        - $ref: "#/components/parameters/note_id"
      requestBody:
        content:
          application/json:
            schema:
              x-body-name: "note"
              type: "object"
              properties:
                content:
                  type: "string"
      responses:
        "200":
          description: "Successfully updated note"

    delete:
      tags:
        - Notes
      operationId: "notes.delete"
      summary: "Delete a note"
      parameters:
        - $ref: "#/components/parameters/note_id"
      responses:
        "204":
          description: "Successfully deleted note"
```

### Create a Note for a Person

Add the ability to create new notes by implementing the `create()` function in `notes.py`. This will complete our CRUD operations for notes.

```py
from models import Note, Person, note_schema # Added Person

# ...

def create(note):
    person_id = note.get("person_id")
    content = note.get("content")

    if not content:
        abort(400, "Note content is required")

    person = Person.query.get(person_id)

    if person:
        new_note = note_schema.load(note, session=db.session)
        person.notes.append(new_note)
        db.session.commit()
        return note_schema.dump(new_note), 201
    else:
        abort(
            404,
            f"Person not found for ID: {person_id}"
        )
```

The `create()` function enforces the relationship between notes and people - every note must belong to a person. It first checks if the specified `person_id` exists in the database. If the person is found, it creates the new note and adds it to that person's notes collection.

Even though we're working with the person table when we append the note, SQLAlchemy handles storing the note in the correct note table automatically, maintaining the relationship.

Now let's add the endpoint in `swagger.yml` to make this creation function accessible through your API.
