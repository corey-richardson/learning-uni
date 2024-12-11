# Deploy your Service using Docker

## Overview

Deploy and publish your service that manages people’s names and notes using Docker.

How to write a Dockerfile, build a Docker image, publish it to a container registry, and run a container. 

```
Flask_Tutorial/S3/
│
├── templates/
│   └── home.html
│
├── app.py
├── config.py
├── models.py
├── notes.py
├── people.py
├── requirements.txt
└── swagger.yml
```

## Verify Installation

To verify your Docker installation, run the commands below in PowerShell or Terminal.

```bash
docker --version
docker run hello-world
```

```
C:\Users\richa\Desktop\learning>docker --version
Docker version 27.2.0, build 3ab4256

C:\Users\richa\Desktop\learning>docker run hello-world
Unable to find image 'hello-world:latest' locally
latest: Pulling from library/hello-world
c1ec31eb5944: Download complete
Digest: sha256:305243c734571da2d100c8c8b3c3167a098cab6049c9a5b066b6021a60fcb966
Status: Downloaded newer image for hello-world:latest

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/
```

## Create the Dockerfile

A Dockerfile is a script containing commands to build a Docker image. 

In your project directory, create a new file named Dockerfile without any extension and copy the following lines into it.

```
Flask_Tutorial_Docker/
│
├── templates/
│   └── home.html
│
├── app.py
├── config.py
├── models.py
├── notes.py
├── people.py
├── requirements.txt
├── swagger.yml
└── Dockerfile
```

```dockerfile
FROM python:3.9-slim

ENV ACCEPT_EULA=Y
RUN apt-get update -y && apt-get update \
  && apt-get install -y --no-install-recommends curl gcc g++ gnupg unixodbc-dev

RUN curl https://packages.microsoft.com/keys/microsoft.asc | apt-key add - \
  && curl https://packages.microsoft.com/config/debian/10/prod.list > /etc/apt/sources.list.d/mssql-release.list \
  && apt-get update \
  && apt-get install -y --no-install-recommends --allow-unauthenticated msodbcsql17 mssql-tools \
  && echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bash_profile \
  && echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bashrc

COPY . .

RUN pip install --upgrade pip
RUN pip install -r requirements.txt

RUN apt-get -y clean

EXPOSE 8000

CMD ["python", "app.py"]
```

### Base Image Selection

```dockerfile
FROM python:3.9-slim
```

This line specifies the starting point - a lightweight Python 3.9 image. The slim variant provides a balance between image size and functionality, containing just enough packages to run Python applications. This is particularly important in production environments where image size affects deployment time and resource usage.

### SQL Server Requirements

```dockerfile
ENV ACCEPT_EULA=Y
RUN apt-get update -y && apt-get update \
  && apt-get install -y --no-install-recommends curl gcc g++ gnupg unixodbc-dev
```

These commands prepare our container for SQL Server connectivity. We accept the End User License Agreement (EULA), update the package lists, and install necessary build tools. The `--no-install-recommends` flag keeps our image size smaller by avoiding optional packages.

### SQL Server Driver Installation

```dockerfile
RUN curl https://packages.microsoft.com/keys/microsoft.asc | apt-key add - \
  && curl https://packages.microsoft.com/config/debian/10/prod.list > /etc/apt/sources.list.d/mssql-release.list \
  && apt-get update \
  && apt-get install -y --no-install-recommends --allow-unauthenticated msodbcsql17 mssql-tools \
  && echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bash_profile \
  && echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bashrc
```

This command adds Microsoft’s package repository and installs SQL Server drivers. We also configure the system PATH to include SQL Server tools, making them accessible throughout the container. 

### Application Setup

```dockerfile
COPY . .

RUN pip install --upgrade pip
RUN pip install -r requirements.txt

RUN apt-get -y clean

EXPOSE 8000
```

Here, we first copy our code and install all the dependencies listed in requirements.txt. After the installation, we clean up the apt cache to reduce the final image size. We also informs Docker that the container will listen on port 8000 at runtime. Notably, this is purely documentational and doesn’t actually publish the port. Ensure that you explicitly map the port when running the container.

### Run the Code

```dockerfile
CMD ["python", "app.py"]
```

This line specifies the command that will be executed when the container starts.

## Build the Docker Image

Open your PowerShell or Terminal in your project directory and run the build command.

```bash
docker build -t <your_image_name> .
```

The `-t` flag tags your image with a name and version. The period at the end tells Docker to look for the Dockerfile in the current directory. Remember to replace the `<your_image_name>` with an actual name you would like to use. The name must be lowercase.

## Publish the Image

After successfully building your image, the next step is to share it through Docker Hub. 

To authenticate with Docker Hub, use the following command.

```bash
docker login
```

```bash
C:\Users\richa\Desktop\learning\learning-uni\COMP2001\Flask_Tutorial\S4>docker login
Authenticating with existing credentials...
Login Succeeded
```

For Docker Hub, images must be prefixed with your Docker Hub username. Use the command below to tag the image to your username.

```bash
docker tag <your_image_name> <your_username>/<your_image_name>
```

```bash
docker tag flask-tutorial-s4 coreyrichardson1/flask-tutorial-s4
```

You can now push your image using the command.

```bash
docker push <your_username>/<your_image_name>
```

```bash
docker push coreyrichardson1/flask-tutorial-s4
```

During the push process, Docker will upload each layer of your image separately. This layer-based approach means that if you update your image later, only the changed layers need to be uploaded.

## Run the Container

You can pull any image including the one you just published by using the command:

```bash
docker pull <your_username>/<your_image_name>
```

```bash
docker pull coreyrichardson1/flask-tutorial-s4
```

The main command to run your container is:

```bash
docker run -p 8000:8000 <your_username>/<your_image_name>
```

```bash
docker run -p 8000:8000 coreyrichardson1/flask-tutorial-s4
```

The `-p 8000:8000` flag maps port 8000 (the first one) on your host machine to port 8000 in the container. The second number must match the port specified in your `EXPOSE` command.

To verify that your container is running properly, go to Docker Desktop or use the following command.

```bash
docker ps
```

The command show you all running containers, their IDs, names, status, and port mappings.

---

```bash
docker build -t flask-tutorial-s4 .
docker tag flask-tutorial-s4 coreyrichardson1/flask-tutorial-s4
docker push coreyrichardson1/flask-tutorial-s4
docker pull coreyrichardson1/flask-tutorial-s4
docker run -p 8000:8000 coreyrichardson1/flask-tutorial-s4
docker ps
```

```
CONTAINER ID   IMAGE                                COMMAND                  CREATED          STATUS          PORTS                              NAMES
9b2efdfaf69c   coreyrichardson1/flask-tutorial-s4   "python app.py"          44 seconds ago   Up 44 seconds   0.0.0.0:8000->8000/tcp             heuristic_lamport
daea5b7cfe2d   mcr.microsoft.com/azure-sql-edge     "/opt/mssql/bin/perm…"   8 weeks ago      Up 26 minutes   1401/tcp, 0.0.0.0:1433->1433/tcp   COMP2001sqlserv
```
