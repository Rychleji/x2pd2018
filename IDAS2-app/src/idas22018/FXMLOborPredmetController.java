package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejOborPredmet;
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
    @FXML
    private Button pridejButton;
    @FXML
    private Button upravButton;
    @FXML
    private Button odeberButton;
    
    private boolean zmeny = false;
    
    class PrimKlic{
        String predmet, obor;
        PrimKlic(String pred, String ob){
            predmet = pred;
            obor = ob;
        }
    }
    
    private LinkedList<PrimKlic> vymazane = new LinkedList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        pridejButton.setDisable(IDAS22018.druhProhlizeni!=RezimProhlizeni.ADMINISTRATOR);
        upravButton.setDisable(IDAS22018.druhProhlizeni!=RezimProhlizeni.ADMINISTRATOR);
        odeberButton.setDisable(IDAS22018.druhProhlizeni!=RezimProhlizeni.ADMINISTRATOR);
        
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
        DialogPridejOborPredmet dialog2 = new DialogPridejOborPredmet(pridejButton.getParent().getScene().getWindow(),
                dataLayer.getConnect(), idOboru, idPredmetu, idOboru!=null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSpecializationSubject(dialog2.getObor(),
                        dialog2.getPredmet(), dialog2.getKategorie());
                zmeny = true;
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

        DialogPridejOborPredmet dialog2 = new DialogPridejOborPredmet(pridejButton.getParent().getScene().getWindow(),
                dataLayer.getConnect(), origID2, origID, idOboru!=null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSpecializationSubject(origID2, origID, dialog2.getObor(),
                        dialog2.getPredmet(), dialog2.getKategorie());
                zmeny = true;
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
            zmeny = true;
            vymazane.add(new PrimKlic(origID2, origID));
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
                boolean jeVKolekci = false;
                for(PrimKlic pk:vymazane){
                    if (pk.obor.equalsIgnoreCase(rs.getString("ZKRATKA_OBORU")) || pk.predmet.equalsIgnoreCase(rs.getString("ZKRATKA_PREDMETU"))){
                        jeVKolekci = true;
                        break;
                    }
                }
                if (!jeVKolekci){
                    List<String> list = FXCollections.observableArrayList(
                            rs.getString("ZKRATKA_PREDMETU"), rs.getString("NAZEV_PREDMETU"),
                            rs.getString("ZKRATKA_OBORU"), rs.getString("NAZEV_OBORU"),
                            rs.getString("KATEGORIE_PREDMETU_KATEGORIE"));
                    seznam.add(list);
                }
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
        if (idOboru != null) {
            fillTable();
        }
    }

    public void setSubjId(String subjId) {
        this.idPredmetu = subjId;
        if (subjId != null) {
            fillTable();
        }
    }

    @FXML
    private void vyucujiciButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledUcitelu();
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPredmetu();
        }
    }

    @FXML
    private void oboryButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledOboru();
        }
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPracovist();
        }
    }
}
