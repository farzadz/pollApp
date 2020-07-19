[![Build Status](https://travis-ci.org/farzadz/pollApp.svg?branch=master)](https://travis-ci.org/farzadz/pollApp)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

</br>

# Spring Boot Polling API

This repository contains a Spring Boot application that defines a minimal REST API for creating polls. 

## Workflow

Users are able to create questions with associated answer options, all logged in and anonymous users are able to 
see questions and options, but only registered users are allowed to vote. The creator of the question can also see the vote
counts, and usernames of those who chose each option.

## How it works

A combination of Spring ACL and Spring AOP capabilities are used for providing fine-grained access control with minimal footprint
in the code-base. Apache Orika mapper handles the translation of DTOs to domain objects. The backend uses Postgresql database for storing data and access control related information, the default ports
(that can be overriden via tweaking application.yaml) are `5440` for postgres database and `9090` for the API server.
 

#### Running Tests
Locally (requires database setup):

`mvn clean test` 

Docker:
```
docker-compose -f docker/test-compose.yaml build && \
docker-compose -f docker/test-compose.yaml up \
    --abort-on-container-exit \
    --exit-code-from test_runner
```