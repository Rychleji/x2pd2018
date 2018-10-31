/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idas12018.nyč.dialogy;

import idas12018.nyč.GuiFXMLController;
import idas12018.nyč.IDAS12018Nyč;
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
 * @author Radim Nyč
 */
public class DialogPridejRA extends Stage {

    private boolean buttonPressed = false;

    int maHodin, pocetStudentu, zpusobVyuky;
    
    float zacinaV;
    
    private String zkratkaPr, idVyuc, roleVyuc;

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public int getMaHodin() {
        return maHodin;
    }

    public int getPocetStudentu() {
        return pocetStudentu;
    }

    public int getZpusobVyuky() {
        return zpusobVyuky;
    }

    public float getZacinaV() {
        return zacinaV;
    }

    public String getZkratkaPr() {
        return zkratkaPr;
    }

    public String getIdVyuc() {
        return idVyuc;
    }

    public String getRoleVyuc() {
        return roleVyuc;
    }



    public DialogPridejRA(Window okno) {
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
        TextField zkratkaPrTF = new TextField();
        TextField idVyucTF = new TextField();
        TextField zacinaVTF = new TextField();
        TextField pocetStudentuTF = new TextField();
        TextField maHodinTF = new TextField();
        
        ObservableList<String> list1 = FXCollections.observableArrayList(IDAS12018Nyč.mainController.getCiselnikRoleVyuc());
        ObservableList<GuiFXMLController.HelpClass> list2 = FXCollections.observableArrayList(IDAS12018Nyč.mainController.getCiselnikZpusobVyuky().values());   
               
        ComboBox<String> roleCB = new ComboBox<>(list1);
        ComboBox<GuiFXMLController.HelpClass> zpusobCB = new ComboBox<>(list2);

        grid.add(new Label("Zkratka předmětu:"), 0, 0);
        grid.add(zkratkaPrTF, 1, 0);
        grid.add(new Label("ID vyučujícího:"), 0, 1);
        grid.add(idVyucTF, 1, 1);
        grid.add(new Label("Začíná v:"), 0, 2);
        grid.add(zacinaVTF, 1, 2);
        grid.add(new Label("Má hodin:"), 0, 3);
        grid.add(maHodinTF, 1, 3);
        grid.add(new Label("Počet studentů:"), 0, 4);
        grid.add(pocetStudentuTF, 1, 4);
        grid.add(new Label("Role vyučujícího:"), 0, 5);
        grid.add(roleCB, 1, 5);
        grid.add(new Label("Způsob výuky:"), 0, 6);
        grid.add(zpusobCB, 1, 6);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                zkratkaPr = zkratkaPrTF.getText();
                idVyuc = idVyucTF.getText();
                zacinaV = Float.parseFloat(zacinaVTF.getText());
                maHodin = Integer.parseInt(maHodinTF.getText());
                pocetStudentu = Integer.parseInt(pocetStudentuTF.getText());
                roleVyuc = roleCB.getValue();
                zpusobVyuky =  zpusobCB.getValue().getId();

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
