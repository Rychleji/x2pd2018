
-------zamestnanec-------
create or replace PROCEDURE vlozZamestnance
  (p_jmeno VARCHAR2, p_prijmeni VARCHAR2, p_titulPred VARCHAR2, p_titulZa VARCHAR2, p_email VARCHAR2, 
   p_zkratkaKatedry VARCHAR2, p_opravneni NUMBER, p_idRole NUMBER, p_mobil NUMBER, p_telefon NUMBER, p_obrazek DATA.OBRAZEK%TYPE,
   p_uzivJmeno VARCHAR2, p_heslo VARCHAR2)
IS
    v_id    ZAMESTNANEC.ID_ZAMESTNANEC%TYPE;
    v_pass  VARCHAR2(50);
BEGIN
	INSERT INTO ZAMESTNANEC (jmeno, prijmeni,titul_pred,titul_za,
		email,katedra_zkratka_katedry, id_opravneni, id_role, mobil,telefon)
	VALUES (p_jmeno, p_prijmeni, p_titulPred, p_titulZa, p_email, p_zkratkaKatedry, p_opravneni,  p_idRole, p_mobil, p_telefon);

    SELECT ID_ZAMESTNANEC INTO v_id FROM ZAMESTNANEC WHERE JMENO = p_jmeno AND PRIJMENI = p_prijmeni AND EMAIL = p_email;

    IF NOT(p_obrazek is null) THEN
        INSERT INTO DATA (OBRAZEK, DATUMPRIDANI, DATUMMODIFIKACE, ID_ZAMESTNANEC)
        VALUES (p_obrazek, SYSDATE, SYSDATE, v_id);
    END IF;

    select standard_hash(p_heslo, 'MD5') into v_pass from dual;

    INSERT INTO UDAJE (UZIVATELSKEJMENO, HESLO, ID_ZAMESTNANEC)
    VALUES (p_uzivJmeno, v_pass, v_id);
END;
/
create or replace PROCEDURE upravZamestnance 
  (p_id NUMBER, p_jmeno VARCHAR2, p_prijmeni VARCHAR2, p_titulPred VARCHAR2, p_titulZa VARCHAR2, p_email VARCHAR2, 
   p_zkratkaKatedry VARCHAR2, p_opravneni NUMBER, p_idRole NUMBER, p_mobil NUMBER, p_telefon NUMBER, p_obrazek DATA.OBRAZEK%TYPE,
   p_uzivJmeno VARCHAR2, p_heslo VARCHAR2)
IS
    v_pass  VARCHAR2(50);
BEGIN
    UPDATE ZAMESTNANEC
    SET JMENO = p_jmeno, PRIJMENI = p_prijmeni, TITUL_PRED = p_titulPred, TITUL_ZA = p_titulZa, TELEFON = p_telefon, MOBIL = p_mobil,
        EMAIL = p_email, KATEDRA_ZKRATKA_KATEDRY = p_zkratkaKatedry, ID_OPRAVNENI = p_opravneni, ID_ROLE = p_idRole
    WHERE ID_ZAMESTNANEC = p_id;

    IF NOT(p_obrazek is null) THEN
        UPDATE DATA
        SET OBRAZEK = p_obrazek, DATUMMODIFIKACE = SYSDATE
        WHERE ID_ZAMESTNANEC = p_id;
    END IF;

    IF ((P_HESLO != '') AND (P_UZIVJMENO != '')) THEN
        select standard_hash(p_heslo, 'MD5') into v_pass from dual;

        UPDATE UDAJE
        SET UZIVATELSKEJMENO = p_uzivJmeno, HESLO = v_pass
        WHERE ID_ZAMESTNANEC = p_id;
    END IF;
END;
/
create or replace PROCEDURE smazZamestnance (p_id NUMBER)--smazani udaju a dat je pomoci triggeru
IS
BEGIN
    DELETE FROM ZAMESTNANEC
    WHERE ID_ZAMESTNANEC = p_id;
