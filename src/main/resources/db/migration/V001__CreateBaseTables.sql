CREATE SCHEMA IF NOT EXISTS webKatalog;

create table IF NOT EXISTS webKatalog.users (
id  SERIAL PRIMARY KEY,
ime varchar(255),
prezime varchar(255),
adresa varchar(255),
broj_telefona varchar(255),
postanski_broj varchar(255),
mesto varchar(255),
drzava varchar(255),
email varchar(255),
lozinka varchar(255),
role varchar(255)
);
