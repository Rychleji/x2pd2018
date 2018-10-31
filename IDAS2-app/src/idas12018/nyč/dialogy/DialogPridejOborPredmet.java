/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas12018.nyč.dialogy;

import idas12018.nyč.IDAS12018Nyč;
import javafx.collections.FXCollections;
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
    
    private String obor, predmet, kategorie;

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

    public DialogPridejOborPredmet(Window okno) {
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
        TextField zkratkaOTF = new TextField();
        TextField zkratkaPTF = new TextField();
        
        ComboBox<String> kategorieCB = new ComboBox<>(FXCollections.observableArrayList(IDAS12018Nyč.mainController.getCiselnikKatPredmetu()));

        grid.add(new Label("Zkratka oboru:"), 0, 0);
        grid.add(zkratkaOTF, 1, 0);
        grid.add(new Label("Zkratka předmětu:"), 0, 1);
        grid.add(zkratkaPTF, 1, 1);
        grid.add(new Label("Kategorie:"), 0, 2);
        grid.add(kategorieCB, 1, 2);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                obor = zkratkaOTF.getText();
                predmet = zkratkaPTF.getText();
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