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
 * @author Radim
 */
public class DialogPridejPredmet extends Stage {

    private boolean buttonPressed = false;

    int rocnik, semestr, zpusobZak, forma;
    
    private String zkratka, nazev;

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public int getRocnik() {
        return rocnik;
    }

    public int getSemestr() {
        return semestr;
    }

    public int getZpusobZak() {
        return zpusobZak;
    }

    public int getForma() {
        return forma;
    }

    public String getZkratka() {
        return zkratka;
    }

    public String getNazev() {
        return nazev;
    }

    public DialogPridejPredmet(Window okno) {
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
        TextField rocnikTF = new TextField();
        
        ObservableList<GuiFXMLController.HelpClass> list1 = FXCollections.observableArrayList(IDAS12018Nyč.mainController.getCiselnikFormaVyuky().values());
        ObservableList<GuiFXMLController.HelpClass> list2 = FXCollections.observableArrayList(IDAS12018Nyč.mainController.getCiselnikZpusobZak().values());  
        ObservableList<GuiFXMLController.HelpClass> list3 = FXCollections.observableArrayList(IDAS12018Nyč.mainController.getCiselnikSemestr().values());
               
        ComboBox<GuiFXMLController.HelpClass> formaCB = new ComboBox<>(list1);
        ComboBox<GuiFXMLController.HelpClass> zpusobCB = new ComboBox<>(list2);
        ComboBox<GuiFXMLController.HelpClass> semestrCB = new ComboBox<>(list3);

        grid.add(new Label("Zkratka předmětu:"), 0, 0);
        grid.add(zkratkaTF, 1, 0);
        grid.add(new Label("Název předmětu:"), 0, 1);
        grid.add(nazevTF, 1, 1);
        grid.add(new Label("Doporučený ročník:"), 0, 2);
        grid.add(rocnikTF, 1, 2);
        grid.add(new Label("Forma:"), 0, 3);
        grid.add(formaCB, 1, 3);
        grid.add(new Label("Zakončení:"), 0, 4);
        grid.add(zpusobCB, 1, 4);
        grid.add(new Label("Semestr:"), 0, 5);
        grid.add(semestrCB, 1, 5);

        // Tlačítko
        Button tlacitko1 = new Button("Vlož");

        grid2.add(tlacitko1, 0, 0);

        tlacitko1.setOnAction((ActionEvent e) -> {
            try {
                zkratka = zkratkaTF.getText();
                nazev = nazevTF.getText();
                rocnik = Integer.parseInt(rocnikTF.getText());
                forma = formaCB.getValue().getId();
                semestr = semestrCB.getValue().getId();
                zpusobZak =  zpusobCB.getValue().getId();

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
