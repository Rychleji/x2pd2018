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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
public class DialogPripojeni extends Stage {

    private String jmeno;
    private String heslo;
    private boolean buttonPressed = false;

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getHeslo() {
        return heslo;
    }

    public DialogPripojeni(Window okno) {
        setTitle("Připojování...");
        setWidth(750);
        setHeight(200);

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
        TextField jmenoTF = new TextField();
        PasswordField hesloTF = new PasswordField();

        grid.add(new Label("Uživatelské jméno:"), 0, 0);
        grid.add(jmenoTF, 0, 1);
        grid.add(new Label("Uživatelské heslo:"), 1, 0);
        grid.add(hesloTF, 1, 1);

        // Tlačítko
        Button tlacitko1 = new Button("Připoj");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                jmeno=jmenoTF.getText();
                heslo=hesloTF.getText();
          
                buttonPressed = true;
                hide();
            } catch (IllegalArgumentException ex) {
                DialogChyba dialog3 = new DialogChyba(null, "Něco se pokazilo :(");
                dialog3 = (DialogChyba) dialog3.getScene().getWindow();
                dialog3.showAndWait();
            }
        });

        box.getChildren().addAll(grid, grid2);
        return new Scene(box);
    }
}