END;
/
-------KATEDRA-------

CREATE OR REPLACE PROCEDURE vlozKatedru 
  (p_zkratka VARCHAR2, p_nazev VARCHAR2, p_zkrFakulty VARCHAR2)
IS
BEGIN
	INSERT INTO KATEDRA (ZKRATKA_KATEDRY, NAZEV_KATEDRY, FAKULTA_ZKRATKA_FAKULTY)
	values (p_zkratka, p_nazev, p_zkrFakulty);
END;
/
CREATE OR REPLACE PROCEDURE upravKatedru 
  (p_puvZkratka VARCHAR2, p_zkratka VARCHAR2, p_nazev VARCHAR2, p_zkrFakulty VARCHAR2)
IS
BEGIN
    UPDATE KATEDRA
    SET ZKRATKA_KATEDRY = p_zkratka, NAZEV_KATEDRY = p_nazev, FAKULTA_ZKRATKA_FAKULTY = p_zkrFakulty
    WHERE ZKRATKA_KATEDRY = p_puvZkratka;
END;
/
CREATE OR REPLACE PROCEDURE smazKatedru (p_zkratka VARCHAR2)
IS
BEGIN
    DELETE FROM KATEDRA
    WHERE ZKRATKA_KATEDRY = p_zkratka;
END;
/
-------FAKULTA-------

CREATE OR REPLACE PROCEDURE vlozFakultu 
  (p_zkratka VARCHAR2, p_nazev VARCHAR2)
IS
BEGIN
	INSERT INTO FAKULTA (ZKRATKA_FAKULTY, NAZEV_FAKULTY)
	values (p_zkratka, p_nazev);
END;
/
CREATE OR REPLACE PROCEDURE upravFakultu 
  (p_puvZkratka VARCHAR2, p_zkratka VARCHAR2, p_nazev VARCHAR2)
IS
BEGIN
    UPDATE FAKULTA
    SET ZKRATKA_FAKULTY = p_zkratka, NAZEV_FAKULTY = p_nazev
    WHERE ZKRATKA_FAKULTY = p_puvZkratka;
END;
/
CREATE OR REPLACE PROCEDURE smazFakultu (p_zkratka VARCHAR2)
IS
BEGIN
    DELETE FROM FAKULTA
    WHERE ZKRATKA_FAKULTY = p_zkratka;
END;
/

-------OBOR-------

CREATE OR REPLACE PROCEDURE vlozObor 
  (p_zkratka VARCHAR2, p_nazev VARCHAR2, p_zkrFakulty VARCHAR2)
IS
BEGIN
	INSERT INTO OBOR (ZKRATKA_OBORU, NAZEV_OBORU, FAKULTA_ZKRATKA_FAKULTY)
	values (p_zkratka, p_nazev, p_zkrFakulty);
END;
/
CREATE OR REPLACE PROCEDURE upravObor 
  (p_puvZkratka VARCHAR2, p_zkratka VARCHAR2, p_nazev VARCHAR2, p_zkrFakulty VARCHAR2)
IS
BEGIN
    UPDATE OBOR
    SET ZKRATKA_OBORU = p_zkratka, NAZEV_OBORU = p_nazev, FAKULTA_ZKRATKA_FAKULTY = p_zkrFakulty
    WHERE ZKRATKA_OBORU = p_puvZkratka;
END;
/
CREATE OR REPLACE PROCEDURE smazObor (p_zkratka VARCHAR2)
IS
BEGIN
    DELETE FROM OBOR
    WHERE ZKRATKA_OBORU = p_zkratka;
END;
/

-------PŘEDMĚT-------

CREATE OR REPLACE PROCEDURE vlozPredmet 
  (p_zkratka VARCHAR2, p_nazev VARCHAR2, p_dopRocnik NUMBER, p_semestr NUMBER, p_zpusobZak NUMBER, p_formaVyuk NUMBER)
