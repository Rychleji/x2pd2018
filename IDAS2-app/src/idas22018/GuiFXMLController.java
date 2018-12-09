/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import static idas22018.KnihovnaZobrazovani.getKnihovnaZobrazovani;
import datovavrstva.ISkolniDB;
import datovavrstva.SkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejVyucujiciho;
import idas22018.dialogy.DialogPripojeni;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

/**
 *
 * @author Radim
 */
public class GuiFXMLController implements Initializable {

    private static ISkolniDB dataLayer;
    Connection conn;
    List<String> ciselnikKatPredmetu = new ArrayList<>(3);
    Map<Integer, HelpClass> ciselnikZpusobVyuky = new HashMap<>(3);
    Map<Integer, HelpClass> ciselnikSemestr = new HashMap<>(3);
    List<String> ciselnikRoleVyuc = new ArrayList<>(2);
    Map<Integer, HelpClass> ciselnikZpusobZak = new HashMap<>(2);
    Map<Integer, HelpClass> ciselnikFormaVyuky = new HashMap<>(3);
    Map<Integer, HelpClass> ciselnikRole = new HashMap<>(5);
    Map<Integer, HelpClass> ciselnikOpravneni = new HashMap<>(3);
    List<String> ciselnikDen = new ArrayList<>(7);

    @FXML
    private Label versionLabel;

    boolean readyForDialog = false;
    private boolean vyucujici = false;

    public static ISkolniDB getDataLayer() {
        return dataLayer;
    }
    @FXML
    private VBox disablovatelnyButtonyVBox;
    @FXML
    private VBox vboxZamestnancu;
    @FXML
    private Button raButton;

    public List<String> getCiselnikKatPredmetu() {
        return ciselnikKatPredmetu;
    }

    public Map<Integer, HelpClass> getCiselnikZpusobVyuky() {
        return ciselnikZpusobVyuky;
    }

    public Map<Integer, HelpClass> getCiselnikSemestr() {
        return ciselnikSemestr;
    }

    public List<String> getCiselnikRoleVyuc() {
        return ciselnikRoleVyuc;
    }

    public Map<Integer, HelpClass> getCiselnikZpusobZak() {
        return ciselnikZpusobZak;
    }

    public Map<Integer, HelpClass> getCiselnikFormaVyuky() {
        return ciselnikFormaVyuky;
    }

    public Map<Integer, HelpClass> getCiselnikRole() {
        return ciselnikRole;
    }

    public Map<Integer, HelpClass> getCiselnikOpravneni() {
        return ciselnikOpravneni;
    }

    public List<String> getCiselnikDen() {
        return ciselnikDen;
    }
    
    public void panelProRegistrovaneStatus(RezimProhlizeni rp){
        vboxZamestnancu.setDisable(rp==RezimProhlizeni.NEREGISTROVANY);
    }
    
