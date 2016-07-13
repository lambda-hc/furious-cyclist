

# MYSQL DATABASE SCHEMA

Update your mysql database to Run the Application

```
CREATE DATABASE furiousCyclists;

USE furiousCyclists;

CREATE TABLE users
  (
     userId             BIGINT NOT NULL auto_increment,
     username           VARCHAR(255) NOT NULL UNIQUE,
     password_hash      VARCHAR(128),
     name               VARCHAR(255),
     email              VARCHAR(255) UNIQUE,
     city               VARCHAR(255),
     join_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY (userId)
  );


CREATE TABLE entries
      (
         entryId         BIGINT NOT NULL auto_increment,
         userId             BIGINT NOT NULL,
         vehicleNumber      VARCHAR(128),
         description        VARCHAR(500),
         location           VARCHAR(255),
         city               VARCHAR(255),
         registeredDate     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
         PRIMARY KEY (entryId)
      );