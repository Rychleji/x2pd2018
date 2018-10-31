/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejKatedru;
import idas22018.dialogy.DialogPridejRA;
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
public class FXMLPracovisteController implements Initializable {

    private Scene predScena;
    private Scene aktScena;
    
    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    
    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private TableColumn<List<String>, String> zkKatedraCol;
    @FXML
    private TableColumn<List<String>, String> katedraCol;
    @FXML
    private TableColumn<List<String>, String> zkFakultaCol;
    @FXML
    private TableColumn<List<String>, String> fakultaCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = mainController.getDataLayer();

        zkKatedraCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        katedraCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        zkFakultaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        fakultaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(3)));

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
        DialogPridejKatedru dialog2 = new DialogPridejKatedru(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addDepartment(dialog2.getZkratka(), dialog2.getNazev(),
                        dialog2.getZkratkaFak());
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
        DialogPridejKatedru dialog2 = new DialogPridejKatedru(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editDepartment(origID, dialog2.getZkratka(), dialog2.getNazev(),
                        dialog2.getZkratkaFak());
                fillTable();
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
        String origID =tableView.getSelectionModel().getSelectedItem().get(0);

        try {
            dataLayer.deleteDepartment(origID);
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }

    }

    private void fillTable() {
        try {
            ResultSet rs = dataLayer.selectWorkSpaces();
            seznam.clear();

            while (rs.next()) {
                List<String> list = FXCollections.observableArrayList(rs.getString("ZKRATKA_KATEDRY"),
                        rs.getString("NAZEV_KATEDRY"), rs.getString("ZKRATKA_FAKULTY"),
                        rs.getString("NAZEV_FAKULTY"));
                seznam.add(list);
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
            root = fxmlLoader.load();
            FXMLVyucujiciController controller = fxmlLoader.<FXMLVyucujiciController>getController();
            
            Scene scena = new Scene(root);
            controller.setKatedraFiltr(tableView.getSelectionModel().getSelectedItem().get(0));
            controller.setDataLayer(dataLayer);
            controller.setScenes(aktScena, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }
    
}