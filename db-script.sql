create schema if not exists ID430820_KingDomino;
use ID430820_KingDomino;

create table if not exists Speler (
	gebruikersnaam varchar(255) not null,
    geboortejaar int not null,
    aantalGewonnen int,
    aantalGespeeld int
);