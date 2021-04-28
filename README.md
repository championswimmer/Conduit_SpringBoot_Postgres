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