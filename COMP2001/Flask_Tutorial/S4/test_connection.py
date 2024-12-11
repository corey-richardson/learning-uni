from sqlalchemy import create_engine

connection_string = (
    "mssql+pyodbc:///?odbc_connect="
    "DRIVER={ODBC Driver 17 for SQL Server};"
    "SERVER=host.docker.internal;"
    "DATABASE=Flask_Tutorial;"
    "UID=SA;"
    "PWD=C0mp2001!;"
    "TrustServerCertificate=yes;"
    "Encrypt=yes;"
)

engine = create_engine(connection_string)
connection = engine.connect()

print("Connection successful!")
connection.close()
