/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.close;
import static idas22018.IDAS22018.stageP;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejObor;
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
public class FXMLOboryController implements Initializable {

    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();

    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private TableColumn<List<String>, String> zkratkaCol;
    @FXML
    private TableColumn<List<String>, String> nazevCol;
    @FXML
    private TableColumn<List<String>, String> zkFakultaCol;
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
        zkFakultaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));

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
        Parent root;
        close(predScena);
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejObor dialog2 = new DialogPridejObor(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSpecialization(dialog2.getZkratka(), dialog2.getNazev(),
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
        DialogPridejObor dialog2 = new DialogPridejObor(null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSpecialization(origID, dialog2.getZkratka(), dialog2.getNazev(),
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
        String origID = tableView.getSelectionModel().getSelectedItem().get(0);

        try {
            dataLayer.deleteSpecialization(origID);
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }

    private void fillTable() {
        try {
            ResultSet rs = dataLayer.selectSpecializations();
            seznam.clear();

            while (rs.next()) {
                List<String> list = FXCollections.observableArrayList(rs.getString("ZKRATKA_OBORU"),
                        rs.getString("NAZEV_OBORU"), rs.getString("ZKRATKA_FAKULTY"));
                seznam.add(list);
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        Parent root;
        if (tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null) {
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLOborPredmet.fxml"));
                root = fxmlLoader.load();
                FXMLOborPredmetController controller = fxmlLoader.<FXMLOborPredmetController>getController();

                controller.setFilterId(tableView.getSelectionModel().getSelectedItem().get(0));
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
    private void vyucijiciButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVyucujici.fxml"));
            root = fxmlLoader.load();
            FXMLVyucujiciController controller = fxmlLoader.<FXMLVyucujiciController>getController();

            Scene scena = new Scene(root);
            //controller.setDataLayer(dataLayer);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPracoviste.fxml"));
            root = fxmlLoader.load();
            FXMLPracovisteController controller = fxmlLoader.<FXMLPracovisteController>getController();

            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void prehledPredmetuButtonClick(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPredmety.fxml"));
            root = fxmlLoader.load();
            FXMLPredmetyController controller = fxmlLoader.<FXMLPredmetyController>getController();
            Scene scena = new Scene(root);
            controller.setScenes(IDAS22018.mainScene, scena);
            stageP.setScene(scena);
            stageP.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
