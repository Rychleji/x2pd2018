drop table "ZPUSOB_ZAKONCENI" cascade constraints;
drop table "ZPUSOB_VYUKY" cascade constraints;
drop table "VYUCUJICI" cascade constraints;
drop table "SEMESTR" cascade constraints;
drop table "ROZVRHOVA_AKCE" cascade constraints;
drop table "ROLE_VYUCUJICIHO" cascade constraints;
drop table "PREDMET" cascade constraints;
drop table "OBOR" cascade constraints;
drop table "OBOR_PREDMET" cascade constraints;
drop table "KATEGORIE_PREDMETU" cascade constraints;
drop table "KATEDRA" cascade constraints;
drop table "FORMA_VYUKY" cascade constraints;
drop table "FAKULTA" cascade constraints;

CREATE TABLE fakulta (
    zkratka_fakulty   VARCHAR2(10) NOT NULL,
    nazev_fakulty     VARCHAR2(60) NOT NULL
);

ALTER TABLE fakulta ADD CONSTRAINT fakulta_pk PRIMARY KEY ( zkratka_fakulty );

CREATE TABLE forma_vyuky (
    id_fv   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    forma   VARCHAR2(30) NOT NULL
);

ALTER TABLE forma_vyuky ADD CONSTRAINT forma_vyuky_pk PRIMARY KEY ( id_fv );

CREATE TABLE katedra (
    zkratka_katedry           VARCHAR2(10) NOT NULL,
    nazev_katedry             VARCHAR2(60) NOT NULL,
    fakulta_zkratka_fakulty   VARCHAR2(10) NOT NULL
);

ALTER TABLE katedra ADD CONSTRAINT katedra_pk PRIMARY KEY ( zkratka_katedry );

CREATE TABLE kategorie_predmetu (
    kategorie   VARCHAR2(1) NOT NULL
);

ALTER TABLE kategorie_predmetu ADD CONSTRAINT kategorie_predmetu_pk PRIMARY KEY ( kategorie );

CREATE TABLE obor (
    zkratka_oboru             VARCHAR2(8) NOT NULL,
    nazev_oboru               VARCHAR2(50) NOT NULL,
    fakulta_zkratka_fakulty   VARCHAR2(10) NOT NULL
);

ALTER TABLE obor ADD CONSTRAINT obor_pk PRIMARY KEY ( zkratka_oboru );

CREATE TABLE obor_predmet (
    obor_zkratka_oboru             VARCHAR2(8) NOT NULL,
    predmet_zkratka_predmetu       VARCHAR2(8) NOT NULL,
    kategorie_predmetu_kategorie   VARCHAR2(1) NOT NULL
);

ALTER TABLE obor_predmet ADD CONSTRAINT obor_predmet_pk PRIMARY KEY ( obor_zkratka_oboru,
predmet_zkratka_predmetu );

CREATE TABLE predmet (
    zkratka_predmetu         VARCHAR2(8) NOT NULL,
    nazev_predmetu           VARCHAR2(255) NOT NULL,
    rozsah_hodin             NUMBER NOT NULL,
    doporuceny_rocnik        NUMBER,
    semestr_id_semestr       NUMBER NOT NULL,
    zpusob_zakonceni_id_zz   NUMBER NOT NULL,
    forma_vyuky_id_fv        NUMBER NOT NULL
);

ALTER TABLE predmet ADD CONSTRAINT predmet_pk PRIMARY KEY ( zkratka_predmetu );

CREATE TABLE role_vyucujiciho (
    role   VARCHAR2(40) NOT NULL
);

ALTER TABLE role_vyucujiciho ADD CONSTRAINT role_vyucujiciho_pk PRIMARY KEY ( role );

CREATE TABLE rozvrhova_akce (
    id_ra                      NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    pocet_studentu             NUMBER NOT NULL,
    mahodin                    NUMBER NOT NULL,
    zacinav                    NUMBER NOT NULL,
    predmet_zkratka_predmetu   VARCHAR2(8) NOT NULL,
    zpusob_vyuky_id_zv         NUMBER NOT NULL,
    role_vyucujiciho_role      VARCHAR2(40) NOT NULL,
    vyucujici_id_vyucujiciho   VARCHAR2(7) NOT NULL
);

ALTER TABLE rozvrhova_akce ADD CONSTRAINT rozvrhova_akce_pk PRIMARY KEY ( id_ra );

CREATE TABLE semestr (
    id_semestr   NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    sem          VARCHAR2(30) NOT NULL
);

