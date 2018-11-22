
-------Procedure na inser do zamestnancu -------
CREATE OR REPLACE PROCEDURE vlozZamestnance 
  (p_jmeno VARCHAR2, p_prijmeni VARCHAR, p_titulPred VARCHAR, p_titulZa VARCHAR, p_email VARCHAR, 
   p_zratkaKatedry VARCHAR, p_typRole VARCHAR, p_idRole NUMBER, p_mobil NUMBER, p_telefon NUMBER)
IS
BEGIN
 INSERT INTO ZAMESTNANEC (id_zamestnanec, jmeno, prijmeni,titul_pred,titul_za,
email,katedra_zkratka_katedry, typrole, id_role, mobil,telefon)
values (zamestnanec_idSEQ.NEXTVAL, p_jmeno, p_prijmeni,p_titulPred,p_titulZa,p_email,p_zratkaKatedry,p_typRole,  p_idRole, p_mobil, p_telefon);
END;