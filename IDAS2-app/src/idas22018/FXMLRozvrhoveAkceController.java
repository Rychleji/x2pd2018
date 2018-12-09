package idas22018;

import datovavrstva.ISkolniDB;
import static idas22018.IDAS22018.*;
import idas22018.dialogy.DialogChyba;
import idas22018.dialogy.DialogPridejRA;
import idas22018.dialogy.DialogZobrazRAGraficky;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class FXMLRozvrhoveAkceController implements Initializable {

    ISkolniDB dataLayer;
    ObservableList<List<String>> seznam = FXCollections.observableArrayList();
    String vyucId, subjId, roomId;

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
    @FXML
    private TableColumn<List<String>, String> ucebnaCol;
    @FXML
    private TableColumn<List<String>, String> schvalenoCol;
    @FXML
    private TableColumn<List<String>, String> denCol;
    @FXML
    private Button schvalitButton;
    @FXML
    private Button pridejButton;
    @FXML
    private Button upravButton;
    @FXML
    private Button odeberButton;

    private Scene predScena;
    private Scene aktScena;

    private boolean vlastni = false;
    private boolean zmeny = false;
    private LinkedList<Integer> vymazane = new LinkedList<>();
    @FXML
    private Button zobrazGraficky;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataLayer = GuiFXMLController.getDataLayer();

        schvalitButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR));
        pridejButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR));
        upravButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR));
        odeberButton.setDisable((IDAS22018.druhProhlizeni != RezimProhlizeni.ADMINISTRATOR));
        //zobrazGraficky.setVisible((IDAS22018.druhProhlizeni == RezimProhlizeni.REGISTROVANY));

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
        denCol.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> data) -> new ReadOnlyStringWrapper(data.getValue().get(10)));

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

    public void setRoomId(String room) {
        this.roomId = room;
        if (room != null) {
            fillTable();
        }
    }

    public void setVlastni(boolean vlastni) {
        this.vlastni = vlastni;
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
        DialogPridejRA dialog2 = new DialogPridejRA(schvalitButton.getParent().getScene().getWindow(), dataLayer.getConnect(),
                0, subjId, vyucId == null ? 0 : Integer.parseInt(vyucId), vlastni);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.addSchedule(dialog2.getPocetStudentu(), dialog2.getZacinaV(),
                        dialog2.getMaHodin(), dialog2.getZkratkaPr(), dialog2.getZpusobVyuky(),
                        dialog2.getRoleVyuc(), dialog2.getIdVyuc(), dialog2.getUcebnaId(),
                        dialog2.getDen());
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
                dataLayer.getConnect(), origID, subjId, vyucId == null ? 0 : Integer.parseInt(vyucId), vlastni);
        dialog2.showAndWait();

        if (dialog2.isButtonPressed()) {
            try {
                dataLayer.editSchedule(origID, dialog2.getPocetStudentu(), dialog2.getZacinaV(),
                        dialog2.getMaHodin(), dialog2.getZkratkaPr(), dialog2.getZpusobVyuky(),
                        dialog2.getRoleVyuc(), dialog2.getIdVyuc(), dialog2.getUcebnaId(),
                        dialog2.getDen());
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
            vymazane.add(origID);
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }

    }

    private void fillTable() {
        if (vlastni) { //pokud vlastní, enabluj, pokud ne, nech dle nastavení opravnění
            pridejButton.setDisable(false);
            upravButton.setDisable(false);
            odeberButton.setDisable(false);
        }
        zobrazGraficky.setVisible((vlastni||vyucId!=null)||roomId!=null);
        try {
            ResultSet rs = null;

            if (vyucId != null) {
                rs = dataLayer.selectSchedules_byTeacherId(vyucId);
            } else if (subjId != null) {
                rs = dataLayer.selectSchedules(subjId);
            } else if (roomId != null) {
                rs = dataLayer.selectSchedules_byClassroom(Integer.parseInt(roomId));
            } else {
                rs = dataLayer.selectSchedules();
            }
            seznam.clear();

            while (rs.next()) {
                if (!vymazane.contains(rs.getInt("ID_ROZVRHOVE_AKCE"))
                        && ((IDAS22018.druhProhlizeni == RezimProhlizeni.ADMINISTRATOR || vlastni) ? true : rs.getInt("SCHVALENO") != 0)) {
                    String tP = rs.getString("TITUL_PRED") == null ? "" : rs.getString("TITUL_PRED");
                    String tZ = rs.getString("TITUL_ZA") == null ? "" : rs.getString("TITUL_ZA");

                    List<String> list = FXCollections.observableArrayList(rs.getString("ID_ROZVRHOVE_AKCE"),
                            rs.getString("ZKRATKA_PREDMETU"), rs.getString("NAZEV_PREDMETU"),
                            tP + " " + rs.getString("JMENO_VYUCUJICIHO") + " "
                            + rs.getString("PRIJMENI_VYUCUJICIHO") + " " + tZ,
                            rs.getString("ROLE_VYUCUJICIHO_ROLE"), rs.getString("ZACINAV"),
                            rs.getString("MAHODIN"), rs.getString("ZPUSOB"), rs.getString("NAZEV_UCEBNY"),
                            rs.getString("SCHVALENO"), rs.getString("DENVTYDNU"));
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
            CallableStatement stmt = dataLayer.getConnect().prepareCall("{call schvalAkci(?)}");
            stmt.setInt(1, Integer.parseInt(tableView.getSelectionModel().getSelectedItem().get(0)));

            stmt.executeUpdate();
            zmeny = true;
            fillTable();
        } catch (SQLException ex) {
            DialogChyba dialog = new DialogChyba(null, ex.getMessage());
            dialog.showAndWait();
        }
    }

    @FXML
    private void zobrazGrafickyButton(ActionEvent event) {
        ResultSet rs = null;
        List<String> list = new ArrayList();
            for(List<String> ls : seznam){
                if(!ls.get(9).equalsIgnoreCase("0")){//pokud je předmět schválený
                    list.add(ls.get(1));//zkratka predmetu
                    list.add(ls.get(5));//zacatek
                    list.add(ls.get(6));//delka
                    list.add(ls.get(7));//zpusob
                    if(vyucId!=null)//pokud filtrujeme podle vyučujícího
                        list.add(ls.get(8));//ucebna
                    else if(roomId!=null)//pokud filtrujeme dle učebny
                        list.add(ls.get(3));//vyučující
                    list.add(ls.get(10));//den
                }
            }
        DialogZobrazRAGraficky dialog = new DialogZobrazRAGraficky(schvalitButton.getParent().getScene().getWindow(), list);
        dialog.showAndWait();
    }
}
