# Social network

It's my first java project within the EPAM java training course
I'd prefer to rewrite it using Spring & Hibernate, but unfortunately has no time now to do it :(

## Technologies
- [Tomcat](http://tomcat.apache.org/) as a web-server
- [PostgreSQL](https://www.postgresql.org/) for production mode
- [Yandex embedded postgreSQL server](https://github.com/yandex-qatools/postgresql-embedded) for unit tests
- [Gradle](https://gradle.org/) build

## Under the hood

- JDBC via Tomcat connection pool
- MVC architecture with servlets and JSP (no Spring)
- Apache query runner (no Hibernate)
- Bootstrap for a beautiful view
- [Letter avatar by Artur Heinze](https://agentejo.com/blog/tired-of-gravatar-try-letter-avatar) instead of profile photos
- JUnit for testing :)
- Log4J for logging :)

## Functionality

- Login & Registration forms
- Contact information update
- Add / remove friends
- Send messages
- View last messages

## Features

- Authorisation & Authentication (without admin or management roles)
- Internationalisation (english/russian interfaces)
- Server-side contact validation
- Pagination
- Users & friends search
- Error handler

## Known bugs and Future prospects

- Rewrite project using Spring & Hibernate
- Rewrite client-side using AngularJS / React and SPA approach
- Solve problem with multiple start of embedded PostgreSQL for each unit class
- Provide clear logging

## Related links

- [Demo](https://drive.google.com/open?id=0B2rnMGPgU6HnWTZJRi1vR182N1U) (video in russian)
