# Bus lines assignment
This is my solution for the bus lines assignment. 
The application gathers information about bus lines and the stops along their routes, and compiles a summary of the lines with the most stops. 

Built with Java 17 and Spring Boot 3.1, with Maven as build tool (wrapper provided for convenience).
Utilizes Trafiklab's API, specifically _SL Stops and lines v2.0_. More info at: https://www.trafiklab.se

## Tests
Run unit tests: `./mvnw test`

## Application
How to run the application:  
`./mvnw spring-boot:run -Dspring-boot.run.arguments="--TRAFIKLAB_API_KEY=<your API key>"`  
(Mac/Linux: `./mvnw`, Windows: `mvnw`)