IS
BEGIN
	INSERT INTO PREDMET 
	values (p_zkratka, p_nazev, 0, p_dopRocnik, p_semestr, p_zpusobZak, p_formaVyuk);
END;
/
CREATE OR REPLACE PROCEDURE upravPredmet 
  (p_puvZkratka VARCHAR2, p_zkratka VARCHAR2, p_nazev VARCHAR2, p_dopRocnik NUMBER, p_semestr NUMBER, p_zpusobZak NUMBER, p_formaVyuk NUMBER)
IS
BEGIN
    UPDATE PREDMET
    SET ZKRATKA_PREDMETU = p_zkratka, NAZEV_PREDMETU = p_nazev, DOPORUCENY_ROCNIK = p_dopRocnik, SEMESTR_ID_SEMESTR = p_semestr,
        ZPUSOB_ZAKONCENI_ID_ZZ = p_zpusobZak, FORMA_VYUKY_ID_FV = p_formaVyuk
    WHERE ZKRATKA_PREDMETU = p_puvZkratka;
END;
/
CREATE OR REPLACE PROCEDURE smazPredmet (p_zkratka VARCHAR2)
IS
BEGIN
    DELETE FROM PREDMET
    WHERE ZKRATKA_PREDMETU = p_zkratka;
END;
/

-------VAZBA PŘEDMĚT/OBOR-------

CREATE OR REPLACE PROCEDURE vlozOborPredmet
  (p_zkratkaOboru VARCHAR2, p_zkratkaPredmetu VARCHAR2, p_katPredmetu VARCHAR2)
IS
BEGIN
	INSERT INTO OBOR_PREDMET 
	values (p_zkratkaOboru, p_zkratkaPredmetu, p_katPredmetu);
END;
/
CREATE OR REPLACE PROCEDURE upravOborPredmet 
  (p_pZkratkaOboru VARCHAR2, p_pZkratkaPredmetu VARCHAR2, p_zkratkaOboru VARCHAR2, p_zkratkaPredmetu VARCHAR2, p_katPredmetu VARCHAR2)
IS
BEGIN
    UPDATE OBOR_PREDMET
    SET OBOR_ZKRATKA_OBORU = p_zkratkaOboru, PREDMET_ZKRATKA_PREDMETU = p_zkratkaPredmetu, KATEGORIE_PREDMETU_KATEGORIE = p_katPredmetu
    WHERE OBOR_ZKRATKA_OBORU = p_pZkratkaOboru AND PREDMET_ZKRATKA_PREDMETU = p_pZkratkaPredmetu;
END;
/
CREATE OR REPLACE PROCEDURE smazOborPredmet (p_zkratkaOboru VARCHAR2, p_zkratkaPredmetu VARCHAR2)
IS
BEGIN
    DELETE FROM OBOR_PREDMET
    WHERE OBOR_ZKRATKA_OBORU = p_zkratkaOboru AND PREDMET_ZKRATKA_PREDMETU = p_zkratkaPredmetu;
END;
/

-------ROZVRHOVÁ AKCE-------

create or replace PROCEDURE vlozRozvrhovouAkci
    (p_pocetStudentu NUMBER, p_maHodin NUMBER, p_zacinaV NUMBER, p_predmet VARCHAR2, p_zpusobVyuky NUMBER, 
        p_roleVyucujiciho VARCHAR2, p_idVyucujiciho NUMBER, p_idUcebna NUMBER)
IS
    ex_kryti EXCEPTION;
