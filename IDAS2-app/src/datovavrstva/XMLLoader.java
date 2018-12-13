package datovavrstva;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Radim
 */
public class XMLLoader {

    @XmlRootElement(name = "getPredmetyByKatedraFullInfoResponse", namespace = "http://stag-ws.zcu.cz/")
    public static class Garbage {

        private Predmety pr;

        public Predmety getPr() {
            return pr;
        }

        @XmlElement(name = "predmetyKatedryFullInfo")
        public void setPr(Predmety pr) {
            this.pr = pr;
        }

    }

    @XmlRootElement(name = "predmetyKatedryFullInfo")
    public static class Predmety {

        private List<Predmet> predmety;

        public List<Predmet> getPredmety() {
            return predmety;
        }

        @XmlElement(name = "predmetKatedryFullInfo")
        public void setPredmety(List<Predmet> predmety) {
            this.predmety = predmety;
        }
    }

        @XmlRootElement(name = "predmetKatedryFullInfo")
    public static class Predmet {

        private String zkratka, nazev, vyukaZS, vyukaLS, typZkousky, zarazenDoPrezencnihoStudia,
                zarazenDoKombinovanehoStudian;

        public String getZkratka() {
            return zkratka;
        }

        @XmlElement(name = "zkratka")
        public void setZkratka(String zkratka) {
            this.zkratka = zkratka;
        }

        public String getNazev() {
            return nazev;
        }

        @XmlElement(name = "nazev")
        public void setNazev(String nazev) {
            this.nazev = nazev;
        }

        public String getVyukaZS() {
            return vyukaZS;
        }

        @XmlElement(name = "vyukaZS")
        public void setVyukaZS(String vyukaZS) {
            this.vyukaZS = vyukaZS;
        }

        public String getVyukaLS() {
            return vyukaLS;
        }

        @XmlElement(name = "vyukaLS")
        public void setVyukaLS(String vyukaLS) {
            this.vyukaLS = vyukaLS;
        }

        public String getTypZkousky() {
            return typZkousky;
        }

        @XmlElement(name = "typZkousky")
        public void setTypZkousky(String typZkousky) {
            this.typZkousky = typZkousky;
        }

        public String getZarazenDoPrezencnihoStudia() {
            return zarazenDoPrezencnihoStudia;
        }

        @XmlElement(name = "zarazenDoPrezencnihoStudia")
        public void setZarazenDoPrezencnihoStudia(String zarazenDoPrezencnihoStudia) {
            this.zarazenDoPrezencnihoStudia = zarazenDoPrezencnihoStudia;
        }

        public String getZarazenDoKombinovanehoStudian() {
            return zarazenDoKombinovanehoStudian;
        }

        @XmlElement(name = "zarazenDoKombinovanehoStudia")
        public void setZarazenDoKombinovanehoStudian(String zarazenDoKombinovanehoStudian) {
            this.zarazenDoKombinovanehoStudian = zarazenDoKombinovanehoStudian;
        }
    }

    public static void importPredmetu(String filePath) throws JAXBException, FileNotFoundException {
        Unmarshaller um = JAXBContext.newInstance(Garbage.class).createUnmarshaller();
        Garbage input = (Garbage) um.unmarshal(new FileReader(filePath));

        Predmety predmety = input.getPr();

        for (Predmet predmet : predmety.getPredmety()) {
            //získej ID semestru
            String sem = "";
            if (predmet.getVyukaLS().equalsIgnoreCase("A")) {
                if (predmet.getVyukaZS().equalsIgnoreCase("A")) {
                    sem = "Oba";
                } else {
                    sem = "Letní";
                }
            } else if (predmet.getVyukaZS().equalsIgnoreCase("A")) {
                sem = "Zimní";
            } else {
                //nemá žádný semestr -> přeskoč
                continue;
            }
            int idSem = 0;
            for (idas22018.GuiFXMLController.HelpClass hc : idas22018.IDAS22018.mainController.getCiselnikSemestr().values()) {
                if (hc.getString().equalsIgnoreCase(sem)) {
                    idSem = hc.getId();
                    break;
                }
            }
            //získej ID způsobu
            String form;
            if (predmet.getZarazenDoPrezencnihoStudia().equalsIgnoreCase("A")) {
                form = "Prezenční";
            } else if (predmet.getZarazenDoKombinovanehoStudian().equalsIgnoreCase("A")) {
                form = "Kombinovaná";
            } else {
                continue;
            }
            int idForm = 0;
            for (idas22018.GuiFXMLController.HelpClass hc : idas22018.IDAS22018.mainController.getCiselnikFormaVyuky().values()) {
                if (hc.getString().equalsIgnoreCase(form)) {
                    idForm = hc.getId();
                    break;
                }
            }
            //získej ID zakončení
            String zak;
            if (predmet.getTypZkousky().equalsIgnoreCase("Zkouška")) {
                zak = "Zkouškou";
            } else if (predmet.getTypZkousky().equalsIgnoreCase("Zápočet")) {
                zak = "Zápočtem";
            } else {
                System.out.println("TypZkousky: "+predmet.getTypZkousky());
                continue;
            }
            int idZak = 0;
            for (idas22018.GuiFXMLController.HelpClass hc : idas22018.IDAS22018.mainController.getCiselnikZpusobZak().values()) {
                if (hc.getString().equalsIgnoreCase(zak)) {
                    idZak = hc.getId();
                    break;
                }
            }

            try {
                idas22018.GuiFXMLController.getDataLayer().addSubject(predmet.getZkratka(),
                        predmet.getNazev(), 0, idSem, idZak, idForm);
            } catch (SQLException ex) {
                Logger.getLogger(XMLLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
