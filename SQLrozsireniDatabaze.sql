DELETE FROM ROZVRHOVA_AKCE;

ALTER TABLE ROZVRHOVA_AKCE
DROP CONSTRAINT ROZVRHOVA_AKCE_VYUCUJICI_FK;

ALTER TABLE ROZVRHOVA_AKCE
DROP CONSTRAINT SYS_C00144134;

ALTER TABLE ROZVRHOVA_AKCE DROP COLUMN VYUCUJICI_ID_VYUCUJICIHO;

DROP TABLE VYUCUJICI;

CREATE TABLE zamestnanec (
    id_zamestnanec            NUMBER NOT NULL,
    jmeno                     VARCHAR2(25) NOT NULL,
    prijmeni                  VARCHAR2(30) NOT NULL,
    titul_pred                VARCHAR2(20),
    titul_za                  VARCHAR2(15),
    telefon                   VARCHAR2(13),
    mobil                     VARCHAR2(13),
    email                     VARCHAR2(255) NOT NULL,
    id_pracoviste             NUMBER NOT NULL,
    katedra_zkratka_katedry   VARCHAR2(10) NOT NULL,
    typrole                   VARCHAR2(15),
    id_role              NUMBER NOT NULL
);

ALTER TABLE zamestnanec ADD CONSTRAINT zamestnanec_pk PRIMARY KEY ( id_zamestnanec );

CREATE TABLE role (
    id_role   NUMBER NOT NULL,
    typrole   VARCHAR2(40) NOT NULL
);

ALTER TABLE role ADD CONSTRAINT role_pk PRIMARY KEY ( id_role );

CREATE TABLE ucebna (
    id_ucebna   NUMBER NOT NULL,
    nazev       VARCHAR2(40) NOT NULL,
    kapacita    NUMBER NOT NULL
);

ALTER TABLE ucebna ADD CONSTRAINT ucebna_pk PRIMARY KEY ( id_ucebna );

CREATE TABLE udaje (
    id_udaje                     NUMBER NOT NULL,
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
    id_data                      NUMBER NOT NULL,
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