DELETE FROM ROZVRHOVA_AKCE;

ALTER TABLE ROZVRHOVA_AKCE
DROP CONSTRAINT ROZVRHOVA_AKCE_VYUCUJICI_FK;

ALTER TABLE ROZVRHOVA_AKCE
DROP CONSTRAINT SYS_C00144134;

ALTER TABLE ROZVRHOVA_AKCE DROP COLUMN VYUCUJICI_ID_VYUCUJICIHO;

DROP TABLE VYUCUJICI;

CREATE TABLE zamestnanec (
    id_zamestnanec            NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    jmeno                     VARCHAR2(25) NOT NULL,
    prijmeni                  VARCHAR2(30) NOT NULL,
    titul_pred                VARCHAR2(20),
    titul_za                  VARCHAR2(15),
    telefon                   VARCHAR2(13),
    mobil                     VARCHAR2(13),
    email                     VARCHAR2(255) NOT NULL,
    katedra_zkratka_katedry   VARCHAR2(10) NOT NULL,
    id_opravneni              NUMBER NOT NULL,
    id_role                   NUMBER NOT NULL
);

ALTER TABLE zamestnanec ADD CONSTRAINT zamestnanec_pk PRIMARY KEY ( id_zamestnanec );

CREATE TABLE role (
    id_role   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    typrole   VARCHAR2(40) NOT NULL
);

ALTER TABLE role ADD CONSTRAINT role_pk PRIMARY KEY ( id_role );

CREATE TABLE ucebna (
    id_ucebna   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    nazev       VARCHAR2(40) NOT NULL,
    kapacita    NUMBER NOT NULL
);

ALTER TABLE ucebna ADD CONSTRAINT ucebna_pk PRIMARY KEY ( id_ucebna );

CREATE TABLE udaje (
    id_udaje                     NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    uzivatelskejmeno             VARCHAR2(50) NOT NULL,
    heslo                        VARCHAR2(50) NOT NULL,
    id_zamestnanec   NUMBER NOT NULL
);

CREATE UNIQUE INDEX udaje__idx ON
    udaje (
        id_zamestnanec
    ASC );

ALTER TABLE udaje ADD CONSTRAINT udaje_pk PRIMARY KEY ( id_udaje );

CREATE TABLE data (
    id_data                      NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    obrazek                      BLOB NOT NULL,
    datumpridani                 DATE NOT NULL,
    datummodifikace              DATE NOT NULL,
    id_zamestnanec   NUMBER NOT NULL
);

CREATE UNIQUE INDEX data__idx ON
    data (
        id_zamestnanec
    ASC );

ALTER TABLE data ADD CONSTRAINT data_pk PRIMARY KEY ( id_data );

ALTER TABLE data
    ADD CONSTRAINT data_zamestnanec_fk FOREIGN KEY ( id_zamestnanec )
        REFERENCES zamestnanec ( id_zamestnanec );

ALTER TABLE udaje
    ADD CONSTRAINT udaje_zamestnanec_fk FOREIGN KEY ( id_zamestnanec )
        REFERENCES zamestnanec ( id_zamestnanec );

ALTER TABLE zamestnanec
    ADD CONSTRAINT zamestnanec_role_fk FOREIGN KEY ( id_role )
        REFERENCES role ( id_role );

ALTER TABLE zamestnanec
    ADD CONSTRAINT zamestnanec_katedra_fk FOREIGN KEY ( katedra_zkratka_katedry )
        REFERENCES katedra ( zkratka_katedry );

ALTER TABLE ROZVRHOVA_AKCE
  ADD (id_zamestnanec   NUMBER NOT NULL,
    id_ucebna             NUMBER NOT NULL,
    konciv                       TIMESTAMP NOT NULL);

ALTER TABLE rozvrhova_akce
    ADD CONSTRAINT rozvrhova_akce_ucebna_fk FOREIGN KEY ( id_ucebna )
        REFERENCES ucebna ( id_ucebna );

ALTER TABLE rozvrhova_akce
    ADD CONSTRAINT rozvrhova_akce_zamestnanec_fk FOREIGN KEY ( id_zamestnanec )
        REFERENCES zamestnanec ( id_zamestnanec );
		
		
