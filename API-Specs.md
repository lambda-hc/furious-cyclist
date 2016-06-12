
## Api Specs


#### Register User

url : /api/auth/register

requestData

```
{
  "userName":"vishnu667",
  "password":"password",
  "name":"Vishnu Prasad",
  "email":"vishnu667@gmail.com",
  "city":"Bangalore"
}
```

Response Data

```
{
  "status": "ok",
  "message": "Registration successful"
}
```

#### Login

url : /api/auth/login

requestData 

```
{
  "userName":"vishnu667",  // either the userName or the email
  "email":"vishnu667@gmail.com", //
  "password":"password"
  
}
```

Response

```
{
  "status": "ok",
  "message": "Login successful"
}
```


#### Logout

url : /api/auth/logout