BEGIN
    IF ((MAVOLNO(p_idVyucujiciho, p_zacinaV, p_maHodin, p_idUcebna, 0)) AND (DOSTACUJEMISTO(p_pocetStudentu, p_idUcebna))) THEN
        INSERT INTO ROZVRHOVA_AKCE (POCET_STUDENTU, MAHODIN, ZACINAV, PREDMET_ZKRATKA_PREDMETU, ZPUSOB_VYUKY_ID_ZV,
            ROLE_VYUCUJICIHO_ROLE, ID_ZAMESTNANEC, ID_UCEBNA)
        values (p_pocetStudentu, p_maHodin, p_zacinaV, p_predmet, p_zpusobVyuky, p_roleVyucujiciho, p_idVyucujiciho, p_idUcebna);
    ELSE
        RAISE ex_kryti;
    END IF;
    
    EXCEPTION
        WHEN ex_kryti THEN
            raise_application_error(-20000, 'Ve zvolený čas je obsazena učebna nebo vyučující už uči.');
END;
/
create or replace PROCEDURE upravRozvrhovouAkci 
    (p_id Number, p_pocetStudentu NUMBER, p_maHodin NUMBER, p_zacinaV NUMBER, p_predmet VARCHAR2, p_zpusobVyuky NUMBER, 
        p_roleVyucujiciho VARCHAR2, p_idVyucujiciho NUMBER, p_idUcebna NUMBER, p_schvaleno NUMBER)
IS
    ex_kryti EXCEPTION;
BEGIN
    IF ((MAVOLNO(p_idVyucujiciho, p_zacinaV, p_maHodin, p_idUcebna, p_id)) AND (DOSTACUJEMISTO(p_pocetStudentu, p_idUcebna))) THEN
        UPDATE ROZVRHOVA_AKCE 
        SET POCET_STUDENTU = p_pocetStudentu, MAHODIN = p_maHodin, ZACINAV = p_zacinaV, PREDMET_ZKRATKA_PREDMETU = p_predmet, 
        ZPUSOB_VYUKY_ID_ZV = p_zpusobVyuky, ROLE_VYUCUJICIHO_ROLE = p_roleVyucujiciho, ID_ZAMESTNANEC = p_idVyucujiciho, ID_UCEBNA = p_idUcebna,
        SCHVALENO = p_schvaleno
        WHERE ID_RA = p_id;
    ELSE
        RAISE ex_kryti;
    END IF;

    EXCEPTION
        WHEN ex_kryti THEN
            raise_application_error(-20000, 'Ve zvolený čas je obsazena učebna nebo vyučující už uči.');    
END;
/
CREATE OR REPLACE PROCEDURE smazRozvrhovouAkci (p_id NUMBER)
IS
BEGIN
    DELETE FROM ROZVRHOVA_AKCE
    WHERE ID_RA = p_id;
END;
/

-------TRIGGERY-------

CREATE OR REPLACE TRIGGER BeforePredmetDelete
BEFORE DELETE
   ON Predmet FOR EACH ROW

DECLARE
    v_zkratkaPredmetu PREDMET.ZKRATKA_PREDMETU%TYPE;
    v_zkratkaOboru OBOR.ZKRATKA_OBORU%TYPE;
    v_RAID ROZVRHOVA_AKCE.ID_RA%TYPE;

    CURSOR c1 (p_zp in varchar2)
        IS SELECT OBOR_ZKRATKA_OBORU FROM OBOR_PREDMET WHERE PREDMET_ZKRATKA_PREDMETU = p_zp;
    CURSOR c2 (p_zp in varchar2)  
        IS SELECT ID_RA FROM ROZVRHOVA_AKCE where PREDMET_ZKRATKA_PREDMETU = p_zp;
BEGIN
    v_zkratkaPredmetu := :old.zkratka_predmetu;
   
    OPEN c1(v_zkratkaPredmetu);
    LOOP
        FETCH c1 INTO v_zkratkaOboru;
        EXIT WHEN c1%NOTFOUND;
        SMAZOBORPREDMET(v_zkratkaOboru, v_zkratkaPredmetu);   
    END LOOP;
    CLOSE c1;
    
    OPEN c2(v_zkratkaPredmetu);
    LOOP
        FETCH c2 INTO v_RAID;
        EXIT WHEN c2%NOTFOUND;
        SMAZROZVRHOVOUAKCI(v_RAID);  
    END LOOP;
    CLOSE c2;
     
