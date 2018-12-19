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
    id_ucebna             NUMBER NOT NULL);

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
insert into ROLE (typrole) values ('Správce systému');
		   
ALTER TABLE ROZVRHOVA_AKCE ADD (SCHVALENO NUMBER(1) default(0) NOT NULL);

declare
    v_opravneni number(1);
    v_role      number(1);
begin
    --vyučující
    select id_opravneni into v_opravneni from opravneni where opravneni = 'Registrovaný';
    select id_role into v_role from role where typrole = 'Vyučující';
    VLOZZAMESTNANCE('Jan','Fikejz','Ing.',null,'jan.fikejz@upce.cz','KST',v_opravneni,v_role,null,null,'fiky1','fiky1');
    VLOZZAMESTNANCE('Filip','Majerik','Ing.',null,'st40429@student.upce.cz','KST',v_opravneni,v_role,null,null,'reg','reg');
    VLOZZAMESTNANCE('Lubomír','Andrle','Ing.',null,'lubomir.andrle@upce.cz','KIT',v_opravneni,v_role,null,null,'andrle','lubomir');
    VLOZZAMESTNANCE('Paul Charles','Hooper',null,null,'paul.hooper@upce.cz','FEI',v_opravneni,v_role,466036710,null,'hooper','looper');
    VLOZZAMESTNANCE('Michal','Horáček','Ing.',null,'michal.horacek.czech@gmail.com','KE',v_opravneni,v_role,null,null,'horacek','prezident');
    VLOZZAMESTNANCE('Pavel','Horák','JUDr.',null,'pavel.horak@upce.cz','KIT',v_opravneni,v_role,null,null,'horak.pavel','zubr');
    VLOZZAMESTNANCE('Josef','Horálek','Mgr.','Ph.D.','josef.horalek@upce.cz','KIT',v_opravneni,v_role,466036488,null,'josef.horalek','Sierra');
    VLOZZAMESTNANCE('Klára','Hrůzová','Mgr.','Ph.D.','Klara.Hruzova@upce.cz','KMF',v_opravneni,v_role,null,null,'hruzova','Hotel');
    VLOZZAMESTNANCE('Jan','Hřídel','Ing.',null,'jan.hridel@upce.cz','KST',v_opravneni,v_role,null,null,'jan.hridel','India');
    VLOZZAMESTNANCE('Tomáš','Hudec','Mgr.',null,'tomas.hudec@upce.cz','KIT',v_opravneni,v_role,null,null,'tomas.hudec','Echo');
    VLOZZAMESTNANCE('Veronika','Hurtová','Ing.',null,'veronika.hurtova@student.upce.cz','KIT',v_opravneni,v_role,null,null,'veronika.h','Lima');
    VLOZZAMESTNANCE('Pavel','Chmelař','Ing.',null,'st19544@student.upce.cz','KE',v_opravneni,v_role,null,null,'st19544','Delta');
    VLOZZAMESTNANCE('Dana','Jablonská',null,null,'dana.jablonska@upce.cz','KIT',v_opravneni,v_role,null,null,'jablonska','SHIELD');
    VLOZZAMESTNANCE('Jiří','Lebduška',null,null,'jiri.lebduska@student.upce.cz','KIT',v_opravneni,v_role,null,null,'lebduska','heslo');
    VLOZZAMESTNANCE('Jiří','Kysela','Ing.','Ph.D.','jiri.kysela@upce.cz','KIT',v_opravneni,v_role,null,null,null,null);
    --zamestnanci
    select id_opravneni into v_opravneni from opravneni where opravneni = 'Administrátor';
    select id_role into v_role from role where typrole = 'Správce systému';
    VLOZZAMESTNANCE('Jiří','Zůstavec',null,null,'zustavec.jiri@upce.cz','FEI',v_opravneni,v_role,null,null,'admin','admin');
end;

CREATE OR REPLACE VIEW VYUC_VIEW AS 
  select z.ID_ZAMESTNANEC as ID_VYUCUJICIHO,z.JMENO,z.PRIJMENI,z.TITUL_PRED,z.TITUL_ZA,z.TELEFON,z.MOBIL,z.EMAIL,z.KATEDRA_ZKRATKA_KATEDRY, o.OPRAVNENI, d.OBRAZEK, d.DATUMPRIDANI, d.DATUMMODIFIKACE from zamestnanec z
  left join OPRAVNENI o on z.ID_OPRAVNENI = o.ID_OPRAVNENI
  left join ROLE r on z.ID_ROLE = r.ID_ROLE
  left join DATA d on z.ID_ZAMESTNANEC = d.ID_ZAMESTNANEC
  where r.typrole = 'Vyučující';
  
