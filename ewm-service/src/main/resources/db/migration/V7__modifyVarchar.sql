ALTER TABLE if EXISTS categories ALTER COLUMN "name" TYPE varchar(255);

ALTER TABLE if EXISTS users ALTER COLUMN email TYPE varchar(255);
ALTER TABLE if EXISTS users ALTER COLUMN "name" TYPE varchar(255);

ALTER TABLE if EXISTS events ALTER COLUMN title TYPE varchar(255);
ALTER TABLE if EXISTS events ALTER COLUMN description TYPE varchar(255);
ALTER TABLE if EXISTS events ALTER COLUMN annotation TYPE varchar(255);
ALTER TABLE if EXISTS events ALTER COLUMN "state" TYPE varchar(255);

ALTER TABLE if EXISTS compilations ALTER COLUMN title TYPE varchar(255);

ALTER TABLE if EXISTS requests ALTER COLUMN "status" TYPE varchar(255);

