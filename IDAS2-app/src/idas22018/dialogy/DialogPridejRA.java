/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

import idas22018.GuiFXMLController;
import idas22018.GuiFXMLController.HelpClass;
import idas22018.IDAS22018;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Radim Nyč
 */
public class DialogPridejRA extends Stage {

    private class Vyucujici {

        public int id;
        public String jmeno;
        public String prijmeni;
        public String titul;
        public String titulZa;

        public Vyucujici(int id, String jmeno, String prijmeni, String titul, String titulZa) {
            this.id = id;
            this.jmeno = jmeno;
            this.prijmeni = prijmeni;
            this.titul = titul;
            this.titulZa = titulZa;
        }

        @Override
        public String toString() {
            return (titul != null ? titul + ' ' : "") + jmeno + ' ' + prijmeni + (titulZa != null ? ' ' + titulZa : "");
        }

    }

    private boolean buttonPressed = false;

    int pocetStudentu = 15, zpusobVyuky = 0, ucebnaId, idVyuc;

    double zacinaV = 8.0, maHodin = 2.0;

    private String zkratkaPr, roleVyuc, den;

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public float getMaHodin() {
        return (float) maHodin;
    }

    public int getPocetStudentu() {
        return pocetStudentu;
    }

    public int getZpusobVyuky() {
        return zpusobVyuky;
    }

    public float getZacinaV() {
        return (float) zacinaV;
    }

    public String getZkratkaPr() {
        return zkratkaPr;
    }

    public int getIdVyuc() {
        return idVyuc;
    }

    public String getRoleVyuc() {
        return roleVyuc;
    }

    public int getUcebnaId() {
        return ucebnaId;
    }

    public String getDen() {
        return den;
    }

