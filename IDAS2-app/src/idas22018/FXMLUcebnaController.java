/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.close;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejUcebnu;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author David
 */
public class FXMLUcebnaController implements Initializable {

    @FXML
    private TableView<List<String>> tableView;
    @FXML
    private Button pridejButton;
    @FXML
    private Button upravButton;
    @FXML
    private Button odeberButton;
    @FXML
    private Button rozvrhoveAkce;
    @FXML
    private TableColumn<List<String>, String> nazevCol;
    @FXML
    private TableColumn<List<String>, String> kapacitaCol;

    private ISkolniDB dataLayer;
    private ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    private Scene predScena;
    private Scene aktScena;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        pridejButton.setDisable(IDAS22018.druhProhlizeni != IDAS22018.RezimProhlizeni.ADMINISTRATOR);
        upravButton.setDisable(IDAS22018.druhProhlizeni != IDAS22018.RezimProhlizeni.ADMINISTRATOR);
        odeberButton.setDisable(IDAS22018.druhProhlizeni != IDAS22018.RezimProhlizeni.ADMINISTRATOR);

        nazevCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        kapacitaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
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
        DialogPridejUcebnu dialog = new DialogPridejUcebnu(pridejButton.getParent().getScene().getWindow(), dataLayer.getConnect());
        dialog.showAndWait();

        if (dialog.isButtonPressed()) {
            try {
                dataLayer.addClassroom("dudu", 10);
                dataLayer.commit();
                //dataLayer.addClassroom(dialog.getZkratkaPredmetu(), dialog.getKapacita());
             
                //dataLayer.commit();
                //fillTable();
            } catch (SQLException ex) {
                System.out.println("pusss");
                DialogChyba dialogChyba = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void upravButtonClick(ActionEvent event) {
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
    }

    @FXML
    private void buttonRozvrhoveAkceZobraz(ActionEvent event) {
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
    }

    @FXML
    private void oboryButtonClick(ActionEvent event) {
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
    }

    @FXML
    private void zamestnanciButtonClick(ActionEvent event) {
    }

    private void fillTable() {
        try {
            ResultSet rs = dataLayer.selectClassromm();

            seznam.clear();

            while (rs.next()) {
                List<String> list = FXCollections.observableArrayList(rs.getString("NAZEV"), rs.getString("KAPACITA"));
                seznam.add(list);
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

}
