[![Build Status](https://travis-ci.org/farzadz/pollApp.svg?branch=master)](https://travis-ci.org/farzadz/pollApp)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

</br>

# Spring Boot Polling API

This repository contains a Spring Boot application that defines a minimal REST API for creating polls.


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