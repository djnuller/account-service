## Account service

### This service is responsible for managing accounts.

To run the project first spin up the database using the following command:
```bash
docker-compose up -d
```
Then run the project using the following command:
```bash
./gradlew bootRun
```
or if you want to run it from any IDE, you can run the main class `AccountServiceApplication`.

The service will be running on port 8080.
The service has the following endpoints (also visible in the swagger UI)

| Method | Path                               | Description                          |
|--------|------------------------------------|--------------------------------------|
| GET    | /api/account/{accountNumber}       | Get account by account number        |
| POST   | /api/account/create                | Create account                       |
| POST   | /api/account/deposit               | Deposit money to account             |
| GET    | /api/account-event/{accountNumber} | Get account events by account number |
| GET    | /api/exchange-rate                 | Get exchange rate                    |
| POST   | /api/transfer/transfer             | Transfer money between accounts      |


Visit the swagger UI at http://localhost:8080/swagger-ui.html

So far there is only tests for the account service, but the other services will be tested as well.