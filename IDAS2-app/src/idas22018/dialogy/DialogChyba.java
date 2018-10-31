/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class DialogChyba extends Stage {

    public DialogChyba(Window okno, String s) {
        setTitle("Chyba!");
        setWidth(750);
        setHeight(200);

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);

        setScene(vytvorScenu(s));

    }

    private Scene vytvorScenu(String s) {
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
        grid.add(new Label(s), 0, 0);

        // Tlačítko
        Button tlacitko1 = new Button("OK");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                hide();
            } catch (IllegalArgumentException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }

}
