# Rewards Program

A Spring Boot REST API that calculates retail customer reward points based on purchase transactions.

## Reward Rules

| Transaction Amount | Points |
|---|---|
| Less than $50 | 0 |
| $50 – $100 | 50 (flat) |
| Over $100 | 50 for first $100 + 2 per dollar over $100 |

**Examples:** $120 → 90 pts, $75 → 50 pts, $40 → 0 pts.

## Tech Stack

- **Java 17**, **Spring Boot 4.0.6** (Web, Data JPA, Validation)
- **PostgreSQL**, **Hibernate** (`ddl-auto: update`)
- **Lombok**, **SLF4J** (via LoggerUtil)
- **JUnit 5 + Mockito + MockMvc**

## API

```
GET /api/v1/rewards/{customerId}?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
```

**Response** (customer 1, Jan–Mar 2026):
```json
{
  "customerId": 1,
  "customerName": "John Doe",
  "monthlyRewards": [
    { "month": "JANUARY", "points": 140 },
    { "month": "FEBRUARY", "points": 250 },
    { "month": "MARCH", "points": 50 }
  ],
  "totalPoints": 440
}
```

## Implementation Flow

`RewardServiceImpl.getCustomerRewards()` follows 6 steps:

1. **Validate customer** — query `CustomerRepository` by ID; throw `NotFoundException` (404) if missing
2. **Fetch transactions** — query `TransactionRepository` by customer ID + date range (uses DTO projection via JPQL constructor expression)
3. **Group & sum by month** — stream transactions, `.collect(Collectors.groupingBy)` on `transactionDate.getMonth().name()`, summing points via `calculatePoints()`
4. **Map to DTOs** — transform `Map<String, Integer>` → `List<MonthReward>`
5. **Aggregate total** — sum all monthly point values
6. **Build response** — construct `CustomerResponse` with customer info, monthly breakdown, and total

**Point calculation** (`calculatePoints`):
- `amount < 50 || null` → return 0
- `50 ≤ amount ≤ 100` → flat 50 points
- `amount > 100` → `(amount - 100) * 2 + 50`

## Project Structure

```
src/main/java/com/retailer/rewards/
├── RewardsProgramApplication.java       -- @SpringBootApplication entry
├── controller/RewardsController.java    -- REST endpoint
├── service/
│   ├── RewardService.java               -- Interface
│   └── impl/RewardServiceImpl.java      -- Business logic
├── repository/
│   ├── CustomerRepository.java          -- JPA + DTO projection
│   └── TransactionRepository.java       -- JPA + date-range query
├── entity/
│   ├── Customer.java                    -- customers table
│   └── Transaction.java                 -- transactions table
├── dto/
│   ├── CustomerDto / TransactionDto     -- DB projection DTOs
│   ├── CustomerResponse / MonthReward   -- API response DTOs
├── exception/
│   ├── NotFoundException                -- Custom 404 exception
│   ├── GlobalExceptionHandler           -- @RestControllerAdvice
│   └── ErrorResponse                    -- Error payload
└── util/LoggerUtil.java                 -- SLF4J static wrapper

src/main/resources/
├── application.properties
├── application.yaml                     -- DB & JPA config
└── data.sql                             -- Seed data

src/test/java/com/retailer/rewards/
├── RewardsProgramApplicationTests.java  -- Context load test
├── controller/RewardsControllerTest.java-- MockMvc integration tests
└── service/impl/RewardServiceImplTest.java-- Unit tests with Mockito
```

## Setup

1. **Prerequisites:** Java 17, Maven, PostgreSQL on `localhost:5432`
2. **Create database:**
   ```sql
   CREATE DATABASE rewards_db;
   ```
3. **Update credentials** in `application.yaml` if needed
4. **Run:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Hibernate creates tables; `data.sql` seeds 2 customers + 6 transactions (idempotent via `ON CONFLICT DO NOTHING`).

5. **Test:**
   ```
   curl "http://localhost:8080/api/v1/rewards/1?startDate=2026-01-01&endDate=2026-03-31"
   ```

## Error Handling

| Scenario | Status | Body |
|---|---|---|
| Customer not found | 404 | `{ "error": "Not Found", "message": "..." }` |
| End date before start date | 400 | `{ "error": "Bad Request", "message": "..." }` |
| Unexpected error | 500 | `{ "error": "Internal Server Error", "message": "..." }` |

All errors return `{ "timestamp", "status", "error", "message" }` via `GlobalExceptionHandler`.

## Seed Data

| Customer | Transactions | Total Points (Jan–Mar) |
|---|---|---|
| John Doe (ID: 1) | $120 (Jan 10), $75 (Jan 22), $200 (Feb 14), $50 (Mar 5) | 440 |
| Jane Smith (ID: 2) | $80 (Jan 11), $110 (Feb 20) | 120 |

## Tests

| Test | What it verifies |
|---|---|
| `RewardsControllerTest.getRewards_SuccessScenario` | Happy path returns 200 with expected fields |
| `RewardsControllerTest.getRewards_InvalidDates_ReturnsBadRequest` | End before start → 400 |
| `RewardServiceImplTest.testCalculatePoints_CorrectCalculation` | 3 transactions ($120, $80, $40) → 140 total pts, 2 months |
| `RewardServiceImplTest.testCustomerNotFound_ThrowsException` | Missing ID → `NotFoundException` |

## Build History

| PR | What was added |
|---|---|
| `#1` | Base setup: entities (`Customer`, `Transaction`), DB config, DTOs |
| `#2` | Database seeding script (`data.sql`) |
| `#3` | Custom exception + `GlobalExceptionHandler` |
| `#4` | Reward calculation logic, service layer, controller endpoint |
| `#5` | JUnit tests + `LoggerUtil` utility |
| `#6` | JavaDocs + README documentation |
