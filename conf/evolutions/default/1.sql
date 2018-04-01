# --- !Ups
create table user(id int primary key auto_increment,
     firstName varchar(50) not null,
     middleName varchar(50),
     lastName varchar(50) not null,
     username varchar(50) not null unique key,
     password varchar(50) not null,
     mobile varchar(10) not null,
     gender varchar(10) not null,
     age int not null,
     hobbies varchar(200),
     isActive varchar(5) default 'true',
     isAdmin varchar(5) default 'false'
     );

# --- !Downs
drop table user