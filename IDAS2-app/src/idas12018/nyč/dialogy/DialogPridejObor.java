/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas12018.nyč.dialogy;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class DialogPridejObor extends Stage {

    private boolean buttonPressed = false;

    private String zkratka, nazev, zkratkaFak;

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

    public DialogPridejObor(Window okno) {
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
        TextField zkratkaTF = new TextField();
        TextField nazevTF = new TextField();
        TextField fakultaTF = new TextField();

        grid.add(new Label("Zkratka oboru:"), 0, 0);
        grid.add(zkratkaTF, 1, 0);
        grid.add(new Label("Název oboru:"), 0, 1);
        grid.add(nazevTF, 1, 1);
        grid.add(new Label("Zkratka fakulty:"), 0, 2);
        grid.add(fakultaTF, 1, 2);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                zkratka = zkratkaTF.getText();
                nazev = nazevTF.getText();
                zkratkaFak = fakultaTF.getText();

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
