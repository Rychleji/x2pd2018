/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejPredmet;
import idas22018.dialogy.DialogPridejVyucujiciho;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Radim
 */
public class FXMLPredmetyController implements Initializable {

    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    String vyucId;

    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private TableColumn<List<String>, String> zkratkaCol;
    @FXML
    private TableColumn<List<String>, String> nazevCol;
    @FXML
    private TableColumn<List<String>, String> rocnikCol;
    @FXML
    private TableColumn<List<String>, String> semestrCol;
    @FXML
    private TableColumn<List<String>, String> formaCol;
    @FXML
    private TableColumn<List<String>, String> zakonceniCol;
    @FXML
    private TableColumn<List<String>, String> pocetStudentuCol;
    private Scene predScena;
    private Scene aktScena;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        zkratkaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        nazevCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        rocnikCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        semestrCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(3)));
        formaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(4)));
        zakonceniCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(5)));
        pocetStudentuCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(6)));

        tableView.setItems(seznam);
        fillTable();
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

    public void setVyucId(String vyucId) {
        this.vyucId = vyucId;
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejPredmet dialog2 = new DialogPridejPredmet(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSubject(dialog2.getZkratka(), dialog2.getNazev(),
                        dialog2.getRocnik(), dialog2.getSemestr(), dialog2.getZpusobZak(),
                        dialog2.getForma());
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
        DialogPridejPredmet dialog2 = new DialogPridejPredmet(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSubject(origID, dialog2.getZkratka(), dialog2.getNazev(),
                        dialog2.getRocnik(), dialog2.getSemestr(), dialog2.getZpusobZak(),
                        dialog2.getForma());
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
            dataLayer.deleteSubject(origID);
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }

    private void fillTable() {
        try {
            ResultSet rs = null;
            if (vyucId != null) {
                rs = dataLayer.selectSubjects(vyucId);
            } else {
                rs = dataLayer.selectSubjects();
            }
            seznam.clear();

            while (rs.next()) {
                List<String> list = FXCollections.observableArrayList(rs.getString("ZKRATKA_PREDMETU"),
                        rs.getString("NAZEV_PREDMETU"), rs.getString("DOPORUCENY_ROCNIK"),
                        rs.getString("SEM"), rs.getString("FORMA"), rs.getString("ZPUSOB_ZAK"), rs.getString("POCET_STUDENTU"));
                seznam.add(list);
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    @FXML
    private void oboryButtonClick(ActionEvent event) {
        Parent root;
        if (tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null) {
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLOborPredmet.fxml"));
                root = fxmlLoader.load();
                FXMLOborPredmetController controller = fxmlLoader.<FXMLOborPredmetController>getController();

                Scene scena = new Scene(root);
                controller.setSubjId(tableView.getSelectionModel().getSelectedItem().get(0));
                controller.initialize(null, null);
                controller.setScenes(aktScena, scena);
                stageP.setScene(scena);
                stageP.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void raButtonClick(ActionEvent event) {
        if (tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null) {

        } else {
            String origID = tableView.getSelectionModel().getSelectedItem().get(0);
            Parent root;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLRozvrhoveAkce.fxml"));
                root = fxmlLoader.load();
                FXMLRozvrhoveAkceController controller = fxmlLoader.<FXMLRozvrhoveAkceController>getController();
                controller.setSubjId(origID);
                controller.initialize(null, null);
                Scene scena = new Scene(root);
                controller.setScenes(aktScena, scena);
                stageP.setScene(scena);
                stageP.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
        KnihovnaZobrazovani.zobrazPredhledUcitelu(fxmlLoader);
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPracoviste.fxml"));
        KnihovnaZobrazovani.zobrazPrehledPracovistBezVyberu(fxmlLoader);
    }

    @FXML
    private void zamestnanciButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLZamestnanci.fxml"));
        KnihovnaZobrazovani.zobrazPrehledZamestnancu(fxmlLoader);
    }

    @FXML
    private void prehledOboruButtonClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLObory.fxml"));
        KnihovnaZobrazovani.zobrazPrehledOboruBezVyberu(fxmlLoader);
    }

}
