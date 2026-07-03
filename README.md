# Enterprise API Test Automation Framework

![API Automation Tests](https://github.com/kumareshsp4/enterprise-api-test-automation-framework/actions/workflows/api-tests.yml/badge.svg)

A scalable, multi-service API automation framework built with **Java, REST Assured, TestNG, Maven, Docker, GitHub Actions, and Allure Reporting**.

This project demonstrates how modern API test automation can be designed beyond simple request/response checks. It focuses on reusable framework architecture, secure token handling, dynamic test data, CRUD workflow validation, negative testing, CI execution, Docker-based test runs, and readable test reporting.

---

## Why This Project Exists

Many API automation projects show isolated test scripts. This project is built to demonstrate a more realistic enterprise-style approach:

* reusable API clients
* service-specific models and endpoints
* secure runtime configuration
* dynamic test data generation
* positive and negative API workflows
* cleanup-safe tests
* CI-ready execution
* Dockerized test runner
* Allure dashboard reporting
* smoke and regression test separation

The goal is to show practical quality engineering skills that are useful in real teams building distributed systems, microservices, and API-driven platforms.

---

## Tech Stack

| Area              | Tools / Frameworks                       |
| ----------------- | ---------------------------------------- |
| Language          | Java 17                                  |
| API Testing       | REST Assured                             |
| Test Framework    | TestNG                                   |
| Build Tool        | Maven                                    |
| Reporting         | Allure Reports, Surefire                 |
| CI/CD             | GitHub Actions                           |
| Containerization  | Docker, Docker Compose                   |
| Test Data         | Dynamic factory-based test data          |
| Config Management | Environment-based properties             |
| Secrets           | Environment variables and GitHub Secrets |

---

## Services Covered

This framework currently validates two public API services:

### GoRest API

Base URL:

```text
https://gorest.co.in/public/v2
```

Covered workflows:

* User CRUD workflow
* Post CRUD workflow
* Authentication negative testing
* Invalid payload validation
* Search and response validation
* Cleanup after test execution

### Swagger Petstore API

Base URL:

```text
https://petstore3.swagger.io/api/v3
```

Covered workflows:

* Pet CRUD workflow
* Find pet by status
* Delete validation using 404 response
* Dynamic pet data generation

---

## Current Test Coverage

### Smoke Tests

Smoke tests validate that core public endpoints are reachable and returning valid responses.

| Test                | Purpose                                                |
| ------------------- | ------------------------------------------------------ |
| `GoRestSmokeTest`   | Verifies GoRest users endpoint availability            |
| `PetStoreSmokeTest` | Verifies Petstore find-by-status endpoint availability |

### GoRest User Tests

| Test               | Coverage                                                             |
| ------------------ | -------------------------------------------------------------------- |
| `UserCrudTest`     | Create user → retrieve user → update user → delete user → verify 404 |
| `UserNegativeTest` | Missing token validation and invalid payload validation              |

### GoRest Post Tests

| Test           | Coverage                                                                             |
| -------------- | ------------------------------------------------------------------------------------ |
| `PostCrudTest` | Create user → create post → retrieve post → update post → delete post → cleanup user |

### Petstore Tests

| Test          | Coverage                                                                          |
| ------------- | --------------------------------------------------------------------------------- |
| `PetCrudTest` | Create pet → retrieve pet → update pet → find by status → delete pet → verify 404 |

---

## Framework Architecture

```text
src
├── main
│   ├── java
│   │   └── com.api.automation
│   │       ├── core
│   │       │   ├── authentication
│   │       │   ├── client
│   │       │   ├── config
│   │       │   ├── exception
│   │       │   ├── specification
│   │       │   └── utils
│   │       ├── gorest
│   │       │   ├── client
│   │       │   ├── endpoint
│   │       │   ├── factory
│   │       │   └── model
│   │       └── petstore
│   │           ├── client
│   │           ├── endpoint
│   │           ├── factory
│   │           └── model
│   └── resources
│       └── config
│           └── qa.properties
│
└── test
    ├── java
    │   └── com.api.automation.tests
    │       ├── gorest
    │       └── petstore
    └── resources
        ├── allure.properties
        ├── categories.json
        └── testng.xml
```

---

## Key Design Decisions

### Reusable API Clients

Each service has its own API client layer. Tests do not directly build raw REST Assured requests everywhere.

Example:

```java
userApiClient.createUser(request);
postApiClient.createPostForUser(userId, request);
petApiClient.createPet(request);
```

This keeps tests readable and separates business workflows from low-level HTTP implementation.

---

### Central Request Specifications

Request setup is centralized through `RequestSpecFactory`.

This avoids duplication for:

* base URI
* content type
* accept header
* authorization header
* logging filters

---

### Secure Token Handling

The GoRest token is never stored in source code.

The framework reads the token from:

```text
GOREST_TOKEN
```

Local usage:

```bash
export GOREST_TOKEN="your_gorest_token"
mvn clean test
```

GitHub Actions usage:

```yaml
env:
  GOREST_TOKEN: ${{ secrets.GOREST_TOKEN }}
```

This keeps credentials out of Git history, pull requests, logs, and Docker images.

---

### Dynamic Test Data

The framework uses factory classes for dynamic test data creation.

Examples:

* unique GoRest users
* unique GoRest posts
* unique Petstore pets
* reusable update payloads

This keeps tests repeatable and avoids collisions with existing public API data.

---

### Cleanup-Safe Test Design

CRUD workflows use cleanup logic to avoid leaving unnecessary test data behind.

Example patterns:

* delete created posts before deleting users
* cleanup users when a test fails midway
* verify deleted resources return `404`

---

### Public API Handling

Swagger Petstore is a shared public API, so `/pet/findByStatus` may return data created by other users.

To avoid brittle deserialization issues, the test validates only the created pet ID from the response instead of deserializing the full public list into a strict model.

---

## Allure Reporting

This project uses Allure to generate readable test reports and dashboards.

Allure adds:

* test suites
* features
* stories
* step-level execution visibility
* failure categories
* environment metadata
* cleaner reporting than raw console output

### Generate Allure Report Locally

Run tests:

```bash
mvn clean test
```

Generate report:

```bash
mvn allure:report
```

Serve report locally:

```bash
cd target/site/allure-maven-plugin
python3 -m http.server 8081
```

Open:

```text
http://localhost:8081
```

### Allure Report Output

```text
target/allure-results
target/site/allure-maven-plugin
```

### Failure Categories

The project includes `categories.json` for grouping common API failures:

* authentication failures
* validation failures
* resource not found
* response mismatch / contract issues

---

## GitHub Actions CI

The project includes GitHub Actions workflow support for:

### Smoke Tests on Push / Pull Request

Runs quick smoke tests when code is pushed or a PR is opened.

```bash
mvn --batch-mode clean test -Dtest=GoRestSmokeTest,PetStoreSmokeTest
```

### Nightly Full Regression

Runs the full suite on a scheduled nightly trigger.

```bash
mvn --batch-mode clean test
```

### CI Artifacts

The workflow uploads:

* Surefire test reports
* Allure raw results
* Allure HTML report

This makes it easier to debug failed test runs directly from GitHub Actions.

---

## Docker Support

The framework can run inside Docker to avoid local environment differences.

### Build Docker Image

```bash
docker build -t enterprise-api-tests .
```

### Run Full Suite

```bash
docker run --rm \
  -e GOREST_TOKEN="$GOREST_TOKEN" \
  enterprise-api-tests
```

### Run Smoke Tests

```bash
docker run --rm \
  -e GOREST_TOKEN="$GOREST_TOKEN" \
  enterprise-api-tests \
  clean test -Dtest=GoRestSmokeTest,PetStoreSmokeTest
```

### Run Petstore Test Only

```bash
docker run --rm \
  enterprise-api-tests \
  clean test -Dtest=PetCrudTest
```

Petstore tests do not require the GoRest token.

---

## Docker Compose Support

Run full suite:

```bash
docker compose run --rm api-tests
```

Run smoke tests:

```bash
docker compose run --rm smoke-tests
```

Run Petstore tests only:

```bash
docker compose run --rm petstore-tests
```

---

## Local Setup

### Prerequisites

Install:

* Java 17 or newer
* Maven 3.9+
* Docker
* Git

Verify:

```bash
java -version
mvn -version
docker --version
git --version
```

---

## Configuration

Environment configuration is stored under:

```text
src/main/resources/config/qa.properties
```

Example:

```properties
environment=qa

gorest.base.url=https://gorest.co.in/public/v2
petstore.base.url=https://petstore3.swagger.io/api/v3

connection.timeout.ms=10000
response.timeout.ms=10000
```

---

## Running Tests Locally

### Full Suite

```bash
mvn clean test
```

### Smoke Tests Only

```bash
mvn clean test -Dtest=GoRestSmokeTest,PetStoreSmokeTest
```

### Petstore CRUD Test

```bash
mvn clean test -Dtest=PetCrudTest
```

### GoRest User CRUD Test

```bash
mvn clean test -Dtest=UserCrudTest
```

### GoRest Post CRUD Test

```bash
mvn clean test -Dtest=PostCrudTest
```

---

## GitHub Secret Setup

To run GoRest tests in GitHub Actions, add this repository secret:

```text
GOREST_TOKEN
```

GitHub path:

```text
Repository → Settings → Secrets and variables → Actions → New repository secret
```

The token is injected at runtime and is not stored in the repository.

---

## Reporting and Metrics

This framework provides visibility through:

| Metric Area          | Source                   |
| -------------------- | ------------------------ |
| Pass / fail status   | Surefire, Allure         |
| Test suite execution | TestNG, Allure           |
| Test step visibility | Allure steps             |
| Failure categories   | Allure categories        |
| CI execution history | GitHub Actions           |
| Report artifacts     | GitHub Actions artifacts |
| Local dashboard      | Allure HTML report       |

---

## Example Workflow Validated by This Framework

### GoRest User Workflow

```text
Create user
Retrieve user
Update user
Delete user
Verify user no longer exists
```

### GoRest Post Workflow

```text
Create user
Create post for user
Retrieve post
Update post
Delete post
Verify post no longer exists
Delete user
```

### Petstore Pet Workflow

```text
Create pet
Retrieve pet
Update pet status
Confirm updated pet
Find pet by status
Delete pet
Verify pet no longer exists
```

---

## Project Strengths

This project demonstrates:

* API automation framework design
* REST Assured implementation
* TestNG suite management
* service-based package structure
* request and response modeling
* test data factory pattern
* secure token handling
* negative testing
* cleanup-safe workflows
* Dockerized test execution
* GitHub Actions CI
* Allure reporting and dashboards
* recruiter-friendly portfolio presentation

---
