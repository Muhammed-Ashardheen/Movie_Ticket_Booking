# Movie Ticket Booking System
Java 17 + Jakarta Servlets (Tomcat 10.1+) + HTML + PostgreSQL + JDBC (Maven WAR project)

## Setup
1. Create DB and tables:
       createdb moviedb
       psql -d moviedb -f sql/schema.sql
2. Edit DB URL / user / password in src/main/java/com/movie/util/DBConnection.java
3. Build:   mvn clean package
4. Copy target/movie-ticket-booking.war into Tomcat 10.1+ webapps/ and start Tomcat.
5. Open http://localhost:8080/movie-ticket-booking/

## Run locally with Jetty
From this directory (the directory containing `pom.xml`), run:

       mvn org.eclipse.jetty.ee10:jetty-ee10-maven-plugin:12.0.40:run

Then open http://localhost:8080/. Jetty EE10 is used to match the Jakarta Servlet 6 API.

## Structure
- sql/schema.sql            tables + sample movies
- util/DBConnection         JDBC connection
- dao/MovieDAO, BookingDAO  all SQL (booking and cancel use transactions)
- servlet/                  /movies, /book, /bookings
- webapp/index.html, style.css

Note: Tomcat 9 or older uses javax.servlet - change the imports and the pom dependency if you use it.