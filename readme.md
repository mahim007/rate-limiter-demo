# HotelBooking System with Rate Limiter
## A SpringBoot application with a custom rate limiter implemented using Filter

### Description
This application provides 5 HTTP APIs to expose its services. They are,
1. "/"  >
    to invoke the homepage/non rate limited path
2. "/city/{cityName}" > that returns all the hotels belonging to a specific city
3. "/city/{cityName}/{order}" > that returns all the hotels belonging to a specific city with sorting by price. i.e. ASC or DESC
4. "/room/{roomType}" > returns all the hotels that have the requested room type
5. "/room/{roomType}/{order}" > returns all the hotels that have the requested room type with sorting by price. i.e. ASC or DESC

### Key Decisions
1. Java: Java is language of choice here as I have more experience to write code in this language and use it daily basis.
2. SpringBoot : It's the industry standard web/application development framework. It takes little to no pre-configuration, easy to integrate, compile, package and deploy.
3. Rate limiter as a Filter: Since every request goes through a chain in spring before it hits the controller, It's convenient to write a custom filter that extend OncePerRequestFilter. The request path will provide the prefix of the request Url, and then it's a matter of implementation regarding how to enforce rate limiting to that specific path.
4. Usage of Data Structure: A Queue of request and Map(path as Key, Record as details data) are used in order to properly identify the current state of request processing for every given path. i.e. /city or /room. Only a counter variable may not suffice, because it only tracks the information regarding when an http request arrives but not finished. Also, A Queue may not reflect the total processing within a given time frame, because every time a request finished, the request needed to be dropped off from the queue. But combining both the variables/data structure give proper information regarding what is the current state and how many requests can be allowed within the current time window.
5. A custom .properties file (rate-limiter.properties) is used to add/modify config regarding the rate limiter path, and it's attribute (threshold and time frame). By leveraging this feature, separation of concern is achieved, as the code/business logic does not need to know where the config comes from/how does it change. It just reads the config at boot time and act accordingly. Hence, no extra development needed. 
6. If the no of request gets larger than the allowed no, 503 Service Unavailable returned as response.
7. If there is a situation where the no of requests in the current window is smaller than allowed no of request, but there are some in progress request from the previous time frame, and together they sum up more than the allowed no of request than a 429 Too Many Requests returned as response.
8. An embedded tomcat web server is used, as it's out of the box in springboot.
9. H2 in-memory database is used here to achieve portability, ease of use/run/test/deploy.
10. A custom implementation of CommandLineRunner is used to bootstrap the h2 database with data from the csv file.
11. JPA and Lombok are used to achieve annotation driven DB model and POJO creation.