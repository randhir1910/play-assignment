# --- !Ups
create table assignment(id int primary key auto_increment,title varchar(50),description text);
# --- !Downs
drop table assignment;
