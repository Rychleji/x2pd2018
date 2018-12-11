/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

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
public class DialogPridejKatedru extends Stage {

    private boolean buttonPressed = false;

    private String zkratka="", nazev="", zkratkaFak="";
    
    private class Fakulta{
        String zkratka="", nazev="";

        public Fakulta(String z, String n) {
            this.zkratka = z;
            this.nazev = n;
        }

        @Override
        public String toString() {
            return nazev;
        }
        
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public String getZkratka() {
        return zkratka;
    }

    public String getNazev() {
        return nazev;
    }

    public String getZkratkaFak() {
        return zkratkaFak;
    }

    public DialogPridejKatedru(Window okno, Connection conn, String zkratkaKatedry) {
        setTitle("Pracoviště");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(conn, zkratkaKatedry));
    }

    private Scene vytvorScenu(Connection conn, String zkratkaKatedry) {
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
        
        ObservableList<Fakulta> list1 = FXCollections.observableArrayList();
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from FAKULTA");
            while(rs.next()){
                list1.add(new Fakulta(rs.getString("ZKRATKA_FAKULTY"), rs.getString("NAZEV_FAKULTY")));
            }
        }catch (SQLException e){}
        
        if(zkratkaKatedry!=null && !zkratkaKatedry.equals("")){
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from KATEDRA where ZKRATKA_KATEDRY = '"+ zkratkaKatedry +"'");
                
                rs.next();
                zkratka = zkratkaKatedry;
                nazev = rs.getString("NAZEV_KATEDRY");
                zkratkaFak = rs.getString("FAKULTA_ZKRATKA_FAKULTY");
            } catch (SQLException ex) {
                Logger.getLogger(DialogPridejKatedru.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        TextField zkratkaTF = new TextField(zkratka);
        TextField nazevTF = new TextField(nazev);
        ComboBox<Fakulta> fakultaCb = new ComboBox<>(list1);
        
        fakultaCb.getSelectionModel().selectFirst();
        if(zkratkaKatedry!=null && !zkratkaKatedry.equals("")){
            for(Fakulta f : list1){
                if(f.zkratka.equals(zkratkaFak)){
                    fakultaCb.getSelectionModel().select(f);
                    break;
                }
            }
        }

        grid.add(new Label("Zkratka katedry:"), 0, 0);
        grid.add(zkratkaTF, 1, 0);
        grid.add(new Label("Název katedry:"), 0, 1);
        grid.add(nazevTF, 1, 1);
        grid.add(new Label("Fakulta:"), 0, 2);
        grid.add(fakultaCb, 1, 2);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                zkratka = zkratkaTF.getText();
                nazev = nazevTF.getText();
                zkratkaFak = fakultaCb.getValue().zkratka;

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