ALTER TABLE semestr ADD CONSTRAINT semestr_pk PRIMARY KEY ( id_semestr );

CREATE TABLE vyucujici (
    id_vyucujiciho            VARCHAR2(7) NOT NULL,
    jmeno                     VARCHAR2(25) NOT NULL,
    prijmeni                  VARCHAR2(30) NOT NULL,
    titul_pred                VARCHAR2(20),
    titul_za                  VARCHAR2(15),
    telefon                   VARCHAR2(13),
    mobil                     VARCHAR2(13),
    email                     VARCHAR2(255) NOT NULL,
    katedra_zkratka_katedry   VARCHAR2(10) NOT NULL
);

ALTER TABLE vyucujici ADD CONSTRAINT vyucujici_pk PRIMARY KEY ( id_vyucujiciho );

CREATE TABLE zpusob_vyuky (
    id_zv    NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    zpusob   VARCHAR2(30) NOT NULL
);

ALTER TABLE zpusob_vyuky ADD CONSTRAINT zpusob_vyuky_pk PRIMARY KEY ( id_zv );

CREATE TABLE zpusob_zakonceni (
    id_zz        NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    zpusob_zak   VARCHAR2(20) NOT NULL
);

ALTER TABLE zpusob_zakonceni ADD CONSTRAINT zpusob_zakonceni_pk PRIMARY KEY ( id_zz );

ALTER TABLE katedra
    ADD CONSTRAINT katedra_fakulta_fk FOREIGN KEY ( fakulta_zkratka_fakulty )
        REFERENCES fakulta ( zkratka_fakulty );

ALTER TABLE obor
    ADD CONSTRAINT obor_fakulta_fk FOREIGN KEY ( fakulta_zkratka_fakulty )
        REFERENCES fakulta ( zkratka_fakulty );

ALTER TABLE obor_predmet
    ADD CONSTRAINT obor_predmet_kat_pred_fk FOREIGN KEY ( kategorie_predmetu_kategorie )
        REFERENCES kategorie_predmetu ( kategorie );

ALTER TABLE obor_predmet
    ADD CONSTRAINT obor_predmet_obor_fk FOREIGN KEY ( obor_zkratka_oboru )
        REFERENCES obor ( zkratka_oboru );

ALTER TABLE obor_predmet
    ADD CONSTRAINT obor_predmet_predmet_fk FOREIGN KEY ( predmet_zkratka_predmetu )
        REFERENCES predmet ( zkratka_predmetu );

ALTER TABLE predmet
    ADD CONSTRAINT predmet_forma_vyuky_fk FOREIGN KEY ( forma_vyuky_id_fv )
        REFERENCES forma_vyuky ( id_fv );

ALTER TABLE predmet
    ADD CONSTRAINT predmet_semestr_fk FOREIGN KEY ( semestr_id_semestr )
        REFERENCES semestr ( id_semestr );

ALTER TABLE predmet
    ADD CONSTRAINT predmet_zpusob_zakonceni_fk FOREIGN KEY ( zpusob_zakonceni_id_zz )
        REFERENCES zpusob_zakonceni ( id_zz );

ALTER TABLE rozvrhova_akce
    ADD CONSTRAINT rozvrhova_akce_predmet_fk FOREIGN KEY ( predmet_zkratka_predmetu )
        REFERENCES predmet ( zkratka_predmetu );

ALTER TABLE rozvrhova_akce
    ADD CONSTRAINT rozvrh_akce_role_vyuc_fk FOREIGN KEY ( role_vyucujiciho_role )
        REFERENCES role_vyucujiciho ( role );

ALTER TABLE rozvrhova_akce
    ADD CONSTRAINT rozvrhova_akce_vyucujici_fk FOREIGN KEY ( vyucujici_id_vyucujiciho )
        REFERENCES vyucujici ( id_vyucujiciho );

ALTER TABLE rozvrhova_akce
    ADD CONSTRAINT rozvrhova_akce_zpusob_vyuky_fk FOREIGN KEY ( zpusob_vyuky_id_zv )
        REFERENCES zpusob_vyuky ( id_zv );

ALTER TABLE vyucujici
    ADD CONSTRAINT vyucujici_katedra_fk FOREIGN KEY ( katedra_zkratka_katedry )
        REFERENCES katedra ( zkratka_katedry );
        
INSERT INTO FAKULTA VALUES ('FEI', 'Fakulta elektrotechniky a informatiky');

