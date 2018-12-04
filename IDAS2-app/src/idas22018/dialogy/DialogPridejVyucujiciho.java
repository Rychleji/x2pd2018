/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Radim Nyč
 */
public class DialogPridejVyucujiciho extends Stage {

    private boolean buttonPressed = false;

    private String idV, jmeno, prijmeni, titulP, titulZ, telefon, mobil, email, zkratkaKatedry;

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public String getIdV() {
        return idV;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getTitulP() {
        return titulP;
    }

    public String getTitulZ() {
        return titulZ;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getMobil() {
        return mobil;
    }

    public String getEmail() {
        return email;
    }

    public String getZkratkaKatedry() {
        return zkratkaKatedry;
    }

    public DialogPridejVyucujiciho(Window okno) {
        setTitle("");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu());
    }

    private Scene vytvorScenu() {
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
        TextField idTF = new TextField();
        TextField jmenoTF = new TextField();
        TextField prijmeniTF = new TextField();
        TextField titulPTF = new TextField();
        TextField titulZTF = new TextField();
        TextField telefonTF = new TextField();
        TextField mobilTF = new TextField();
        TextField emailTF = new TextField();
        TextField zkratKatTF = new TextField();

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idTF, 1, 0);
        grid.add(new Label("Jméno:"), 0, 1);
        grid.add(jmenoTF, 1, 1);
        grid.add(new Label("Příjmení:"), 0, 2);
        grid.add(prijmeniTF, 1, 2);
        grid.add(new Label("Titul před:"), 0, 3);
        grid.add(titulPTF, 1, 3);
        grid.add(new Label("Titul za:"), 0, 4);
        grid.add(titulZTF, 1, 4);
        grid.add(new Label("Telefon:"), 0, 5);
        grid.add(telefonTF, 1, 5);
        grid.add(new Label("Mobil:"), 0, 6);
        grid.add(mobilTF, 1, 6);
        grid.add(new Label("e-mail:"), 0, 7);
        grid.add(emailTF, 1, 7);
        grid.add(new Label("Zkratka katedry:"), 0, 8);
        grid.add(zkratKatTF, 1, 8);
      

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                idV = idTF.getText();
                jmeno = jmenoTF.getText();
                prijmeni = prijmeniTF.getText();
                titulP = titulPTF.getText();
                titulZ = titulZTF.getText();
                telefon = telefonTF.getText();
                mobil = mobilTF.getText();
                email = emailTF.getText();
                zkratkaKatedry = zkratKatTF.getText();

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
