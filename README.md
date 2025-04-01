# AlaCom
A component of a shopping platform that calculates product prices based on configurable discount policies.\
During calculation, the highest entitled discount will be applied.\
More on discount policies is in the *Usage::Configuration* section.

## Table of Contents
- [Build](#build)
- [Run](#run)
- [Test](#test)
- [Usage](#usage)
  - [API](#api)
  - [Configuration](#configuration)
  - [Application Data](#application-data)

## Build
Inside the project's root directory, create an executable jar:
```bash
./gradlew clean bootJar
```
You may need to add privileges to the *gradlew* file first:
```bash
chmod +x ./gradlew
```

## Run
Inside project's root directory, run *docker compose* command:
```bash
docker compose up [--build]
```
*--build* argument is optional. Needed when rebuilding project after applying changes. 

## Test
Inside the project's root directory, run the *gradlew* command:
```bash
./gradlew clean test
```

## Usage

### API
All API endpoints are accessible via `http://localhost:8080` by default.

#### GET /api/v1/products/{id}

##### Description
Retrieves detailed information about a product by its unique UUID.

##### Response

###### Success (200 OK)
```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "name": "Product",
  "price": 10.00
}
```

#### POST /api/v1/products/{id}/price

##### Description
Calculates the price of the product based on discount policies and unit count.

##### Request Body
```json
{
  "quantity": 1
}
```

##### Responses

###### Success (200 OK)
```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "name": "Product",
  "quantity": 1,
  "unitPrice": 10.00,
  "finalPrice": 9.00,
  "discount": 1.00
}
```

###### Error (400 Bad Request)
Occurs if *quantity* is missing or negative.
```json
{
  "timestamp": "2025-01-01T15:15:15.985+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/v1/products/11111111-1111-1111-1111-111111111111/price"
}
```

### Configuration
Example of *application.yaml* placed in the *src/main/resource/* directory:
```yaml
discount:
  fixedValue: 1.5%
  thresholds:
    - requiredQuantity: 10
      value: 5%
    - requiredQuantity: 20
      value: 10%
    - requiredQuantity: 50
      value: 15%
```
There are two types of discounts:
- fixedValue - constant discount disregarding the quantity of units
- thresholds - describe intervals of discount rate, where *requiredQuantity* is a minimum number of units to apply the discount

Discounts are presented in percentage values. To consider a value valid, you do not need to put *%* after it.

### Application Data
The published version of the application does not contain any data and does not provide an API to create one.\
The easiest way to add some data to play with is to add a migration file, re-build and re-run the project.\
To do this, create *V1.0.1__add_data.sql* in the *src/main/resource/db/migration/* directory. Example of such a file:
```sql
INSERT INTO product (id, name, price)
VALUES
    ('a26021ca-029a-4866-8d2c-cec78cb0764a', 'Product A', 10.00),
    ('b11fb433-c4e5-4f28-b56b-5028dd976ddb', 'Product B', 23.99),
    ('fa94d9b9-27f3-4180-a61f-942c0c69f6b0', 'Product C', 15.16);
```