    public DialogPridejRA(Window okno, Connection conn, int idRA, String zkratkaPredmetuFiltr, int idVyucFiltr, boolean vlastni) {
        setTitle("Rozvrhová akce");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(conn, idRA, zkratkaPredmetuFiltr, idVyucFiltr, vlastni));
    }

    private Scene vytvorScenu(Connection conn, int idRA, String zkratkaPredmetu, int idVyucFiltr, boolean vlastni) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

        // Mřížka s TextFieldy a Labely
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.BOTTOM_CENTER);
        grid2.setPadding(new Insets(10));

        Statement stmt;
        ResultSet rs;

        if (idRA != 0) {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW where ID_ROZVRHOVE_AKCE = " + String.valueOf(idRA));
                rs.next();
                pocetStudentu = rs.getInt("POCET_STUDENTU");
                zpusobVyuky = rs.getInt("ID_ZPUSOBU");
                ucebnaId = rs.getInt("ID_UCEBNY");
                idVyuc = rs.getInt("ID_VYUCUJICIHO");
                zacinaV = rs.getFloat("ZACINAV");
                maHodin = rs.getFloat("MAHODIN");
                zkratkaPr = rs.getString("ZKRATKA_PREDMETU");
                roleVyuc = rs.getString("ROLE_VYUCUJICIHO_ROLE");
                den = rs.getString("DENVTYDNU");
            } catch (SQLException ex) {
                Logger.getLogger(DialogPridejRA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Komponenty
        ComboBox<String> zkratkaPrCb = new ComboBox<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from PREDMET_EXT_VIEW");

            while (rs.next()) {
                zkratkaPrCb.getItems().add(rs.getString("ZKRATKA_PREDMETU"));
            }

            if (idRA == 0) { //při přidávání
                if (zkratkaPredmetu != null) {
                    zkratkaPrCb.getSelectionModel().select(zkratkaPredmetu);
                } else {
                    zkratkaPrCb.getSelectionModel().selectFirst();
                }
            } else { //editace
                zkratkaPrCb.getSelectionModel().select(zkratkaPr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DialogPridejRA.class.getName()).log(Level.SEVERE, null, ex);
        }
        ComboBox<Vyucujici> vyucCb = new ComboBox<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from VYUC_VIEW");

            while (rs.next()) {
                vyucCb.getItems().add(new Vyucujici(rs.getInt("ID_VYUCUJICIHO"), rs.getString("JMENO"),
                        rs.getString("PRIJMENI"), rs.getString("TITUL_PRED"), rs.getString("TITUL_ZA")));
            }
            if (idRA == 0) {
                if (idVyucFiltr != 0) {
                    Vyucujici sel = null;
                    for (Vyucujici v : vyucCb.getItems()) {
                        if (v.id == idVyucFiltr) {
                            sel = v;
                            break;
                        }
                    }
                    vyucCb.getSelectionModel().select(sel);
                } else {
                    vyucCb.getSelectionModel().selectFirst();
                }
            } else {
                Vyucujici sel = null;
                for (Vyucujici v : vyucCb.getItems()) {
                    if (v.id == idVyuc) {
                        sel = v;
                        break;
                    }
                }
                vyucCb.getSelectionModel().select(sel);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DialogPridejRA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        vyucCb.setDisable(vlastni);
        
        Spinner<Double> zacinaVSp = new Spinner<>(7.0, 19.0, zacinaV, 1); // inkrement po jedne hodine, fakt nehodlam resit graficky znazorneni pulhodin 
        Spinner<Integer> pocetStudentuSp = new Spinner<>(1, Integer.MAX_VALUE - 1, pocetStudentu, 1);
        Spinner<Double> maHodinSp = new Spinner<>(1, 5.0, maHodin, 1); //to same
        ComboBox<String> ucebnaCb = new ComboBox<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from UCEBNA");

            while (rs.next()) {
                ucebnaCb.getItems().add(rs.getString("NAZEV"));
            }

            if (idRA == 0) {
                ucebnaCb.getSelectionModel().selectFirst();
            } else {
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("select nazev from ucebna where id_ucebna = " + ucebnaId);
                rs2.next();
                ucebnaCb.getSelectionModel().select(rs2.getString("NAZEV"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DialogPridejRA.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObservableList<String> list1 = FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikRoleVyuc());
        ObservableList<GuiFXMLController.HelpClass> list2 = FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikZpusobVyuky().values());
        ObservableList<String> list3 = FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikDen());

        ComboBox<String> roleCB = new ComboBox<>(list1);
        ComboBox<GuiFXMLController.HelpClass> zpusobCB = new ComboBox<>(list2);
        ComboBox<String> denCb = new ComboBox<>(list3);

        if (idRA == 0) {
            roleCB.getSelectionModel().selectFirst();
            zpusobCB.getSelectionModel().selectFirst();
            denCb.getSelectionModel().selectFirst();
        } else {
            roleCB.getSelectionModel().select(roleVyuc);
            HelpClass sel = IDAS22018.mainController.getCiselnikZpusobVyuky().get(zpusobVyuky);
            zpusobCB.getSelectionModel().select(sel);
            denCb.getSelectionModel().select(den);
        }

        grid.add(new Label("Zkratka předmětu:"), 0, 0);
        grid.add(zkratkaPrCb, 1, 0);
        grid.add(new Label("ID vyučujícího:"), 0, 1);
        grid.add(vyucCb, 1, 1);
        grid.add(new Label("Začíná v:"), 0, 2);
        grid.add(zacinaVSp, 1, 2);
        grid.add(new Label("Má hodin:"), 0, 3);
        grid.add(maHodinSp, 1, 3);
        grid.add(new Label("Počet studentů:"), 0, 4);
        grid.add(pocetStudentuSp, 1, 4);
        grid.add(new Label("Role vyučujícího:"), 0, 5);
        grid.add(roleCB, 1, 5);
        grid.add(new Label("Způsob výuky:"), 0, 6);
        grid.add(zpusobCB, 1, 6);
        grid.add(new Label("Učebna:"), 0, 7);
        grid.add(ucebnaCb, 1, 7);
        grid.add(new Label("Den v týdnu:"), 0, 8);
        grid.add(denCb, 1, 8);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                zkratkaPr = zkratkaPrCb.getValue();
                idVyuc = vyucCb.getValue().id;
                zacinaV = zacinaVSp.getValue();
                maHodin = maHodinSp.getValue();
                pocetStudentu = pocetStudentuSp.getValue();
                roleVyuc = roleCB.getValue();
                zpusobVyuky = zpusobCB.getValue().getId();
                den = denCb.getValue();

                ResultSet rs1;
                Statement stmt1 = conn.createStatement();
                String help = String.format("select ID_UCEBNA from UCEBNA where NAZEV = '%s'", ucebnaCb.getValue());
                rs1 = stmt1.executeQuery(help);
                rs1.next();

                ucebnaId = rs1.getInt("ID_UCEBNA");

                buttonPressed = true;
                hide();
            } catch (IllegalArgumentException ex) {
                DialogChyba dialog3 = new DialogChyba(null, "Špatný formát");
                dialog3 = (DialogChyba) dialog3.getScene().getWindow();
                dialog3.showAndWait();
            } catch (SQLException ex1) {
                DialogChyba dialog3 = new DialogChyba(null, ex1.getMessage());
                dialog3 = (DialogChyba) dialog3.getScene().getWindow();
                dialog3.showAndWait();
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }
}
