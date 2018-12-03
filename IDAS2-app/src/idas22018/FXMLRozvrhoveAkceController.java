package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejRA;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    @FXML
    private TableColumn<List<String>, String> ucebnaCol;
    @FXML
    private TableColumn<List<String>, String> schvalenoCol;
    @FXML
    private Button schvalitButton;
    @FXML
    private Button pridejButton;
    @FXML
    private Button upravButton;
    @FXML
    private Button odeberButton;

    boolean vlastni = false;
    boolean zmeny = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        schvalitButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR));
        pridejButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR) || vlastni);
        upravButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR) || vlastni);
        odeberButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR) || vlastni);

        idCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        zkratkaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(1)));
        predmetCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(2)));
        vyucujiciCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(3)));
        roleCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(4)));
        zacatekCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(5)));
        maHodinCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(6)));
        zpusobCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(7)));
        ucebnaCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(8)));
        schvalenoCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(9)));

        tableView.setItems(seznam);
        fillTable();
    }

    public void setVyucId(String vyucId) {
        this.vyucId = vyucId;
        if (vyucId != null) {
            fillTable();
        }
    }

    public void setSubjId(String subjId) {
        this.subjId = subjId;
        if (subjId != null) {
            fillTable();
        }
    }

    @FXML
    private void okButtonClick(ActionEvent event) {
        dataLayer.commit();
        zmeny = false;
        close(predScena);
    }

    @FXML
    private void cancelButtonClick(ActionEvent event) {
        dataLayer.rollback();
        zmeny = false;
        close(predScena);
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejRA dialog2 = new DialogPridejRA(schvalitButton.getParent().getScene().getWindow(), dataLayer.getConnect(),
                0, subjId, vyucId == null ? 0 : Integer.parseInt(vyucId));
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSchedule(dialog2.getPocetStudentu(), dialog2.getZacinaV(),
                        dialog2.getMaHodin(), dialog2.getZkratkaPr(), dialog2.getZpusobVyuky(),
                        dialog2.getRoleVyuc(), dialog2.getIdVyuc(), dialog2.getUcebnaId());
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
        int origID = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));
        DialogPridejRA dialog2 = new DialogPridejRA(schvalitButton.getParent().getScene().getWindow(),
                dataLayer.getConnect(), origID, subjId, vyucId == null ? 0 : Integer.parseInt(vyucId));
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSchedule(origID, dialog2.getPocetStudentu(), dialog2.getZacinaV(),
                        dialog2.getMaHodin(), dialog2.getZkratkaPr(), dialog2.getZpusobVyuky(),
                        dialog2.getRoleVyuc(), dialog2.getIdVyuc(), dialog2.getUcebnaId());
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
        int origID = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));

        try {
            dataLayer.deleteSchedule(origID);
            zmeny = true;
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
                String tP = rs.getString("TITUL_PRED") == null ? "" : rs.getString("TITUL_PRED");
                String tZ = rs.getString("TITUL_ZA") == null ? "" : rs.getString("TITUL_ZA");

                List<String> list = FXCollections.observableArrayList(rs.getString("ID_ROZVRHOVE_AKCE"),
                        rs.getString("ZKRATKA_PREDMETU"), rs.getString("NAZEV_PREDMETU"),
                        tP + " " + rs.getString("JMENO_VYUCUJICIHO") + " "
                        + rs.getString("PRIJMENI_VYUCUJICIHO") + " " + tZ,
                        rs.getString("ROLE_VYUCUJICIHO_ROLE"), rs.getString("ZACINAV"),
                        rs.getString("MAHODIN"), rs.getString("ZPUSOB"), rs.getString("NAZEV_UCEBNY"),
                        rs.getString("SCHVALENO"));
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

    @FXML
    private void zamestnanciButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledZamestnancu();
        }
    }

    @FXML
    private void onSchvalitButtonClick(ActionEvent event) {
        try {
            Statement st = dataLayer.getConnect().createStatement();
            st.execute(String.format("EXEC schvalAkci(%d)", Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0))));
            zmeny = true;
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }
}
