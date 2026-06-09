# Rewards Program

A Spring Boot REST API that calculates retail customer reward points based on purchase transactions.

## Reward Rules

| Transaction Amount | Points Earned |
|---|---|
| Less than $50 | 0 points |
| $50 – $100 | 50 points (flat) |
| Over $100 | 50 points for first $100 + 2 points per dollar over $100 |

## Tech Stack

- **Java 17**, **Spring Boot 4.0.6** (Web, Data JPA, Validation)
- **PostgreSQL**, **Hibernate** (DDL auto-update)
- **Lombok**, **SLF4J** (LoggerUtil)
- **JUnit 5 + Mockito** (tests)

## API

```
GET /api/v1/rewards/{customerId}?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
```

**Response:**
```json
{
  "customerId": 1,
  "customerName": "John Doe",
  "monthlyRewards": [
    { "month": "JANUARY", "points": 90 },
    { "month": "FEBRUARY", "points": 250 },
    { "month": "MARCH", "points": 0 }
  ],
  "totalPoints": 340
}
```

## Project Structure

```
src/main/java/com/retailer/rewards/
├── RewardsProgramApplication.java    -- Entry point
├── controller/
│   └── RewardsController.java        -- GET /api/v1/rewards/{customerId}
├── service/
│   ├── RewardService.java            -- Interface
│   └── impl/RewardServiceImpl.java    -- Business logic & point calculation
├── repository/
│   ├── CustomerRepository.java       -- JPA + DTO projection query
│   └── TransactionRepository.java    -- JPA + date-range query
├── entity/
│   ├── Customer.java                 -- customers table
│   └── Transaction.java              -- transactions table
├── dto/
│   ├── CustomerDto.java / TransactionDto.java   -- DB projection DTOs
│   ├── CustomerResponse.java / MonthReward.java  -- API response DTOs
├── exception/
│   ├── NotFoundException.java                    -- 404 exception
│   ├── GlobalExceptionHandler.java               -- @RestControllerAdvice
│   └── ErrorResponse.java                        -- Standard error payload
└── util/
    └── LoggerUtil.java             -- SLF4J logging utility

src/main/resources/
├── application.properties
├── application.yaml                 -- DB & JPA config
└── data.sql                         -- Seed data (2 customers, 6 transactions)

src/test/java/com/retailer/rewards/
├── RewardsProgramApplicationTests.java
├── controller/RewardsControllerTest.java
└── service/impl/RewardServiceImplTest.java
```

## Setup

1. **Prerequisites:** Java 17, Maven, PostgreSQL running on `localhost:5432`
2. **Create database:**
   ```sql
   CREATE DATABASE rewards_db;
   ```
3. **Update credentials** in `application.yaml` if needed:
   ```yaml
   spring.datasource.username: postgres
   spring.datasource.password: 2810
   ```
4. **Run the app:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Hibernate auto-creates tables, and `data.sql` seeds sample data.

5. **Test the API:**
   ```
   curl "http://localhost:8080/api/v1/rewards/1?startDate=2026-01-01&endDate=2026-03-31"
   ```

## Exception Handling

| Scenario | HTTP Status | Error Code |
|---|---|---|
| Customer not found | 404 | Not Found |
| Invalid date range (end before start) | 400 | Bad Request |
| Any unexpected error | 500 | Internal Server Error |

## Build History

| PR | Description |
|---|---|
| `#1` | Base setup: entities, DB config, initial DTOs |
| `#2` | Database seeding script |
| `#3` | Custom exceptions + global exception handler |
| `#4` | Reward point calculation logic + controller |
| `#5` | JUnit tests + LoggerUtil utility |