ALTER TABLE ZAMESTNANEC DROP COLUMN TELEFON;    
ALTER TABLE ZAMESTNANEC DROP COLUMN MOBIL; 
ALTER TABLE ZAMESTNANEC ADD (MOBIL NUMBER, TELEFON NUMBER);

/*ALTER TABLE ZAMESTNANEC ADD (id_opravneni number not null);*/
/*drop table role;
drop table ucebna;
drop table data;
drop table udaje;
drop table zamestnanec;*/

/*
CREATE TABLE zamestnanec (
    id_zamestnanec            NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    jmeno                     VARCHAR2(25) NOT NULL,
    prijmeni                  VARCHAR2(30) NOT NULL,
    titul_pred                VARCHAR2(20),
    titul_za                  VARCHAR2(15),
    telefon                   NUMBER,
    mobil                     NUMBER,
    email                     VARCHAR2(255) NOT NULL,
    katedra_zkratka_katedry   VARCHAR2(10) NOT NULL,
    id_opravneni              NUMBER NOT NULL,
    id_role                   NUMBER NOT NULL
);

ALTER TABLE zamestnanec ADD CONSTRAINT zamestnanec_pk PRIMARY KEY ( id_zamestnanec );

CREATE TABLE role (
    id_role   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    typrole   VARCHAR2(40) NOT NULL
);

ALTER TABLE role ADD CONSTRAINT role_pk PRIMARY KEY ( id_role );

CREATE TABLE ucebna (
    id_ucebna   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    nazev       VARCHAR2(40) NOT NULL,
    kapacita    NUMBER NOT NULL
);

ALTER TABLE ucebna ADD CONSTRAINT ucebna_pk PRIMARY KEY ( id_ucebna );

CREATE TABLE udaje (
    id_udaje                     NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    uzivatelskejmeno             VARCHAR2(50) NOT NULL,
    heslo                        VARCHAR2(50) NOT NULL,
    id_zamestnanec   NUMBER NOT NULL
);

CREATE UNIQUE INDEX udaje__idx ON
    udaje (
        id_zamestnanec
    ASC );

ALTER TABLE udaje ADD CONSTRAINT udaje_pk PRIMARY KEY ( id_udaje );

CREATE TABLE data (
    id_data                      NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    obrazek                      BLOB NOT NULL,
    datumpridani                 DATE NOT NULL,
    datummodifikace              DATE NOT NULL,
    id_zamestnanec   NUMBER NOT NULL
);

CREATE UNIQUE INDEX data__idx ON
    data (
        id_zamestnanec
    ASC );

ALTER TABLE data ADD CONSTRAINT data_pk PRIMARY KEY ( id_data );

ALTER TABLE data
    ADD CONSTRAINT data_zamestnanec_fk FOREIGN KEY ( id_zamestnanec )
        REFERENCES zamestnanec ( id_zamestnanec );

ALTER TABLE udaje
    ADD CONSTRAINT udaje_zamestnanec_fk FOREIGN KEY ( id_zamestnanec )
        REFERENCES zamestnanec ( id_zamestnanec );

ALTER TABLE zamestnanec
    ADD CONSTRAINT zamestnanec_role_fk FOREIGN KEY ( id_role )
        REFERENCES role ( id_role );

ALTER TABLE zamestnanec
    ADD CONSTRAINT zamestnanec_katedra_fk FOREIGN KEY ( katedra_zkratka_katedry )
        REFERENCES katedra ( zkratka_katedry );
*/

CREATE TABLE opravneni (
    id_opravneni   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    opravneni      VARCHAR2(40) NOT NULL
);

ALTER TABLE opravneni ADD CONSTRAINT opravneni_pk PRIMARY KEY ( id_opravneni );

ALTER TABLE zamestnanec
    ADD CONSTRAINT zamestnanec_opravneni_fk FOREIGN KEY ( id_opravneni )
        REFERENCES opravneni ( id_opravneni );
		