--EXCEPTION
   --WHEN ...
   -- exception handling

END;
/
CREATE OR REPLACE TRIGGER BeforeOborDelete
BEFORE DELETE
   ON Obor FOR EACH ROW

DECLARE
    v_zkratkaPredmetu PREDMET.ZKRATKA_PREDMETU%TYPE;
    v_zkratkaOboru OBOR.ZKRATKA_OBORU%TYPE;

    CURSOR c1 (p_zo in VARCHAR2)
        IS SELECT OBOR_ZKRATKA_OBORU FROM OBOR_PREDMET WHERE OBOR_ZKRATKA_OBORU = p_zo;
BEGIN
    v_zkratkaOboru := :old.zkratka_oboru;
   
    OPEN c1(v_zkratkaOboru);
    LOOP
        FETCH c1 INTO v_zkratkaPredmetu;
        EXIT WHEN c1%NOTFOUND;
        SMAZOBORPREDMET(v_zkratkaOboru, v_zkratkaPredmetu);   
    END LOOP;
    CLOSE c1;

--EXCEPTION
   --WHEN ...
   -- exception handling

END;
/
CREATE OR REPLACE TRIGGER BeforeKatedraDelete
BEFORE DELETE
   ON Katedra FOR EACH ROW

DECLARE
    v_zkratkaKatedry KATEDRA.ZKRATKA_KATEDRY%TYPE;
    v_idZamestnance ZAMESTNANEC.ID_ZAMESTNANEC%TYPE;

    CURSOR c1 (p_zkrOb in varchar2)
    IS SELECT ID_ZAMESTNANEC FROM ZAMESTNANEC WHERE KATEDRA_ZKRATKA_KATEDRY = p_zkrOb;
BEGIN
    v_zkratkaKatedry := :old.zkratka_katedry;
   
    OPEN c1(v_zkratkaKatedry);
    LOOP
        FETCH c1 INTO v_idZamestnance;
        EXIT WHEN c1%NOTFOUND;
        SMAZZAMESTNANCE(v_idZamestnance);   
    END LOOP;
    CLOSE c1;

--EXCEPTION
   --WHEN ...
   -- exception handling

END;
/
CREATE OR REPLACE TRIGGER BeforeFakultaDelete
BEFORE DELETE
   ON Fakulta FOR EACH ROW

DECLARE
    v_zkratkaFakulty FAKULTA.ZKRATKA_FAKULTY%TYPE;
    v_zkratkaKatedry KATEDRA.ZKRATKA_KATEDRY%TYPE;
    v_zkratkaOboru   OBOR.ZKRATKA_OBORU%TYPE;

    CURSOR c1 (p_zf in varchar2)
        IS SELECT ZKRATKA_KATEDRY FROM KATEDRA WHERE FAKULTA_ZKRATKA_FAKULTY = p_zf;
    CURSOR c2 (p_zf in varchar2)
        IS SELECT ZKRATKA_OBORU FROM OBOR WHERE FAKULTA_ZKRATKA_FAKULTY = p_zf;
BEGIN
    v_zkratkaFakulty := :old.zkratka_fakulty;
   
    OPEN c1(v_zkratkaFakulty);
    LOOP
        FETCH c1 INTO v_zkratkaKatedry;
        EXIT WHEN c1%NOTFOUND;
        SMAZKATEDRU(v_zkratkaKatedry);
    END LOOP;
    CLOSE c1;
    
    OPEN c2(v_zkratkaFakulty);
    LOOP
        FETCH c2 INTO v_zkratkaOboru;
        EXIT WHEN c2%NOTFOUND;
        SMAZOBOR(v_zkratkaOboru);
    END LOOP;
    CLOSE c2;
         
--EXCEPTION
   --WHEN ...
   -- exception handling

END;
/
CREATE OR REPLACE TRIGGER BeforeZamestnanecDelete
BEFORE DELETE
   ON ZAMESTNANEC FOR EACH ROW

