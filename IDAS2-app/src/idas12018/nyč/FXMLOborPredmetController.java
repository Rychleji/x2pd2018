/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas12018.nyč;

import datovavrstva.ISkolniDB;
import static idas12018.nyč.IDAS12018Nyč.*;
import idas12018.nyč.dialogy.DialogChyba;
import idas12018.nyč.dialogy.DialogPridejOborPredmet;
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
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Radim
 */
public class FXMLOborPredmetController implements Initializable {

    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    Scene predScena;
    Scene aktScena;

    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private TableColumn<List<String>, String> zkPredmetCol;
    @FXML
    private TableColumn<List<String>, String> predmetCol;
    @FXML
    private TableColumn<List<String>, String> zkOborCol;
    @FXML
    private TableColumn<List<String>, String> oborCol;
    @FXML
    private TableColumn<List<String>, String> kategorieCol;
    private String idOboru;
    private String idPredmetu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = IDAS12018Nyč.mainController.getDataLayer();

        zkPredmetCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        predmetCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        zkOborCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        oborCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(3)));
        kategorieCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(4)));

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

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejOborPredmet dialog2 = new DialogPridejOborPredmet(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSpecializationSubject(dialog2.getObor(),
                        dialog2.getPredmet(), dialog2.getKategorie());
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
        String origID2 = tableView.getSelectionModel().getSelectedItem().get(2);

        DialogPridejOborPredmet dialog2 = new DialogPridejOborPredmet(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSpecializationSubject(origID2, origID, dialog2.getObor(),
                        dialog2.getPredmet(), dialog2.getKategorie());
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
        String origID2 = tableView.getSelectionModel().getSelectedItem().get(2);

        try {
            dataLayer.deleteSpecializationSubject(origID2, origID);
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }

    private void fillTable() {
        try {
            ResultSet rs = null;
            if (idOboru == null) {
                rs = dataLayer.selectSpecializationSubjects_bySubj(this.idPredmetu);
            } else {
                rs = dataLayer.selectSpecializationSubjects(this.idOboru);
            }

            seznam.clear();

            while (rs.next()) {
                List<String> list = FXCollections.observableArrayList(
                        rs.getString("ZKRATKA_PREDMETU"), rs.getString("NAZEV_PREDMETU"),
                        rs.getString("ZKRATKA_OBORU"), rs.getString("NAZEV_OBORU"),
                        rs.getString("KATEGORIE_PREDMETU_KATEGORIE"));
                seznam.add(list);
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    public void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

    public void setFilterId(String idOboru) {
        this.idOboru = idOboru;
    }

    public void setSubjId(String subjId) {
        this.idPredmetu = subjId;
    }
}
