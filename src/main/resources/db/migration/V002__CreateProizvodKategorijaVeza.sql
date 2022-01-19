

create table IF NOT EXISTS webKatalog.proizvod (
id  SERIAL PRIMARY KEY,
naziv varchar(255),
opis varchar(255),
cena INT NOT NULL,
dostupna_kolicina INT NOT NULL
);




create table IF NOT EXISTS webKatalog.kategorija (
id  SERIAL PRIMARY KEY,
naziv varchar(255),
opis varchar(255)
);

create table IF NOT EXISTS webKatalog.veznatabela (
id  SERIAL PRIMARY KEY,
id_proizvoda INT NOT NULL,
id_kategorije INT NOT NULL,
FOREIGN KEY (id_proizvoda) REFERENCES webKatalog.proizvod (id),
FOREIGN KEY (id_kategorije) REFERENCES webKatalog.kategorija (id)
);




INSERT INTO webKatalog.proizvod (naziv, opis,cena,dostupna_kolicina)
VALUES
('proizvod1','opis1',1,1),
('proizvod2','opis2',2,2),
('proizvod3','opis3',3,3),
('proizvod4','opis4',4,4),
('proizvod5','opis5',5,5);


INSERT INTO webKatalog.kategorija (naziv, opis)
VALUES
('kategorija1','opis1'),
('kategorija2','opis2'),
('kategorija3','opis3'),
('kategorija4','opis4'),
('kategorija5','opis5'),
('kategorija6','opis6'),
('kategorija7','opis7');

INSERT INTO webKatalog.veznatabela (id_proizvoda, id_kategorije)
VALUES(1,1),
(1,2),
(2,3),
(3,4),
(4,5);