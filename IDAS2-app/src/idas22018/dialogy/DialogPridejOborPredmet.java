/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Radim
 */
public class DialogPridejOborPredmet extends Stage {

    private boolean buttonPressed = false;
    
    private String obor="", predmet="", kategorie="";
    
    private class Obor{
        String zkratka, nazev;

        public Obor(String zkratka, String nazev) {
            this.zkratka = zkratka;
            this.nazev = nazev;
        }

        @Override
        public String toString() {
            return nazev;
        }
    }
    
    private class Predmet{
        String zkratka, nazev;

        public Predmet(String zkratka, String nazev) {
            this.zkratka = zkratka;
            this.nazev = nazev;
        }
        
        @Override
        public String toString() {
            return nazev;
        }
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public String getObor() {
        return obor;
    }

    public String getPredmet() {
        return predmet;
    }

    public String getKategorie() {
        return kategorie;
    }

    public DialogPridejOborPredmet(Window okno, Connection conn, String zkrObor, String zkrPrdmt, boolean filtrObor) {
        setTitle("Předmět/obor");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(conn, zkrObor, zkrPrdmt, filtrObor));
    }

    private Scene vytvorScenu(Connection conn, String zkrObor, String zkrPrdmt, boolean filtrObor) {
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

        // Komponenty
        Statement stmt;
        ResultSet rs;
        
        ObservableList<Obor> list1 = FXCollections.observableArrayList();
        ObservableList<Predmet> list2 = FXCollections.observableArrayList();
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from OBOR");
            while(rs.next()){
                list1.add(new Obor(rs.getString("ZKRATKA_OBORU"), rs.getString("NAZEV_OBORU")));
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from PREDMET");
            while(rs.next()){
                list2.add(new Predmet(rs.getString("ZKRATKA_PREDMETU"), rs.getString("NAZEV_PREDMETU")));
            }
        }catch (SQLException e){}
        
        ComboBox<Obor> zkratkaOCb = new ComboBox<>(list1);
        ComboBox<Predmet> zkratkaPCb = new ComboBox<>(list2);
        zkratkaOCb.setDisable(filtrObor);
        zkratkaPCb.setDisable(!zkratkaOCb.isDisabled());
        
        ComboBox<String> kategorieCB = new ComboBox<>(FXCollections.observableArrayList(IDAS22018.mainController.getCiselnikKatPredmetu()));
        
        zkratkaOCb.getSelectionModel().selectFirst();
        zkratkaPCb.getSelectionModel().selectFirst();
        kategorieCB.getSelectionModel().selectFirst();
        
        if(zkrObor!=null && !zkrObor.equals("")){
            for(Obor o : list1){
                if(o.zkratka.equals(zkrObor)){
                    zkratkaOCb.getSelectionModel().select(o);
                    break;
                }
            }
        }
        if(zkrPrdmt!=null && !zkrPrdmt.equals("")){
            for(Predmet p : list2){
                if(p.zkratka.equals(zkrPrdmt)){
                    zkratkaPCb.getSelectionModel().select(p);
                    break;
                }
            }
        }
        
        if(zkrObor!=null && !zkrObor.equals("") && zkrPrdmt!=null && !zkrPrdmt.equals("")){
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from OBOR_PREDMET where "
                        + "OBOR_ZKRATKA_OBORU = '"+ zkrObor +"' and PREDMET_ZKRATKA_PREDMETU = '"+ zkrPrdmt +"'");
                
                rs.next();
                String pom = rs.getString("KATEGORIE_PREDMETU_KATEGORIE");
                
                kategorieCB.getSelectionModel().select(pom);

            } catch (SQLException ex) {
                Logger.getLogger(DialogPridejKatedru.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        grid.add(new Label("Obor:"), 0, 0);
        grid.add(zkratkaOCb, 1, 0);
        grid.add(new Label("Předmět:"), 0, 1);
        grid.add(zkratkaPCb, 1, 1);
        grid.add(new Label("Kategorie:"), 0, 2);
        grid.add(kategorieCB, 1, 2);
        
        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                obor = zkratkaOCb.getValue().zkratka;
                predmet = zkratkaPCb.getValue().zkratka;
                kategorie = kategorieCB.getValue();

                buttonPressed = true;
                hide();
            } catch (IllegalArgumentException ex) {
                DialogChyba dialog3 = new DialogChyba(null, "Špatný formát");
                dialog3 = (DialogChyba) dialog3.getScene().getWindow();
                dialog3.showAndWait();
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }
}