DECLARE
    v_idZam     ZAMESTNANEC.ID_ZAMESTNANEC%TYPE;
    v_idRA      ROZVRHOVA_AKCE.ID_RA%TYPE;
    v_idUdaje   UDAJE.ID_UDAJE%TYPE;
    v_idData    DATA.ID_DATA%TYPE;

    CURSOR c1 (p_iz in NUMBER)
        IS SELECT ID_RA FROM ROZVRHOVA_AKCE WHERE ID_ZAMESTNANEC = p_iz;
BEGIN
    v_idZam := :old.ID_ZAMESTNANEC;
   
    OPEN c1(v_idZam);
    LOOP
        FETCH c1 INTO v_idRA;
        EXIT WHEN c1%NOTFOUND;
        SMAZROZVRHOVOUAKCI(v_idRA);
    END LOOP;
    CLOSE c1;
    
    DELETE FROM UDAJE WHERE ID_ZAMESTNANEC = v_idZam;
    DELETE FROM DATA WHERE ID_ZAMESTNANEC = v_idZam;
     
--EXCEPTION
   --WHEN ...
   -- exception handling

END;
/
-------FUNKCE-------
CREATE OR REPLACE FUNCTION konecHodiny (p_idRA ROZVRHOVA_AKCE.ID_RA%TYPE)
RETURN number IS 
   v_konec ROZVRHOVA_AKCE.ZACINAV%TYPE;
BEGIN 
   SELECT (MAHODIN + ZACINAV) into v_konec 
   FROM ROZVRHOVA_AKCE
   WHERE ID_RA = p_idRA; 
    
   RETURN v_konec; 
END;
/
create or replace FUNCTION maVolno 
    (p_idZam ZAMESTNANEC.ID_ZAMESTNANEC%TYPE, p_zacatek ROZVRHOVA_AKCE.ZACINAV%TYPE, p_delka ROZVRHOVA_AKCE.MAHODIN%TYPE,
    p_idUcebny UCEBNA.ID_UCEBNA%TYPE, p_idRA ROZVRHOVA_AKCE.ID_RA%TYPE)
RETURN BOOLEAN IS
    v_jeVolno   BOOLEAN := true;
    v_zacatek   ROZVRHOVA_AKCE.ZACINAV%TYPE;
    v_delka     ROZVRHOVA_AKCE.MAHODIN%TYPE;

    CURSOR c1 (p_iz in NUMBER)
        IS SELECT ZACINAV, MAHODIN FROM ROZVRHOVA_AKCE WHERE (ID_ZAMESTNANEC = p_iz OR ID_UCEBNA = p_idUcebny) AND NOT(ID_RA=p_idRA);
BEGIN 
   open c1 (p_idZam);
   LOOP
        FETCH c1 INTO v_delka, v_zacatek;
        EXIT WHEN c1%NOTFOUND;
        if ((p_zacatek BETWEEN v_zacatek AND (v_zacatek + v_delka)) or ((p_zacatek+p_delka) BETWEEN v_zacatek AND (v_zacatek+v_delka))) then
            v_jeVolno := false;
        end if;
    END LOOP;
    CLOSE c1;

   RETURN v_jeVolno; 
END;
/
CREATE OR REPLACE FUNCTION dostacujeMisto 
    (p_pocetStudentu ROZVRHOVA_AKCE.POCET_STUDENTU%TYPE, p_idUcebny UCEBNA.ID_UCEBNA%TYPE)
RETURN BOOLEAN IS 
   v_jeVolno    BOOLEAN := false;
   v_kapacita   UCEBNA.KAPACITA%TYPE;
BEGIN 
   SELECT KAPACITA INTO v_kapacita FROM UCEBNA WHERE ID_UCEBNA = p_idUcebny;
   v_jeVolno := v_kapacita>=p_pocetStudentu;
    
   RETURN v_jeVolno; 
END;
/