# For Compiling/Testing/Packaging/Running the project:

### For compilation: go to the project's root directory and run the command in terminal: mvn clean compile
### For testing (integration test): run the command: mvn clean test
### For Packaging: run the following command: mvn clean package
### To run the project: use the following command:  mvn spring-boot:run

## API description
1. http://localhost:8080/  > to invoke the homepage/non rate limited path
2. http://localhost:8080/city/Bangkok > that returns all the hotels belonging to a specific city
3. http://localhost:8080/city/Bangkok/desc > that returns all the hotels belonging to a specific city with sorting by price. i.e. ASC or DESC
4. http://localhost:8080/room/Deluxe > returns all the hotels that have the requested room type
5. http://localhost:8080/room/Deluxe/asc > returns all the hotels that have the requested room type with sorting by price. i.e. ASC or DESC

# For testing the rate limiting feature:
Open a terminal and put the following command: curl -I http://localhost:8080/city/Bangkok/asc?[1-20]. 
As the /city endpoint accept 10 http requests in 5 seconds, the first 10 requests will provide with a response of 
http 200, but the rest 10 requests will respond with http 503 Service Unavailable

# To add new HTTP end point
1. open and modify the ratelimiter.properties file under resources' folder. 
2. add a new path. i.e. /demo
3. add a threshold (no of requests to be allowed) i.e. 50
4. add a timeframe (how much time the threshold will be activated for) i.e. 5
5. Run/deploy the project