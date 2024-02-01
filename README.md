# WorkTimeTrace
An application that can be used to record and manage working hours. It is primarily aimed at employees who can enter their working hours in the application. By entering their salary, it should also be possible to automatically generate a sort of payslip.


# Documentation
## API of REST-Controllers

### Timemanagement
All Urls privided by this microservice require authorisation.
This is done through two Values that need to be present in the header of the request.
1. Authorization Bearer Token: contains the JWT token provided by usermanagement.
2. username: contains the username of the user requesting information.

#### /all
Returns all Entries
GET Request
Only requires Authorization 

Return format:
```json
[
    {
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
    {
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
...
]
```

#### /byID/{id}
Returns entry with set id.
GET Request
Only requires Authorization 
Id Paramter is set in Path
Return format:
```json
{
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
}
```

#### /insertOne
Insert entry into the database
POST Request
body:
```json
{
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
}
```

#### /insertMany
Insert entries into the database
POST Request
body:
```json
[
    {
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
    {
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
...
]
```

#### /byNID
Returns all entries for given user.
GET Request
Only requires Authorization 
response:
```json
[
    {
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
    {
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
...
]
```

#### /byNIDforMonth/{month}
Return entries for given user and month
GET Request
Only requires Authorization 
Month is set in path. format: yyyy-mm
response:
```json
[
    {
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
    {
        "id": "int",
        "hourcount": "float",
        "date": "yyyy-mm-dd",
        "userid": "int"
    },
...
]
```


### pdfexport
#### /bill/{rate}
Returns the generated bill as byte[]
GET Request
Authorization is required
Rate is set in Path 


### Usermanagement
Usermanagement offers an open and a private controller.
Authorization for path /user/* equals timemanagement.

### Open Controller

### /auth/register
Register user and save user information
Returns the created user entity
POST Request
No authorization
Validation (frontend in progress): everything required, housenumber between 1 and 9999, zipcode five digits
body:
```json
{
    "username": "String",
    "password": "String",
    "firstname": "String",
    "lastname": "String",
    "street": "String",
    "housenumber": "int",
    "zipcode": "String",
    "city": "String"
}
```
Return format:
```json
{
    "id": "int",
    "username": "String",
    "password": "String/Hash",
    "firstname": "String",
    "lastname": "String",
    "street": "String",
    "housenumber": "int",
    "zipcode": "String",
    "city": "String",
    "roles": [
        "ROLE_USER"
    ]
}
```
Currently "User" is the only available role, "Admin" could follow in the future

### /auth/login
Returns the username and the generated JSON Web Token
POST Request
No authorization
Validation (frontend in progress): everything required
body:
```json
{
    "username": "String",
    "password": "String"
}
```
Return format:
```json
{
    "username": "String",
    "token": "String/JWT"
}
```

### Private Controller

### /user/info
Returns information about the given user
GET Request
Only requires Authorization

Return format:
```json
{
    "id": "bigint",
    "firstname": "String",
    "lastname": "String",
    "username": "String",
    "street": "String",
    "housenumber": "int",
    "zipcode": "String",
    "city": "String"
}
```
