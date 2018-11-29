package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejVyucujiciho;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

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
        if(katedraFiltr!=null)
            fillTable();
    }

    public void setPredmetFiltr(String predmetFiltr) {
        this.predmetFiltr = predmetFiltr;
        if (predmetFiltr != null)
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
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(null);
        dialog2.showAndWait();

        /*if (dialog2.isButtonPressed()) {
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
        }*///TODO
    }

    @FXML
    private void upravButtonClick(ActionEvent event) {
        String origID = tableView.getSelectionModel().getSelectedItem().get(0);
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(null);
        dialog2.showAndWait();

        /*if (dialog2.isButtonPressed()) {
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
        }*///TODO
    }

    @FXML
    private void odeberButtonClick(ActionEvent event) {
        int origID = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));

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
        if ((!tableView.getItems().isEmpty()) && (tableView.getSelectionModel().getSelectedItem() != null)) {
            String origID = tableView.getSelectionModel().getSelectedItem().get(0);
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazRozvrhoveAkce(origID, null, aktScena);
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        if (!(tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null)) {
            String origID = tableView.getSelectionModel().getSelectedItem().get(0);
            
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPredmetu(origID, aktScena);
        }
    }

    void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPracovist();
    }

    @FXML
    private void oboryButttonClick(ActionEvent event) {
         KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledOboru();
    }

    @FXML
    private void prehledPredmetuButtonClick(ActionEvent event) {
        KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPredmetu();
    }

    @FXML
    private void prehledZamestnancuButtonClick(ActionEvent event) {
        KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledZamestnancu();
    }

   
}