Insert into KATEDRA (ZKRATKA_KATEDRY,NAZEV_KATEDRY,FAKULTA_ZKRATKA_FAKULTY) values ('KE','elektrotechniky','FEI');
Insert into KATEDRA (ZKRATKA_KATEDRY,NAZEV_KATEDRY,FAKULTA_ZKRATKA_FAKULTY) values ('KIT','informaèních technologií','FEI');
Insert into KATEDRA (ZKRATKA_KATEDRY,NAZEV_KATEDRY,FAKULTA_ZKRATKA_FAKULTY) values ('KMF','matematiky a fyziky','FEI');
Insert into KATEDRA (ZKRATKA_KATEDRY,NAZEV_KATEDRY,FAKULTA_ZKRATKA_FAKULTY) values ('KRP','øízení procesù','FEI');
Insert into KATEDRA (ZKRATKA_KATEDRY,NAZEV_KATEDRY,FAKULTA_ZKRATKA_FAKULTY) values ('KST','softwarových technologií','FEI');
Insert into KATEDRA (ZKRATKA_KATEDRY,NAZEV_KATEDRY,FAKULTA_ZKRATKA_FAKULTY) values ('FEI','Fakulta elektrotechniky a informatiky','FEI');

Insert into FORMA_VYUKY (FORMA) values ('Prezenèní');
Insert into FORMA_VYUKY (FORMA) values ('Kombinovaná');
Insert into FORMA_VYUKY (FORMA) values ('Distanèní');

Insert into ZPUSOB_VYUKY (ZPUSOB) values ('Cvièení');
Insert into ZPUSOB_VYUKY (ZPUSOB) values ('Semináø');
Insert into ZPUSOB_VYUKY (ZPUSOB) values ('Pøednáška');
Insert into ZPUSOB_VYUKY (ZPUSOB) values ('Jiné');

Insert into ZPUSOB_ZAKONCENI (ZPUSOB_ZAK) values ('Zkouškou');
Insert into ZPUSOB_ZAKONCENI (ZPUSOB_ZAK) values ('Zápoètem');

Insert into KATEGORIE_PREDMETU (KATEGORIE) values ('A');
Insert into KATEGORIE_PREDMETU (KATEGORIE) values ('B');
Insert into KATEGORIE_PREDMETU (KATEGORIE) values ('C');

Insert into OBOR (ZKRATKA_OBORU,NAZEV_OBORU,FAKULTA_ZKRATKA_FAKULTY) values ('IT','Informaèní technologie','FEI');
Insert into OBOR (ZKRATKA_OBORU,NAZEV_OBORU,FAKULTA_ZKRATKA_FAKULTY) values ('RP','Øízení procesù','FEI');
Insert into OBOR (ZKRATKA_OBORU,NAZEV_OBORU,FAKULTA_ZKRATKA_FAKULTY) values ('KMT','Komunikaèní a mikroprocesorová technika','FEI');

Insert into ROLE_VYUCUJICIHO values ('Cvièící');
Insert into ROLE_VYUCUJICIHO values ('Pøenášející');

Insert into SEMESTR (SEM) values ('Letní');
Insert into SEMESTR (SEM) values ('Zimní');
Insert into SEMESTR (SEM) values ('Oba');
      
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('1234','Jan','Fikejz','Ing.',null,'je',null,'jan.fikejz@upce.cz','KST');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('8579','Filip','Majerík','Ing.',null,null,null,'st40429@student.upce.cz','KST');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('6144','Lubomír','Andrle','Ing.',null,null,null,'lubomir.andrle@upce.cz','KIT');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('1982','Paul Charles','Hooper',null,null,'46 603 6710',null,'paul.hooper@upce.cz','FEI');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('8213','Michal','Horáèek','Ing.',null,null,null,'michal.horacek.czech@gmail.com','KE');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('1055','Pavel','Horák','JUDr.',null,null,null,'pavel.horak@upce.cz','KIT');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('2559','Josef','Horálek','Mgr.','Ph.D.','466 036 488',null,'josef.horalek@upce.cz','KIT');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('8142','Klára','Hrùzová','Mgr.','Ph.D.',null,null,'Klara.Hruzova@upce.cz','KMF');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('3546','Jan','Høídel','Ing.',null,null,null,'jan.hridel@upce.cz','KST');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('353','Tomáš','Hudec','Mgr.',null,null,null,'tomas.hudec@upce.cz','KIT');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('7736','Veronika','Hurtová','Ing.',null,null,null,'veronika.hurtova@student.upce.cz','KIT');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('5662','Pavel','Chmelaø','Ing.',null,null,null,'st19544@student.upce.cz','KE');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('4095','Dana','Jablonská',null,null,null,null,'dana.jablonska@upce.cz','KIT');
Insert into VYUCUJICI (ID_VYUCUJICIHO,JMENO,PRIJMENI,TITUL_PRED,TITUL_ZA,TELEFON,MOBIL,EMAIL,KATEDRA_ZKRATKA_KATEDRY) values ('4583','Jiøí','Lebduška','Ing.',null,null,null,'jiri.lebduska@student.upce.cz','KIT');      
       
