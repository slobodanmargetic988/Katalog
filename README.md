# Katalog
 katalog demo aplikacija
 
 
project base created using start.spring.io
selected maven project, java language, jar packaging, java 8 version,
added dependancies :
thymleaf
spring web
postgresql driver

Zadatak:
Napraviti mini web katalog proizvoda u Java Spring framework-u. Katalog treba da sadrži sledeće funkcionalnosti:

Administracija osnovnih informacija o proiyvodu (nayiv, opis,cena, dostupna količina).
Potrebno je omogućiti dodavanje,brisanje i ažuriranje proiyvoda.

Administracija kategorija proizvoda (naziv i opis kategorije). Kao i za prethodnu tačku, treba napraviti XRUD za kategorije.

Stranica za pretragu proizvoda, gde je moguće pretražiti proizvode odabirom kategorije ili pretragom po delu naziva proizvoda.

Artikal može pripadati jednoj ili više kategorija, potrebno je omogućiti korisniku da kroz UI uradi administraciju pripadnosti artikla kategoriji.

Podatke je potrebno čuvati u PostgreSQL bazi, operacije nad bazom uraditi koristeći stored procedure ( ne koristiti framework.)



Setup/instalacija
Da bi aplikacija radila mora da postoji instaliran postgresql server.
neophodno je u application.properties file-u podesiti odgovarajuće parametre za pristup bazi što se svodi na modifikovanje sledeća tri parametra:

spring.datasource.url = jdbc:postgresql://localhost:5432/test1
spring.datasource.username = postgres
spring.datasource.password = 1234


Pomoću migracija se automatski pravi shema i početne tabele.
Posto ja u karijeri nisam imao previše potrebe da se bavim migracijom Stored procedura pa nisam siguran kako je najbolje pristupiti tom problemu (rešenja koja sam našao na internetu nisu sjajna) onda sam kreiranje svih stored procedura spakovao u main controller klasu i stavio da se sve izvrše prilikom loadovanja home stranice.
Tako da nakon otvaranja home stranice sve ostale funkcionalnosti bi trebalo da rade kako treba.


projekat je wrapovan mavenom tako da bi bilo koje radno okruženje uz minimalno podešavanje u vidu konektora za bazu trebalo da pokrene i bilduje projekat normalno.


OpenApi dokumentaciji (dok radi server ) može da se pristupi na linku:
http://localhost:8080/swagger-ui/index.html#/


Dodatna napomena: u projektu je korišteno više tipova pristupa bazi, napravio sam login formu u User klasu vežbe radi da bih video da li ima velikih razlika između MySQL i PostgreSQL tako da se User objekti loaduju koristeći framework hibernate.
Proizvodi i kategorije se loaduju koristeći named query. a sve izmene nad bazom se rade koristeći cist stored procedure koji se generise iz jave sa parametrima.

Takođe većinu frontenda sam ukrao iz postojećih projekata na kojima radim pa ima ti u viška css fajlova ali nisam hteo previše vremena da trošim da vidim tačno šta je neophodno jer i piše u zadatku da ne gubim previše vremena na UI.
Isto važi i za security configuraciju, iskopirana je iz postojećeg projekta da bih isprobao login formu koja i nije deo zadatka tako da ima viškova tu kojih bih mogao da se rešim.