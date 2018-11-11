/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import datovavrstva.SkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPripojeni;
import java.io.IOException;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @FXML
    private Label versionLabel;
    @FXML
    private VBox disablovatelnyButtonyVBox;
    @FXML
    private Button pripojitButton;
    
    public static ISkolniDB getDataLayer(){
        return dataLayer;
    }

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
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        versionLabel.setText("Verze: " + VERSION);
        stageP.setTitle(PROGRAMNAME);
        if(dataLayer == null){
            dataLayer = new SkolniDB();
        }
        conn = dataLayer.getConnect();
        afterConnect();
        
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
                if (dataLayer.getConnect() != null)
                    dataLayer.getConnect().close();
            } catch (SQLException ex) {
                Logger.getLogger(GuiFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
            root = fxmlLoader.load();
            FXMLVyucujiciController controller = fxmlLoader.<FXMLVyucujiciController>getController();
            Scene scena = new Scene(root);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            } catch (SQLException ex) {
                DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                dialog2.showAndWait();
            }
        }
        
        //Není potřeba
        /*disablovatelnyButtonyVBox.getChildren().forEach((node) -> {
            node.setDisable(conn == null);
        });

        pripojitButton.setDisable(conn != null);*/
    }
    
    @FXML
    private void pracovisteButtonClick(ActionEvent event) {        
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPracoviste.fxml"));
            root = fxmlLoader.load();
            FXMLPracovisteController controller = fxmlLoader.<FXMLPracovisteController>getController();

            Scene scena = new Scene(root);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPredmety.fxml"));
            root = fxmlLoader.load();
            FXMLPredmetyController controller = fxmlLoader.<FXMLPredmetyController>getController();
            
            Scene scena = new Scene(root);
            controller.setScenes(mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void oborButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLObory.fxml"));
            root = fxmlLoader.load();
            FXMLOboryController controller = fxmlLoader.<FXMLOboryController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setConnection(Connection conn) {
        this.conn = conn;
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
    private void pripojeniButtonClick(ActionEvent event) {
        DialogPripojeni dialog = new DialogPripojeni(null);
        dialog = (DialogPripojeni) dialog.getScene().getWindow();
        dialog.showAndWait();
        if (dialog.isButtonPressed()) {
            try {
                conn = dataLayer.connectToDB("fei-sql1.upceucebny.cz", 1521, "IDAS12", dialog.getJmeno(), dialog.getHeslo());
            } catch (SQLException ex) {
                DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                dialog2.showAndWait();
                conn = null;
            }

            afterConnect();
        }
    }
    
    

    @FXML
    private void exitButtonClick(ActionEvent event) {
        stageP.close();
    }

}
