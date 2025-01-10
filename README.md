# Fetch Rewards Receipt Processor Challenge 

## **1. Overview**
Receipt processor webservice prepared for [Fetch Rewards](https://github.com/fetch-rewards/receipt-processor-challenge) and implemented in Java with a lightweight, 
non-opinionated web microframework, [Javalin](https://javalin.io/). The project aims to provide accurate results while 
prioritizing readability, maintainability, and extensibility for further development considerations.

## **2. Quick Start**
This application exposes port 7070 for the API endpoints, please ensure it's available or see troubleshooting below.
### Running Locally
#### Prerequisites
- Java 17+
- Maven 3.6+
- IDE of your choice (IntelliJ IDEA, Eclipse, etc.)

1. Clone the repository
2. Navigate to the project directory
3. Download dependencies
   ```bash
   mvn clean install
   ```
4. Run the application
   ```bash
   mvn exec:java
   ```
   Alternatively, you can run the application from your IDE by running the `Main.java` 
OR `mvn clean install exec:java`, by combining steps 3 and 4


5. To stop the application, press `Ctrl + C` in the terminal

The webservice will be available at `http://localhost:7070`

### Running with Docker
#### OPTION 1: Docker Compose (Recommended)

```bash
# Build and start
docker-compose up --build

# Stop containers
docker-compose down
```


#### OPTION 2: Directly Using Docker
1. Build the image:
   ```bash
   docker build -t receipt-processor .
   ```
2. Run the container:
   ```bash
   docker run -p 7070:7070 receipt-processor
   ```

### Verify Deployment
1. Check if API documentation is running and visit http://localhost:7070/swagger/ in your browser

![image](https://github.com/user-attachments/assets/11db8008-006e-4480-8c39-99e3e29e25c1)

2. Use `curl` to test the API endpoints, here are two test receipt examples:
```bash
curl -X POST http://localhost:7070/receipts/process \
-H "Content-Type: application/json" \
-d '{"retailer": "Walgreens", "purchaseDate": "2022-01-02", "purchaseTime": "08:13", "total": "2.65", "items": [{"shortDescription": "Pepsi - 12-oz", "price": "1.25"}, {"shortDescription": "Dasani", "price": "1.40"}]}'
```

```bash
curl -X POST http://localhost:7070/receipts/process \
-H "Content-Type: application/json" \
-d '{"retailer": "Target", "purchaseDate": "2022-01-02", "purchaseTime": "13:13", "total": "1.25", "items": [{"shortDescription": "Pepsi - 12-oz", "price": "1.25"}]}'
```

### Troubleshooting:

1. My port is already in use?
```bash
# Check what's using port 7070
sudo lsof -i :7070

# Kill the process
kill -9 <PID>
```

2. Docker permission issues:
```bash
# Add your user to docker group
sudo usermod -aG docker $USER
# Log out and back in
```

3. Useful Docker Commands:
```bash
# View running containers
docker ps

# View logs
docker logs <container_id>

# View all images (can check image size)
docker images

# Stop all containers
docker stop $(docker ps -a -q)

# Remove all containers
docker rm $(docker ps -a -q)

# Remove all images
docker rmi $(docker images -q)
```

## **3. API Documentation**
The API documentation is available at [Swagger UI](http://localhost:7000/swagger/) once the application is up and running.

### Endpoints

#### (1) Process Receipt
```
POST /api/v1/receipts/process
```
![PostRequest](https://github.com/user-attachments/assets/da330aeb-87ca-45ea-83dc-7dc95d06bf98)
Request body: here's an example of a morning receipt 
```json
{
  "retailer": "Walgreens",
  "purchaseDate": "2022-01-02",
  "purchaseTime": "08:13",
  "total": "2.65",
  "items": [
    {"shortDescription": "Pepsi - 12-oz", "price": "1.25"},
    {"shortDescription": "Dasani", "price": "1.40"}
  ]
}
```

Response:
```json
{
  "id": "b1f6d3ca-fd1c-4604-ad6b-07d59af335f9"
}
```
![IdResponse](https://github.com/user-attachments/assets/be14c89b-b0f2-4b95-aa4b-cf13cf9267b7)
#### (2) Get Points
```
GET /api/v1/receipts/{id}/points
```
![GetRequest](https://github.com/user-attachments/assets/fd97ebc7-5859-4a96-a3ef-b8488d0a1180)
Request path parameter: `id` is the same UUID generated from the POST request that we saw earlier
Response:
```json
{
  "points": 15
}
```
![PointsResponse](https://github.com/user-attachments/assets/71579449-e658-417e-8065-6456dd7cb6f1)

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
- [x] **Documentation** for better maintainability and usability
 
## **5. Future Roadmap**
If warranted by development resources while providing additional value to internal/external parties, the following nice-to-have 
features would be considered to make the current implementation more robust and production-ready:

- **Security Enhancements**: 
  - Rate limiter to avoid abuse of API
    - Priority Level: P0
    - Reasoning: increases the performance of our application by limiting the number of requests
  - Authentication/authorization Middleware 
    - Priority Level: P1.5
    - Reasoning: we can create user roles and permissions to access endpoints, but it's not necessary for the current scope
  - Prevent duplicate processing of the same receipt
    - Priority Level: P3
    - Reasoning: need to discuss business logic to determine what is even a duplicate receipt
    

- **Performance Improvements**:
  - Caching for frequently accessed data
    - Priority Level: P1
    - Reasoning: improves performance by reducing the number of requests to the server
  - Load testing to identify bottlenecks
    - Priority Level: P2
    - Reasoning: ensures the application can handle the expected load, more important as we scale
  - Database integration for persistence
    - Priority Level: P3
    - Reasoning: not necessary for the current scope
    

- **CI/CD Considerations**:
  - Health check endpoints
    - Priority Level: P0
    - Reasoning: ensures the application is running and healthy
  - Monitoring and alerting setup
    - Priority Level: P2.5
    - Reasoning: not critical as we already have sufficient logging
    

- **Documentation Improvements**: 
  - API versioning with backwards compatibility
    - Priority Level: P3
    - Reasoning: no plans to create multiple versions of the API at this time
  - API changelog
    - Priority Level: P3
    - Reasoning: useful for developers to understand what has changed


