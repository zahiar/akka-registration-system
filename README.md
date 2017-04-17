# Akka Registration System Example

A technical exercise exploring the Reactive toolkit Akka along with Akka HTTP.
Currently it allows you to create an account and to get an account's details.

## Instructions
Checkout the repository and then run this command from within the directory to start the server.
It will run on http://localhost:8080 and will listen out for requests.
```
gradle run
```

---

To get details of an existing account, run this cURL command:
```
curl -X GET http://localhost:8080/account/1
```

---

To create a new account, run this cURL command:
```
curl -X POST http://localhost:8080/create-account -H 'content-type: application/json' -d '{"firstName": "Zahiar", "lastName": "Ahmed", "email": "zahiar.ahmed@example.com"}'
```
