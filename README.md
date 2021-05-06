# Conduit - Spring Boot + Postgres 

## Development 

### Database Setup 

#### PostgreSQL 

```postgresql
create database conduitspring;
create user conduitspring with encrypted password 'conduitspring';
grant all privileges on database conduitspring to conduitspring;
```

#### MySQL or MariaDB 

```mysql
create database conduitspring;
create user conduitspring identified by 'conduitspring';
grant all privileges on conduitspring.* to conduitspring;
```


## Deployment 

### Heroku 

#### Java Version 
Add file `system.properties`

```properties
java.runtime.version=11
```

This tells Heroku which Java version to use. 

#### PORT config
Heroku provides it's own port. To use that ... 

```yaml
server:
  port: ${PORT:4567}
```

This means, use `$PORT` environment variable if available, or if that is null, use `4567`

#### Setup Database
If application.yaml or application.properites has Postgres driver, then heroku installs Postgres automatically. 
Otherwise deploy Postgres addon (free tier is fine)

Add config params
```yaml 
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/conduitspring}
    username: ${SPRING_DATASOURCE_USERNAME:conduitspring}
    password: ${SPRING_DATASOURCE_USERNAME:conduitspring}
    driver-class-name: org.postgresql.Driver
```