Insert into PREDMET (ZKRATKA_PREDMETU,NAZEV_PREDMETU,ROZSAH_HODIN,DOPORUCENY_ROCNIK,SEMESTR_ID_SEMESTR,ZPUSOB_ZAKONCENI_ID_ZZ,FORMA_VYUKY_ID_FV) values ('IDAS1','Databázové systémy 1','0','2','1','1','1');
Insert into PREDMET (ZKRATKA_PREDMETU,NAZEV_PREDMETU,ROZSAH_HODIN,DOPORUCENY_ROCNIK,SEMESTR_ID_SEMESTR,ZPUSOB_ZAKONCENI_ID_ZZ,FORMA_VYUKY_ID_FV) values ('IDATS','Datové struktury','0','2','1','1','1');
Insert into PREDMET (ZKRATKA_PREDMETU,NAZEV_PREDMETU,ROZSAH_HODIN,DOPORUCENY_ROCNIK,SEMESTR_ID_SEMESTR,ZPUSOB_ZAKONCENI_ID_ZZ,FORMA_VYUKY_ID_FV) values ('IMSW','Modelování ve výpoètových softwarech','0','2','1','1','1');

Insert into OBOR_PREDMET (OBOR_ZKRATKA_OBORU,PREDMET_ZKRATKA_PREDMETU,KATEGORIE_PREDMETU_KATEGORIE) values ('IT','IDAS1','A');

Insert into ROZVRHOVA_AKCE (POCET_STUDENTU,MAHODIN,ZACINAV,PREDMET_ZKRATKA_PREDMETU,ZPUSOB_VYUKY_ID_ZV,ROLE_VYUCUJICIHO_ROLE,VYUCUJICI_ID_VYUCUJICIHO) values ('20','2','17','IDAS1','1','Cvièící','8579');
Insert into ROZVRHOVA_AKCE (POCET_STUDENTU,MAHODIN,ZACINAV,PREDMET_ZKRATKA_PREDMETU,ZPUSOB_VYUKY_ID_ZV,ROLE_VYUCUJICIHO_ROLE,VYUCUJICI_ID_VYUCUJICIHO) values ('20','2','13','IDATS','1','Cvièící','1234');
Insert into ROZVRHOVA_AKCE (POCET_STUDENTU,MAHODIN,ZACINAV,PREDMET_ZKRATKA_PREDMETU,ZPUSOB_VYUKY_ID_ZV,ROLE_VYUCUJICIHO_ROLE,VYUCUJICI_ID_VYUCUJICIHO) values ('12','2','9','IDATS','1','Cvièící','1234');

/*select * from VYUCUJICI 
    left join KATEDRA on VYUCUJICI.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY
    left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY;
    
select * from rozvrhova_akce
    inner join VYUCUJICI on ROZVRHOVA_AKCE.VYUCUJICI_ID_VYUCUJICIHO = VYUCUJICI.ID_VYUCUJICIHO
    left join KATEDRA on VYUCUJICI.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY
    left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY
    inner join PREDMET on ROZVRHOVA_AKCE.PREDMET_ZKRATKA_PREDMETU = PREDMET.ZKRATKA_PREDMETU;
    
select * from KATEDRA
    left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY;*/
    
create or replace view PRACOVISTE as 
    select * from KATEDRA
        left join FAKULTA on FAKULTA.ZKRATKA_FAKULTY = KATEDRA.FAKULTA_ZKRATKA_FAKULTY;
        
