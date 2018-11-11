/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejRA;
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
public class FXMLRozvrhoveAkceController implements Initializable {

    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    String vyucId, subjId;

    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private TableColumn<List<String>, String> idCol;
    @FXML
    private TableColumn<List<String>, String> zkratkaCol;
    @FXML
    private TableColumn<List<String>, String> predmetCol;
    @FXML
    private TableColumn<List<String>, String> vyucujiciCol;
    @FXML
    private TableColumn<List<String>, String> roleCol;
    @FXML
    private TableColumn<List<String>, String> zacatekCol;
    @FXML
    private TableColumn<List<String>, String> maHodinCol;
    @FXML
    private TableColumn<List<String>, String> zpusobCol;
    private Scene predScena;
    private Scene aktScena;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = mainController.getDataLayer();

        idCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        zkratkaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        predmetCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        vyucujiciCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(3)));
        roleCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(4)));
        zacatekCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(5)));
        maHodinCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(6)));
        zpusobCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(7)));

        tableView.setItems(seznam);
        fillTable();
    }

    public void setVyucId(String vyucId) {
        this.vyucId = vyucId;
    }
    
    public void setSubjId(String subjId){
        this.subjId = subjId;
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
        DialogPridejRA dialog2 = new DialogPridejRA(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSchedule(dialog2.getPocetStudentu(), dialog2.getZacinaV(),
                        dialog2.getMaHodin(), dialog2.getZkratkaPr(), dialog2.getZpusobVyuky(),
                        dialog2.getRoleVyuc(), dialog2.getIdVyuc());
                fillTable();
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void upravButtonClick(ActionEvent event) {
        int origID = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));
        DialogPridejRA dialog2 = new DialogPridejRA(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSchedule(origID, dialog2.getPocetStudentu(), dialog2.getZacinaV(),
                        dialog2.getMaHodin(), dialog2.getZkratkaPr(), dialog2.getZpusobVyuky(),
                        dialog2.getRoleVyuc(), dialog2.getIdVyuc());
                fillTable();
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
        int origID = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));

        try {
            dataLayer.deleteSchedule(origID);
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
                rs = dataLayer.selectSchedules_byTeacherId(vyucId);
            } else if (subjId != null) {
                rs = dataLayer.selectSchedules(subjId);
            } else {
                rs = dataLayer.selectSchedules();
            }
            seznam.clear();

            while (rs.next()) {
                String tP = rs.getString("TITUL_PRED")==null?"":rs.getString("TITUL_PRED");
                String tZ = rs.getString("TITUL_ZA")==null?"":rs.getString("TITUL_ZA");
                
                List<String> list = FXCollections.observableArrayList(rs.getString("ID_ROZVRHOVE_AKCE"),
                        rs.getString("ZKRATKA_PREDMETU"), rs.getString("NAZEV_PREDMETU"),
                        tP + " " + rs.getString("JMENO_VYUCUJICIHO") + " " +
                        rs.getString("PRIJMENI_VYUCUJICIHO") + " " + tZ,
                        rs.getString("ROLE_VYUCUJICIHO_ROLE"), rs.getString("ZACINAV"),
                        rs.getString("MAHODIN"), rs.getString("ZPUSOB"));
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
}