    public void controlsProVyucujici(String role){
        vyucujici = role.equalsIgnoreCase("Vyučující");
        raButton.setDisable(!vyucujici);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        versionLabel.setText("Verze: " + VERSION);
        stageP.setTitle(PROGRAMNAME);
        if (dataLayer == null) {
            dataLayer = new SkolniDB();
        }
        conn = dataLayer.getConnect();
        disablovatelnyButtonyVBox.setDisable(true);
        
        stageP.setOnShown((dd) -> {
            while (!prihlaseno) {
                DialogPripojeni dialog = new DialogPripojeni(null);
                dialog = (DialogPripojeni) dialog.getScene().getWindow();
                dialog.setOnCloseRequest((d) -> {
                    prihlaseno = true;
                    Platform.exit();
                });
                dialog.showAndWait();
                if (dialog.isButtonPressed()) {
                    try {
                        conn = dataLayer.connectToDB("fei-sql1.upceucebny.cz", 1521, "IDAS12", dialog.getJmeno(), dialog.getHeslo());
                        prihlaseno = true;

                        getKnihovnaZobrazovani().zobrazPrihlaseni();
                        disablovatelnyButtonyVBox.setDisable(false);
                        afterConnect();
                    } catch (SQLException ex) {
                        DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                        dialog2.showAndWait();
                        conn = null;
                    }
                }
            }
        });

        stageP.setOnCloseRequest((WindowEvent event) -> {
            Alert closeComf = new Alert(Alert.AlertType.CONFIRMATION, "Opravdu chcete zavřít program?", ButtonType.YES, ButtonType.NO);

            closeComf.setHeaderText("Potvrďte prosím");
            closeComf.initModality(Modality.APPLICATION_MODAL);

            Optional<ButtonType> optBtn = closeComf.showAndWait();
            if (optBtn.get() != ButtonType.YES) {
                event.consume();
                return;
            }

            try {
                if (dataLayer.getConnect() != null) {
                    dataLayer.getConnect().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GuiFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        readyForDialog = true;
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazPrehledUcitelu();
    }

    private void afterConnect() {
        //naplnění listů s číselníky
        if (conn != null) {
            try {
                ResultSet rs;
                Statement query = conn.createStatement();

                rs = query.executeQuery("select * from FORMA_VYUKY");

                while (rs.next()) {
                    HelpClass input = new HelpClass(rs.getInt("ID_FV"), rs.getString("FORMA"));
                    ciselnikFormaVyuky.put(input.getId(), input);
                }

                rs = query.executeQuery("select * from KATEGORIE_PREDMETU");

                while (rs.next()) {
                    ciselnikKatPredmetu.add(rs.getString("KATEGORIE"));
                }

                rs = query.executeQuery("select * from ROLE_VYUCUJICIHO");

                while (rs.next()) {
                    ciselnikRoleVyuc.add(rs.getString("ROLE"));
                }

                rs = query.executeQuery("select * from SEMESTR");

                while (rs.next()) {
                    HelpClass input = new HelpClass(rs.getInt("ID_SEMESTR"), rs.getString("SEM"));
                    ciselnikSemestr.put(input.getId(), input);
                }

                rs = query.executeQuery("select * from ZPUSOB_VYUKY");

                while (rs.next()) {
                    HelpClass input = new HelpClass(rs.getInt("ID_ZV"), rs.getString("ZPUSOB"));
                    ciselnikZpusobVyuky.put(input.getId(), input);
                }

                rs = query.executeQuery("select * from ZPUSOB_ZAKONCENI");

                while (rs.next()) {
                    HelpClass input = new HelpClass(rs.getInt("ID_ZZ"), rs.getString("ZPUSOB_ZAK"));
                    ciselnikZpusobZak.put(input.getId(), input);
                }
                
                rs = query.executeQuery("select * from ROLE");

                while (rs.next()) {
                    HelpClass input = new HelpClass(rs.getInt("ID_ROLE"), rs.getString("TYPROLE"));
                    ciselnikRole.put(input.getId(), input);
                }
                
                rs = query.executeQuery("select * from OPRAVNENI");

                while (rs.next()) {
                    HelpClass input = new HelpClass(rs.getInt("ID_OPRAVNENI"), rs.getString("OPRAVNENI"));
                    ciselnikOpravneni.put(input.getId(), input);
                }
                
                rs = query.executeQuery("select * from dnyvtydnu order by priorita");

                while (rs.next()) {
                    ciselnikDen.add(rs.getString("DEN"));
                }
            } catch (SQLException ex) {
                DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                dialog2.showAndWait();
            }
        }
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazPrehledPracovist();
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazPrehledPredmetu();
    }

    @FXML
    private void oborButtonClick(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazPrehledOboru();
    }

    void setConnection(Connection conn) {
        this.conn = conn;
    }

    @FXML
    private void zamestnanciButtonClick(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazPrehledZamestnancu();
    }

    @FXML
    private void udajeButtonAction(ActionEvent event) {
        int origID = idPrihlasenehoZamestnance;
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(vboxZamestnancu.getParent().getScene().getWindow(),
                vyucujici, origID, dataLayer.getConnect(), true);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editTeacher(origID, dialog2.getJmeno(),
                        dialog2.getPrijmeni(), dialog2.getTitulP(), dialog2.getTitulZ(),
                        dialog2.getTelefon(), dialog2.getMobil(), dialog2.getEmail(),
                        dialog2.getZkratkaKatedry(), dialog2.getRole(), dialog2.getOpravneni(),
                        dialog2.getuJmeno(), dialog2.getuHeslo());
                dataLayer.commit();
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void raButtonAction(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazRozvrhoveAkceVlastni(String.valueOf(idPrihlasenehoZamestnance), mainScene);
    }

    @FXML
    private void clickUcebnyButton(ActionEvent event) {
        getKnihovnaZobrazovani().zobrazPrehledUceben();
    }

    public class HelpClass {

        int id;
        String string;

        public HelpClass(int id, String string) {
            this.id = id;
            this.string = string;
        }

        public int getId() {
            return id;
        }

        public String getString() {
            return string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    @FXML
    private void exitButtonClick(ActionEvent event) {
        stageP.close();
    }

}
