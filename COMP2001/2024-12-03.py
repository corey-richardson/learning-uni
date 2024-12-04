# Test Authenticator

import requests

auth_url = 'https://web.socem.plymouth.ac.uk/COMP2001/auth/api/users'
email = 'tim@plymouth.ac.uk'
password = 'COMP2001!'

credentials = {
    'email' : email,
    'password' : password,
}

response = requests.post(auth_url, json=credentials)

if response.status_code == 200:
    try:
        json_respone = response.json()
        print("Successful Authentication: \n",
              json_respone)
    except requests.JSONDecodeError:
        print("Response is not valid JSON. Raw Response: \n",
              response.text)
else:
    print(f"Authentication failed with status code {response.status_code}")
    print("Response Content: \n", response.text)
