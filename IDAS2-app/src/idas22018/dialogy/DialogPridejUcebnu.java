/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas22018.dialogy;

import java.sql.Connection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author David
 */
public class DialogPridejUcebnu extends Stage {

    private boolean buttonPressed = false;
    private Spinner<Integer> spinnerKapacita = new Spinner<>(1, Integer.MAX_VALUE - 1, 1, 1);
    
    private int kapacita;
    private String zkratkaPredmetu;

    public DialogPridejUcebnu(Window okno, Connection conn) {
        setTitle("Rozvrhová akce");

        initStyle(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(okno);
        spinnerKapacita.setEditable(true);

        setScene(vytvorScenu(conn));
        this.setWidth(400);
        this.setResizable(false);
    }

    private Scene vytvorScenu(Connection conn) {

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.BOTTOM_CENTER);
        grid2.setPadding(new Insets(10));
        
        TextField textFiedlZkratkaPredmetu = new TextField();

        grid.add(new Label("Zkratka učebny:"), 0, 0);
        grid.add(textFiedlZkratkaPredmetu, 1, 0);

        grid.add(new Label("Kapacita: "), 0, 1);
        grid.add(spinnerKapacita, 1, 1);

        Button tlacitko1 = new Button("Vlož");
        

        grid2.add(tlacitko1, 0, 0);

        box.getChildren().addAll(grid, grid2);
        
        tlacitko1.setOnAction((event) ->{
            this.zkratkaPredmetu = textFiedlZkratkaPredmetu.getText();
            this.kapacita = spinnerKapacita.getValue();
            
            buttonPressed = true;
            this.close();
        });
        return new Scene(box);
    }

    public int getKapacita() {
        return kapacita;
    }

    public String getZkratkaPredmetu() {
        return zkratkaPredmetu;
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }
    
}
