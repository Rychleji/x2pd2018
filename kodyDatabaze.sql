
-------zamestnanec-------
CREATE OR REPLACE PROCEDURE vlozZamestnance 
  (p_jmeno VARCHAR2, p_prijmeni VARCHAR2, p_titulPred VARCHAR2, p_titulZa VARCHAR2, p_email VARCHAR2, 
   p_zratkaKatedry VARCHAR2, p_opravneni NUMBER, p_idRole NUMBER, p_mobil NUMBER, p_telefon NUMBER)
IS
BEGIN
	INSERT INTO ZAMESTNANEC (jmeno, prijmeni,titul_pred,titul_za,
		email,katedra_zkratka_katedry, id_opravneni, id_role, mobil,telefon)
	values (p_jmeno, p_prijmeni, p_titulPred, p_titulZa, p_email, p_zratkaKatedry, p_opravneni,  p_idRole, p_mobil, p_telefon);
END;
/
CREATE OR REPLACE PROCEDURE upravZamestnance 
  (p_id NUMBER, p_jmeno VARCHAR2, p_prijmeni VARCHAR2, p_titulPred VARCHAR2, p_titulZa VARCHAR2, p_email VARCHAR2, 
   p_zkratkaKatedry VARCHAR2, p_opravneni NUMBER, p_idRole NUMBER, p_mobil NUMBER, p_telefon NUMBER)
IS
BEGIN
    UPDATE ZAMESTNANEC
    SET JMENO = p_jmeno, PRIJMENI = p_prijmeni, TITUL_PRED = p_titulPred, TITUL_ZA = p_titulZa, TELEFON = p_telefon, MOBIL = p_mobil,
        EMAIL = p_email, KATEDRA_ZKRATKA_KATEDRY = p_zkratkaKatedry, ID_OPRAVNENI = p_opravneni, ID_ROLE = p_idRole
    WHERE ID_ZAMESTNANEC = p_id;
END;
/
CREATE OR REPLACE PROCEDURE upravZamestnance 
  (p_id NUMBER, p_jmeno VARCHAR2, p_prijmeni VARCHAR2, p_titulPred VARCHAR2, p_titulZa VARCHAR2, p_email VARCHAR2, 
   p_zkratkaKatedry VARCHAR2, p_opravneni NUMBER, p_idRole NUMBER, p_mobil NUMBER, p_telefon NUMBER)
IS
BEGIN
    UPDATE ZAMESTNANEC
    SET JMENO = p_jmeno, PRIJMENI = p_prijmeni, TITUL_PRED = p_titulPred, TITUL_ZA = p_titulZa, TELEFON = p_telefon, MOBIL = p_mobil,
        EMAIL = p_email, KATEDRA_ZKRATKA_KATEDRY = p_zkratkaKatedry, ID_OPRAVNENI = p_opravneni, ID_ROLE = p_idRole
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
	--možnost triggeru pro smazani oborů a kateder
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
	--moznost triggeru pro smazaní všech vazeb obor/předmět
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
    --moznost triggeru na smazani vsech vazeb predmet/obor a rozvrhovych akci
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

CREATE OR REPLACE PROCEDURE vlozRozvrhovouAkci
    (p_pocetStudentu NUMBER, p_maHodin NUMBER, p_zacinaV NUMBER, p_predmet VARCHAR2, p_zpusobVyuky NUMBER, 
        p_roleVyucujiciho VARCHAR2, p_idVyucujiciho NUMBER, p_idUcebna NUMBER)
IS
BEGIN
	--nutno dodělat podminku pro nekryti rozvrhu
	INSERT INTO ROZVRHOVA_AKCE (POCET_STUDENTU, MAHODIN, ZACINAV, PREDMET_ZKRATKA_PREDMETU, ZPUSOB_VYUKY_ID_ZV,
        ROLE_VYUCUJICIHO_ROLE, ID_ZAMESTNANEC, ID_UCEBNA)
	values (p_pocetStudentu, p_maHodin, p_zacinaV, p_predmet, p_zpusobVyuky, p_roleVyucujiciho, p_idVyucujiciho, p_idUcebna);
END;
/
CREATE OR REPLACE PROCEDURE upravRozvrhovouAkci 
    (p_id Number, p_pocetStudentu NUMBER, p_maHodin NUMBER, p_zacinaV NUMBER, p_predmet VARCHAR2, p_zpusobVyuky NUMBER, 
        p_roleVyucujiciho VARCHAR2, p_idVyucujiciho NUMBER, p_idUcebna NUMBER)
IS
BEGIN
	--nutno dodělat podminku pro nekryti rozvrhu
    UPDATE ROZVRHOVA_AKCE 
    SET POCET_STUDENTU = p_pocetStudentu, MAHODIN = p_maHodin, ZACINAV = p_zacinaV, PREDMET_ZKRATKA_PREDMETU = p_predmet, 
    ZPUSOB_VYUKY_ID_ZV = p_zpusobVyuky, ROLE_VYUCUJICIHO_ROLE = p_roleVyucujiciho, ID_ZAMESTNANEC = p_idVyucujiciho, ID_UCEBNA = p_idUcebna
    WHERE ID_RA = p_id;
END;
/
CREATE OR REPLACE PROCEDURE smazRozvrhovouAkci (p_id NUMBER)
IS
BEGIN
    DELETE FROM ROZVRHOVA_AKCE
    WHERE ID_RA = p_id;
END;
/