create or replace view PREDMET_EXT_VIEW as        
    select ZKRATKA_PREDMETU, NAZEV_PREDMETU, DOPORUCENY_ROCNIK, SEMESTR_ID_SEMESTR, ZPUSOB_ZAKONCENI_ID_ZZ, FORMA_VYUKY_ID_FV, SEM, ZPUSOB_ZAK, FORMA, SUM(POCET_STUDENTU) as POCET_STUDENTU from PREDMET
        left join SEMESTR on PREDMET.SEMESTR_ID_SEMESTR = SEMESTR.ID_SEMESTR 
        left join ZPUSOB_ZAKONCENI ON PREDMET.ZPUSOB_ZAKONCENI_ID_ZZ = ZPUSOB_ZAKONCENI.ID_ZZ
        left join FORMA_VYUKY on PREDMET.FORMA_VYUKY_ID_FV = FORMA_VYUKY.ID_FV
        left join ROZVRHOVA_AKCE on PREDMET.ZKRATKA_PREDMETU = ROZVRHOVA_AKCE.PREDMET_ZKRATKA_PREDMETU group by ZKRATKA_PREDMETU, NAZEV_PREDMETU, DOPORUCENY_ROCNIK, SEMESTR_ID_SEMESTR, ZPUSOB_ZAKONCENI_ID_ZZ, 
FORMA_VYUKY_ID_FV, SEM, ZPUSOB_ZAK, FORMA;

create or replace view SPOJENI_OBORU_PREDMETU_VIEW as        
    select ZKRATKA_OBORU, NAZEV_OBORU, ZKRATKA_PREDMETU, NAZEV_PREDMETU, KATEGORIE_PREDMETU_KATEGORIE, DOPORUCENY_ROCNIK, SEM, ZPUSOB_ZAK, FORMA from OBOR_PREDMET
        inner join PREDMET on OBOR_PREDMET.PREDMET_ZKRATKA_PREDMETU = PREDMET.ZKRATKA_PREDMETU
        inner join OBOR on OBOR_PREDMET.OBOR_ZKRATKA_OBORU = OBOR.ZKRATKA_OBORU
        left join SEMESTR on PREDMET.SEMESTR_ID_SEMESTR = SEMESTR.ID_SEMESTR 
        left join ZPUSOB_ZAKONCENI ON PREDMET.ZPUSOB_ZAKONCENI_ID_ZZ = ZPUSOB_ZAKONCENI.ID_ZZ
        left join FORMA_VYUKY on PREDMET.FORMA_VYUKY_ID_FV = FORMA_VYUKY.ID_FV;
        
create or replace view ROZVRHOVE_AKCE_EXT_VIEW as
    select ID_RA as ID_ROZVRHOVE_AKCE, POCET_STUDENTU, MAHODIN, ZACINAV, ROLE_VYUCUJICIHO_ROLE, PREDMET_ZKRATKA_PREDMETU as ZKRATKA_PREDMETU, VYUCUJICI_ID_VYUCUJICIHO as ID_VYUCUJICIHO, NAZEV_PREDMETU, DOPORUCENY_ROCNIK, ZPUSOB, JMENO as JMENO_VYUCUJICIHO, PRIJMENI as PRIJMENI_VYUCUJICIHO, TITUL_PRED, TITUL_ZA, TELEFON, MOBIL, EMAIL, KATEDRA_ZKRATKA_KATEDRY from ROZVRHOVA_AKCE
        inner join PREDMET on ROZVRHOVA_AKCE.PREDMET_ZKRATKA_PREDMETU = PREDMET.ZKRATKA_PREDMETU
        left join ZPUSOB_VYUKY on ROZVRHOVA_AKCE.ZPUSOB_VYUKY_ID_ZV = ZPUSOB_VYUKY.ID_ZV
        left join VYUCUJICI on ROZVRHOVA_AKCE.VYUCUJICI_ID_VYUCUJICIHO = VYUCUJICI.ID_VYUCUJICIHO;
        
CREATE OR REPLACE FORCE EDITIONABLE VIEW "C##ST47130"."VYUC_VIEW" ("ID_VYUCUJICIHO", "JMENO", "PRIJMENI", "TITUL_PRED", "TITUL_ZA", "TELEFON", "MOBIL", "EMAIL", "KATEDRA_ZKRATKA_KATEDRY") AS 
  select "ID_VYUCUJICIHO","JMENO","PRIJMENI","TITUL_PRED","TITUL_ZA","TELEFON","MOBIL","EMAIL","KATEDRA_ZKRATKA_KATEDRY" from vyucujici;
  
--insert into FAKULTA values ('FEI', 'Fakulta elektrotechniky a informatiky');
insert into FAKULTA values ('DFJP', 'Dopravní fakulta Jana Pernera');
insert into FAKULTA values ('FES', 'Fakulta ekonomicko-správní');
insert into FAKULTA values ('FCHT', 'Fakulta chemicko-technologická');
insert into FAKULTA values ('FF', 'Fakulta filozofická');
insert into FAKULTA values ('FR', 'Fakulta restaurování');
insert into FAKULTA values ('FZS', 'Fakulta zdravotnických studií');