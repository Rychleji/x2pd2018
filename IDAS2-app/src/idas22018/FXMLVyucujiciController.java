/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejVyucujiciho;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Radim
 */
public class FXMLVyucujiciController implements Initializable {

    ObservableList<List<String>> seznam = FXCollections.observableArrayList();

    String katedraFiltr, predmetFiltr;

    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private TableColumn<List<String>, String> idCol;
    @FXML
    private TableColumn<List<String>, String> jmenoCol;
    @FXML
    private TableColumn<List<String>, String> prijmeniCol;
    @FXML
    private TableColumn<List<String>, String> titulPredCol;
    @FXML
    private TableColumn<List<String>, String> titulZaCol;
    @FXML
    private TableColumn<List<String>, String> telefonCol;
    @FXML
    private TableColumn<List<String>, String> mobilCol;
    @FXML
    private TableColumn<List<String>, String> emailCol;
    @FXML
    private TableColumn<List<String>, String> katedraCol;
    @FXML
    private TableColumn<List<String>, String> fakultaCol;
    private Scene predScena;
    private Scene aktScena;

    
    //Vezmu si datovou vrstvu
    private ISkolniDB dataLayer;
    @FXML
    private ImageView imageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();
        idCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        jmenoCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        prijmeniCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        titulPredCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(3)));
        titulZaCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(4)));
        telefonCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(5)));
        mobilCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(6)));
        emailCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(7)));
        katedraCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(8)));
        fakultaCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(9)));

        tableView.setItems(seznam);
        fillTable();
    }

    public void setKatedraFiltr(String katedraFiltr) {
        this.katedraFiltr = katedraFiltr;
    }

    public void setPredmetFiltr(String predmetFiltr) {
        this.predmetFiltr = predmetFiltr;
    }

    @FXML
    private void okButtonClick(ActionEvent event) {
        dataLayer.commit();
        close(predScena);
    }

    @FXML
    private void cancelButtonClick(ActionEvent event) {
        dataLayer.rollback();
        close(predScena);
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addTeacher(dialog2.getIdV(), dialog2.getJmeno(),
                        dialog2.getPrijmeni(), dialog2.getTitulP(), dialog2.getTitulZ(),
                        dialog2.getTelefon(), dialog2.getMobil(), dialog2.getEmail(),
                        dialog2.getZkratkaKatedry());
                fillTable();
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void upravButtonClick(ActionEvent event) {
        String origID = tableView.getSelectionModel().getSelectedItem().get(0);
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editTeacher(origID, dialog2.getIdV(), dialog2.getJmeno(),
                        dialog2.getPrijmeni(), dialog2.getTitulP(), dialog2.getTitulZ(),
                        dialog2.getTelefon(), dialog2.getMobil(), dialog2.getEmail(),
                        dialog2.getZkratkaKatedry());
                fillTable();
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
        String origID = tableView.getSelectionModel().getSelectedItem().get(0);

        try {
            dataLayer.deleteTeacher(origID);
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }

    private void fillTable() {
        try {
            ResultSet rs = null;
            if (predmetFiltr != null) {
                rs = dataLayer.selectTeachers("", "", predmetFiltr);
            } else if (katedraFiltr != null) {
                rs = dataLayer.selectTeachers(katedraFiltr);
            } else {
                rs = dataLayer.selectTeachers();
            }

            seznam.clear();

            while (rs.next()) {
                List<String> list = FXCollections.observableArrayList(rs.getString("ID_VYUCUJICIHO"),
                        rs.getString("JMENO"), rs.getString("PRIJMENI"), rs.getString("TITUL_PRED"),
                        rs.getString("TITUL_ZA"), rs.getString("TELEFON"), rs.getString("MOBIL"),
                        rs.getString("EMAIL"), rs.getString("ZKRATKA_KATEDRY"), rs.getString("ZKRATKA_FAKULTY"));
                seznam.add(list);
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    @FXML
    private void akceButtonClick(ActionEvent event) {
        if (tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null) {

        } else {
            String origID = tableView.getSelectionModel().getSelectedItem().get(0);
            Parent root;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLRozvrhoveAkce.fxml"));
                root = fxmlLoader.load();
                FXMLRozvrhoveAkceController controller = fxmlLoader.<FXMLRozvrhoveAkceController>getController();
                controller.setVyucId(origID);

                Scene scena = new Scene(root);
                controller.setScenes(aktScena, scena);
                controller.initialize(null, null);
                stageP.setScene(scena);
                stageP.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        if (tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null) {

        } else {
            String origID = tableView.getSelectionModel().getSelectedItem().get(0);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPredmety.fxml"));
            KnihovnaZobrazovani.zobrazPrehledPredmetuUcitele(origID, fxmlLoader, aktScena);
        }
    }

    void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPracoviste.fxml"));
        KnihovnaZobrazovani.zobrazPrehledPracovistBezVyberu(fxmlLoader);
    }

    @FXML
    private void oboryButttonClick(ActionEvent event) {
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLObory.fxml"));
         KnihovnaZobrazovani.zobrazPrehledOboruBezVyberu(fxmlLoader);
    }

    @FXML
    private void prehledPredmetuButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPredmety.fxml")); //nemuze byt staticke
        KnihovnaZobrazovani.zobrazPrehledPredmetuBezVyberu(fxmlLoader);
    }

    @FXML
    private void prehledZamestnancuButtonClick(ActionEvent event) {
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLZamestnanci.fxml")); //nemuze byt staticke
        KnihovnaZobrazovani.zobrazPrehledZamestnancu(fxmlLoader);
    }

   
}
