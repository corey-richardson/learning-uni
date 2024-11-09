import requests

r = requests.get("https://randomuser.me/api/")

print(r.status_code)
print(r.text)
print(r.headers)
print(r.request)
print(r.url)
print(r.headers)

cat_r = requests.get("https://api.thecatapi.com/v1/breeds/abys")
print(cat_r.headers)
print(cat_r.request.headers)
