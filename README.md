# Synchrony Imgur REST API

This is my implementation of the specification given for a RESTful API that which allows registered users to interface with Imgur's public-facing API.

## Running the Server

A good place to start is by cleaning the working directory.

```sh
./mvnw clean
```

You can then either run the server from within a development context, with nifty tools that do cool things like reload the server on a rebuild:

```sh
./mvnw spring-boot:run
```

Or you can create a "production-ready" jar and run that.

```sh
./mvnw package && target/synchronyapi-0.0.1-SNAPSHOT.jar
```

## Endpoints

This API exposes services through 3 base endpoints: `/auth`, `/users`, and `/imgur`. As the names might suggest, the auth endpoint is for registering and logging in, the users endpoint is for information on particular or aggregate users, and the imgur endpoint is for uploading, viewing, and deleting images.

### The Auth Endpoint

You will likely start by registering an account. This can be done by sending a `POST` request to `/auth/register` with a JSON body that follows the following format:

```json
{
    "username": "myusername",
    "password": "password1",
    "email": "myemail@test.com",
    "firstName": "john",
    "lastName": "doe"
}
```

The first and last name fields are optional. The endpoint will respond with the newly created user resource sent back to you.

You can then log into your newly created account by `POST`ing to `/auth/login`, where you must send it your credentials in the form of a username and password.

```json
{
    "username": "myusername",
    "password": "password1"
}
```

It will reply with a JWT access token which you must send as an authorization header on subsequent requests, specifically as a Bearer token.

```json
{
    "accessToken": "somelongandveryhardtoreadstringofnumbersandletters"
}
```
