create schema if not exists ID342835_g999;
use ID342835_g999;

create table if not exists Speler (
	spelerID int not null auto_increment,
    naam varchar(50) not null,
    voornaam varchar(25) not null,
    emailadres varchar(100) not null,
    wachtwoord varchar(20) not null,
    geboortedatum date not null,
    adminrechten bit(1) not null,
    krediet float not null,
    primary key(spelerID)
);

create table if not exists Spel(
	spelID int not null auto_increment,
    aantalOgenEersteWorp int not null,
    aantalOgenLaatsteWorp int not null,
    totaalAantalWorpen int not null,
    spelerID int not null,
    primary key (spelID),
    foreign key (spelerID) references Speler(spelerID)
    on delete cascade
    on update cascade
);