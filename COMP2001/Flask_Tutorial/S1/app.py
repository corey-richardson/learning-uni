# Flask and Connexion

# This tutorial will walk you through creating a Flask-based API from scratch. Starting with a basic Flask application,
# you'll learn to develop API endpoints.
# Throughout this lab session, you'll also implement Swagger UI documentation to help you test and visualize your
# API's functionality.
# Specifically, this tutorial guides you through building a REST API for managing users.
# You'll create API endpoints to create, retrieve, and delete user information.
# You'll create dummy people like Grace Hopper, Tim Berners-Lee, and Ada Lovelace as seen in the assignment.


# # Creating the VENV

# python -m venv 2024-11-12
# cd 2024-11-12
# .\Scripts\activate.bat

# pip install Flask==2.2.2
# pip install "connexion[swagger-ui]==2.14.1"

# mkdir comp2001_flask_tutorial
# cd comp2001_flask_tutorial

# code app.py

# mkdir templates
# code home.html

# app.py

from flask import render_template
import connexion

app = connexion.App(__name__, specification_dir="./")
app.add_api("swagger.yml")

@app.route("/")
def home():
    return render_template("home.html")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)

# Run

# python app.py
# https://localhost:8000
# http://127.0.0.1:8000/

# http://127.0.0.1:8000/api/people

# Swagger UI
# http://127.0.0.1:8000/api/ui/
