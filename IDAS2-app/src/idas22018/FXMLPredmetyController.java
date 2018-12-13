package idas22018;

import datovavrstva.ISkolniDB;
import datovavrstva.XMLLoader;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejPredmet;
import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.xml.bind.JAXBException;

public class FXMLPredmetyController implements Initializable {

    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    String vyucId;
    LinkedList<String> vymazane = new LinkedList<>();

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
    private boolean zmeny = false;
    @FXML
    private Button pridejButton;
    @FXML
    private Button upravButton;
    @FXML
    private Button odeberButton;
    @FXML
    private VBox controlsVBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();
        
        pridejButton.setDisable(IDAS22018.druhProhlizeni!=RezimProhlizeni.ADMINISTRATOR);
        upravButton.setDisable(IDAS22018.druhProhlizeni!=RezimProhlizeni.ADMINISTRATOR);
        odeberButton.setDisable(IDAS22018.druhProhlizeni!=RezimProhlizeni.ADMINISTRATOR);
        if(IDAS22018.druhProhlizeni == RezimProhlizeni.ADMINISTRATOR){
            Button butt = new Button("Import");
            butt.setPrefWidth(odeberButton.getPrefWidth());
            butt.setOnAction((event) -> {
                FileChooser fileChoicer = new FileChooser();
                fileChoicer.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XML", "*.xml")
                );
                fileChoicer.setTitle("Zvol soubor");
                File file = fileChoicer.showOpenDialog(odeberButton.getParent().getScene().getWindow());

                try {
                    XMLLoader.importPredmetu(file.getAbsolutePath());
                } catch (JAXBException | FileNotFoundException ex) {
                    DialogChyba dialog = new DialogChyba(null, ex.getMessage());
                    dialog.showAndWait();
                    //Logger.getLogger(FXMLPredmetyController.class.getName()).log(Level.SEVERE, null, ex);
                }
                fillTable();
            });
            
            controlsVBox.getChildren().add(butt);
        }
        
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

    public void setVyucId(String vyucId) {
        this.vyucId = vyucId;
        if (vyucId != null)
            fillTable();
    }

    @FXML
    private void pridejButtonClick(ActionEvent event) {
        DialogPridejPredmet dialog2 = new DialogPridejPredmet(
                tableView.getParent().getScene().getWindow(), null, null);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSubject(dialog2.getZkratka(), dialog2.getNazev(),
                        dialog2.getRocnik(), dialog2.getSemestr(), dialog2.getZpusobZak(),
                        dialog2.getForma());
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
        DialogPridejPredmet dialog2 = new DialogPridejPredmet(
                tableView.getParent().getScene().getWindow(), dataLayer.getConnect(), origID);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSubject(origID, dialog2.getZkratka(), dialog2.getNazev(),
                        dialog2.getRocnik(), dialog2.getSemestr(), dialog2.getZpusobZak(),
                        dialog2.getForma());
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

        try {
            dataLayer.deleteSubject(origID);
            zmeny = true;
            vymazane.add(origID);
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
                if(!vymazane.contains(rs.getString("ZKRATKA_PREDMETU"))){
                    List<String> list = FXCollections.observableArrayList(rs.getString("ZKRATKA_PREDMETU"),
                            rs.getString("NAZEV_PREDMETU"), rs.getString("DOPORUCENY_ROCNIK"),
                            rs.getString("SEM"), rs.getString("FORMA"), rs.getString("ZPUSOB_ZAK"), rs.getString("POCET_STUDENTU"));
                    seznam.add(list);
                }
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    @FXML
    private void oboryButtonClick(ActionEvent event) {
        Parent root;
        if (!(tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null)) {
            if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
                zmeny = false;
                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazVazbyOborPredmet(null, tableView.getSelectionModel().getSelectedItem().get(0), aktScena);
            }
        }
    }

    @FXML
    private void raButtonClick(ActionEvent event) {
        if (!(tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null)) {
            if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
                zmeny = false;
                String origID = tableView.getSelectionModel().getSelectedItem().get(0);
                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazRozvrhoveAkce(null, origID, null, aktScena);
            }
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
    private void prehledOboruButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledOboru();
        }
    }

}