CREATE OR REPLACE VIEW ZAM_VIEW AS 
  select z.ID_ZAMESTNANEC,z.JMENO,z.PRIJMENI,z.TITUL_PRED,z.TITUL_ZA,z.TELEFON,z.MOBIL,z.EMAIL,z.KATEDRA_ZKRATKA_KATEDRY, o.OPRAVNENI, r.TYPROLE, d.OBRAZEK, d.DATUMPRIDANI, d.DATUMMODIFIKACE from zamestnanec z
  left join OPRAVNENI o on z.ID_OPRAVNENI = o.ID_OPRAVNENI
  left join ROLE r on z.ID_ROLE = r.ID_ROLE
  left join DATA d on z.ID_ZAMESTNANEC = d.ID_ZAMESTNANEC;
  
ALTER TABLE udaje
ADD CONSTRAINT prihlasovacijmeno_unique UNIQUE (uzivatelskejmeno); --musí být jedinečné

ALTER TABLE ucebna
ADD CONSTRAINT ucebnanazev_unique UNIQUE (nazev); --musí být jedinečné
		
ALTER TABLE ROZVRHOVA_AKCE
MODIFY ZACINAV NUMBER(5,3); 

ALTER TABLE ROZVRHOVA_AKCE
MODIFY MAHODIN NUMBER(5,3); 

create table DNYVTYDNU(
    DEN VARCHAR2(10) NOT NULL,
	PRIORITA NUMBER(1) DEFAULT(1) NOT NULL,
    CONSTRAINT den_pk PRIMARY KEY (den)
);

insert into DNYVTYDNU values('Pondělí', 1);
insert into DNYVTYDNU values('Úterý', 2);
insert into DNYVTYDNU values('Středa', 3);
insert into DNYVTYDNU values('Čtvrtek', 4);
insert into DNYVTYDNU values('Pátek', 5);
insert into DNYVTYDNU values('Sobota', 6);
insert into DNYVTYDNU values('Neděle', 7);

alter table ROZVRHOVA_AKCE
    ADD DENVTYDNU VARCHAR2(10) DEFAULT('Pondělí') NOT NULL; 
    
alter table ROZVRHOVA_AKCE
    ADD CONSTRAINT DEN_FK FOREIGN KEY (DENVTYDNU) REFERENCES DNYVTYDNU(DEN);

create or replace view ROZVRHOVE_AKCE_EXT_VIEW as
	select r.ID_RA as ID_ROZVRHOVE_AKCE, r.POCET_STUDENTU, r.DENVTYDNU, r.SCHVALENO, r.MAHODIN, r.ZACINAV, r.ROLE_VYUCUJICIHO_ROLE, r.PREDMET_ZKRATKA_PREDMETU as ZKRATKA_PREDMETU, r.ID_ZAMESTNANEC as ID_VYUCUJICIHO, p.NAZEV_PREDMETU, p.DOPORUCENY_ROCNIK, v.ZPUSOB, v.ID_ZV as ID_ZPUSOBU, z.JMENO as JMENO_VYUCUJICIHO, z.PRIJMENI as PRIJMENI_VYUCUJICIHO, z.TITUL_PRED, z.TITUL_ZA, z.TELEFON, z.MOBIL, z.EMAIL, z.KATEDRA_ZKRATKA_KATEDRY, u.nazev as NAZEV_UCEBNY, u.kapacita as KAPACITA_UCEBNY, u.ID_UCEBNA as ID_UCEBNY, s.sem, s.id_semestr from ROZVRHOVA_AKCE r
		inner join PREDMET p on r.PREDMET_ZKRATKA_PREDMETU = p.ZKRATKA_PREDMETU
		left join SEMESTR s on p.semestr_id_semestr = s.id_semestr
		left join ZPUSOB_VYUKY v on r.ZPUSOB_VYUKY_ID_ZV = v.ID_ZV
		left join ZAMESTNANEC z on r.ID_ZAMESTNANEC = z.ID_ZAMESTNANEC
		left join UCEBNA u on r.ID_UCEBNA = u.ID_UCEBNA;
		
ALTER TABLE zamestnanec
ADD CONSTRAINT zamestnanecemail_unique UNIQUE (email); --musí být jedinečné