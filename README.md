# Fetch Rewards Receipt Processor Challenge 

## **1. Overview**
Receipt processor webservice prepared for [Fetch Rewards](https://github.com/fetch-rewards/receipt-processor-challenge) and implemented in Java with a lightweight, 
non-opinionated web microframework, [Javalin](https://javalin.io/). The project aims to provide accurate results while 
prioritizing readability, maintainability, and extensibility for further development considerations.

## **2. Quick Start** 

## **3. API Documentation**

## **4. Development Guide**
### 4.1 Project Requirements
(1) Data does not need to persist when application stops/restarts --> storing information in memory without the use of a database suffices

(2) Service must provide the expected results from specified rules for awarding points --> validation and testing are integral to the project

(3) Since the submission wasn't developed in the Go programming language --> dockerized setup is included to run the code (with detailed instructions provided as necessary)

### 4.2 MVP Definition
Simple receipt processor API with two endpoints, namely a (1) POST request that takes a given receipt in JSON format and generates a UUID for the processed receipt
and (2) GET request that looks up the corresponding point(s) rewarded from the processed receipt through its UUID.

```
Project Structure 

src/main/java
 ├── Main.java                                         // Application entry point
 ├── models/
 │   ├── Receipt.java                                  // Receipt domain model to be processed by POST request
 │   └── ReceiptPointsResponse.java                    // Response for GET request to retrieve points from generated receipt id
 ├── controllers/
 │   └── ReceiptController.java                        // HTTP endpoint handlers: separation of concerns from actual business logic
 ├── services/
 │   └── ReceiptService.java                           // Business logic: process receipts and calculate points based on specific rules
 ├── utils/
 │   ├── StringManipulationMethods.java                // Helper function(s): trimming and removing extra spaces to support PointsCalculator
 │   └── PointsCalculator.java                         // Points calculation as specified by the rules
 └── exceptions/
     └── ApiException.java                             // Custom exceptions
```

### 4.3 Core Components

- [x] **Validation** of request input to ensure validity and accuracy   
- [x] **Testing** to increase the reliability of application results
- [x] **Error Handling & Logging** for more observability and transparency to improve debugging experience
- [ ] **Documentation** for better maintainability and usability
 
## **5. Future Roadmap** 
If warranted by development resources while providing additional value to internal/external parties, the following nice-to-have 
features would be considered to make the current implementation more robust and production-ready:

- **Security Enhancements**: 
  - Rate limiter to avoid abuse of API
  - Authentication/authorization Middleware 
  - Prevent duplicate processing of the same receipt
- **Performance Optimizations**: 
  - Data persistence and/or caching layer 
  - Circuit breakers for external services
- **CI/CD Considerations**:
  - Health check endpoints
  - Monitoring and alerting setup
- **Documentation Improvements**: 
  - API versioning with backwards compatibility
  - API changelog


