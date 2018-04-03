# --- !Ups
create table assignment(id int primary key auto_increment,title varchar(50),description text);
create table users(id int primary key auto_increment,
firstName varchar(50) not null,
middleName varchar(50),
lastName varchar(50) not null,
username varchar(50) not null,
password  varchar(50) not null,
mobile     varchar(10) not null,
gender  varchar(10) not null,
age int,
hobbies varchar(200),
isActive varchar(5) default 'true',
isAdmin  varchar(5) default 'false'
);
# --- !Downs
drop table users;
drop table assignment;