Insert into OPRAVNENI (opravneni) values ('Administrátor');
Insert into OPRAVNENI (opravneni) values ('Registrovaný');
Insert into OPRAVNENI (opravneni) values ('Neregistrovaný');
insert into ROLE (typrole) values ('Vyučující');

Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Jan','Fikejz','Ing.',null,null,null,'jan.fikejz@upce.cz','KST',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Filip','Majerík','Ing.',null,null,null,'st40429@student.upce.cz','KST',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Lubomír','Andrle','Ing.',null,null,null,'lubomir.andrle@upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Paul Charles','Hooper',null,null,466036710,null,'paul.hooper@upce.cz','FEI',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Michal','Horáček','Ing.',null,null,null,'michal.horacek.czech@gmail.com','KE',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Pavel','Horák','JUDr.',null,null,null,'pavel.horak@upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Josef','Horálek','Mgr.','Ph.D.',466036488,null,'josef.horalek@upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Klára','Hrůzová','Mgr.','Ph.D.',null,null,'Klara.Hruzova@upce.cz','KMF',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Jan','Hřídel','Ing.',null,null,null,'jan.hridel@upce.cz','KST',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Tomáš','Hudec','Mgr.',null,null,null,'tomas.hudec@upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Veronika','Hurtová','Ing.',null,null,null,'veronika.hurtova@student.upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Pavel','Chmelař','Ing.',null,null,null,'st19544@student.upce.cz','KE',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Dana','Jablonská',null,null,null,null,'dana.jablonska@upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));
Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Jiří','Lebduška','Ing.',null,null,null,'jiri.lebduska@student.upce.cz','KIT',(select id_opravneni from opravneni where opravneni = 'Registrovaný'),(select id_role from role where typrole = 'Vyučující'));

insert into ROLE (typrole) values ('Správce systému');

Insert into ZAMESTNANEC (JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY,ID_OPRAVNENI,ID_ROLE) values ('Jiří','Zůstavec','Ing.',null,null,null,'zustavec.jiri@upce.cz','FEI',(select id_opravneni from opravneni where opravneni = 'Administrátor'),(select id_role from role where typrole = 'Správce systému')); 

create or replace view ROZVRHOVE_AKCE_EXT_VIEW as
	select r.ID_RA as ID_ROZVRHOVE_AKCE, r.POCET_STUDENTU, r.MAHODIN, r.ZACINAV, r.ROLE_VYUCUJICIHO_ROLE, r.PREDMET_ZKRATKA_PREDMETU as ZKRATKA_PREDMETU, r.ID_ZAMESTNANEC as ID_VYUCUJICIHO, p.NAZEV_PREDMETU, p.DOPORUCENY_ROCNIK,v.ZPUSOB, z.JMENO as JMENO_VYUCUJICIHO, z.PRIJMENI as PRIJMENI_VYUCUJICIHO, z.TITUL_PRED, z.TITUL_ZA, z.TELEFON, z.MOBIL, z.EMAIL, z.KATEDRA_ZKRATKA_KATEDRY, u.nazev as NAZEV_UCEBNY, u.kapacita as KAPACITA_UCEBNY from ROZVRHOVA_AKCE r
		inner join PREDMET p on r.PREDMET_ZKRATKA_PREDMETU = p.ZKRATKA_PREDMETU
		left join ZPUSOB_VYUKY v on r.ZPUSOB_VYUKY_ID_ZV = v.ID_ZV
		left join ZAMESTNANEC z on r.ID_ZAMESTNANEC = z.ID_ZAMESTNANEC
		left join UCEBNA u on r.ID_UCEBNA = u.ID_UCEBNA;
		
CREATE OR REPLACE VIEW VYUC_VIEW AS 
  select ID_ZAMESTNANEC,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY, OPRAVNENI from zamestnanec
  left join OPRAVNENI on ZAMESTNANEC.ID_OPRAVNENI = OPRAVNENI.ID_OPRAVNENI
  left join ROLE on ZAMESTNANEC.ID_ROLE = ROLE.ID_ROLE
  where typrole = 'Vyučující';
  
CREATE OR REPLACE VIEW ZAM_VIEW AS 
  select ID_ZAMESTNANEC,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY, OPRAVNENI, typrole from zamestnanec
  left join OPRAVNENI on ZAMESTNANEC.ID_OPRAVNENI = OPRAVNENI.ID_OPRAVNENI
  left join ROLE on ZAMESTNANEC.ID_ROLE = ROLE.ID_ROLE;