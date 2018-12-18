package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejVyucujiciho;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

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
    @FXML
    private TableColumn<List<String>, String> roleCol;
    @FXML
    private TableColumn<List<String>, String> opravneniCol;
    @FXML
    private ImageView imageView;
    @FXML
    private Button pridejButton;
    @FXML
    private Button upravButton;
    @FXML
    private Button odeberButton;
    @FXML
    private Label nadpisLabel;
    @FXML
    private Button akceFiltrButton;
    @FXML
    private Button predmetyFiltrButton;
    @FXML
    private Button zamestnanciButton;
    @FXML
    private Button nahrajObrazekBtn;
    @FXML
    private Button smazObrazekBtn;
    
    private Scene predScena;
    private Scene aktScena;
    private boolean skrytControlsProVyucujici = false;
    private ISkolniDB dataLayer;    
    private boolean zmeny = false;
    private LinkedList<Integer> vymazane = new LinkedList<>();
    
    public void setSkrytVeci(boolean skryt) {
        skrytControlsProVyucujici = skryt;
        fillTable();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        pridejButton.setDisable(IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR);
        upravButton.setDisable(IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR);
        odeberButton.setDisable(IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR);
        nahrajObrazekBtn.setDisable(IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR);
        smazObrazekBtn.setDisable(IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR);

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
        roleCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(10)));
        opravneniCol.setCellValueFactory((CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(11)));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            tableClickItem();
        });
        tableView.setItems(seznam);
        fillTable();
    }

    public void setKatedraFiltr(String katedraFiltr) {
        this.katedraFiltr = katedraFiltr;
        if (katedraFiltr != null) {
            fillTable();
        }
    }

    public void setPredmetFiltr(String predmetFiltr) {
        this.predmetFiltr = predmetFiltr;
        if (predmetFiltr != null) {
            fillTable();
        }
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
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(tableView.getParent().getScene().getWindow(),
                !skrytControlsProVyucujici, dataLayer.getConnect());
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addTeacher(dialog2.getJmeno(),
                        dialog2.getPrijmeni(), dialog2.getTitulP(), dialog2.getTitulZ(),
                        dialog2.getTelefon(), dialog2.getMobil(), dialog2.getEmail(),
                        dialog2.getZkratkaKatedry(), dialog2.getRole(), dialog2.getOpravneni(),
                        dialog2.getuJmeno(), dialog2.getuHeslo());
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
        DialogPridejVyucujiciho dialog2 = new DialogPridejVyucujiciho(tableView.getParent().getScene().getWindow(),
                !skrytControlsProVyucujici, Integer.parseInt(origID), dataLayer.getConnect(), false);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editTeacher(Integer.parseInt(origID), dialog2.getJmeno(),
                        dialog2.getPrijmeni(), dialog2.getTitulP(), dialog2.getTitulZ(),
                        dialog2.getTelefon(), dialog2.getMobil(), dialog2.getEmail(),
                        dialog2.getZkratkaKatedry(), dialog2.getRole(), dialog2.getOpravneni(),
                        dialog2.getuJmeno(), dialog2.getuHeslo());
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
            dataLayer.deleteTeacher(origID);
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
            if (!skrytControlsProVyucujici) {
                if (predmetFiltr != null) {
                    rs = dataLayer.selectTeachers("", "", predmetFiltr);
                } else if (katedraFiltr != null) {
                    skrytControlsProVyucujici = true;
                    rs = dataLayer.selectTeachers(katedraFiltr);
                } else {
                    rs = dataLayer.selectTeachers();
                }
            } else {
                rs = dataLayer.selectEmployees();
            }
            
            nadpisLabel.setText(skrytControlsProVyucujici ? "Zaměstnanci" : "Vyučující");
            akceFiltrButton.setVisible(!skrytControlsProVyucujici);
            predmetyFiltrButton.setVisible(!skrytControlsProVyucujici);
            zamestnanciButton.setText(skrytControlsProVyucujici ? "Vyučující" : "Zaměstnanci");

            seznam.clear();

            while (rs.next()) {
                if (!vymazane.contains(rs.getInt(skrytControlsProVyucujici ? "ID_ZAMESTNANEC" : "ID_VYUCUJICIHO"))){
                    List<String> list = FXCollections.observableArrayList(rs.getString(skrytControlsProVyucujici ? "ID_ZAMESTNANEC" : "ID_VYUCUJICIHO"),
                            rs.getString("JMENO"), rs.getString("PRIJMENI"), rs.getString("TITUL_PRED"),
                            rs.getString("TITUL_ZA"), rs.getString("TELEFON"), rs.getString("MOBIL"),
                            rs.getString("EMAIL"), rs.getString("ZKRATKA_KATEDRY"), rs.getString("ZKRATKA_FAKULTY"),
                            skrytControlsProVyucujici?rs.getString("TYPROLE"):"Vyučující", rs.getString("OPRAVNENI"));
                    seznam.add(list);
                }
            }
        } catch (SQLException ex) {
            DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
            dialog2.showAndWait();
        }
    }

    @FXML
    private void akceButtonClick(ActionEvent event) {
        if ((!tableView.getItems().isEmpty()) && (tableView.getSelectionModel().getSelectedItem() != null)) {
            if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
                zmeny = false;
                String origID = tableView.getSelectionModel().getSelectedItem().get(0);
                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazRozvrhoveAkce(origID, null, null, aktScena);
            }
        }
    }

    @FXML
    private void predmetyButtonClick(ActionEvent event) {
        if (!(tableView.getItems().isEmpty() || tableView.getSelectionModel().getSelectedItem() == null)) {
            if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
                zmeny = false;
                String origID = tableView.getSelectionModel().getSelectedItem().get(0);

                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPredmetu(origID, aktScena);
            }
        }
    }

    void setScenes(Scene predScena, Scene aktScena) {
        this.predScena = predScena;
        this.aktScena = aktScena;
    }

    @FXML
    private void pracovisteButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPracovist();
        }
    }

    @FXML
    private void oboryButttonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledOboru();
        }
    }

    @FXML
    private void prehledPredmetuButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledPredmetu();
        }
    }

    @FXML
    private void prehledZamestnancuButtonClick(ActionEvent event) {
        if ((zmeny && prejdiZOknaBezCommitu()) || !zmeny) {
            zmeny = false;
            if (!skrytControlsProVyucujici) {
                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledZamestnancu();
            } else {
                KnihovnaZobrazovani.getKnihovnaZobrazovani().zobrazPrehledUcitelu();
            }
        }
    }

    @FXML
    private void pridejFotoButton(ActionEvent event) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            List<String> obj = tableView.getSelectionModel().getSelectedItem();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(null);

            int id = Integer.parseInt(obj.get(0));
            if (file != null) {
                try {
                    InputStream is = new FileInputStream(file);
                    dataLayer.addPicture(is, id);
                    zmeny = true;
                } catch (FileNotFoundException | SQLException ex) {
                    DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                    dialog2.showAndWait();
                }
            }
            tableView.getSelectionModel().clearSelection();
            tableView.getSelectionModel().select(obj);
        }

    }

    @FXML
    private void smazObrazekButton(ActionEvent event) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            List<String> obj = tableView.getSelectionModel().getSelectedItem();
            int id = Integer.parseInt(obj.get(0));

            try {
                dataLayer.deletePicture(id);
                imageView.setImage(null);
                zmeny = true;
            } catch (SQLException ex) {
                DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                dialog2.showAndWait();
            }

            tableView.getSelectionModel().clearSelection();
            tableView.getSelectionModel().select(obj);
        }
    }

    //@FXML
    private void tableClickItem(/*MouseEvent event*/) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            int id = Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0));
            Image image = null;
            try {
                imageView.setImage(null);
                InputStream inputStream = dataLayer.selectPictureToTeacher(id);
                if(inputStream != null){
                    image = new Image(inputStream);
                    imageView.setImage(image);
                }
            } catch (SQLException ex) {
                DialogChyba dialog2 = new DialogChyba(null, ex.getMessage());
                dialog2.showAndWait();
            }

        }
    }

}
