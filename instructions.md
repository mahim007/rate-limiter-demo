# For Compiling/Testing/Packaging/Running the project:

## For compilation: go to the project's root directory and run the command in terminal: mvn clean compile
## For testing (integration test): run the command: mvn clean test
## For Packaging: run the following command: mvn clean package
## To run the project: use the following command:  mvn spring-boot:run

# For testing the rate limiting feature:
Open a terminal and put the following command: curl -I http://localhost:8080/city/Bangkok1/asc?[1-20]. 
As the /city endpoint accept 10 http requests in 5 seconds, the first 10 requests will provide with a response of 
http 200, but the rest 10 requests will respond with http 503 Service Unavailable

# To add new HTTP end point
1. open and modify the ratelimiter.properties file under resources' folder. 
2. add a new path. i.e. /demo
3. add a threshold (no of requests to be allowed) i.e. 50
4. add a timeframe (how much time the threshold will be activated for) i.e. 5
5. Run/deploy the project