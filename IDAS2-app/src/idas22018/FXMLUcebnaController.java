/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.close;
import static idas22018.IDAS22018.prejdiZOknaBezCommitu;
import static idas22018.KnihovnaZobrazovani.getKnihovnaZobrazovani;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejUcebnu;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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

    @FXML
    private TableColumn<List<String>, String> colId;

    private ISkolniDB dataLayer;
    private ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    private Scene predScena;
    private Scene aktScena;
    private boolean zmeny = false;
    private LinkedList<Integer> vymazane = new LinkedList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        pridejButton.setDisable(IDAS22018.druhProhlizeni != IDAS22018.RezimProhlizeni.ADMINISTRATOR);
        upravButton.setDisable(IDAS22018.druhProhlizeni != IDAS22018.RezimProhlizeni.ADMINISTRATOR);
        odeberButton.setDisable(IDAS22018.druhProhlizeni != IDAS22018.RezimProhlizeni.ADMINISTRATOR);

        colId.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        nazevCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        kapacitaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        tableView.setItems(seznam);

        fillTable();

    }

    @FXML
    private void okButtonClick(ActionEvent event) {
        dataLayer.commit();
        zmeny = false;
        vymazane.clear();
        close(predScena);
    }

    @FXML
    private void cancelButtonClick(ActionEvent event) {
        dataLayer.rollback();
        zmeny = false;
        vymazane.clear();
        close(predScena);
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejUcebnu dialog = new DialogPridejUcebnu(pridejButton.getParent().getScene().getWindow(), dataLayer.getConnect());
        dialog.showAndWait();

        if (dialog.isButtonPressed()) {
            try {
                dataLayer.addClassroom(dialog.getNazev(), dialog.getKapacita());
                zmeny = true;

                fillTable();
            } catch (SQLException ex) {
                DialogChyba dialogChyba = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void upravButtonClick(ActionEvent event) {
        if (tableView.getSelectionModel().getSelectedItem().get(0) != null) {
            int id_ucebna = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));
            String nazev = tableView.getSelectionModel().getSelectedItem().get(1);
            int kapacita = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(2));

            DialogPridejUcebnu dialog = new DialogPridejUcebnu(pridejButton.getParent().getScene().getWindow(), dataLayer.getConnect(), nazev, kapacita);
            dialog.showAndWait();

            if (dialog.isButtonPressed()) {
                try {
                    dataLayer.editClassroom(id_ucebna, dialog.getNazev(), dialog.getKapacita());
                    zmeny = true;

                    fillTable();
                } catch (SQLException ex) {
                    DialogChyba dialogChyba = new DialogChyba(null, ex.getMessage());
                    dialogChyba.showAndWait();
                }
            }
        }
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
        if (tableView.getSelectionModel().getSelectedItem().get(0) != null) {
            try {
                int id = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));
                dataLayer.deleteClassroom(id);
                vymazane.add(id);
                zmeny = true;
            } catch (SQLException ex) {
                DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void buttonRozvrhoveAkceZobraz(ActionEvent event) {
        if (tableView.getSelectionModel().getSelectedItem().get(0) != null) {
            String roomID = tableView.getSelectionModel().getSelectedItem().get(0);
            if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
                zmeny = false;
                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazRozvrhoveAkce(null, null, roomID, aktScena);
            }
        }
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            getKnihovnaZobrazovani().zobrazPrehledUcitelu();
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            getKnihovnaZobrazovani().zobrazPrehledPredmetu();
        }
    }

    @FXML
    private void oboryButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            getKnihovnaZobrazovani().zobrazPrehledOboru();
        }
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            getKnihovnaZobrazovani().zobrazPrehledPracovist();
        }
    }

    @FXML
    private void zamestnanciButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            getKnihovnaZobrazovani().zobrazPrehledZamestnancu();
        }
    }

    private void fillTable() {
        try {
            ResultSet rs = dataLayer.selectClassromm();

            seznam.clear();

            while (rs.next()) {
                if(!vymazane.contains(rs.getInt("ID_UCEBNA"))){
                    List<String> list = FXCollections.observableArrayList(rs.getString("ID_UCEBNA"), rs.getString("NAZEV"), rs.getString("KAPACITA"));
                    seznam.add(list);
                }
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
